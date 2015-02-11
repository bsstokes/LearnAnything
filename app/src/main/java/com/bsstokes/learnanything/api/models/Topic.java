package com.bsstokes.learnanything.api.models;

import java.util.List;

public class Topic {

    public static final String KIND_TOPIC = "Topic";

    public String id;
    public String translated_title;
    public String slug;
    public String kind;
    public List<Child> children;

    public boolean isTopic() {
        return KIND_TOPIC.equals(kind);
    }

    @Override
    public String toString() {
        return translated_title + " (Topic)";
    }
}
