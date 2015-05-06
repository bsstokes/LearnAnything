package com.bsstokes.learnanything.models;

import android.support.annotation.Nullable;

import auto.parcel.AutoParcel;

@AutoParcel
public abstract class Topic {

    @Nullable
    public abstract String getId();

    @Nullable
    public abstract String getTitle();

    @Nullable
    public abstract String getSlug();

    @Nullable
    public abstract String getKind();

    public abstract boolean isTopLevel();

    @Nullable
    public abstract String getParentId();

    public static Builder builder() {
        return new AutoParcel_Topic.Builder()
                .kind("Topic");
    }

    @AutoParcel.Builder
    public interface Builder {
        Builder id(String id);

        Builder title(String title);

        Builder slug(String slug);

        Builder kind(String kind);

        Builder topLevel(boolean topLevel);

        Builder parentId(String parentId);

        Topic build();
    }
}
