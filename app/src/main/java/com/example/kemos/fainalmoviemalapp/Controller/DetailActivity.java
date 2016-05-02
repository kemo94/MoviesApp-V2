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

import com.example.kemos.fainalmoviemalapp.Model.MovieItem;
import com.example.kemos.fainalmoviemalapp.R;

public class DetailActivity extends AppCompatActivity {

    Bundle extras ;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        extras = getIntent().getExtras();
         if ( extras != null ) {
             MovieItem movieItem  =  extras.getParcelable("movieItem");
             setTitle(movieItem.getTitle() + " (" + movieItem.getDate().substring(0,4) + ")");

             MovieDetailFragment movieDetailFragment = (MovieDetailFragment) getSupportFragmentManager().findFragmentById(R.id.movie_detail);
             movieDetailFragment.setMovieItem(movieItem);
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

        SharedPreferences.Editor editor = sharedpreferences.edit();
        if (id == R.id.action_pop)
            editor.putString("Choice", "TopRated");
        if (id == R.id.action_top_rated)
            editor.putString("Choice", "Popular");
        if (id == R.id.action_favorites)
            editor.putString("Choice", "Favorites");
        if (id == R.id.action_watch_list)
            editor.putString("Choice", "WatchList");



        editor.commit();
        startActivity(new Intent(this,MainActivity.class));

        return super.onOptionsItemSelected(item);

    }


}


