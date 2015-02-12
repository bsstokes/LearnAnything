package com.bsstokes.learnanything.sync;

import com.bsstokes.learnanything.db.models.Topic;

public class TopicConverter {

    public static void convert(
            com.bsstokes.learnanything.api.models.Topic apiTopic,
            Topic dbTopic) {

        dbTopic.setId(apiTopic.id);
        dbTopic.setTitle(apiTopic.translated_title);
        dbTopic.setSlug(apiTopic.slug);
    }
}
