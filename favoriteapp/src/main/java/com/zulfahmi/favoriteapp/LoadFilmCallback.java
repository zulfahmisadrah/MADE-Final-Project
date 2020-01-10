package com.zulfahmi.favoriteapp;

import android.database.Cursor;

public interface LoadFilmCallback {
    void onPostExecute(Cursor movies);
}
