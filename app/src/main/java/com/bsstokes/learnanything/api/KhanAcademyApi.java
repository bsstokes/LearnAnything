package com.bsstokes.learnanything.api;

import com.bsstokes.learnanything.BuildConfig;
import com.bsstokes.learnanything.api.models.Article;
import com.bsstokes.learnanything.api.models.Exercise;
import com.bsstokes.learnanything.api.models.Topic;
import com.bsstokes.learnanything.api.models.TopicTree;
import com.bsstokes.learnanything.api.models.Video;

import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

public class KhanAcademyApi {

    public static final String WEBSITE_URL = "http://www.khanacademy.org";
    private static final String API_ENDPOINT_URL = "http://www.khanacademy.org/api/v1/";

    protected interface KhanAcademyService {
        @GET("/topictree")
        TopicTree getTopicTree(@Query("kind") String kind);

        @GET("/topic/{topic_slug}")
        Topic getTopic(@Path("topic_slug") String topicSlug);

        @GET("/videos/{video_id}")
        Video getVideo(@Path("video_id") String videoId);

        @GET("/exercises/{exercise_name}")
        Exercise getExercise(@Path("exercise_name") String exerciseName);

        @GET("/articles/{article_internal_id}")
        Article getArticle(@Path("article_internal_id") String articleInternalId);
    }

    private KhanAcademyService mService;

    public KhanAcademyApi() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(API_ENDPOINT_URL)
                .setLogLevel(BuildConfig.DEBUG ? RestAdapter.LogLevel.BASIC : RestAdapter.LogLevel.NONE)
                .build();

        mService = restAdapter.create(KhanAcademyService.class);
    }

    public TopicTree getTopicTreeOfKindTopic() {
        return mService.getTopicTree("Topic");
    }

    public Topic getTopic(String topicSlug) {
        return mService.getTopic(topicSlug);
    }

    public Video getVideo(String videoId) {
        return mService.getVideo(videoId);
    }

    public Exercise getExercise(String exerciseName) {
        return mService.getExercise(exerciseName);
    }

    public Article getArticle(String articleInternalId) {
        return mService.getArticle(articleInternalId);
    }
}
