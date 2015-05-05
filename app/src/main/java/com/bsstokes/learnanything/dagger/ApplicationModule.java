package com.bsstokes.learnanything.dagger;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import com.bsstokes.learnanything.db.DbOpenHelper;
import com.bsstokes.learnanything.ui.MainApplication;
import com.squareup.sqlbrite.SqlBrite;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {
    private final MainApplication application;

    public ApplicationModule(MainApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    @ForApplication
    Context provideApplicationContext() {
        return application;
    }

    @Provides
    @Singleton
    SQLiteOpenHelper provideSQLiteOpenHelper() {
        return new DbOpenHelper(application);
    }

    @Provides
    @Singleton
    SqlBrite provideSqlBrite(SQLiteOpenHelper sqliteOpenHelper) {
        return SqlBrite.create(sqliteOpenHelper);
    }
}
