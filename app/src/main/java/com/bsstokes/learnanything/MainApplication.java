package com.bsstokes.learnanything;

import android.app.Application;

import com.crashlytics.android.Crashlytics;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Crashlytics.start(this);
    }
}
