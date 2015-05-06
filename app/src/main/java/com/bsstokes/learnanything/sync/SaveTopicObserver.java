package com.bsstokes.learnanything.sync;

import android.content.Context;

import com.bsstokes.learnanything.api.models.Topic;
import com.bsstokes.learnanything.data.transformers.ApiChildToChild;
import com.bsstokes.learnanything.db.Database;
import com.bsstokes.learnanything.db.models.Child;
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
            com.bsstokes.learnanything.models.Child child = new ApiChildToChild().call(apiChild);
            database.createOrUpdate(child);
        }

        try (final Realm realm = Realm.getInstance(context)) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    com.bsstokes.learnanything.db.models.Topic dbTopic = new com.bsstokes.learnanything.db.models.Topic();
                    Converter.convert(topic, dbTopic);
                    dbTopic.setTopLevel(isTopLevel);
                    dbTopic = realm.copyToRealmOrUpdate(dbTopic);

                    for (com.bsstokes.learnanything.api.models.Child apiChild : topic.children) {
                        Child dbChild = new Child();
                        Converter.convert(apiChild, dbChild);
                        dbChild = realm.copyToRealmOrUpdate(dbChild);
                        dbTopic.getChildren().add(dbChild);

                        String kind = dbChild.getKind();
                        if ("Topic".equalsIgnoreCase(kind)) {

                            com.bsstokes.learnanything.db.models.Topic topic = realm.where(com.bsstokes.learnanything.db.models.Topic.class).equalTo("id", apiChild.internal_id).findFirst();
                            if (null == topic) {
                                topic = new com.bsstokes.learnanything.db.models.Topic();
                                Converter.convert(apiChild, topic);
                                topic = realm.copyToRealm(topic);
                            }

                            dbChild.setTopic(topic);
                        }
                    }
                }
            });
        }
    }
}
