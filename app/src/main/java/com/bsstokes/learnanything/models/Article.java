package com.bsstokes.learnanything.models;

import android.support.annotation.Nullable;

import auto.parcel.AutoParcel;

@AutoParcel
public abstract class Article {

    @Nullable
    public abstract String getId();

    @Nullable
    public abstract String getName();

    @Nullable
    public abstract String getTitle();

    public static Builder builder() {
        return new AutoParcel_Article.Builder();
    }

    @AutoParcel.Builder
    public interface Builder {
        public Builder id(String id);

        public Builder name(String name);

        public Builder title(String title);

        public Article build();
    }
}
