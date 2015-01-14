package com.bsstokes.learnanything.api;

import com.bsstokes.learnanything.BuildConfig;
import com.bsstokes.learnanything.api.models.Child;
import com.bsstokes.learnanything.api.models.Topic;
import com.bsstokes.learnanything.api.models.TopicTree;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapter;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

public class KhanAcademyApi {

    protected interface KhanAcademyService {
        @GET("/topictree")
        void getTopicTree(@Query("kind") String kind, Callback<TopicTree> callback);

        @GET("/topic/{topic_slug}")
        void getTopic(@Path("topic_slug") String topicSlug, Callback<Topic> callback);
    }

    public static class Client {

        protected KhanAcademyService mService;

        public Client() {

            RuntimeTypeAdapter<Child> childAdapter =
                    RuntimeTypeAdapter.create(Child.class, Child.Unknown.class, "kind")
                            .registerSubtype(Child.Topic.class, "Topic")
                            .registerSubtype(Child.Video.class, "Video")
                            .registerSubtype(Child.Exercise.class, "Exercise")
                            .registerSubtype(Child.Article.class, "Article");

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Child.class, childAdapter)
                    .create();

            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint("http://www.khanacademy.org/api/v1/")
                    .setConverter(new GsonConverter(gson))
                    .setLogLevel(BuildConfig.DEBUG ? RestAdapter.LogLevel.BASIC : RestAdapter.LogLevel.NONE)
                    .build();

            mService = restAdapter.create(KhanAcademyService.class);
        }

        public void getTopicTreeOfKindTopic(Callback<TopicTree> callback) {
            mService.getTopicTree("Topic", callback);
        }

        public void getTopic(String topicSlug, Callback<Topic> callback) {
            mService.getTopic(topicSlug, callback);
        }
    }
}
