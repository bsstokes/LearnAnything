package com.bsstokes.learnanything;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bsstokes.learnanything.api.Categories;
import com.bsstokes.learnanything.api.KhanAcademyApi;
import com.bsstokes.learnanything.api.models.Topic;
import com.bsstokes.learnanything.api.models.TopicTree;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;
import io.realm.Realm;
import io.realm.RealmBaseAdapter;
import io.realm.RealmResults;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

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

        RealmResults<com.bsstokes.learnanything.db.Topic> topics = realm.where(com.bsstokes.learnanything.db.Topic.class)
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
        com.bsstokes.learnanything.db.Topic topic = mTopicAdapter.getTopic(position);
        Intent intent = TopicActivity.buildIntent(this, topic.getTitle(), topic.getSlug(), topic.getSlug());
        startActivity(intent);
    }

    private void loadTopicTree() {
        KhanAcademyApi.Client khanAcademyClient = new KhanAcademyApi.Client();
        khanAcademyClient.getTopicTreeOfKindTopic(new Callback<TopicTree>() {
            @Override
            public void success(TopicTree topicTree, Response response) {

                Toast.makeText(TopicTreeActivity.this, "Downloaded topic tree", Toast.LENGTH_SHORT).show();

                for (Topic apiTopic : topicTree.children) {

                    com.bsstokes.learnanything.db.Topic dbTopic = realm.where(com.bsstokes.learnanything.db.Topic.class)
                            .equalTo("id", apiTopic.id)
                            .findFirst();
                    if (null == dbTopic) {
                        dbTopic = new com.bsstokes.learnanything.db.Topic();
                        dbTopic.setId(apiTopic.id);
                    }

                    dbTopic.setTitle(apiTopic.translated_title);
                    dbTopic.setSlug(apiTopic.slug);
                    dbTopic.setTopLevel(true);

                    realm.beginTransaction();
                    realm.copyToRealm(dbTopic);
                    realm.commitTransaction();
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    public static class TopicTreeListAdapter extends RealmBaseAdapter<com.bsstokes.learnanything.db.Topic> implements ListAdapter {

        public TopicTreeListAdapter(Context context, RealmResults<com.bsstokes.learnanything.db.Topic> realmResults) {
            super(context, realmResults, true);
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
            com.bsstokes.learnanything.db.Topic topic = realmResults.get(position);
            viewHolder.titleTextView.setText(topic.getTitle());
            viewHolder.categoryColorView.setVisibility(View.VISIBLE);

            int colorRes = Categories.getColorForCategory(topic.getSlug());
            int color = context.getResources().getColor(colorRes);
            viewHolder.categoryColorView.setBackgroundColor(color);
        }

        public com.bsstokes.learnanything.db.Topic getTopic(int position) {
            return getRealmResults().get(position);
        }

        public RealmResults<com.bsstokes.learnanything.db.Topic> getRealmResults() {
            return realmResults;
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
