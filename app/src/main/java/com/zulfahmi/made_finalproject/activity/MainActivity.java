package com.zulfahmi.made_finalproject.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.zulfahmi.made_finalproject.R;
import com.zulfahmi.made_finalproject.fragments.FavoriteTabFragment;
import com.zulfahmi.made_finalproject.fragments.FilmsFragment;
import com.zulfahmi.made_finalproject.fragments.MyPreferenceFragment;

public class MainActivity extends AppCompatActivity {
    int index = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener( item -> {
            switch (item.getItemId()) {
                case R.id.menu_movies:
                    index = 1;
                    loadFragment(index);
                    break;
                case R.id.menu_tv_shows:
                    index = 2;
                    loadFragment(index);
                    break;
                case R.id.menu_favorite:
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new FavoriteTabFragment(), "favorite").commit();
                    break;
            }
            return true;
        });
        if(savedInstanceState==null){
            bottomNavigationView.setSelectedItemId(R.id.menu_movies);
        }
    }

    private void loadFragment(int index) {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, FilmsFragment.newInstance(index, ""), "film").commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if (searchManager != null) {
            SearchView searchView = (SearchView) (menu.findItem(R.id.menu_search_toolbar)).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setQueryHint(getResources().getString(R.string.search_hint));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
                @Override
                public boolean onQueryTextSubmit(String s) {
                    searchView.clearFocus();
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_container, FilmsFragment.newInstance(index, s), "film_search").commit();
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    return false;
                }
            });
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.language_settings) {
            startActivity(new Intent(Settings.ACTION_LOCALE_SETTINGS));
        } else if (item.getItemId() == R.id.reminder_settings) {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new MyPreferenceFragment()).commit();
        }
        return super.onOptionsItemSelected(item);
    }
}
