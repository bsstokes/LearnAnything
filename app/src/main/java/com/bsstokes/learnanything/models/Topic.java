package com.bsstokes.learnanything.models;

import android.support.annotation.Nullable;

import auto.parcel.AutoParcel;

@AutoParcel
public abstract class Topic {

    @Nullable
    public abstract String getId();

    @Nullable
    public abstract String getTranslatedTitle();

    @Nullable
    public abstract String getSlug();

    @Nullable
    public abstract String getKind();

    public static Builder builder() {
        return new AutoParcel_Topic.Builder()
                .kind("Topic");
    }

    @AutoParcel.Builder
    public interface Builder {
        Builder id(String id);

        Builder translatedTitle(String translatedTitle);

        Builder kind(String topic);

        Builder slug(String slug);

        Topic build();
    }
}
