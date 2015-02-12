package com.bsstokes.learnanything.topic_tree;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.bsstokes.learnanything.R;
import com.bsstokes.learnanything.TopicActivity;
import com.bsstokes.learnanything.sync.SyncService;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;
import io.realm.Realm;
import io.realm.RealmResults;

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

        RealmResults<com.bsstokes.learnanything.db.models.Topic> topics = realm.where(com.bsstokes.learnanything.db.models.Topic.class)
                .equalTo("topLevel", true)
                .findAll();
        mTopicAdapter = new TopicTreeListAdapter(this, topics);
        mTopicListView.setAdapter(mTopicAdapter);

        loadTopicTree();

        Log.d("DB", "Topics: " + topics.size());
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (R.id.action_refresh == item.getItemId()) {
            loadTopicTree();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnItemClick(R.id.topic_list_view)
    void onTopicItemClick(int position) {
        com.bsstokes.learnanything.db.models.Topic topic = mTopicAdapter.getTopic(position);
        Intent intent = TopicActivity.buildIntent(this, topic.getTitle(), topic.getSlug(), topic.getSlug());
        startActivity(intent);
    }

    private void loadTopicTree() {
        SyncService.startActionSyncTopicTree(this);
    }
}
