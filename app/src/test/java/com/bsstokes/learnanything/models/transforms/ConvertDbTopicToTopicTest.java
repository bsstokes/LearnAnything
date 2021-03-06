package com.bsstokes.learnanything.models.transforms;

import com.bsstokes.learnanything.data.Topic;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ConvertDbTopicToTopicTest {

    @Test
    public void testShouldConvertCorrectly() {

        com.bsstokes.learnanything.db.models.Topic dbTopic = new com.bsstokes.learnanything.db.models.Topic();
        dbTopic.setId("123");
        dbTopic.setTitle("title");
        dbTopic.setSlug("slug");

        Topic topic = new ConvertDbTopicToTopic().call(dbTopic);

        assertEquals(dbTopic.getId(), topic.getId());
        assertEquals(dbTopic.getTitle(), topic.getTranslatedTitle());
        assertEquals(dbTopic.getSlug(), topic.getSlug());
    }
}