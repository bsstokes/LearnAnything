package com.bsstokes.learnanything.topic_tree;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.bsstokes.learnanything.R;
import com.bsstokes.learnanything.TopicActivity;
import com.bsstokes.learnanything.api.KhanAcademyApi;
import com.bsstokes.learnanything.db.RealmUtils;
import com.bsstokes.learnanything.db.models.Topic;
import com.bsstokes.learnanything.dev_tools.CopyFile;
import com.bsstokes.learnanything.sync.rx.EndlessObserver;
import com.bsstokes.learnanything.sync.rx.TopicTreeDataSource;

import java.io.IOException;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;
import io.realm.Realm;
import io.realm.RealmResults;
import rx.Subscription;

public class TopicTreeActivity extends ActionBarActivity {

    @InjectView(R.id.topic_list_view)
    ListView mTopicListView;

    private TopicTreeListAdapter mTopicAdapter;
    private Realm realm;

    private KhanAcademyApi khanAcademyApi = new KhanAcademyApi();
    private Subscription topicTreeRequest;

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

        Log.d("DB", "Topics: " + topics.size());

        TopicTreeDataSource topicTreeDataSource = new TopicTreeDataSource(khanAcademyApi, realm);
        topicTreeRequest = topicTreeDataSource.loadTopicTree(new EndlessObserver<List<com.bsstokes.learnanything.api.models.Topic>>() {
            @Override
            public void onNext(List<com.bsstokes.learnanything.api.models.Topic> topics) {
                Log.d("RX", "Got topics");
                for (com.bsstokes.learnanything.api.models.Topic topic : topics) {
                    Log.d("RX", "Got topic: " + topic.id);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
        topicTreeRequest.unsubscribe();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_topic_tree, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.action_refresh):
                requestSync();
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
//        SyncService.startActionSyncTopicTree(this);
        copyDatabaseToSDCard(realm.getPath());
    }

    private void copyDatabaseToSDCard(String databasePath) {
        try {
            CopyFile.backupDatabaseFile(databasePath);
            Toast.makeText(this, "Copied " + databasePath, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Failed to copy " + databasePath, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
