package com.bsstokes.learnanything.data.transformers;

import android.content.ContentValues;

import com.bsstokes.learnanything.db.Db;
import com.bsstokes.learnanything.db.TableConfig;
import com.bsstokes.learnanything.models.Video;

import rx.functions.Func1;

public class VideoToContentValues implements Func1<Video, ContentValues> {

    public static ContentValues convert(Video video) {
        return new VideoToContentValues().call(video);
    }

    @Override
    public ContentValues call(Video video) {
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
