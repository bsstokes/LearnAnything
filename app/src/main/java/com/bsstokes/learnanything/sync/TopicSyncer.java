package com.bsstokes.learnanything.sync;

import android.content.Context;

import com.bsstokes.learnanything.api.KhanAcademyApi;
import com.bsstokes.learnanything.api.models.Topic;
import com.bsstokes.learnanything.db.models.Article;
import com.bsstokes.learnanything.db.models.Child;
import com.bsstokes.learnanything.db.models.Exercise;
import com.bsstokes.learnanything.db.models.Video;
import com.crashlytics.android.Crashlytics;

import io.realm.Realm;

public class TopicSyncer {

    public static void syncTopic(Context context, String topicSlug, final boolean isTopLevel) {
        KhanAcademyApi api = new KhanAcademyApi();

        final Topic topic;
        try {
            topic = api.getTopic(topicSlug);
        } catch (KhanAcademyApi.ApiException e) {
            Crashlytics.log("Failed to get topic");
            Crashlytics.logException(e);
            Crashlytics.logException(e.getRetrofitError());
            return;
        }

        if (null == topic) {
            Crashlytics.logException(new Exception("Failed to get a topic"));
            return;
        }

        // Create the realm on this Service's worker thread
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

                        } else if ("Video".equalsIgnoreCase(kind)) {

                            Video video = realm.where(Video.class).equalTo("id", apiChild.internal_id).findFirst();
                            if (null == video) {
                                video = new Video();
                                Converter.convert(apiChild, video);
                                video = realm.copyToRealm(video);
                            }

                            dbChild.setVideo(video);

                        } else if ("Exercise".equalsIgnoreCase(kind)) {

                            Exercise exercise = realm.where(Exercise.class).equalTo("id", apiChild.internal_id).findFirst();
                            if (null == exercise) {
                                exercise = new com.bsstokes.learnanything.db.models.Exercise();
                                Converter.convert(apiChild, exercise);
                                exercise = realm.copyToRealm(exercise);
                            }

                            dbChild.setExercise(exercise);

                        } else if ("Article".equalsIgnoreCase(kind)) {

                            Article article = realm.where(Article.class).equalTo("id", apiChild.internal_id).findFirst();
                            if (null == article) {
                                article = new Article();
                                Converter.convert(apiChild, article);
                                article = realm.copyToRealm(article);
                            }

                            dbChild.setArticle(article);
                        }
                    }
                }
            });
        }
    }
}
