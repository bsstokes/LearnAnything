package com.bsstokes.learnanything.models;

import android.support.annotation.Nullable;

import auto.parcel.AutoParcel;

@AutoParcel
public abstract class Article {

    @Nullable
    public abstract String getId();

    @Nullable
    public abstract String getTitle();

    @Nullable
    public abstract String getRelativeUrl();

    @Nullable
    public abstract String getHtmlContent();

    @Nullable
    public abstract String getContentId();

    public static Builder builder() {
        return new AutoParcel_Article.Builder();
    }

    @AutoParcel.Builder
    public interface Builder {
        public Builder id(String id);

        public Builder title(String title);

        public Builder relativeUrl(String relativeUrl);

        public Builder htmlContent(String htmlContent);

        public Builder contentId(String contentId);

        public Article build();
    }
}
