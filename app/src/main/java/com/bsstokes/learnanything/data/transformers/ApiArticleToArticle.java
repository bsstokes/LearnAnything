package com.bsstokes.learnanything.data.transformers;

import com.bsstokes.learnanything.data.Article;

import rx.functions.Func1;

public class ApiArticleToArticle implements Func1<com.bsstokes.learnanything.api.models.Article, Article> {

    @Override
    public Article call(com.bsstokes.learnanything.api.models.Article apiArticle) {
        return Article.builder()
                .id(apiArticle.id)
                .title(apiArticle.translated_title)
                .relativeUrl(apiArticle.relative_url)
                .htmlContent(apiArticle.translated_html_content)
                .contentId(apiArticle.content_id)
                .build();
    }
}
