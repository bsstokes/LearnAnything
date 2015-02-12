package com.bsstokes.learnanything;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bsstokes.learnanything.api.Categories;
import com.bsstokes.learnanything.api.KhanAcademyApi;
import com.bsstokes.learnanything.api.models.Child;
import com.bsstokes.learnanything.api.models.Topic;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class TopicActivity extends ActionBarActivity {

    public static void startActivity(Context context, String title, String topicSlug) {
        startActivity(context, title, topicSlug, topicSlug);
    }

    public static void startActivity(Context context, String title, String topTopicSlug, String topicSlug) {

        if (TextUtils.isEmpty(topTopicSlug)) {
            throw new RuntimeException("Top topic slug (" + EXTRA_TOP_TOPIC_SLUG + ") can't be empty");
        }

        if (TextUtils.isEmpty(topicSlug)) {
            throw new RuntimeException("Topic slug (" + EXTRA_TOPIC_SLUG + ") can't be empty");
        }

        Intent intent = new Intent(context, TopicActivity.class);
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_TOP_TOPIC_SLUG, topTopicSlug);
        intent.putExtra(EXTRA_TOPIC_SLUG, topicSlug);
        context.startActivity(intent);
    }

    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_TOP_TOPIC_SLUG = "topTopicSlug";
    public static final String EXTRA_TOPIC_SLUG = "topicSlug";

    @InjectView(R.id.topic_list_view)
    ListView mTopicListView;

    private String mTitle;
    private String mTopTopicSlug;
    private String mTopicSlug;
    private TopicListAdapter mChildAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        ButterKnife.inject(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mChildAdapter = new TopicListAdapter(this);
        mTopicListView.setAdapter(mChildAdapter);

        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            mTitle = extras.getString(EXTRA_TITLE, mTitle);
            mTopTopicSlug = extras.getString(EXTRA_TOP_TOPIC_SLUG, mTopTopicSlug);
            mTopicSlug = extras.getString(EXTRA_TOPIC_SLUG, mTopicSlug);
        }

        setTitle(mTitle);

        KhanAcademyApi api = new KhanAcademyApi();
        api.getTopic(mTopicSlug, new Callback<Topic>() {
            @Override
            public void success(Topic topic, Response response) {
                String message = String.format(Locale.getDefault(), "%s, %s, %s",
                        topic.translated_title,
                        topic.slug,
                        topic.kind);
                Toast.makeText(TopicActivity.this, message, Toast.LENGTH_SHORT).show();

                mTitle = topic.translated_title;
                setTitle(mTitle);
                mChildAdapter.setChildren(topic.children);
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(TopicActivity.this, "Oops. Topic list failed to download.", Toast.LENGTH_SHORT).show();
            }
        });

        int colorResId = Categories.getColorForCategory(mTopTopicSlug);
        int color = getResources().getColor(colorResId);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (android.R.id.home == item.getItemId()) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnItemClick(R.id.topic_list_view)
    void onItemClick(int position) {
        Child child = mChildAdapter.getChild(position);
        String kind = child.kind;
        // TODO: This is lame type checking
        if ("Topic".equalsIgnoreCase(kind)) {
            onTopicItemClick(child);
        } else if ("Video".equalsIgnoreCase(kind)) {
            onVideoItemClick(child);
        } else if ("Exercise".equalsIgnoreCase(kind)) {
            onExerciseItemClick(child);
        } else if ("Article".equalsIgnoreCase(kind)) {
            onArticleItemClick(child);
        } else {
            onContentItemClick(child);
        }
    }

    private void onContentItemClick(Child child) {
        Toast.makeText(this, "Oops (Child/" + child.getClass().getSimpleName() + ")", Toast.LENGTH_SHORT).show();
    }

    private void onTopicItemClick(Child topic) {
        startActivity(this, topic.translated_title, mTopTopicSlug, topic.id);
    }

    private void onVideoItemClick(Child video) {
        VideoPlayerActivity.startActivity(this, video.id);
    }

    private void onExerciseItemClick(Child exercise) {
        ExerciseActivity.startActivity(this, exercise.id);
    }

    private void onArticleItemClick(Child article) {
        ArticleActivity.startActivity(this, article.internal_id);
    }

    public static class TopicListAdapter extends BaseAdapter {

        private static final int VIEW_TYPE_OTHER = 0;
        private static final int VIEW_TYPE_TOPIC = VIEW_TYPE_OTHER + 1;
        private static final int VIEW_TYPE_VIDEO = VIEW_TYPE_OTHER + 2;
        private static final int VIEW_TYPE_EXERCISE = VIEW_TYPE_OTHER + 3;
        private static final int VIEW_TYPE_ARTICLE = VIEW_TYPE_OTHER + 4;
        private static final int VIEW_TYPE_COUNT = VIEW_TYPE_OTHER + 5;

        private Context context;
        private List<Child> children = new ArrayList<>();

        public TopicListAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return children.size();
        }

        @Override
        public Object getItem(int position) {
            return getChild(position);
        }

        public Child getChild(int position) {
            return children.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (null == convertView) {
                convertView = inflate(position, parent);
                convertView.setTag(new ViewHolder(convertView));
            }

            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            bind(viewHolder, position);

            return convertView;
        }

        private View inflate(int position, ViewGroup parent) {
            int viewType = getItemViewType(position);
            return LayoutInflater.from(context).inflate(getLayoutRes(viewType), parent, false);
        }

        @LayoutRes
        private int getLayoutRes(int viewType) {
            switch (viewType) {
                case (VIEW_TYPE_VIDEO):
                    return R.layout.row_video;
                case (VIEW_TYPE_EXERCISE):
                    return R.layout.row_exercise;
                case (VIEW_TYPE_ARTICLE):
                    return R.layout.row_article;
                case (VIEW_TYPE_TOPIC):
                    return R.layout.row_topic;
                default:
                    return R.layout.row_other;
            }
        }

        private void bind(ViewHolder viewHolder, int position) {
            Child child = getChild(position);
            viewHolder.titleTextView.setText(child.translated_title);
        }

        @Override
        public int getItemViewType(int position) {
            Child child = getChild(position);
            String kind = child.kind;
            if ("Topic".equalsIgnoreCase(kind)) {
                return VIEW_TYPE_TOPIC;
            } else if ("Video".equalsIgnoreCase(kind)) {
                return VIEW_TYPE_VIDEO;
            } else if ("Exercise".equalsIgnoreCase(kind)) {
                return VIEW_TYPE_EXERCISE;
            } else if ("Article".equalsIgnoreCase(kind)) {
                return VIEW_TYPE_ARTICLE;
            } else {
                return VIEW_TYPE_OTHER;
            }
        }

        @Override
        public int getViewTypeCount() {
            return VIEW_TYPE_COUNT;
        }

        public void setChildren(List<Child> children) {
            this.children.clear();
            this.children.addAll(children);
            notifyDataSetChanged();
        }

        public static class ViewHolder {

            @InjectView(R.id.title_text_view)
            TextView titleTextView;

            public ViewHolder(View view) {
                ButterKnife.inject(this, view);
            }
        }
    }
}
