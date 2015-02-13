package com.bsstokes.learnanything.sync;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

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
        Syncer.syncTopicTree(this);
    }
}
