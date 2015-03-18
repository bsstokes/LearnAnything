package com.bsstokes.learnanything.sync.rx.transforms;

import com.bsstokes.learnanything.api.models.Child;
import com.bsstokes.learnanything.api.models.Topic;

import java.util.List;

import rx.functions.Func1;

public final class TopicToChildList implements Func1<Topic, List<Child>> {
    @Override
    public List<Child> call(Topic topic) {
        return topic.children;
    }
}
