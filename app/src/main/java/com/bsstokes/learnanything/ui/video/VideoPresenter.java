package com.bsstokes.learnanything.ui.video;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.bsstokes.learnanything.models.Video;

public class VideoPresenter {

    @NonNull
    private Video video = NullVideo;

    @NonNull
    private final VideoView videoView;

    public VideoPresenter(@NonNull VideoView videoView) {
        this.videoView = videoView;
    }

    public void setVideo(@NonNull Video video) {
        this.video = video;
        update();
    }

    public void openVideo() {
        String url = video.getUrl();
        if (!TextUtils.isEmpty(url)) {
            videoView.openVideoUrl(url);
        }
    }

    public void update() {
        if (NullVideo == video) {
            videoView.onLoading();
        } else {
            //videoView.update(video);
            videoView.setVideoTitle(video.getTitle());
            videoView.setVideoHtmlDescription(video.getHtmlDescription());
            videoView.setVideoImageUrl(video.getImageUrl());
        }
    }

    static Video NullVideo = new Video() {

        @Nullable
        @Override
        public String getId() {
            return null;
        }

        @Nullable
        @Override
        public String getTitle() {
            return null;
        }

        @Nullable
        @Override
        public String getHtmlDescription() {
            return null;
        }

        @Nullable
        @Override
        public String getUrl() {
            return null;
        }

        @Nullable
        @Override
        public String getImageUrl() {
            return null;
        }

        @Nullable
        @Override
        public String getReadableId() {
            return null;
        }
    };
}
