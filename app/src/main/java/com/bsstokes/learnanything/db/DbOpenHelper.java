package com.bsstokes.learnanything.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public final class DbOpenHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "database.db";

    public DbOpenHelper(Context context) {
        super(context, DATABASE_NAME, null /* factory */, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TableConfig.Children.CREATE);
        db.execSQL(TableConfig.Articles.CREATE);
        db.execSQL(TableConfig.Videos.CREATE);
        db.execSQL(TableConfig.Exercises.CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Do nothing
    }
}
