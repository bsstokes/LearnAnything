package com.bsstokes.learnanything.ui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bsstokes.learnanything.R;
import com.bsstokes.learnanything.data.transformers.CursorToChild;
import com.bsstokes.learnanything.db.Database;
import com.bsstokes.learnanything.models.Child;
import com.bsstokes.learnanything.models.Topic;
import com.bsstokes.learnanything.sync.SyncService;
import com.bsstokes.learnanything.sync.rx.EndlessObserver;
import com.squareup.sqlbrite.SqlBrite;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class TopicActivity extends BaseActionBarActivity {

    public static void startActivity(Context context, Topic topic) {
        startActivity(context, topic, topic.getSlug());
    }

    public static void startActivity(Context context, Child topic, String topTopicSlug) {

        String parentId = topic.getInternalId();
        String title = topic.getTitle();
        String topicSlug = topic.getId();

        if (TextUtils.isEmpty(topTopicSlug)) {
            throw new RuntimeException("Top topic slug (" + EXTRA_TOP_TOPIC_SLUG + ") can't be empty");
        }

        if (TextUtils.isEmpty(topicSlug)) {
            throw new RuntimeException("Topic slug (" + EXTRA_TOPIC_SLUG + ") can't be empty");
        }

        Intent intent = new Intent(context, TopicActivity.class);
        intent.putExtra(EXTRA_PARENT_ID, parentId);
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_TOP_TOPIC_SLUG, topTopicSlug);
        intent.putExtra(EXTRA_TOPIC_SLUG, topicSlug);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, Topic topic, String topTopicSlug) {

        String parentId = topic.getId();
        String title = topic.getTitle();
        String topicSlug = topic.getSlug();

        if (TextUtils.isEmpty(topTopicSlug)) {
            throw new RuntimeException("Top topic slug (" + EXTRA_TOP_TOPIC_SLUG + ") can't be empty");
        }

        if (TextUtils.isEmpty(topicSlug)) {
            throw new RuntimeException("Topic slug (" + EXTRA_TOPIC_SLUG + ") can't be empty");
        }

        Intent intent = new Intent(context, TopicActivity.class);
        intent.putExtra(EXTRA_PARENT_ID, parentId);
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_TOP_TOPIC_SLUG, topTopicSlug);
        intent.putExtra(EXTRA_TOPIC_SLUG, topicSlug);
        context.startActivity(intent);
    }

    public static final String EXTRA_PARENT_ID = "parentId";
    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_TOP_TOPIC_SLUG = "topTopicSlug";
    public static final String EXTRA_TOPIC_SLUG = "topicSlug";

    @InjectView(R.id.topic_list_view)
    ListView mTopicListView;

    @Inject
    Database database;

    private String mParentId;
    private String mTitle;
    private String mTopTopicSlug;
    private String mTopicSlug;
    private TopicListAdapter mChildAdapter;

    private static final String TAG = "TopicActivity";

    @Override
    @LayoutRes
    protected int getContentView() {
        return R.layout.activity_topic;
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


        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            mParentId = extras.getString(EXTRA_PARENT_ID, mParentId);
            mTitle = extras.getString(EXTRA_TITLE, mTitle);
            mTopTopicSlug = extras.getString(EXTRA_TOP_TOPIC_SLUG, mTopTopicSlug);
            mTopicSlug = extras.getString(EXTRA_TOPIC_SLUG, mTopicSlug);
        }

        setTitle(mTitle);

        mChildAdapter = new TopicListAdapter(this);
        mTopicListView.setAdapter(mChildAdapter);

        update();
        requestSync();

        configureColors(mTopTopicSlug);
    }

    private void update() {
        database.getChildren(mParentId)
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<SqlBrite.Query, List<Child>>() {
                    @Override
                    public List<Child> call(SqlBrite.Query query) {
                        List<Child> values;

                        try (Cursor cursor = query.run()) {
                            values = new ArrayList<>(cursor.getCount());

                            while (cursor.moveToNext()) {
                                Child child = new CursorToChild().call(cursor);
                                values.add(child);
                            }
                        }

                        return values;
                    }
                })
                .subscribe(new EndlessObserver<List<Child>>() {
                    @Override
                    public void onNext(List<Child> children) {
                        mChildAdapter.setChildren(children);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e(TAG, "", throwable);
                    }
                });
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

    private void onContentItemClick(Child child) {
        Toast.makeText(this, "Oops (Child/" + child.getClass().getSimpleName() + ")", Toast.LENGTH_SHORT).show();
    }

    private void onTopicItemClick(Child topic) {
        TopicActivity.startActivity(this, topic, mTopTopicSlug);
    }

    private void onVideoItemClick(Child video) {
        VideoPlayerActivity.startActivity(this, video.getId(), video.getTranslatedTitle(), mTopTopicSlug);
    }

    private void onExerciseItemClick(Child exercise) {
        ExerciseActivity.startActivity(this, exercise.getId(), exercise.getTranslatedTitle(), mTopTopicSlug);
    }

    private void onArticleItemClick(Child article) {
        ArticleActivity.startActivity(this, article.getInternalId(), article.getTranslatedTitle(), mTopTopicSlug);
    }

    public static class TopicListAdapter extends BaseAdapter {

        private static final int VIEW_TYPE_OTHER = 0;
        private static final int VIEW_TYPE_TOPIC = VIEW_TYPE_OTHER + 1;
        private static final int VIEW_TYPE_VIDEO = VIEW_TYPE_OTHER + 2;
        private static final int VIEW_TYPE_EXERCISE = VIEW_TYPE_OTHER + 3;
        private static final int VIEW_TYPE_ARTICLE = VIEW_TYPE_OTHER + 4;
        private static final int VIEW_TYPE_COUNT = VIEW_TYPE_OTHER + 5;

        private final Context context;
        private List<Child> children = new ArrayList<>();

        public TopicListAdapter(Context context) {
            this.context = context;
        }

        public void setChildren(List<Child> children) {
            this.children = children;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return children.size();
        }

        public Child getChild(int position) {
            return children.get(position);
        }

        @Override
        public Object getItem(int position) {
            return getChild(position);
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
            viewHolder.titleTextView.setText(child.getTranslatedTitle());
        }

        @Override
        public int getViewTypeCount() {
            return VIEW_TYPE_COUNT;
        }

        @Override
        public int getItemViewType(int position) {
            Child child = getChild(position);
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
