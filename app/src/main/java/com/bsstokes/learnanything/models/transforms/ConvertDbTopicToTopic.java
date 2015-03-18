package com.bsstokes.learnanything.models.transforms;

import com.bsstokes.learnanything.models.Topic;

import rx.functions.Func1;

public class ConvertDbTopicToTopic implements Func1<com.bsstokes.learnanything.db.models.Topic, Topic> {
    @Override
    public Topic call(com.bsstokes.learnanything.db.models.Topic dbTopic) {
        return Topic.builder()
                .id(dbTopic.getId())
                .translatedTitle(dbTopic.getTitle())
                .slug(dbTopic.getSlug())
                .build();
    }
}
