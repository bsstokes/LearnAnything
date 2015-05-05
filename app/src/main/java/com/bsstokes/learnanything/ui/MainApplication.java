package com.bsstokes.learnanything.ui;

import android.app.Application;

import com.bsstokes.learnanything.dagger.ApplicationComponent;
import com.bsstokes.learnanything.dagger.ApplicationModule;
import com.bsstokes.learnanything.dagger.DaggerApplicationComponent;
import com.crashlytics.android.Crashlytics;

public class MainApplication extends Application {

    private ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        Crashlytics.start(this);

        component = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        component().inject(this);
    }

    public ApplicationComponent component() {
        return component;
    }
}
