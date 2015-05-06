package com.bsstokes.learnanything.db;

public class TableConfig {

    public static class Articles {
        public static final String TABLE = "articles";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_RELATIVE_URL = "relativeUrl";
        public static final String COLUMN_HTML_CONTENT = "htmlContent";
        public static final String COLUMN_CONTENT_ID = "contentId";

        public static final String CREATE = ""
                + "CREATE TABLE " + TABLE + "("
                + COLUMN_ID + " TEXT NOT NULL PRIMARY KEY,"
                + COLUMN_TITLE + " TEXT,"
                + COLUMN_RELATIVE_URL + " TEXT,"
                + COLUMN_HTML_CONTENT + " TEXT,"
                + COLUMN_CONTENT_ID + " TEXT"
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

    public static class Exercises {
        public static final String TABLE = "exercises";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_URL = "url";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_IMAGE_URL = "imageUrl";
        public static final String COLUMN_IMAGE_256_URL = "image256Url";

        public static final String CREATE = ""
                + "CREATE TABLE " + TABLE + "("
                + COLUMN_ID + " TEXT NOT NULL PRIMARY KEY,"
                + COLUMN_URL + " TEXT,"
                + COLUMN_DESCRIPTION + " TEXT,"
                + COLUMN_TITLE + " TEXT,"
                + COLUMN_IMAGE_URL + " TEXT,"
                + COLUMN_IMAGE_256_URL + " TEXT"
                + ")";
    }

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
}
