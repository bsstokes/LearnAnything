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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bsstokes.learnanything.api.Categories;
import com.bsstokes.learnanything.db.models.Topic;
import com.bsstokes.learnanything.sync.SyncService;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;

public class TopicActivity extends ActionBarActivity {

    public static void startActivity(Context context, Topic topic) {
        startActivity(context, topic, topic.getSlug());
    }

    public static void startActivity(Context context, com.bsstokes.learnanything.db.models.Child topic, String topTopicSlug) {

        String title = topic.getTitle();
        String topicSlug = topic.getId();

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

    public static void startActivity(Context context, Topic topic, String topTopicSlug) {

        String title = topic.getTitle();
        String topicSlug = topic.getSlug();

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
    private TopicListRealmAdapter mChildAdapter;

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        ButterKnife.inject(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        realm = Realm.getInstance(this);
        realm.addChangeListener(new RealmChangeListener() {
            @Override
            public void onChange() {
                update();
            }
        });

        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            mTitle = extras.getString(EXTRA_TITLE, mTitle);
            mTopTopicSlug = extras.getString(EXTRA_TOP_TOPIC_SLUG, mTopTopicSlug);
            mTopicSlug = extras.getString(EXTRA_TOPIC_SLUG, mTopicSlug);
        }

        setTitle(mTitle);

        update();
        requestSync();

        int colorResId = Categories.getColorForCategory(mTopTopicSlug);
        int color = getResources().getColor(colorResId);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    private void update() {
        Topic topic = realm.where(Topic.class).equalTo("slug", mTopicSlug).findFirst();

        if (null == topic) {
            return;
        }

//        mChildAdapter = new TopicListAdapter(this);
        mChildAdapter = new TopicListRealmAdapter(this, realm, topic.getChildren());
        mTopicListView.setAdapter(mChildAdapter);
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
        com.bsstokes.learnanything.db.models.Child child = mChildAdapter.getItem(position);
        String kind = child.getKind();
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

    private void onContentItemClick(com.bsstokes.learnanything.db.models.Child child) {
        Toast.makeText(this, "Oops (Child/" + child.getClass().getSimpleName() + ")", Toast.LENGTH_SHORT).show();
    }

    private void onTopicItemClick(com.bsstokes.learnanything.db.models.Child topic) {
        TopicActivity.startActivity(this, topic, mTopTopicSlug);
    }

    private void onVideoItemClick(com.bsstokes.learnanything.db.models.Child video) {
        VideoPlayerActivity.startActivity(this, video.getId(), video.getTranslatedTitle(), mTopTopicSlug);
    }

    private void onExerciseItemClick(com.bsstokes.learnanything.db.models.Child exercise) {
        ExerciseActivity.startActivity(this, exercise.getId(), exercise.getTranslatedTitle(), mTopTopicSlug);
    }

    private void onArticleItemClick(com.bsstokes.learnanything.db.models.Child article) {
        ArticleActivity.startActivity(this, article.getInternalId(), article.getTranslatedTitle(), mTopTopicSlug);
    }

    public static class TopicListRealmAdapter extends RealmListBaseAdapter<com.bsstokes.learnanything.db.models.Child> {

        private static final int VIEW_TYPE_OTHER = 0;
        private static final int VIEW_TYPE_TOPIC = VIEW_TYPE_OTHER + 1;
        private static final int VIEW_TYPE_VIDEO = VIEW_TYPE_OTHER + 2;
        private static final int VIEW_TYPE_EXERCISE = VIEW_TYPE_OTHER + 3;
        private static final int VIEW_TYPE_ARTICLE = VIEW_TYPE_OTHER + 4;
        private static final int VIEW_TYPE_COUNT = VIEW_TYPE_OTHER + 5;

        public TopicListRealmAdapter(Context context, Realm realm, RealmList<com.bsstokes.learnanything.db.models.Child> realmList) {
            super(context, realm, realmList, true);
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
            com.bsstokes.learnanything.db.models.Child child = getItem(position);
            viewHolder.titleTextView.setText(child.getTranslatedTitle());
        }

        @Override
        public int getViewTypeCount() {
            return VIEW_TYPE_COUNT;
        }

        @Override
        public int getItemViewType(int position) {
            com.bsstokes.learnanything.db.models.Child child = getItem(position);
            String kind = child.getKind();
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

        public static class ViewHolder {

            @InjectView(R.id.title_text_view)
            TextView titleTextView;

            public ViewHolder(View view) {
                ButterKnife.inject(this, view);
            }
        }
    }

    private void requestSync() {
        SyncService.startActionSyncTopic(this, mTopicSlug, isTopLevel());
    }

    private boolean isTopLevel() {
        return !TextUtils.isEmpty(mTopicSlug) && mTopicSlug.equals(mTopTopicSlug);
    }
}
