package com.example.kemos.fainalmoviemalapp.Controller;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.kemos.fainalmoviemalapp.R;
import com.example.kemos.fainalmoviemalapp.Utility;


public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedpreferences = getSharedPreferences(Utility.MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        if ( savedInstanceState != null )
            setContentView(R.layout.activity_main);
        else{
            if (Utility.isNetworkAvailable(this)) {

                setContentView(R.layout.activity_main);
                onRetainNonConfigurationInstance();
                if (savedInstanceState == null) {
                    MovieFragment movieFragment = (MovieFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_movies);
                    MovieDetailFragment movieDetailFragment = (MovieDetailFragment) getSupportFragmentManager().findFragmentById(R.id.movie_details_container);
                    movieFragment.setDetailFragment(movieDetailFragment);

                }
            } else
                Toast.makeText(this, R.string.no_connection, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        final SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        searchView.setIconifiedByDefault(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_pop)
            editor.putString("Choice", "Popular");
        if (id == R.id.action_top_rated)
            editor.putString("Choice", "TopRated");
            if (id == R.id.action_favorites)
            editor.putString("Choice", "Favorites");
        if (id == R.id.action_watch_list)
            editor.putString("Choice", "WatchList");


        editor.commit();
        startActivity(new Intent(this,MainActivity.class));

        return super.onOptionsItemSelected(item);

    }

}
