package com.bsstokes.learnanything.dagger;

import com.bsstokes.learnanything.ui.MainApplication;
import com.bsstokes.learnanything.ui.VideoPlayerActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    void inject(MainApplication application);

    void inject(VideoPlayerActivity activity);
}
