package com.bsstokes.learnanything.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.bsstokes.learnanything.data.transformers.ArticleToContentValues;
import com.bsstokes.learnanything.data.transformers.ChildToContentValues;
import com.bsstokes.learnanything.data.transformers.ExerciseToContentValues;
import com.bsstokes.learnanything.data.transformers.VideoToContentValues;
import com.bsstokes.learnanything.models.Article;
import com.bsstokes.learnanything.models.Child;
import com.bsstokes.learnanything.models.Exercise;
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
        return createOrUpdate(TableConfig.Children.TABLE, ChildToContentValues.convert(child));
    }

    public Observable<SqlBrite.Query> getChildren(@NonNull String parentId) {
        String query = "SELECT * FROM " + TableConfig.Children.TABLE
                + " WHERE "
                + TableConfig.Children.COLUMN_PARENT_ID + " = ?";
        return getSqlBrite().createQuery(TableConfig.Children.TABLE, query, parentId);
    }

    // Exercises

    public long createOrUpdate(Exercise exercise) {
        return createOrUpdate(TableConfig.Exercises.TABLE, ExerciseToContentValues.convert(exercise));
    }

    // Videos

    public long createOrUpdate(Video video) {
        return getSqlBrite().insert(TableConfig.Videos.TABLE, VideoToContentValues.convert(video), SQLiteDatabase.CONFLICT_REPLACE);
    }

    public Observable<SqlBrite.Query> getVideoByReadableId(String readableId) {
        String query = "SELECT * FROM " + TableConfig.Videos.TABLE + " WHERE " + TableConfig.Videos.COLUMN_READABLE_ID + " = ?";
        return getSqlBrite().createQuery(TableConfig.Videos.TABLE, query, readableId);
    }

    public Observable<SqlBrite.Query> getAllVideos() {
        String query = "SELECT * FROM " + TableConfig.Videos.TABLE;
        return getSqlBrite().createQuery(TableConfig.Videos.TABLE, query);
    }

    // Helpers

    private long createOrUpdate(String table, ContentValues values) {
        return getSqlBrite().insert(table, values, SQLiteDatabase.CONFLICT_REPLACE);
    }
}
