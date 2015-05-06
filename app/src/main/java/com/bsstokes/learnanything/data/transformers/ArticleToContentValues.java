package com.bsstokes.learnanything.data.transformers;

import android.content.ContentValues;

import com.bsstokes.learnanything.db.TableConfig;
import com.bsstokes.learnanything.models.Article;

import rx.functions.Func1;

public class ArticleToContentValues implements Func1<Article, ContentValues> {

    public static ContentValues convert(Article article) {
        return new ArticleToContentValues().call(article);
    }

    @Override
    public ContentValues call(Article article) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TableConfig.Articles.COLUMN_ID, article.getId());
        contentValues.put(TableConfig.Articles.COLUMN_TITLE, article.getTitle());
        contentValues.put(TableConfig.Articles.COLUMN_RELATIVE_URL, article.getRelativeUrl());
        contentValues.put(TableConfig.Articles.COLUMN_HTML_CONTENT, article.getHtmlContent());
        contentValues.put(TableConfig.Articles.COLUMN_CONTENT_ID, article.getContentId());
        return contentValues;
    }
}
