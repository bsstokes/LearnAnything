package com.bsstokes.learnanything.models;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ArticleTest {

    @Test
    public void testSomething() {
        Article article = Article.builder()
                .id("1")
                .build();

        assertEquals("1", article.getId());
    }
}
