package com.bsstokes.learnanything.models;

import junit.framework.TestCase;

public class TopicTest extends TestCase {

    public void testSmoke() {
        Topic topic = Topic.builder()
                .id("123")
                .translatedTitle("Title")
//                .slug("test")
                .build();

        assertEquals("123", topic.getId());
        assertNull(topic.getSlug());
        assertNotNull(topic.getId());
        assertEquals(3, topic.getId().length());
    }

    public void testNull() {
        Topic topic = Topic.builder()
                .id(null)
                .translatedTitle("Title")
                .build();
        assertNull(topic.getId());
    }
}