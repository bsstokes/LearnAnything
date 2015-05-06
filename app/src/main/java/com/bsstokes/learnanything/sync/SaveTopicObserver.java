package com.bsstokes.learnanything.sync;

import com.bsstokes.learnanything.data.transformers.ApiChildToChild;
import com.bsstokes.learnanything.data.transformers.ApiTopicToTopic;
import com.bsstokes.learnanything.db.Database;
import com.bsstokes.learnanything.models.Topic;
import com.bsstokes.learnanything.sync.rx.EndlessObserver;

public class SaveTopicObserver extends EndlessObserver<com.bsstokes.learnanything.api.models.Topic> {

    private final Database database;
    private final boolean isTopLevel;

    public SaveTopicObserver(Database database, boolean isTopLevel) {
        this.database = database;
        this.isTopLevel = isTopLevel;
    }

    @Override
    public void onNext(final com.bsstokes.learnanything.api.models.Topic apiTopic) {

        Topic topic = ApiTopicToTopic.convert(apiTopic, isTopLevel);
        database.createOrUpdate(topic);

        for (com.bsstokes.learnanything.api.models.Child apiChild : apiTopic.children) {
            com.bsstokes.learnanything.models.Child child = ApiChildToChild.convert(apiChild, apiTopic.id);
            database.createOrUpdate(child);
        }
    }
}
