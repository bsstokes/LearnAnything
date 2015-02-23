package com.bsstokes.learnanything.db.models;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

public class Topic extends RealmObject {
    @PrimaryKey
    private String id;
    private String title;
    @Index
    private String slug;
    private boolean topLevel;
    @Index
    private String parentId = "";
    private RealmList<Child> children;

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

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public RealmList<Child> getChildren() {
        return children;
    }

    public void setChildren(RealmList<Child> children) {
        this.children = children;
    }
}
