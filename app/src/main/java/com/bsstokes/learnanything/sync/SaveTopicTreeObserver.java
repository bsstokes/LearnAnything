package com.bsstokes.learnanything.sync;

import com.bsstokes.learnanything.api.models.TopicTree;
import com.bsstokes.learnanything.data.transformers.ApiTopicToTopic;
import com.bsstokes.learnanything.db.Database;
import com.bsstokes.learnanything.models.Topic;
import com.bsstokes.learnanything.sync.rx.EndlessObserver;

public class SaveTopicTreeObserver extends EndlessObserver<TopicTree> {

    private final Database database;

    public SaveTopicTreeObserver(Database database) {
        this.database = database;
    }

    @Override
    public void onNext(final TopicTree topicTree) {
        for (com.bsstokes.learnanything.api.models.Topic apiTopic : topicTree.children) {
            Topic topic = ApiTopicToTopic.convert(apiTopic, true);
            database.createOrUpdate(topic);
        }
    }
}
