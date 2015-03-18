package com.bsstokes.learnanything.models;

import junit.framework.TestCase;

public class ArticleTest extends TestCase {

    public void testSomething() {
        Article article = Article.builder()
                .id("1")
                .build();

        assertEquals("1", article.getId());
    }
}
