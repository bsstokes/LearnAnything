package com.bsstokes.learnanything.api.models;

import java.util.List;

public class Topic {
    public String title;
    public String domain_slug;
    public String kind;
    public List<Topic> children;

    @Override
    public String toString() {
        return title;
    }
}
