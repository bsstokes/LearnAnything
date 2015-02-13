package com.bsstokes.learnanything.sync;

public class Converter {

    public static void convert(
            com.bsstokes.learnanything.api.models.Topic apiTopic,
            com.bsstokes.learnanything.db.models.Topic dbTopic) {

        dbTopic.setId(apiTopic.id);
        dbTopic.setTitle(apiTopic.translated_title);
        dbTopic.setSlug(apiTopic.slug);
    }
}
