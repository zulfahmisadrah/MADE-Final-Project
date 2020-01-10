package com.zulfahmi.made_finalproject.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zulfahmi.made_finalproject.R;
import com.zulfahmi.made_finalproject.adapters.FilmAdapter;
import com.zulfahmi.made_finalproject.viewmodels.FilmViewModel;

public class FilmsFragment extends Fragment {
    public static final String ARG_SECTION_NUMBER = "section_number";
    public static final String EXTRA_QUERY = "extra_query";
    private FilmAdapter filmAdapter;
    private ProgressBar progressBar;
    private FilmViewModel filmViewModel;
    private RecyclerView recyclerview;

    private int index;
    private String query;

    public static FilmsFragment newInstance(int index, String query) {
        FilmsFragment fragment = new FilmsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(FilmsFragment.EXTRA_QUERY, query);
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }
    public FilmsFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
            query = getArguments().getString(EXTRA_QUERY);
            Log.d("search", "query=" +query + " index=" + index);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_films, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerview = view.findViewById(R.id.recyclerview);
        progressBar = view.findViewById(R.id.progressbar);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        filmViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(FilmViewModel.class);

        prepare();
        showLoading(true);

        filmViewModel.getFilms().observe(getViewLifecycleOwner(), films ->{
            if (films != null) {
                filmAdapter = new FilmAdapter(getContext());
                filmAdapter.notifyDataSetChanged();
                recyclerview.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
                recyclerview.setAdapter(filmAdapter);
                filmAdapter.setFilms(films);
                showLoading(false);
            }
        });
    }

    private void prepare(){
        if (index == 1) {
            filmViewModel.setFilm("movies", query);
        }else if(index == 2){
            filmViewModel.setFilm("tvshows", query);
        }
    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        }else{
            progressBar.setVisibility(View.GONE);
        }
    }

}
