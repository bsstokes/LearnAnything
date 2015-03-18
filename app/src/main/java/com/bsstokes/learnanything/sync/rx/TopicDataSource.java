package com.bsstokes.learnanything.sync.rx;

import android.util.Log;

import com.bsstokes.learnanything.api.KhanAcademyApi;
import com.bsstokes.learnanything.api.models.Child;
import com.bsstokes.learnanything.api.models.Topic;
import com.bsstokes.learnanything.sync.Converter;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

public class TopicDataSource {

    private final Realm realm;
    private final KhanAcademyApi khanAcademyApi;
    private PublishSubject<Topic> currentTopicRequest;

    public TopicDataSource(KhanAcademyApi khanAcademyApi, Realm realm) {
        this.khanAcademyApi = khanAcademyApi;
        this.realm = realm;
    }

    public Subscription loadTopic(String topicSlug, final boolean isTopLevel, EndlessObserver<Topic> observer) {

        com.bsstokes.learnanything.db.models.Topic dbTopic = realm.where(com.bsstokes.learnanything.db.models.Topic.class)
                .equalTo("slug", topicSlug)
                .findFirst();
        if (null != dbTopic) {
            Topic topic = new Topic();
            topic.children = new ArrayList<>();
            Converter.convert(dbTopic, topic);

            RealmList<com.bsstokes.learnanything.db.models.Child> dbChildren = dbTopic.getChildren();
            if (!dbChildren.isEmpty()) {
                for (com.bsstokes.learnanything.db.models.Child dbChild : dbChildren) {
                    Log.d("RX", "Got child from database: " + dbChild.getId());
                    Child child = new Child();
                    Converter.convert(dbChild, child);
                    topic.children.add(child);
                }

                observer.onNext(topic);
            }
        }

        PublishSubject<Topic> topicRequest = currentTopicRequest;
        if (topicRequest != null) {
            // There's an in-flight network request already. Join it.
            return topicRequest.subscribe(observer);
        }

        topicRequest = PublishSubject.create();
        currentTopicRequest = topicRequest;

        Subscription subscription = topicRequest.subscribe(observer);

        topicRequest.subscribe(new EndObserver<Topic>() {
            @Override
            public void onEnd() {
                currentTopicRequest = null;
            }

            @Override
            public void onNext(Topic topic) {
                saveTopic(topic, isTopLevel);
            }
        });

        khanAcademyApi.getTopicObservable(topicSlug)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(topicRequest);

        return subscription;
    }

    private void saveTopic(final Topic topic, final boolean isTopLevel) {
        com.bsstokes.learnanything.db.models.Topic dbTopic = realm.where(com.bsstokes.learnanything.db.models.Topic.class).equalTo("id", topic.id).findFirst();
        if (null == dbTopic) {
            dbTopic = new com.bsstokes.learnanything.db.models.Topic();
        }

        Converter.convert(topic, dbTopic);
        dbTopic.setTopLevel(isTopLevel);

        final com.bsstokes.learnanything.db.models.Topic finalDbTopic = dbTopic;
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                com.bsstokes.learnanything.db.models.Topic dbTopic = realm.copyToRealmOrUpdate(finalDbTopic);
                saveChildren(dbTopic, topic.children);
            }
        });
    }

    // Inside of transaction
    private void saveChildren(final com.bsstokes.learnanything.db.models.Topic dbTopic, final List<Child> children) {
        for (Child child : children) {

            com.bsstokes.learnanything.db.models.Child dbChild = realm.where(com.bsstokes.learnanything.db.models.Child.class).equalTo("internal_id", child.internal_id).findFirst();
            if (null == dbChild) {
                dbChild = new com.bsstokes.learnanything.db.models.Child();
            }

            Converter.convert(child, dbChild);
            dbChild = realm.copyToRealmOrUpdate(dbChild);
            dbTopic.getChildren().add(dbChild);
        }
    }
}
