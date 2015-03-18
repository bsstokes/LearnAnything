package com.bsstokes.learnanything.api;

import com.bsstokes.learnanything.BuildConfig;
import com.bsstokes.learnanything.api.models.Article;
import com.bsstokes.learnanything.api.models.Exercise;
import com.bsstokes.learnanything.api.models.Topic;
import com.bsstokes.learnanything.api.models.TopicTree;
import com.bsstokes.learnanything.api.models.Video;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

public class KhanAcademyApi {

    protected interface KhanAcademyService {
        @GET("/topictree")
        void getTopicTree(@Query("kind") String kind, Callback<TopicTree> callback);

        @GET("/topictree")
        TopicTree getTopicTree(@Query("kind") String kind);

        @GET("/topictree")
        Observable<TopicTree> getTopicTreeObservable(@Query("kind") String kind);

        @GET("/topic/{topic_slug}")
        void getTopic(@Path("topic_slug") String topicSlug, Callback<Topic> callback);

        @GET("/topic/{topic_slug}")
        Topic getTopic(@Path("topic_slug") String topicSlug);

        @GET("/topic/{topic_slug}")
        Observable<Topic> getTopicObservable(@Path("topic_slug") String topicSlug);

        @GET("/videos/{video_id}")
        void getVideo(@Path("video_id") String videoId, Callback<Video> callback);

        @GET("/exercises/{exercise_name}")
        void getExercise(@Path("exercise_name") String exerciseName, Callback<Exercise> callback);

        @GET("/articles/{article_internal_id}")
        void getArticle(@Path("article_internal_id") String articleInternalId, Callback<Article> callback);
    }

    private KhanAcademyService mService;

    public KhanAcademyApi() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://www.khanacademy.org/api/v1/")
                .setLogLevel(BuildConfig.DEBUG ? RestAdapter.LogLevel.BASIC : RestAdapter.LogLevel.NONE)
                .build();

        mService = restAdapter.create(KhanAcademyService.class);
    }

    public void getTopicTreeOfKindTopic(Callback<TopicTree> callback) {
        mService.getTopicTree("Topic", callback);
    }

    public TopicTree getTopicTreeOfKindTopic() throws ApiException {
        return mService.getTopicTree("Topic");
    }

    public Observable<TopicTree> getTopicTreeOfKindTopicObservable() {
        return mService.getTopicTreeObservable("Topic");
    }

    public void getTopic(String topicSlug, Callback<Topic> callback) {
        mService.getTopic(topicSlug, callback);
    }

    public Topic getTopic(String topicSlug) throws ApiException {
        return mService.getTopic(topicSlug);
    }

    public Observable<Topic> getTopicObservable(String topicSlug) {
        return mService.getTopicObservable(topicSlug);
    }

    public void getVideo(String videoId, Callback<Video> callback) {
        mService.getVideo(videoId, callback);
    }

    public void getExercise(String exerciseName, Callback<Exercise> callback) {
        mService.getExercise(exerciseName, callback);
    }

    public void getArticle(String articleInternalId, Callback<Article> callback) {
        mService.getArticle(articleInternalId, callback);
    }

    public class ApiException extends Exception {
        private RetrofitError retrofitError;

        public ApiException(RetrofitError retrofitError) {
            this.retrofitError = retrofitError;
        }

        public RetrofitError getRetrofitError() {
            return retrofitError;
        }
    }
}
