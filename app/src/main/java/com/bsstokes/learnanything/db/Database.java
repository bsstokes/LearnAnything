package com.bsstokes.learnanything.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import com.bsstokes.learnanything.data.Article;
import com.bsstokes.learnanything.data.transformers.ArticleToContentValues;
import com.bsstokes.learnanything.data.transformers.ChildToContentValues;
import com.bsstokes.learnanything.data.transformers.ExerciseToContentValues;
import com.bsstokes.learnanything.data.transformers.TopicToContentValues;
import com.bsstokes.learnanything.data.transformers.VideoToContentValues;
import com.bsstokes.learnanything.data.Child;
import com.bsstokes.learnanything.data.Exercise;
import com.bsstokes.learnanything.data.Topic;
import com.bsstokes.learnanything.data.Video;
import com.squareup.phrase.Phrase;
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
        final String table = TableConfig.Children.TABLE;
        String queryTemplate = "SELECT * FROM {table} WHERE {parent_id_column} = ?";
        String query = Phrase.from(queryTemplate)
                .put("table", table)
                .put("parent_id_column", TableConfig.Children.COLUMN_PARENT_ID)
                .format().toString();

        logQuery(query);

        return getSqlBrite().createQuery(TableConfig.Children.TABLE, query, parentId);
    }

    // Exercises

    public long createOrUpdate(Exercise exercise) {
        return createOrUpdate(TableConfig.Exercises.TABLE, ExerciseToContentValues.convert(exercise));
    }

    // Topics

    public long createOrUpdate(Topic topic) {
        return createOrUpdate(TableConfig.Topics.TABLE, TopicToContentValues.convert(topic));
    }

    public Observable<SqlBrite.Query> getTopLevelTopics() {
        final String table = TableConfig.Topics.TABLE;
        final String queryTemplate = "SELECT * FROM {table} WHERE {top_level_column} = 1";
        String query = Phrase.from(queryTemplate)
                .put("table", table)
                .put("top_level_column", TableConfig.Topics.COLUMN_TOP_LEVEL)
                .format().toString();
        return getSqlBrite().createQuery(table, query);
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

    private void logQuery(String query) {
        log("Query: " + query);
    }

    private void log(String message) {
        Log.d(TAG, message);
    }

    private static final String TAG = "Database";
}
