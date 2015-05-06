package com.bsstokes.learnanything.ui.topic_tree;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.bsstokes.learnanything.BuildConfig;
import com.bsstokes.learnanything.R;
import com.bsstokes.learnanything.data.transformers.CursorToTopic;
import com.bsstokes.learnanything.db.Database;
import com.bsstokes.learnanything.data.Topic;
import com.bsstokes.learnanything.sync.SyncService;
import com.bsstokes.learnanything.sync.rx.EndlessObserver;
import com.bsstokes.learnanything.ui.BaseActionBarActivity;
import com.bsstokes.learnanything.ui.TopicActivity;
import com.squareup.sqlbrite.SqlBrite;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.InjectView;
import butterknife.OnItemClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class TopicTreeActivity extends BaseActionBarActivity {

    @InjectView(R.id.topic_list_view)
    ListView mTopicListView;

    @Inject
    Database database;

    private TopicTreeListAdapter mTopicAdapter;

    @Override
    @LayoutRes
    protected int getContentView() {
        return R.layout.activity_topic_tree;
    }

    @Nullable
    @Override
    protected View getHeaderSectionView() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getMainApplication().component().inject(this);

        mTopicAdapter = new TopicTreeListAdapter(this);
        mTopicListView.setAdapter(mTopicAdapter);

        requestSync();

        database.getTopLevelTopics()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<SqlBrite.Query, List<Topic>>() {
                    @Override
                    public List<Topic> call(SqlBrite.Query query) {
                        List<Topic> topics = new ArrayList<>();

                        try (Cursor cursor = query.run()) {
                            while (cursor.moveToNext()) {
                                Topic topic = new CursorToTopic().call(cursor);
                                topics.add(topic);
                            }
                        }

                        return topics;
                    }
                })
                .subscribe(new EndlessObserver<List<Topic>>() {
                    @Override
                    public void onNext(List<Topic> topics) {
                        Log.d(TAG, "Got " + topics.size() + " topics from database.");
                        mTopicAdapter.setTopics(topics);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e(TAG, "", throwable);
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_topic_tree, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if (BuildConfig.DEBUG) {
            MenuItem backupDatabaseItem = menu.findItem(R.id.action_backup_database);
            backupDatabaseItem.setVisible(true);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.action_refresh):
                requestSync();
                return true;

            case (R.id.action_backup_database):
                toast("Oops. Not implemented.");
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnItemClick(R.id.topic_list_view)
    void onTopicItemClick(int position) {
        Topic topic = mTopicAdapter.getTopic(position);
        TopicActivity.startActivity(this, topic);
    }

    private void requestSync() {
        SyncService.startActionSyncTopicTree(this);
    }

    private static final String TAG = "TopicTreeActivity";
}
