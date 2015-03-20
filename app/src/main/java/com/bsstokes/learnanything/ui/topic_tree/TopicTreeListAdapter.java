package com.bsstokes.learnanything.ui.topic_tree;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.bsstokes.learnanything.R;
import com.bsstokes.learnanything.api.Categories;
import com.bsstokes.learnanything.db.models.Topic;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.realm.RealmBaseAdapter;
import io.realm.RealmResults;

public class TopicTreeListAdapter extends RealmBaseAdapter<Topic> implements ListAdapter {

    public TopicTreeListAdapter(Context context, RealmResults<Topic> realmResults) {
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
        Topic topic = realmResults.get(position);
        viewHolder.titleTextView.setText(topic.getTitle());
        viewHolder.categoryColorView.setVisibility(View.VISIBLE);

        int colorRes = Categories.getColorForCategory(topic.getSlug());
        int color = context.getResources().getColor(colorRes);
        viewHolder.categoryColorView.setBackgroundColor(color);
    }

    public Topic getTopic(int position) {
        return getRealmResults().get(position);
    }

    public RealmResults<Topic> getRealmResults() {
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
