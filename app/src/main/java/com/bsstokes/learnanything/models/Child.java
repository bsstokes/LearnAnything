package com.bsstokes.learnanything.models;

import android.support.annotation.Nullable;

import auto.parcel.AutoParcel;

@AutoParcel
public abstract class Child {

    @Nullable
    public abstract String getId();

    @Nullable
    public abstract String getParentId();

    @Nullable
    public abstract String getKind();

    public abstract boolean isHidden();

    @Nullable
    public abstract String getKey();

    @Nullable
    public abstract String getInternalId();

    @Nullable
    public abstract String getTitle();

    @Nullable
    public abstract String getUrl();

    @Nullable
    public abstract String getTranslatedTitle();

    @Nullable
    public abstract String getNodeSlug();

    public static Builder builder() {
        return new AutoParcel_Child.Builder();
    }

    @AutoParcel.Builder
    public interface Builder {
        Builder id(String id);

        Builder parentId(String parentId);

        Builder kind(String kind);

        Builder hidden(boolean hidden);

        Builder key(String key);

        Builder internalId(String internalId);

        Builder title(String title);

        Builder url(String url);

        Builder translatedTitle(String translatedTitle);

        Builder nodeSlug(String nodeSlug);

        Child build();
    }
}
