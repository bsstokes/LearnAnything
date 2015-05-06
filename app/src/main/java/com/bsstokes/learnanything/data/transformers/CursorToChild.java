package com.bsstokes.learnanything.data.transformers;

import android.database.Cursor;

import com.bsstokes.learnanything.db.Db;
import com.bsstokes.learnanything.db.TableConfig;
import com.bsstokes.learnanything.models.Child;

import rx.functions.Func1;

public class CursorToChild implements Func1<Cursor, Child> {

    @Override
    public Child call(Cursor cursor) {
        return Child.builder()
                .id(Db.getString(cursor, TableConfig.Children.COLUMN_ID))
                .kind(Db.getString(cursor, TableConfig.Children.COLUMN_KIND))
                .hidden(Db.getBoolean(cursor, TableConfig.Children.COLUMN_HIDDEN))
                .key(Db.getString(cursor, TableConfig.Children.COLUMN_KEY))
                .internalId(Db.getString(cursor, TableConfig.Children.COLUMN_INTERNAL_ID))
                .title(Db.getString(cursor, TableConfig.Children.COLUMN_TITLE))
                .url(Db.getString(cursor, TableConfig.Children.COLUMN_URL))
                .translatedTitle(Db.getString(cursor, TableConfig.Children.COLUMN_TRANSLATED_TITLE))
                .nodeSlug(Db.getString(cursor, TableConfig.Children.COLUMN_NODE_SLUG))
                .build();
    }
}
