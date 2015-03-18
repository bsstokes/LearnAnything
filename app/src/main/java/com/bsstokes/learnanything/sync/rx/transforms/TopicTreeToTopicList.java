package com.bsstokes.learnanything.sync.rx.transforms;

import com.bsstokes.learnanything.api.models.Topic;
import com.bsstokes.learnanything.api.models.TopicTree;

import java.util.List;

import rx.functions.Func1;

public final class TopicTreeToTopicList implements Func1<TopicTree, List<Topic>> {
    @Override
    public List<Topic> call(TopicTree topicTree) {
        return topicTree.children;
    }
}
