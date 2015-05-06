package com.bsstokes.learnanything.data.transformers;

import android.content.ContentValues;

import com.bsstokes.learnanything.db.TableConfig;
import com.bsstokes.learnanything.models.Exercise;

import rx.functions.Func1;

public class ExerciseToContentValues implements Func1<Exercise, ContentValues> {

    public static ContentValues convert(Exercise exercise) {
        return new ExerciseToContentValues().call(exercise);
    }

    @Override
    public ContentValues call(Exercise exercise) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TableConfig.Exercises.COLUMN_ID, exercise.getId());
        contentValues.put(TableConfig.Exercises.COLUMN_URL, exercise.getUrl());
        contentValues.put(TableConfig.Exercises.COLUMN_DESCRIPTION, exercise.getDescription());
        contentValues.put(TableConfig.Exercises.COLUMN_TITLE, exercise.getTitle());
        contentValues.put(TableConfig.Exercises.COLUMN_IMAGE_URL, exercise.getImageUrl());
        contentValues.put(TableConfig.Exercises.COLUMN_IMAGE_256_URL, exercise.getImage256Url());
        return contentValues;
    }
}
