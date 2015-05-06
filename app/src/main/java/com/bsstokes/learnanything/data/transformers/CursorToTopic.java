package com.bsstokes.learnanything.data.transformers;

import android.database.Cursor;

import com.bsstokes.learnanything.db.Db;
import com.bsstokes.learnanything.db.TableConfig;
import com.bsstokes.learnanything.data.Topic;

import rx.functions.Func1;

public class CursorToTopic implements Func1<Cursor, Topic> {

    public static Topic convert(Cursor cursor) {
        return new CursorToTopic().call(cursor);
    }

    @Override
    public Topic call(Cursor cursor) {
        return Topic.builder()
                .id(Db.getString(cursor, TableConfig.Topics.COLUMN_ID))
                .title(Db.getString(cursor, TableConfig.Topics.COLUMN_TITLE))
                .slug(Db.getString(cursor, TableConfig.Topics.COLUMN_SLUG))
                .kind(Db.getString(cursor, TableConfig.Topics.COLUMN_KIND))
                .topLevel(Db.getBoolean(cursor, TableConfig.Topics.COLUMN_TOP_LEVEL))
                .parentId(Db.getString(cursor, TableConfig.Topics.COLUMN_PARENT_ID))
                .build();
    }
}
