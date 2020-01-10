package com.zulfahmi.favoriteapp;

import android.database.Cursor;

import com.zulfahmi.favoriteapp.models.Film;

import java.util.ArrayList;

public class MappingHelper {

    public static ArrayList<Film> mapCursorToArrayList (Cursor moviesCursor, String type)
    {
        ArrayList<Film> films = new ArrayList<>();

        if(moviesCursor != null)
        {
            while(moviesCursor.moveToNext())
            {
                int id = moviesCursor.getInt(moviesCursor.getColumnIndexOrThrow("id"));
                String filmType = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow("film_type"));
                String title = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow("title"));
                String posterPath = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow("poster_path"));
                String overview = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow("overview"));
                String releaseDate = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow("release_date"));
                double voteAverage = moviesCursor.getDouble(moviesCursor.getColumnIndexOrThrow("vote_average"));
                films.add(new Film(id, filmType, title, posterPath, overview, releaseDate, voteAverage));
            }
        }
        return films;
    }
}
