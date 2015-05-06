package com.bsstokes.learnanything.models;

import android.support.annotation.Nullable;

import auto.parcel.AutoParcel;

@AutoParcel
public abstract class Exercise {

    @Nullable
    public abstract String getId();

    @Nullable
    public abstract String getUrl();

    @Nullable
    public abstract String getDescription();

    @Nullable
    public abstract String getTitle();

    @Nullable
    public abstract String getImageUrl();

    @Nullable
    public abstract String getImage256Url();

    public static Builder builder() {
        return new AutoParcel_Exercise.Builder();
    }

    @AutoParcel.Builder
    public interface Builder {

        Builder id(String id);

        Builder url(String url);

        Builder description(String description);

        Builder title(String title);

        Builder imageUrl(String imageUrl);

        Builder image256Url(String image256Url);

        Exercise build();
    }
}
