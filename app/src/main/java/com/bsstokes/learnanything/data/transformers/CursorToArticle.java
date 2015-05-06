package com.bsstokes.learnanything.data.transformers;

import android.database.Cursor;

import com.bsstokes.learnanything.data.Article;
import com.bsstokes.learnanything.db.Db;
import com.bsstokes.learnanything.db.TableConfig;

import rx.functions.Func1;

public class CursorToArticle implements Func1<Cursor, Article> {

    @Override
    public Article call(Cursor cursor) {
        return Article.builder()
                .id(Db.getString(cursor, TableConfig.Articles.COLUMN_ID))
                .title(Db.getString(cursor, TableConfig.Articles.COLUMN_TITLE))
                .relativeUrl(Db.getString(cursor, TableConfig.Articles.COLUMN_RELATIVE_URL))
                .htmlContent(Db.getString(cursor, TableConfig.Articles.COLUMN_HTML_CONTENT))
                .contentId(Db.getString(cursor, TableConfig.Articles.COLUMN_CONTENT_ID))
                .build();
    }
}
