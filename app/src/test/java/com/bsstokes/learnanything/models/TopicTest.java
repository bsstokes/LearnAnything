package com.bsstokes.learnanything.models;

import com.bsstokes.learnanything.data.Topic;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class TopicTest {

    @Test
    public void testSmoke() {
        Topic topic = Topic.builder()
                .id("123")
                .translatedTitle("Title")
                .build();

        assertEquals("123", topic.getId());
        assertNull(topic.getSlug());
        assertNotNull(topic.getId());
        assertEquals(3, topic.getId().length());
    }

    @Test
    public void testNull() {
        Topic topic = Topic.builder()
                .id(null)
                .translatedTitle("Title")
                .build();
        assertNull(topic.getId());
    }
}
