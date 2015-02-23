package com.bsstokes.learnanything.db;

import com.bsstokes.learnanything.db.models.Topic;

import io.realm.Realm;
import io.realm.RealmResults;

public class RealmUtils {

    public static RealmResults<Topic> findAllTopLevelTopics(Realm realm) {
        return realm.where(Topic.class)
                .equalTo("topLevel", true)
                .findAll();
    }
}
