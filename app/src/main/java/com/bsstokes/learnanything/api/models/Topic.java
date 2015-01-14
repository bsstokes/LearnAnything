package com.bsstokes.learnanything.api.models;

import java.util.List;

public class Topic extends Content {

    public static final String KIND_TOPIC = "Topic";

    public String domain_slug;
    public List<Content> children;

    public boolean isTopic() {
        return KIND_TOPIC.equals(kind);
    }
}
