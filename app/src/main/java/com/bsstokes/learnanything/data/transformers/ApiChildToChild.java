package com.bsstokes.learnanything.data.transformers;


import com.bsstokes.learnanything.data.Child;

import rx.functions.Func1;

public class ApiChildToChild implements Func1<com.bsstokes.learnanything.api.models.Child, Child> {

    private final String parentId;

    public ApiChildToChild(String parentId) {
        this.parentId = parentId;
    }

    public static Child convert(com.bsstokes.learnanything.api.models.Child apiChild, String parentId) {
        return new ApiChildToChild(parentId).call(apiChild);
    }

    @Override
    public Child call(com.bsstokes.learnanything.api.models.Child child) {
        return Child.builder()
                .id(child.id)
                .parentId(parentId)
                .kind(child.kind)
                .hidden(child.hide)
                .key(child.key)
                .internalId(child.internal_id)
                .title(child.title)
                .url(child.url)
                .translatedTitle(child.translated_title)
                .nodeSlug(child.node_slug)
                .build();
    }
}
