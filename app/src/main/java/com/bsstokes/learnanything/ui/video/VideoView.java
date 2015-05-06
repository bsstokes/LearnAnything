package com.bsstokes.learnanything.ui.video;

import android.support.annotation.NonNull;

public interface VideoView {
    void onLoading();

    //void update(@NonNull Video video);

    void openVideoUrl(@NonNull String videoUrl);

    // ---

    void setVideoTitle(String title);
    void setVideoHtmlDescription(String htmlDescription);
    void setVideoImageUrl(String imageUrl);
}
