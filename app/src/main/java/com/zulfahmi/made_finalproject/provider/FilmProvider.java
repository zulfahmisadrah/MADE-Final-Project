package com.zulfahmi.made_finalproject.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import com.zulfahmi.made_finalproject.databases.FilmDatabase;
import com.zulfahmi.made_finalproject.models.Film;

import static com.zulfahmi.made_finalproject.databases.DatabaseContract.AUTHORITY;
import static com.zulfahmi.made_finalproject.databases.DatabaseContract.FilmColumns.CONTENT_URI;
import static com.zulfahmi.made_finalproject.databases.DatabaseContract.FilmColumns.TABLE_NAME;

public class FilmProvider extends ContentProvider {
    private static final int FILM = 1;
    private static final int FILM_ID = 2;
    private FilmDatabase db;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI(AUTHORITY, TABLE_NAME, FILM);
        sUriMatcher.addURI(AUTHORITY, TABLE_NAME + "/#", FILM_ID);
    }
    public FilmProvider() {
    }

    @Override
    public boolean onCreate() {
        db = FilmDatabase.getInstance(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case FILM:
                cursor = db.filmDAO().queryAll();
                break;
            case FILM_ID:
                cursor = db.filmDAO().queryAllById(uri.getLastPathSegment());
                break;
            default:
                cursor = null;
                break;
        }
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case FILM:
                return "vnd.android.cursor.dir/" + AUTHORITY + "." + TABLE_NAME ;
            case FILM_ID:
                return "vnd.android.cursor.item/" + AUTHORITY + "." + TABLE_NAME;
            default:
                throw new IllegalArgumentException("Invalid uri detected: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long added;
        switch (sUriMatcher.match(uri)) {
            case FILM:
                added = db.filmDAO().insert(Film.fromContentValues(values));
                break;
            default:
                added = 0;
                break;
        }
        getContext().getContentResolver().notifyChange(CONTENT_URI, null);
        return Uri.parse(CONTENT_URI + "/" + added);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int updated;
        switch (sUriMatcher.match(uri)) {
            case FILM_ID:
                updated = db.filmDAO().update(Film.fromContentValues(values));
                break;
            default:
                updated = 0;
                break;
        }
        getContext().getContentResolver().notifyChange(CONTENT_URI, null);
        return updated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int deleted;
        switch (sUriMatcher.match(uri)) {
            case FILM_ID:
                deleted = db.filmDAO().deleteById(ContentUris.parseId(uri));
                break;
            default:
                deleted = 0;
                break;
        }
        getContext().getContentResolver().notifyChange(CONTENT_URI, null);
        return deleted;
    }
}
