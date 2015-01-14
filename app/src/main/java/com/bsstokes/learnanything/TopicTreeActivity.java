package com.bsstokes.learnanything;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.bsstokes.learnanything.api.KhanAcademyApi;
import com.bsstokes.learnanything.api.models.Topic;
import com.bsstokes.learnanything.api.models.TopicTree;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class TopicTreeActivity extends ActionBarActivity {

    @InjectView(R.id.topic_list_view)
    ListView mTopicListView;

    ArrayAdapter<Topic> mTopicAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_tree);
        ButterKnife.inject(this);

        mTopicAdapter = new ArrayAdapter<>(this, R.layout.row_topic, R.id.topic_title_text_view);
        mTopicListView.setAdapter(mTopicAdapter);

        loadTopicTree();
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
        Topic topic = mTopicAdapter.getItem(position);
        if (topic.isTopic()) {
            Intent intent = TopicActivity.buildIntent(this, topic.translated_title, topic.slug);
            startActivity(intent);
        } else {
            Log.e(TAG, "I don't know what kind this is: " + topic.kind);
        }
    }

    private void loadTopicTree() {
        KhanAcademyApi.Client khanAcademyClient = new KhanAcademyApi.Client();
        khanAcademyClient.getTopicTreeOfKindTopic(new Callback<TopicTree>() {
            @Override
            public void success(TopicTree topicTree, Response response) {
                mTopicAdapter.clear();
                mTopicAdapter.addAll(topicTree.children);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    private static final String TAG = TopicTreeActivity.class.getName();
}
