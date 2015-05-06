package com.bsstokes.learnanything.data.transformers;

import com.bsstokes.learnanything.data.Topic;

import rx.functions.Func1;

public class ApiTopicToTopic implements Func1<com.bsstokes.learnanything.api.models.Topic, Topic> {

    public static Topic convert(com.bsstokes.learnanything.api.models.Topic topic, boolean isTopLevel) {
        return new ApiTopicToTopic(isTopLevel).call(topic);
    }

    private final boolean isTopLevel;

    public ApiTopicToTopic(boolean isTopLevel) {
        this.isTopLevel = isTopLevel;
    }

    @Override
    public Topic call(com.bsstokes.learnanything.api.models.Topic topic) {
        return Topic.builder()
                .id(topic.id)
                .title(topic.translated_title)
                .slug(topic.slug)
                .kind(topic.kind)
                .topLevel(isTopLevel)
                .parentId(null)
                .build();
    }
}
