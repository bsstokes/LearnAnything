package com.bsstokes.learnanything.dagger;

import com.bsstokes.learnanything.sync.SyncService;
import com.bsstokes.learnanything.ui.ArticleActivity;
import com.bsstokes.learnanything.ui.ExerciseActivity;
import com.bsstokes.learnanything.ui.MainApplication;
import com.bsstokes.learnanything.ui.TopicActivity;
import com.bsstokes.learnanything.ui.topic_tree.TopicTreeActivity;
import com.bsstokes.learnanything.ui.video.VideoActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(MainApplication application);

    void inject(ArticleActivity activity);

    void inject(ExerciseActivity activity);

    void inject(TopicActivity activity);

    void inject(TopicTreeActivity activity);

    void inject(VideoActivity activity);

    void inject(SyncService service);
}
