package com.bsstokes.learnanything.sync.rx;

import android.util.Log;

import com.bsstokes.learnanything.api.KhanAcademyApi;
import com.bsstokes.learnanything.api.models.Topic;
import com.bsstokes.learnanything.db.RealmUtils;
import com.bsstokes.learnanything.sync.Converter;
import com.bsstokes.learnanything.sync.rx.transforms.TopicTreeToTopicList;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

public class TopicTreeDataSource {

    private final Realm realm;
    private final KhanAcademyApi khanAcademyApi;
    private PublishSubject<List<Topic>> currentTopicTreeRequest;

    public TopicTreeDataSource(KhanAcademyApi khanAcademyApi, Realm realm) {
        this.khanAcademyApi = khanAcademyApi;
        this.realm = realm;
    }

    public Subscription loadTopicTree(EndlessObserver<List<Topic>> observer) {

        RealmResults<com.bsstokes.learnanything.db.models.Topic> dbTopics = RealmUtils.findAllTopLevelTopics(realm);
        if (!dbTopics.isEmpty()) {
            List<Topic> topics = new ArrayList<>();
            for (com.bsstokes.learnanything.db.models.Topic dbTopic : dbTopics) {
                Log.d("RX", "Got topic from database: " + dbTopic.getId());
                Topic topic = new Topic();
                Converter.convert(dbTopic, topic);
                topics.add(topic);
            }

            observer.onNext(topics);
        }

        PublishSubject<List<Topic>> topicTreeRequest = currentTopicTreeRequest;
        if (topicTreeRequest != null) {
            // There's an in-flight network request already. Join it.
            return topicTreeRequest.subscribe(observer);
        }

        topicTreeRequest = PublishSubject.create();
        currentTopicTreeRequest = topicTreeRequest;

        Subscription subscription = topicTreeRequest.subscribe(observer);

        topicTreeRequest.subscribe(new EndObserver<List<Topic>>() {
            @Override
            public void onEnd() {
                currentTopicTreeRequest = null;
            }

            @Override
            public void onNext(List<Topic> topics) {
                saveTopics(topics);
            }
        });

        khanAcademyApi.getTopicTreeOfKindTopicObservable()
                .map(new TopicTreeToTopicList())
                .flatMap(new Func1<List<Topic>, Observable<Topic>>() {
                    @Override
                    public Observable<Topic> call(List<Topic> topics) {
                        return Observable.from(topics);
                    }
                })
                .filter(new Func1<Topic, Boolean>() {
                    @Override
                    public Boolean call(Topic topic) {
                        return topic.isTopic();
                    }
                })
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(topicTreeRequest);

        return subscription;
    }

    private void saveTopics(final List<Topic> topics) {
        final List<com.bsstokes.learnanything.db.models.Topic> dbTopics = new ArrayList<>();
        for (Topic topic : topics) {
            com.bsstokes.learnanything.db.models.Topic dbTopic = new com.bsstokes.learnanything.db.models.Topic();
            Converter.convert(topic, dbTopic);
            dbTopic.setTopLevel(true);
            dbTopics.add(dbTopic);
        }

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (com.bsstokes.learnanything.db.models.Topic dbTopic : dbTopics) {
                    realm.copyToRealmOrUpdate(dbTopic);
                }
            }
        });
    }
}
