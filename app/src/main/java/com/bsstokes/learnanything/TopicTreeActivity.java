package com.bsstokes.learnanything;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
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

    @OnItemClick(R.id.topic_list_view)
    void onTopicItemClick(int position) {
        Topic topic = mTopicAdapter.getItem(position);
        if (topic.isTopic()) {
            Intent intent = TopicActivity.buildIntent(this, topic.translated_title, topic.domain_slug);
            startActivity(intent);
        } else {
            Log.e(TAG, "I don't know what kind this is: " + topic.kind);
        }
    }

    private static final String TAG = TopicTreeActivity.class.getName();
}
