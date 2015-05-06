package com.bsstokes.learnanything.data.transformers;

import android.content.ContentValues;

import com.bsstokes.learnanything.db.TableConfig;
import com.bsstokes.learnanything.models.Child;

import rx.functions.Func1;

public class ChildToContentValues implements Func1<Child, ContentValues> {

    public static ContentValues convert(Child child) {
        return new ChildToContentValues().call(child);
    }

    @Override
    public ContentValues call(Child child) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TableConfig.Children.COLUMN_ID, child.getId());
        contentValues.put(TableConfig.Children.COLUMN_KIND, child.getKind());
        contentValues.put(TableConfig.Children.COLUMN_HIDDEN, child.isHidden());
        contentValues.put(TableConfig.Children.COLUMN_KEY, child.getKey());
        contentValues.put(TableConfig.Children.COLUMN_INTERNAL_ID, child.getInternalId());
        contentValues.put(TableConfig.Children.COLUMN_TITLE, child.getTitle());
        contentValues.put(TableConfig.Children.COLUMN_URL, child.getUrl());
        contentValues.put(TableConfig.Children.COLUMN_TRANSLATED_TITLE, child.getTranslatedTitle());
        contentValues.put(TableConfig.Children.COLUMN_NODE_SLUG, child.getNodeSlug());
        return contentValues;
    }
}
