package com.bsstokes.learnanything.sync;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.bsstokes.learnanything.api.KhanAcademyApi;
import com.bsstokes.learnanything.db.Database;
import com.bsstokes.learnanything.ui.MainApplication;

import javax.inject.Inject;

import rx.Observable;

public class SyncService extends IntentService {
    private static final String ACTION_SYNC_TOPIC_TREE = "com.bsstokes.learnanything.sync.action.SYNC_TOPIC_LIST";
    private static final String ACTION_SYNC_TOPIC = "com.bsstokes.learnanything.sync.action.SYNC_TOPIC";

    private static final String EXTRA_TOPIC_SLUG = "com.bsstokes.learnanything.sync.extra.EXTRA_TOPIC_SLUG";
    private static final String EXTRA_IS_TOP_LEVEL = "com.bsstokes.learnanything.sync.extra.EXTRA_IS_TOP_LEVEL";

    public static void startActionSyncTopicTree(Context context) {
        Intent intent = new Intent(context, SyncService.class);
        intent.setAction(ACTION_SYNC_TOPIC_TREE);
        context.startService(intent);
    }

    public static void startActionSyncTopic(Context context, String topicSlug, boolean isTopLevel) {
        Intent intent = new Intent(context, SyncService.class);
        intent.setAction(ACTION_SYNC_TOPIC);
        intent.putExtra(EXTRA_TOPIC_SLUG, topicSlug);
        intent.putExtra(EXTRA_IS_TOP_LEVEL, isTopLevel);
        context.startService(intent);
    }

    @Inject
    KhanAcademyApi khanAcademyApi;

    @Inject
    Database database;

    public SyncService() {
        super("SyncService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ((MainApplication) getApplication()).component().inject(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (null != intent) {
            final String action = intent.getAction();
            if (ACTION_SYNC_TOPIC_TREE.equals(action)) {
                handleActionSyncTopicTree();
            } else if (ACTION_SYNC_TOPIC.equals(action)) {
                String topicSlug = intent.getStringExtra(EXTRA_TOPIC_SLUG);
                boolean isTopLevel = intent.getBooleanExtra(EXTRA_IS_TOP_LEVEL, false);
                handleActionSyncTopic(topicSlug, isTopLevel);
            }
        }
    }

    private void handleActionSyncTopicTree() {
        Observable.just(khanAcademyApi.getTopicTreeOfKindTopic())
                .subscribe(new SaveTopicTreeObserver(database));
    }

    private void handleActionSyncTopic(String topicSlug, boolean isTopLevel) {
        Observable.just(khanAcademyApi.getTopic(topicSlug))
                .subscribe(new SaveTopicObserver(database, isTopLevel));
    }
}
