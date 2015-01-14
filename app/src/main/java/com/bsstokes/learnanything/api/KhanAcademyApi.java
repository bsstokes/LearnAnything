package com.bsstokes.learnanything.api;

import com.bsstokes.learnanything.api.models.Topic;
import com.bsstokes.learnanything.api.models.TopicTree;

import retrofit.Callback;
import retrofit.RestAdapter;
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
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint("http://www.khanacademy.org/api/v1/")
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
