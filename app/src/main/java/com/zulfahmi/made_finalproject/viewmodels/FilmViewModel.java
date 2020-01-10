package com.zulfahmi.made_finalproject.viewmodels;

import android.content.Context;
import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.zulfahmi.made_finalproject.BuildConfig;
import com.zulfahmi.made_finalproject.databases.FilmDatabase;
import com.zulfahmi.made_finalproject.models.Film;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import cz.msebera.android.httpclient.Header;

public class FilmViewModel extends ViewModel {
    private static final String API_KEY = BuildConfig.TMDB_API_KEY;
    private MutableLiveData<ArrayList<Film>> listFilms = new MutableLiveData<>();
    private LiveData<List<Film>> listFavoriteFilm;


    public void setFavoriteFilm(Context context, int index) {
        FilmDatabase db = FilmDatabase.getInstance(context);
        if(index==1){
            listFavoriteFilm = db.filmDAO().getFavoriteMovies();
        }else if(index==2){
            listFavoriteFilm = db.filmDAO().getFavoriteTvShows();
        }else{
            listFavoriteFilm = db.filmDAO().getFavoriteFilmList();
        }
    }

    public void setFilm(final String jenisFilm, String query) {
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<Film> listItems = new ArrayList<>();
        String currentLanguage = Locale.getDefault().getLanguage();

        String urlQuery = "";
        String urlType = "discover";

        if (!query.isEmpty()) {
            urlQuery = "&query=" + query;
            urlType = "search";
        }

        String urlLanguage = "&language=en-US";
        if(currentLanguage.equals("in")){
            urlLanguage = "&language=id-ID";
        }

        String urlMovies = "https://api.themoviedb.org/3/" + urlType + "/movie?api_key=" + API_KEY + urlLanguage + urlQuery;
        String urlTvShows = "https://api.themoviedb.org/3/" + urlType + "/tv?api_key=" + API_KEY + urlLanguage + urlQuery;

        String url = "url film";
        if (jenisFilm.equals("movies")) {
            url = urlMovies;
        }else if (jenisFilm.equals("tvshows")) {
            url = urlTvShows;
        }

        Log.d("search", url);

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try{
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("results");
                    for (int i = 0; i < list.length(); i++) {
                        JSONObject filmJSON = list.getJSONObject(i);
                        Film film = new Film();
                        film.setId(filmJSON.getInt("id"));
                        film.setFilmType(jenisFilm);
                        film.setPosterPath(filmJSON.getString("poster_path"));
                        film.setBackdropPath(filmJSON.getString("backdrop_path"));
                        if (jenisFilm.equals("movies")) {
                            film.setTitle(filmJSON.getString("title"));
                            film.setDate(filmJSON.getString("release_date"));
                        }else if (jenisFilm.equals("tvshows")) {
                            film.setTitle(filmJSON.getString("name"));
                            if (!filmJSON.isNull("first_air_date")) {
                                film.setDate(filmJSON.getString("first_air_date"));
                            }
                        }
                        String strOverview = filmJSON.getString("overview");
                        if (strOverview.isEmpty()&&currentLanguage.equals("in")) {
                            strOverview = "Belum ada sinopsis dalam Bahasa Indonesia";
                        }else if(strOverview.isEmpty()&&currentLanguage.equals("en")) {
                            strOverview = "No Overview yet for this film";
                        }
                        film.setOverview(strOverview);
                        film.setLanguage(filmJSON.getString("original_language"));
                        film.setVoteAverage(filmJSON.getDouble("vote_average"));
                        listItems.add(film);
                    }
                    listFilms.postValue(listItems);
                }catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure", String.valueOf(error.getMessage()));
            }
        });
    }
    public LiveData<ArrayList<Film>> getFilms() {
        return listFilms;
    }

    public LiveData<List<Film>> getFavoriteFilms() {
        return listFavoriteFilm;
    }
}
