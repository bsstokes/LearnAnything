package com.bsstokes.learnanything.data.transformers;

import com.bsstokes.learnanything.models.Video;

import rx.functions.Func1;

public class ApiVideoToVideo implements Func1<com.bsstokes.learnanything.api.models.Video, Video> {
    @Override
    public Video call(com.bsstokes.learnanything.api.models.Video video) {
        return Video.builder()
                .id(video.id)
                .title(video.translated_title)
                .htmlDescription(video.translated_description_html)
                .url(video.url)
                .imageUrl(video.image_url)
                .readableId(video.readable_id)
                .build();
    }
}
