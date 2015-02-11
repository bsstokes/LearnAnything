package com.bsstokes.learnanything.db.models;

import io.realm.RealmObject;
import io.realm.annotations.Index;

public class Topic extends RealmObject {
    @Index
    private String id;
    private String title;
    @Index
    private String slug;
    private boolean topLevel;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public boolean isTopLevel() {
        return topLevel;
    }

    public void setTopLevel(boolean topLevel) {
        this.topLevel = topLevel;
    }
}
