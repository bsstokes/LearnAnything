package com.bsstokes.learnanything.db;

public class TableConfig {

    public static class Videos {
        public static final String TABLE = "videos";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_HTML_DESCRIPTION = "htmlDescription";
        public static final String COLUMN_URL = "url";
        public static final String COLUMN_IMAGE_URL = "imageUrl";
        public static final String COLUMN_READABLE_ID = "readableId";
        public static final String COLUMN_ENTIRE_RECORD = "entireRecord";

        public static final String CREATE = ""
                + "CREATE TABLE " + TABLE + "("
                + COLUMN_ID + " TEXT NOT NULL PRIMARY KEY,"
                + COLUMN_TITLE + " TEXT,"
                + COLUMN_HTML_DESCRIPTION + " TEXT,"
                + COLUMN_URL + " TEXT,"
                + COLUMN_IMAGE_URL + " TEXT,"
                + COLUMN_READABLE_ID + " TEXT,"
                + COLUMN_ENTIRE_RECORD + " INTEGER NOT NULL DEFAULT 0"
                + ")";
    }

    public static class Children {
        public static final String TABLE = "children";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_KIND = "kind";
        public static final String COLUMN_HIDDEN = "hidden";
        public static final String COLUMN_KEY = "key";
        public static final String COLUMN_INTERNAL_ID = "internalId";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_URL = "url";
        public static final String COLUMN_TRANSLATED_TITLE = "translatedTitle";
        public static final String COLUMN_NODE_SLUG = "nodeSlug";

        public static final String CREATE = ""
                + "CREATE TABLE " + TABLE + "("
                + COLUMN_ID + " TEXT NOT NULL PRIMARY KEY,"
                + COLUMN_KIND + " TEXT,"
                + COLUMN_HIDDEN + " INTEGER NOT NULL DEFAULT 0,"
                + COLUMN_KEY + " TEXT,"
                + COLUMN_INTERNAL_ID + " TEXT,"
                + COLUMN_TITLE + " TEXT,"
                + COLUMN_URL + " TEXT,"
                + COLUMN_TRANSLATED_TITLE + " TEXT,"
                + COLUMN_NODE_SLUG + " TEXT"
                + ")";
    }
}
