package com.bsstokes.learnanything.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.bsstokes.learnanything.data.transformers.ArticleToContentValues;
import com.bsstokes.learnanything.data.transformers.ChildToContentValues;
import com.bsstokes.learnanything.models.Article;
import com.bsstokes.learnanything.models.Child;
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

    // Articles

    public long createOrUpdate(Article article) {
        return createOrUpdate(TableConfig.Articles.TABLE, ArticleToContentValues.convert(article));
    }

    // Children

    public long createOrUpdate(Child child) {
        return getSqlBrite().insert(TableConfig.Children.TABLE, ChildToContentValues.convert(child), SQLiteDatabase.CONFLICT_REPLACE);
    }

    // Videos

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

    private long createOrUpdate(String table, ContentValues values) {
        return getSqlBrite().insert(table, values, SQLiteDatabase.CONFLICT_REPLACE);
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
