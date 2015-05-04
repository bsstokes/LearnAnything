package com.bsstokes.learnanything.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.bsstokes.learnanything.models.Video;
import com.squareup.sqlbrite.SqlBrite;

import java.io.Closeable;
import java.io.IOException;

import rx.Observable;

public class Database implements Closeable {

    private final SqlBrite sqlBrite;

    public Database(SqlBrite sqlBrite) {
        this.sqlBrite = sqlBrite;
    }

    public SqlBrite getSqlBrite() {
        return sqlBrite;
    }

    @Override
    public void close() throws IOException {
        getSqlBrite().close();
    }

    public long createOrUpdate(Video video) {
        return getSqlBrite().insert(TableConfig.Videos.TABLE, createVideo(video), SQLiteDatabase.CONFLICT_REPLACE);
    }

    public Observable<SqlBrite.Query> getVideoByReadableId(String readableId) {
        String query = "SELECT * FROM " + TableConfig.Videos.TABLE + " WHERE " + TableConfig.Videos.COLUMN_READABLE_ID + " = ?";
        return getSqlBrite().createQuery(TableConfig.Videos.TABLE, query, readableId);
    }

    public Observable<SqlBrite.Query> getAllVideos() {
        String query = "SELECT * FROM " + TableConfig.Videos.TABLE;
        return getSqlBrite().createQuery(TableConfig.Videos.TABLE, query);
    }

    public ContentValues createVideo(Video video) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TableConfig.Videos.COLUMN_ID, video.getId());
        contentValues.put(TableConfig.Videos.COLUMN_TITLE, video.getTitle());
        contentValues.put(TableConfig.Videos.COLUMN_HTML_DESCRIPTION, video.getHtmlDescription());
        contentValues.put(TableConfig.Videos.COLUMN_URL, video.getUrl());
        contentValues.put(TableConfig.Videos.COLUMN_IMAGE_URL, video.getImageUrl());
        contentValues.put(TableConfig.Videos.COLUMN_READABLE_ID, video.getReadableId());
        contentValues.put(TableConfig.Videos.COLUMN_ENTIRE_RECORD, Db.BOOLEAN_FALSE);
        return contentValues;
    }
}