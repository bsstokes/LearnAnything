package com.bsstokes.learnanything.data.transformers;


import com.bsstokes.learnanything.models.Child;

import rx.functions.Func1;

public class ApiChildToChild implements Func1<com.bsstokes.learnanything.api.models.Child, Child> {

    @Override
    public Child call(com.bsstokes.learnanything.api.models.Child child) {
        return Child.builder()
                .id(child.id)
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
