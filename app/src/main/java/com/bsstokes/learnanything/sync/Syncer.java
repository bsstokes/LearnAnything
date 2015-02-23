package com.bsstokes.learnanything.sync;

import android.content.Context;

import com.bsstokes.learnanything.api.KhanAcademyApi;
import com.bsstokes.learnanything.api.models.Topic;
import com.bsstokes.learnanything.api.models.TopicTree;
import com.crashlytics.android.Crashlytics;

import io.realm.Realm;

public class Syncer {

    public static void syncTopicTree(Context context) {
        KhanAcademyApi api = new KhanAcademyApi();

        TopicTree topicTree;
        try {
            topicTree = api.getTopicTreeOfKindTopic();
        } catch (KhanAcademyApi.ApiException e) {
            Crashlytics.log("Failed to get topic tree");
            Crashlytics.logException(e);
            Crashlytics.logException(e.getRetrofitError());
            return;
        }

        if (null == topicTree) {
            Crashlytics.logException(new Exception("Failed to get a topic tree"));
            return;
        }

        // Create the realm on this Service's worker thread
        final Realm realm = Realm.getInstance(context);

        for (Topic apiTopic : topicTree.children) {

            realm.beginTransaction();

            com.bsstokes.learnanything.db.models.Topic dbTopic = new com.bsstokes.learnanything.db.models.Topic();
            Converter.convert(apiTopic, dbTopic);
            dbTopic.setTopLevel(true);


            realm.copyToRealmOrUpdate(dbTopic);
            realm.commitTransaction();
        }

        realm.close();
    }
}
