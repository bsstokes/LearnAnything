package com.bsstokes.learnanything;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bsstokes.learnanything.api.Categories;
import com.bsstokes.learnanything.api.KhanAcademyApi;
import com.bsstokes.learnanything.api.models.Topic;
import com.bsstokes.learnanything.api.models.TopicTree;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class TopicTreeActivity extends ActionBarActivity {

    @InjectView(R.id.topic_list_view)
    ListView mTopicListView;

    TopicTreeListAdapter mTopicAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_tree);
        ButterKnife.inject(this);

        mTopicAdapter = new TopicTreeListAdapter(this);
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
        TopicTreeListAdapter.TopicItem topicItem = mTopicAdapter.getTopicItem(position);
        Topic topic = topicItem.getTopic();
        if (topic.isTopic()) {
            Intent intent = TopicActivity.buildIntent(this, topic.translated_title, topic.slug, topic.slug);
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


    public static final class TopicTreeListAdapter extends BaseAdapter {

        private Context context;
        private List<TopicItem> topicItems = new ArrayList<>();

        public TopicTreeListAdapter(Context context) {
            this.context = context;
        }

        public void clear() {
            topicItems.clear();
        }

        public void addAll(List<Topic> topics) {
            for (Topic topic : topics) {
                topicItems.add(new TopicItem(topic));
            }

            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return topicItems.size();
        }

        @Override
        public Object getItem(int position) {
            return getTopicItem(position);
        }

        public TopicItem getTopicItem(int position) {
            return topicItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (null == convertView) {
                convertView = LayoutInflater.from(context).inflate(R.layout.row_topic_tree, parent, false);
                convertView.setTag(new ViewHolder(convertView));
            }

            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            bind(viewHolder, position);

            return convertView;
        }

        private void bind(ViewHolder viewHolder, int position) {
            TopicItem topicItem = topicItems.get(position);
            viewHolder.titleTextView.setText(topicItem.getTitle());
            viewHolder.categoryColorView.setVisibility(View.VISIBLE);
            viewHolder.categoryColorView.setBackgroundColor(context.getResources().getColor(topicItem.getColor()));
        }

        public static class TopicItem {
            private Topic topic;

            public TopicItem(Topic topic) {
                this.topic = topic;
            }

            public String getTitle() {
                return topic.translated_title;
            }

            @ColorRes
            public int getColor() {
                return Categories.getColorForCategory(topic.slug);
            }

            public Topic getTopic() {
                return topic;
            }
        }

        public static class ViewHolder {

            @InjectView(R.id.topic_title_text_view)
            TextView titleTextView;

            @InjectView(R.id.category_color_view)
            View categoryColorView;

            public ViewHolder(View view) {
                ButterKnife.inject(this, view);
            }
        }
    }
}
