package com.bsstokes.learnanything.sync;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.bsstokes.learnanything.api.KhanAcademyApi;
import com.bsstokes.learnanything.api.models.Topic;
import com.bsstokes.learnanything.api.models.TopicTree;
import com.bsstokes.learnanything.db.TopicConverter;

import io.realm.Realm;

public class SyncService extends IntentService {
    private static final String ACTION_SYNC_TOPIC_TREE = "com.bsstokes.learnanything.sync.action.SYNC_TOPIC_LIST";

    public static void startActionSyncTopicTree(Context context) {
        Intent intent = new Intent(context, SyncService.class);
        intent.setAction(ACTION_SYNC_TOPIC_TREE);
        context.startService(intent);
    }

    public SyncService() {
        super("SyncService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (null != intent) {
            final String action = intent.getAction();
            if (ACTION_SYNC_TOPIC_TREE.equals(action)) {
                handleActionSyncTopicTree();
            }
        }
    }

    private void handleActionSyncTopicTree() {

        KhanAcademyApi.Client apiClient = new KhanAcademyApi.Client();

        TopicTree topicTree;
        try {
            topicTree = apiClient.getTopicTreeOfKindTopic();
        } catch (KhanAcademyApi.ApiException e) {
            // TODO: Log exception
            return;
        }

        if (null == topicTree) {
            return;
        }

        // Create the realm on this Service's worker thread
        final Realm realm = Realm.getInstance(this);

        for (Topic apiTopic : topicTree.children) {
            com.bsstokes.learnanything.db.models.Topic dbTopic = realm.where(com.bsstokes.learnanything.db.models.Topic.class)
                    .equalTo("id", apiTopic.id)
                    .findFirst();
            if (null == dbTopic) {
                dbTopic = new com.bsstokes.learnanything.db.models.Topic();
                dbTopic.setId(apiTopic.id);
                dbTopic.setTopLevel(true);
            }

            realm.beginTransaction();
            TopicConverter.convert(apiTopic, dbTopic);
            dbTopic.setTopLevel(true);
            realm.copyToRealm(dbTopic);
            realm.commitTransaction();
        }

        realm.close();
    }
}
