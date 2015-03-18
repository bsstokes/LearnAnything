package com.bsstokes.learnanything.sync;

public class Converter {

    public static void convert(
            com.bsstokes.learnanything.api.models.Topic apiTopic,
            com.bsstokes.learnanything.db.models.Topic dbTopic) {

        dbTopic.setId(apiTopic.id);
        dbTopic.setTitle(apiTopic.translated_title);
        dbTopic.setSlug(apiTopic.slug);
    }

    public static void convert(
            com.bsstokes.learnanything.db.models.Topic dbTopic,
            com.bsstokes.learnanything.api.models.Topic apiTopic
    ) {

        apiTopic.id = dbTopic.getId();
        apiTopic.translated_title = dbTopic.getTitle();
        apiTopic.slug = dbTopic.getSlug();
    }

    public static void convert(
            com.bsstokes.learnanything.api.models.Child apiChild,
            com.bsstokes.learnanything.db.models.Child dbChild) {

        dbChild.setKind(apiChild.kind);
        dbChild.setHide(apiChild.hide);
        dbChild.setKey(apiChild.key);
        dbChild.setInternalId(apiChild.internal_id);
        dbChild.setTitle(apiChild.title);
        dbChild.setUrl(apiChild.url);
        dbChild.setTranslatedTitle(apiChild.translated_title);
        dbChild.setNodeSlug(apiChild.node_slug);
        dbChild.setId(apiChild.id);
        dbChild.setEditSlug(apiChild.edit_slug);
    }

    public static void convert(
            com.bsstokes.learnanything.db.models.Child dbChild,
            com.bsstokes.learnanything.api.models.Child apiChild
    ) {

        apiChild.kind = dbChild.getKind();
        apiChild.hide = dbChild.isHide();
        apiChild.key = dbChild.getKey();
        apiChild.internal_id = dbChild.getInternalId();
        apiChild.title = dbChild.getTitle();
        apiChild.url = dbChild.getUrl();
        apiChild.translated_title = dbChild.getTranslatedTitle();
        apiChild.node_slug = dbChild.getNodeSlug();
        apiChild.id = dbChild.getId();
        apiChild.edit_slug = dbChild.getEditSlug();
    }

    public static void convert(
            com.bsstokes.learnanything.api.models.Child apiChild,
            com.bsstokes.learnanything.db.models.Video dbVideo) {

        dbVideo.setId(apiChild.internal_id);
        dbVideo.setSlug(apiChild.id);
        dbVideo.setTranslatedTitle(apiChild.translated_title);
    }

    public static void convert(
            com.bsstokes.learnanything.api.models.Child apiChild,
            com.bsstokes.learnanything.db.models.Topic dbTopic) {

        dbTopic.setId(apiChild.internal_id);
        dbTopic.setSlug(apiChild.id);
        dbTopic.setTitle(apiChild.translated_title);
    }

    public static void convert(
            com.bsstokes.learnanything.api.models.Child apiChild,
            com.bsstokes.learnanything.db.models.Exercise dbExercise) {

        dbExercise.setId(apiChild.internal_id);
        dbExercise.setName(apiChild.id);
        dbExercise.setTitle(apiChild.translated_title);
    }

    public static void convert(
            com.bsstokes.learnanything.api.models.Child apiChild,
            com.bsstokes.learnanything.db.models.Article dbArticle) {

        dbArticle.setId(apiChild.internal_id);
        dbArticle.setName(apiChild.id);
        dbArticle.setTitle(apiChild.translated_title);
    }
}
