package com.zulfahmi.made_finalproject.databases;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.zulfahmi.made_finalproject.models.Film;
import java.util.List;

@Dao
public interface FilmDAO {
    @Query("SELECT * FROM film")
    Cursor queryAll();

    @Query("SELECT * FROM film WHERE id=:id")
    Cursor queryAllById(String id);

    @Query("SELECT * FROM film")
    LiveData<List<Film>> getFavoriteFilmList();

    @Query("SELECT poster_path FROM film")
    List<String> getFavoriteFilmImage();

    @Query("SELECT * FROM film WHERE film_type='movies'")
    LiveData<List<Film>> getFavoriteMovies();

    @Query("SELECT * FROM film WHERE film_type='tvshows'")
    LiveData<List<Film>> getFavoriteTvShows();

    @Query("SELECT * FROM film WHERE id=:id")
    List<Film> getFavoriteFilmFromId(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFavorite(Film film);

    @Insert
    long insert(Film film);

    @Update
    void updateFilm(Film film);

    @Update
    int update(Film film);

    @Delete
    void deleteFavorite(Film film);

    @Query("DELETE FROM film WHERE id=:id")
    int deleteById(long id);
}
