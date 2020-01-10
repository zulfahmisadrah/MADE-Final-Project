package com.zulfahmi.made_finalproject.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.zulfahmi.made_finalproject.R;
import com.zulfahmi.made_finalproject.adapters.FilmAdapter;
import com.zulfahmi.made_finalproject.models.Film;
import com.zulfahmi.made_finalproject.viewmodels.FilmViewModel;
import java.util.ArrayList;

public class FavoriteFragment extends Fragment {
    public static final String ARG_SECTION_NUMBER = "section_number";
    private FilmAdapter filmAdapter;
    private ProgressBar progressBar;
    private RecyclerView recyclerview;
    private ArrayList<Film> filmList;
    private int index;
    private FilmViewModel filmViewModel;
    TextView tvNoData;

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
        showTextNoData(false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        filmViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(FilmViewModel.class);
        showLoading(true);
        filmList = new ArrayList<>();
        filmAdapter = new FilmAdapter(getContext());
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerview.setAdapter(filmAdapter);
        filmViewModel.setFavoriteFilm(getContext(), index);
        filmViewModel.getFavoriteFilms().observe(getViewLifecycleOwner(), films -> {
            if (films != null) {
                filmList.clear();
                filmList.addAll(films);
                filmAdapter.setFilms(filmList);
                filmAdapter.notifyDataSetChanged();
                showLoading(false);
            }
            if (filmList.isEmpty()) {
                showTextNoData(true);
            }else{
                showTextNoData(false);
            }
        });
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
}
