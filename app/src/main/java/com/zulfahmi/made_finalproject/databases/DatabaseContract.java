package com.zulfahmi.made_finalproject.databases;

import android.net.Uri;
import android.provider.BaseColumns;

public final class DatabaseContract {
    public static final String AUTHORITY = "com.zulfahmi.made_finalproject";
    private static final String SCHEME = "content";

    private DatabaseContract(){}

    public static final class FilmColumns implements BaseColumns {
        public static final String TABLE_NAME = "film";

        public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build();
    }
}
