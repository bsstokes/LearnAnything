package com.bsstokes.learnanything.db;

public class TopicConverter {

    public static void convert(
            com.bsstokes.learnanything.api.models.Topic apiTopic,
            com.bsstokes.learnanything.db.Topic dbTopic) {

        dbTopic.setId(apiTopic.id);
        dbTopic.setTitle(apiTopic.translated_title);
        dbTopic.setSlug(apiTopic.slug);
    }
}
