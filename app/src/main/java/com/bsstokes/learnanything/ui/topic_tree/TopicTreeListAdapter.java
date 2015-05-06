package com.bsstokes.learnanything.ui.topic_tree;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bsstokes.learnanything.R;
import com.bsstokes.learnanything.api.Categories;
import com.bsstokes.learnanything.data.Topic;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class TopicTreeListAdapter extends BaseAdapter {

    @NonNull
    private final Context context;

    @NonNull
    private List<Topic> topics = new ArrayList<>();

    public TopicTreeListAdapter(@NonNull Context context) {
        this.context = context;
    }

    public void setTopics(@NonNull List<Topic> topics) {
        this.topics = topics;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return topics.size();
    }

    public Topic getTopic(int position) {
        return topics.get(position);
    }

    @Override
    public Object getItem(int position) {
        return getTopic(position);
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
        Topic topic = getTopic(position);
        viewHolder.titleTextView.setText(topic.getTitle());
        viewHolder.categoryColorView.setVisibility(View.VISIBLE);

        int colorRes = Categories.getColorForCategory(topic.getSlug());
        int color = context.getResources().getColor(colorRes);
        viewHolder.categoryColorView.setBackgroundColor(color);
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
