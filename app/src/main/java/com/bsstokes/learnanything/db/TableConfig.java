package com.bsstokes.learnanything.db;

public class TableConfig {

    public static class Videos {
        public static final String TABLE = "videos";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_HTML_DESCRIPTION = "htmlDescription";
        public static final String COLUMN_URL = "url";
        public static final String COLUMN_IMAGE_URL = "imageUrl";
        public static final String COLUMN_READABLE_ID = "readableId";
        public static final String COLUMN_ENTIRE_RECORD = "entireRecord";

        public static final String CREATE = ""
                + "CREATE TABLE " + TABLE + "("
                + COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY,"
                + COLUMN_TITLE + " TEXT,"
                + COLUMN_HTML_DESCRIPTION + " TEXT,"
                + COLUMN_URL + " TEXT,"
                + COLUMN_IMAGE_URL + " TEXT,"
                + COLUMN_READABLE_ID + " TEXT,"
                + COLUMN_ENTIRE_RECORD + " INTEGER NOT NULL DEFAULT 0"
                + ")";
    }
}
