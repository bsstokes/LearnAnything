package com.bsstokes.learnanything.models;

import android.support.annotation.Nullable;

import auto.parcel.AutoParcel;

@AutoParcel
public abstract class Video {

    @Nullable
    public abstract String getId();

    @Nullable
    public abstract String getTitle();

    @Nullable
    public abstract String getHtmlDescription();

//    public abstract String getDownloadUrl();

    @Nullable
    public abstract String getUrl();

    @Nullable
    public abstract String getImageUrl();

//    public abstract String getSlug();

    public static Builder builder() {
        return new AutoParcel_Video.Builder();
    }

    @AutoParcel.Builder
    public interface Builder {
        Builder id(String id);

        Builder title(String title);

        Builder htmlDescription(String title);

        Builder url(String url);

        Builder imageUrl(String title);

        Video build();
    }

}
