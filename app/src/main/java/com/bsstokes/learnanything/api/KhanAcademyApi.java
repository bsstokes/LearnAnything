package com.bsstokes.learnanything.api;

import com.bsstokes.learnanything.BuildConfig;
import com.bsstokes.learnanything.api.models.Article;
import com.bsstokes.learnanything.api.models.Exercise;
import com.bsstokes.learnanything.api.models.Topic;
import com.bsstokes.learnanything.api.models.TopicTree;
import com.bsstokes.learnanything.api.models.Video;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

public class KhanAcademyApi {

    protected interface KhanAcademyService {
        @GET("/topictree")
        Observable<TopicTree> getTopicTree(@Query("kind") String kind);

        @GET("/topic/{topic_slug}")
        Observable<Topic> getTopic(@Path("topic_slug") String topicSlug);

        @GET("/videos/{video_id}")
        Observable<Video> getVideo(@Path("video_id") String videoId);

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

    public Observable<TopicTree> getTopicTreeOfKindTopic() {
        return mService.getTopicTree("Topic");
    }

    public Observable<Topic> getTopic(String topicSlug) {
        return mService.getTopic(topicSlug);
    }

    public Observable<Video> getVideo(String videoId) {
        return mService.getVideo(videoId);
    }

    public void getExercise(String exerciseName, Callback<Exercise> callback) {
        mService.getExercise(exerciseName, callback);
    }

    public void getArticle(String articleInternalId, Callback<Article> callback) {
        mService.getArticle(articleInternalId, callback);
    }
}
