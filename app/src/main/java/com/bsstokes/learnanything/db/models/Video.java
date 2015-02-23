package com.bsstokes.learnanything.db.models;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

public class Video extends RealmObject {
    private String translatedTitle = "";
    private String translatedDescriptionHtml = "";
    private String downloadUrl = "";
    private String url = "";
    private String imageUrl = "";
    @PrimaryKey
    private String id;
    @Index
    private String slug;

    public String getTranslatedTitle() {
        return translatedTitle;
    }

    public void setTranslatedTitle(String translatedTitle) {
        this.translatedTitle = translatedTitle;
    }

    public String getTranslatedDescriptionHtml() {
        return translatedDescriptionHtml;
    }

    public void setTranslatedDescriptionHtml(String translatedDescriptionHtml) {
        this.translatedDescriptionHtml = translatedDescriptionHtml;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }
}
