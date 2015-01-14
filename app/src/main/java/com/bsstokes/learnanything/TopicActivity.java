package com.bsstokes.learnanything;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.bsstokes.learnanything.api.KhanAcademyApi;
import com.bsstokes.learnanything.api.models.Child;
import com.bsstokes.learnanything.api.models.Topic;

import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class TopicActivity extends ActionBarActivity {

    public static Intent buildIntent(Context context, String title, String topicSlug) {

        if (TextUtils.isEmpty(topicSlug)) {
            throw new RuntimeException("Topic slug can't be empty");
        }

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
    ArrayAdapter<Child> mChildAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        ButterKnife.inject(this);

        mChildAdapter = new ArrayAdapter<>(this, R.layout.row_topic, R.id.topic_title_text_view);
        mTopicListView.setAdapter(mChildAdapter);

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
                        topic.translated_title,
                        topic.slug,
                        topic.kind);
                Toast.makeText(TopicActivity.this, message, Toast.LENGTH_SHORT).show();

                mTitle = topic.translated_title;
                setTitle(mTitle);
                mChildAdapter.clear();
                mChildAdapter.addAll(topic.children);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    @OnItemClick(R.id.topic_list_view)
    void onItemClick(int position) {
        Child child = mChildAdapter.getItem(position);

        // TODO: This is lame type checking
        if (child instanceof Child.Topic) {
            onTopicItemClick((Child.Topic) child);
        } else if (child instanceof Child.Video) {
            onVideoItemClick((Child.Video) child);
        } else if (child instanceof Child.Exercise) {
            onExerciseItemClick((Child.Exercise) child);
        } else {
            onContentItemClick(child);
        }
    }

    private void onContentItemClick(Child child) {
        Toast.makeText(this, "Oops (Child/" + child.getClass().getSimpleName() + ")", Toast.LENGTH_SHORT).show();
    }

    private void onTopicItemClick(Child.Topic topic) {
        Intent intent = TopicActivity.buildIntent(this, topic.translated_title, topic.id);
        startActivity(intent);
    }

    private void onVideoItemClick(Child.Video video) {
        Intent intent = VideoPlayerActivity.buildIntent(this, video.id);
        startActivity(intent);
    }

    private void onExerciseItemClick(Child.Exercise exercise) {
        Intent intent = ExerciseActivity.buildIntent(this, exercise.id);
        startActivity(intent);
    }
}
