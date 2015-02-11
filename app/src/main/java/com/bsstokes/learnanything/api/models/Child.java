package com.bsstokes.learnanything.api.models;

public class Child {
    public String kind;
    public boolean hide;
    public String key;
    public String internal_id;
    public String title;
    public String url;
    public String translated_title;
    public String node_slug;
    public String id;
    public String edit_slug;

    @Override
    public String toString() {
        return translated_title;
    }

    public static class Topic extends Child {
    }

    public static class Video extends Child {
    }

    public static class Exercise extends Child {
    }

    public static class Article extends Child {
    }

    public static class Unknown extends Child {
        @Override
        public String toString() {
            return super.toString() + " (Unknown)";
        }
    }
}
