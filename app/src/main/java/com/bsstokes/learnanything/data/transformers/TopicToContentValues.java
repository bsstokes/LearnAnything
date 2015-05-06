package com.bsstokes.learnanything.data.transformers;

import android.content.ContentValues;

import com.bsstokes.learnanything.db.TableConfig;
import com.bsstokes.learnanything.models.Topic;

import rx.functions.Func1;

public class TopicToContentValues implements Func1<Topic, ContentValues> {

    public static ContentValues convert(Topic topic) {
        return new TopicToContentValues().call(topic);
    }

    @Override
    public ContentValues call(Topic topic) {
        return new Builder()
                .id(topic.getId())
                .title(topic.getTitle())
                .slug(topic.getSlug())
                .kind(topic.getKind())
                .topLevel(topic.isTopLevel())
                .parentId(topic.getParentId())
                .build();
    }

    private static class Builder {
        private final ContentValues contentValues = new ContentValues();

        public Builder id(String id) {
            contentValues.put(TableConfig.Topics.COLUMN_ID, id);
            return this;
        }

        public Builder title(String title) {
            contentValues.put(TableConfig.Topics.COLUMN_TITLE, title);
            return this;
        }

        public Builder slug(String slug) {
            contentValues.put(TableConfig.Topics.COLUMN_SLUG, slug);
            return this;
        }

        public Builder kind(String kind) {
            contentValues.put(TableConfig.Topics.COLUMN_KIND, kind);
            return this;
        }

        public Builder topLevel(boolean topLevel) {
            contentValues.put(TableConfig.Topics.COLUMN_TOP_LEVEL, topLevel);
            return this;
        }

        public Builder parentId(String parentId) {
            contentValues.put(TableConfig.Topics.COLUMN_PARENT_ID, parentId);
            return this;
        }

        public ContentValues build() {
            return contentValues;
        }
    }
}
