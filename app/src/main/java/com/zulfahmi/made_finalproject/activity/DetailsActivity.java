package com.zulfahmi.made_finalproject.activity;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.zulfahmi.made_finalproject.R;
import com.zulfahmi.made_finalproject.databases.FilmDatabase;
import com.zulfahmi.made_finalproject.models.Film;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DetailsActivity extends AppCompatActivity {
    public final static String EXTRA_FILM = "extra_film";
    private Boolean isFavorite = false;
    ProgressBar progressBar;
    ConstraintLayout constraintLayout;
    Menu mMenu;

    Film film;
    private FilmDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        TextView tvTitle = findViewById(R.id.tv_title);
        TextView tvDate = findViewById(R.id.tv_date);
        TextView tvDesc = findViewById(R.id.tv_desc);
        TextView tvVote = findViewById(R.id.tv_vote);
        ImageView imgPoster = findViewById(R.id.img_poster);
        ImageView imgBackdrop = findViewById(R.id.img_backdrop);
        constraintLayout = findViewById(R.id.details_layout);
        progressBar = findViewById(R.id.progressbar);

        showLoading(true);

        film = getIntent().getParcelableExtra(EXTRA_FILM);
        db = FilmDatabase.getInstance(this);
        AsyncTask.execute(() -> {
            if (!db.filmDAO().getFavoriteFilmFromId(film.getId()).isEmpty()) {
                isFavorite = true;
            }
        });

        String title = film.getTitle();
        String date = formatDate(film.getDate());
        String desc = film.getOverview();
        String vote = film.getVoteAverage()+"/10";

        if (!film.getPosterPath().equals("null")) {
            Glide.with(this).load("https://image.tmdb.org/t/p/w185/" + film.getPosterPath()).into(imgPoster);
        }
        if (!film.getBackdropPath().equals("null")) {
            Glide.with(this).load("https://image.tmdb.org/t/p/original/" + film.getBackdropPath()).into(imgBackdrop);
        }
        tvTitle.setText(title);
        tvDate.setText(date);
        tvDesc.setText(desc);
        tvVote.setText(vote);
        showLoading(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        getMenuInflater().inflate(R.menu.details_menu, mMenu);
        setIconFavorite(mMenu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.favorite:
                setFavorite();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        }else{
            progressBar.setVisibility(View.GONE);
        }
    }

    private String formatDate(String strDate) {
        String date = strDate;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            Date newDate = format.parse(date);
            format =  new SimpleDateFormat("dd MMM yyyy", Locale.US);
            date = format.format(newDate);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    private void setFavorite(){
        if(isFavorite){
            AsyncTask.execute(()-> db.filmDAO().insertFavorite(film));
            Snackbar.make(constraintLayout, getResources().getString(R.string.added_favorites), Snackbar.LENGTH_SHORT).show();
        }else{
            AsyncTask.execute(()-> db.filmDAO().deleteFavorite(film));
            Snackbar.make(constraintLayout, getResources().getString(R.string.remove_favorites), Snackbar.LENGTH_SHORT).show();
        }
        setIconFavorite(mMenu);
        Intent initialUpdateIntent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        initialUpdateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        sendBroadcast(initialUpdateIntent);
    }

    private void setIconFavorite(Menu menu) {
        if (isFavorite) {
            menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite));
        } else {
            menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite_border));
        }
        isFavorite = !isFavorite;
    }

}
