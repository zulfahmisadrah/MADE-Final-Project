package com.zulfahmi.favoriteapp.fragments;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zulfahmi.favoriteapp.LoadFilmCallback;
import com.zulfahmi.favoriteapp.R;
import com.zulfahmi.favoriteapp.adapters.FilmAdapter;
import com.zulfahmi.favoriteapp.models.Film;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static com.zulfahmi.favoriteapp.MappingHelper.mapCursorToArrayList;
import static com.zulfahmi.favoriteapp.databases.DatabaseContract.FilmColumns.CONTENT_URI;

public class FavoriteFragment extends Fragment implements LoadFilmCallback {
    public static final String ARG_SECTION_NUMBER = "section_number";
    private FilmAdapter filmAdapter;
    private RecyclerView recyclerview;
    private ArrayList<Film> films;
    private int index;
    private DataObserver mDataObserver;
    private TextView tvNoData;
    private ProgressBar progressBar;

    public static FavoriteFragment newInstance(int index) {
        FavoriteFragment fragment = new FavoriteFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }
    public FavoriteFragment() { }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerview = view.findViewById(R.id.recyclerview);
        progressBar = view.findViewById(R.id.progressbar);
        tvNoData = view.findViewById(R.id.tv_nodata);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showTextNoData(false);
        showLoading(true);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerview.setHasFixedSize(true);
        filmAdapter = new FilmAdapter(getContext());
        recyclerview.setAdapter(filmAdapter);
        HandlerThread handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        mDataObserver = new DataObserver(handler, getContext(), this);
        getContext().getContentResolver().registerContentObserver(CONTENT_URI, true, mDataObserver);
        new getData(getContext(), this).execute();
    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        }else{
            progressBar.setVisibility(View.GONE);
        }
    }

    private void showTextNoData(Boolean state) {
        if (state) {
            tvNoData.setVisibility(View.VISIBLE);
        }else{
            tvNoData.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onPostExecute(Cursor cursor) {
        ArrayList<Film> listFilm = new ArrayList<>();
        String filmType="movies";
        if(index==1){
            filmType = "movies";
        }else if(index==2){
            filmType = "tvshows";
        }
        films = mapCursorToArrayList(cursor, filmType);

        if(films.size() > 0){
            for (int i=0; i<films.size(); i++){
                if(filmType.equals(films.get(i).getFilmType())){
                    listFilm.add(films.get(i));
                }
            }
            filmAdapter.setFilms(listFilm);
        }else{
            showTextNoData(true);
        }
        showLoading(false);
    }

    private static class getData extends AsyncTask<Void, Void, Cursor> {
        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadFilmCallback> weakCallback;

        private getData(Context context,LoadFilmCallback callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            return weakContext.get().getContentResolver().query(CONTENT_URI, null, null,null,null);
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            weakCallback.get().onPostExecute(cursor);
        }
    }

    static class DataObserver extends ContentObserver {
        final Context context;
        LoadFilmCallback callback;
        public DataObserver(Handler handler, Context context, LoadFilmCallback callback) {
            super(handler);
            this.context = context;
            this.callback = callback;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            new getData(context, callback).execute();
        }
    }
}
