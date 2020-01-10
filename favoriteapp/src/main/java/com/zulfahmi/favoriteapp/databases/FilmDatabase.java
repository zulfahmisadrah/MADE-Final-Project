package com.zulfahmi.favoriteapp.databases;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.zulfahmi.favoriteapp.models.Film;

@Database(entities = {Film.class}, version = 1, exportSchema = false)
public abstract class FilmDatabase extends RoomDatabase {
    private static final String DB_NAME = "film_db";
    private static FilmDatabase instance;

    public static synchronized FilmDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), FilmDatabase.class, DB_NAME)
                    .allowMainThreadQueries().fallbackToDestructiveMigration().build();
        }
        return instance;
    }

    public abstract FilmDAO filmDAO();
}
