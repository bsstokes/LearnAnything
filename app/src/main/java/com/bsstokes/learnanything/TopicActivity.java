package com.bsstokes.learnanything;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.bsstokes.learnanything.api.KhanAcademyApi;
import com.bsstokes.learnanything.api.models.Topic;

import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class TopicActivity extends ActionBarActivity {

    public static Intent buildIntent(Context context, String title, String topicSlug) {
        Intent intent = new Intent(context, TopicActivity.class);
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_TOPIC_SLUG, topicSlug);
        return intent;
    }

    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_TOPIC_SLUG = "topicSlug";

    @InjectView(R.id.topic_list_view)
    ListView mTopicListView;

    private String mTitle;
    private String mTopicSlug;
    ArrayAdapter<Topic> mTopicAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        ButterKnife.inject(this);

        mTopicAdapter = new ArrayAdapter<>(this, R.layout.row_topic, R.id.topic_title_text_view);
        mTopicListView.setAdapter(mTopicAdapter);

        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            mTitle = extras.getString(EXTRA_TITLE, mTitle);
            mTopicSlug = extras.getString(EXTRA_TOPIC_SLUG, mTopicSlug);
        }

        setTitle(mTitle);

        KhanAcademyApi.Client apiClient = new KhanAcademyApi.Client();
        apiClient.getTopic(mTopicSlug, new Callback<Topic>() {
            @Override
            public void success(Topic topic, Response response) {
                String message = String.format(Locale.getDefault(), "%s, %s, %s",
                        topic.title,
                        topic.domain_slug,
                        topic.kind);
                Toast.makeText(TopicActivity.this, message, Toast.LENGTH_SHORT).show();

                mTitle = topic.title;
                setTitle(mTitle);
                mTopicAdapter.clear();
                mTopicAdapter.addAll(topic.children);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }
}
