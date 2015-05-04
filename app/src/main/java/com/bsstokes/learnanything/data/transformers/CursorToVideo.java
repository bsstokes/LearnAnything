package com.bsstokes.learnanything.data.transformers;

import android.database.Cursor;

import com.bsstokes.learnanything.db.Db;
import com.bsstokes.learnanything.db.TableConfig;
import com.bsstokes.learnanything.models.Video;

import rx.functions.Func1;

public class CursorToVideo implements Func1<Cursor, Video> {

    @Override
    public Video call(Cursor cursor) {
        return Video.builder()
                .id(Db.getString(cursor, TableConfig.Videos.COLUMN_ID))
                .title(Db.getString(cursor, TableConfig.Videos.COLUMN_TITLE))
                .htmlDescription(Db.getString(cursor, TableConfig.Videos.COLUMN_HTML_DESCRIPTION))
                .url(Db.getString(cursor, TableConfig.Videos.COLUMN_URL))
                .imageUrl(Db.getString(cursor, TableConfig.Videos.COLUMN_IMAGE_URL))
                .readableId(Db.getString(cursor, TableConfig.Videos.COLUMN_READABLE_ID))
                .build();
    }
}
