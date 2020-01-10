package com.zulfahmi.favoriteapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.zulfahmi.favoriteapp.R;
import com.zulfahmi.favoriteapp.models.Film;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class FilmAdapter extends RecyclerView.Adapter<FilmAdapter.FilmViewHolder> {
    private Context context;
    private ArrayList<Film> films;

    public FilmAdapter(Context context) {
        this.context = context;
        films = new ArrayList<>();
    }

    public void setFilms(ArrayList<Film> films) {
        this.films = films;
        notifyDataSetChanged();
    }

    public ArrayList<Film> getFilms() {
        return films;
    }

    @NonNull
    @Override
    public FilmAdapter.FilmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_films, parent, false);
        return new FilmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilmAdapter.FilmViewHolder holder, int position) {
        if (!films.get(position).getPosterPath().equals("null")) {
            Glide.with(context).load("https://image.tmdb.org/t/p/w185/" + films.get(position).getPosterPath()).into(holder.imgPoster);
        }
        holder.tvTitle.setText(films.get(position).getTitle());
        holder.tvDate.setText(formatDate(films.get(position).getDate()));
        holder.tvDesc.setText(films.get(position).getOverview());

    }

    @Override
    public int getItemCount() {
        return films.size();
    }

    public class FilmViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle, tvDate, tvDesc;
        private ImageView imgPoster;
        public FilmViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPoster = itemView.findViewById(R.id.img_poster);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvDesc = itemView.findViewById(R.id.tv_desc);
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
}
