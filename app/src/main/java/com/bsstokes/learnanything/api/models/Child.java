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
        @Override
        public String toString() {
            return super.toString() + " (Topic)";
        }
    }

    public static class Video extends Child {
        @Override
        public String toString() {
            return super.toString() + " (Video)";
        }
    }

    public static class Exercise extends Child {
        @Override
        public String toString() {
            return super.toString() + " (Exercise)";
        }
    }

    public static class Article extends Child {
        @Override
        public String toString() {
            return super.toString() + " (Article)";
        }
    }

    public static class Unknown extends Child {
        @Override
        public String toString() {
            return super.toString() + " (Unknown)";
        }
    }
}
