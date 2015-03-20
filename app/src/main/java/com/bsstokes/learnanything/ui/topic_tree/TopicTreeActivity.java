package com.bsstokes.learnanything.ui.topic_tree;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.bsstokes.learnanything.BuildConfig;
import com.bsstokes.learnanything.R;
import com.bsstokes.learnanything.db.RealmUtils;
import com.bsstokes.learnanything.db.models.Topic;
import com.bsstokes.learnanything.dev_tools.CopyFile;
import com.bsstokes.learnanything.sync.SyncService;
import com.bsstokes.learnanything.sync.rx.EndlessObserver;
import com.bsstokes.learnanything.ui.TopicActivity;
import com.crashlytics.android.Crashlytics;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;
import io.realm.Realm;
import io.realm.RealmResults;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TopicTreeActivity extends ActionBarActivity {

    @InjectView(R.id.topic_list_view)
    ListView mTopicListView;

    private TopicTreeListAdapter mTopicAdapter;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_tree);
        ButterKnife.inject(this);

        realm = Realm.getInstance(this);
        Log.d("REALM", "Realm path: " + realm.getPath());

        RealmResults<Topic> topics = RealmUtils.findAllTopLevelTopics(realm);
        mTopicAdapter = new TopicTreeListAdapter(this, topics);
        mTopicListView.setAdapter(mTopicAdapter);

        requestSync();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
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
                copyDatabaseToSDCard();
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

    private void copyDatabaseToSDCard() {
        String databasePath = realm.getPath();
        CopyFile.copyDatabaseToSDCard(databasePath)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new EndlessObserver<String>() {
                    @Override
                    public void onNext(String backedUpPath) {
                        toast("Copied database to " + backedUpPath);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        throwable.printStackTrace();
                        Crashlytics.logException(throwable);
                    }
                });
    }

    private void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
