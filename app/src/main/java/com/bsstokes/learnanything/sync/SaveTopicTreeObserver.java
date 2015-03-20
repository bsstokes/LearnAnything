package com.bsstokes.learnanything.sync;

import android.content.Context;

import com.bsstokes.learnanything.api.models.Topic;
import com.bsstokes.learnanything.api.models.TopicTree;
import com.bsstokes.learnanything.sync.rx.EndlessObserver;

import io.realm.Realm;

public class SaveTopicTreeObserver extends EndlessObserver<TopicTree> {

    private Context context;

    public SaveTopicTreeObserver(Context context) {
        this.context = context;
    }

    @Override
    public void onNext(final TopicTree topicTree) {
        try (Realm realm = Realm.getInstance(context)) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    for (Topic apiTopic : topicTree.children) {

                        com.bsstokes.learnanything.db.models.Topic dbTopic = new com.bsstokes.learnanything.db.models.Topic();
                        Converter.convert(apiTopic, dbTopic);
                        dbTopic.setTopLevel(true);

                        realm.copyToRealmOrUpdate(dbTopic);
                    }
                }
            });
        }
    }
}
