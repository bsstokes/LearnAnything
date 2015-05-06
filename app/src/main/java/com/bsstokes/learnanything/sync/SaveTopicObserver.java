package com.bsstokes.learnanything.sync;

import android.content.Context;

import com.bsstokes.learnanything.api.models.Topic;
import com.bsstokes.learnanything.data.transformers.ApiChildToChild;
import com.bsstokes.learnanything.db.Database;
import com.bsstokes.learnanything.sync.rx.EndlessObserver;

import io.realm.Realm;

public class SaveTopicObserver extends EndlessObserver<Topic> {

    private final Context context;
    private final Database database;
    private final boolean isTopLevel;

    public SaveTopicObserver(Context context, Database database, boolean isTopLevel) {
        this.context = context;
        this.database = database;
        this.isTopLevel = isTopLevel;
    }

    @Override
    public void onNext(final Topic topic) {

        for (com.bsstokes.learnanything.api.models.Child apiChild : topic.children) {
            com.bsstokes.learnanything.models.Child child = ApiChildToChild.convert(apiChild, topic.id);
            database.createOrUpdate(child);
        }

        try (final Realm realm = Realm.getInstance(context)) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    com.bsstokes.learnanything.db.models.Topic dbTopic = new com.bsstokes.learnanything.db.models.Topic();
                    Converter.convert(topic, dbTopic);
                    dbTopic.setTopLevel(isTopLevel);
                    realm.copyToRealmOrUpdate(dbTopic);
                }
            });
        }
    }
}
