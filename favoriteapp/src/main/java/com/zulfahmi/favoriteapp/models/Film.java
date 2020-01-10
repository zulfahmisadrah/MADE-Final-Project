package com.zulfahmi.favoriteapp.models;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = Film.TABLE_NAME)
public class Film implements Parcelable {
    public static final String TABLE_NAME = "film";
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "film_type")
    private String filmType;

    @ColumnInfo(name = "poster_path")
    private String posterPath;

    @ColumnInfo(name = "backdrop_path")
    private String backdropPath;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "release_date")
    private String date;

    @ColumnInfo(name = "overview")
    private String overview;

    @ColumnInfo(name = "original_language")
    private String language;

    @ColumnInfo(name = "vote_average")
    private double voteAverage;

    public Film(){}
    public Film(int id, String filmType, String title, String posterPath, String overview, String releaseDate, double voteAverage) {
        this.id = id;
        this.filmType = filmType;
        this.title = title;
        this.posterPath = posterPath;
        this.overview = overview;
        this.date = releaseDate;
        this.voteAverage = voteAverage;
    }
    public Film(Cursor cursor) {
        this.id = cursor.getInt(cursor.getColumnIndex("id"));
        this.title = cursor.getString(cursor.getColumnIndex("name"));
        this.posterPath = cursor.getString(cursor.getColumnIndex("poster_path"));
        this.backdropPath = cursor.getString(cursor.getColumnIndex("backdrop_path"));
        this.overview = cursor.getString(cursor.getColumnIndex("overview"));
        this.date = cursor.getString(cursor.getColumnIndex("release_date"));
        this.voteAverage = cursor.getFloat(cursor.getColumnIndex("vote_average"));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getFilmType() {
        return filmType;
    }

    public void setFilmType(String filmType) {
        this.filmType = filmType;
    }

    protected Film(Parcel parcel){
        id = parcel.readInt();
        filmType = parcel.readString();
        posterPath = parcel.readString();
        backdropPath = parcel.readString();
        title = parcel.readString();
        date = parcel.readString();
        overview = parcel.readString();
        language = parcel.readString();
        voteAverage = parcel.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(filmType);
        parcel.writeString(posterPath);
        parcel.writeString(backdropPath);
        parcel.writeString(title);
        parcel.writeString(date);
        parcel.writeString(overview);
        parcel.writeString(language);
        parcel.writeDouble(voteAverage);
    }

    public static final Creator<Film> CREATOR = new Creator<Film>() {
        @Override
        public Film createFromParcel(Parcel parcel) {
            return new Film(parcel);
        }

        @Override
        public Film[] newArray(int i) {
            return new Film[i];
        }
    };
}
