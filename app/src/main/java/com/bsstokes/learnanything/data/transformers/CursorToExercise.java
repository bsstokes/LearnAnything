package com.bsstokes.learnanything.data.transformers;

import android.database.Cursor;

import com.bsstokes.learnanything.db.Db;
import com.bsstokes.learnanything.db.TableConfig;
import com.bsstokes.learnanything.data.Exercise;

import rx.functions.Func1;

public class CursorToExercise implements Func1<Cursor, Exercise> {

    @Override
    public Exercise call(Cursor cursor) {
        return Exercise.builder()
                .id(Db.getString(cursor, TableConfig.Exercises.COLUMN_ID))
                .url(Db.getString(cursor, TableConfig.Exercises.COLUMN_URL))
                .description(Db.getString(cursor, TableConfig.Exercises.COLUMN_DESCRIPTION))
                .title(Db.getString(cursor, TableConfig.Exercises.COLUMN_TITLE))
                .imageUrl(Db.getString(cursor, TableConfig.Exercises.COLUMN_IMAGE_URL))
                .image256Url(Db.getString(cursor, TableConfig.Exercises.COLUMN_IMAGE_256_URL))
                .build();
    }
}
