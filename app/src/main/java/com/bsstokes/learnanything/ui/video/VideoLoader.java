package com.bsstokes.learnanything.ui.video;

import com.bsstokes.learnanything.api.KhanAcademyApi;
import com.bsstokes.learnanything.data.transformers.ApiVideoToVideo;
import com.bsstokes.learnanything.models.Video;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

public class VideoLoader {

    public Observable<Video> loadVideo(final String videoId) {
        final KhanAcademyApi khanAcademyApi = new KhanAcademyApi();
        Observable<com.bsstokes.learnanything.api.models.Video> deferredObservable = Observable.defer(new Func0<Observable<com.bsstokes.learnanything.api.models.Video>>() {
            @Override
            public Observable<com.bsstokes.learnanything.api.models.Video> call() {
                return Observable.just(khanAcademyApi.getVideo(videoId));
            }
        });

        return deferredObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new ApiVideoToVideo());
    }
}
