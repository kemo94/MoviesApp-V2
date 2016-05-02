package com.example.kemos.fainalmoviemalapp.Controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.kemos.fainalmoviemalapp.Model.FetchMovieTask;
import com.example.kemos.fainalmoviemalapp.Model.MovieItem;
import com.example.kemos.fainalmoviemalapp.Model.MovieOperations;
import com.example.kemos.fainalmoviemalapp.R;
import com.example.kemos.fainalmoviemalapp.View.Adapter.CustomGridAdapter;

import java.sql.SQLException;
import java.util.ArrayList;


public class MovieFragment extends Fragment {

    final String MOVIE_DATA = "movie_data";
    GridView gridview;
    ArrayList<MovieItem> movieItemArray;
    FetchMovieTask movieTask;
    int curPosition = 0 ;

    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences prefs;

    boolean chosen = false;
    MovieDetailFragment detailFragment;
    private MovieOperations movieDBOperation;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null)
            movieItemArray = savedInstanceState.getParcelableArrayList("moviesItem");

        else
        movieItemArray = new ArrayList<MovieItem>();

        movieDBOperation = new MovieOperations(getActivity());
        try {
            movieDBOperation.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        setHasOptionsMenu(true);


    }

        @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        setRetainInstance(true);

        return setView(rootView);
    }

    View setView(View rootView) {
        final Intent intent = new Intent(getActivity(), DetailActivity.class);
        gridview = (GridView) rootView.findViewById(R.id.grid);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                curPosition = position ;
                if (detailFragment != null && movieItemArray.size() != 0) {

                    detailFragment.setMovieItem(movieItemArray.get(position));
                    getActivity().setTitle(movieItemArray.get(position).getTitle() + " ("
                            + movieItemArray.get(position).getDate().substring(0, 4) + ")");
                } else if (movieItemArray.size() != 0) {
                    intent.putExtra("movieItem", movieItemArray.get(position));
                    startActivity(intent);
                 }
            }


        });
        if ( movieItemArray.size() > 0 )
            gridview.setAdapter(new CustomGridAdapter(getActivity(), movieItemArray));

        else
            updateMovies();

        return rootView;
    }

    public void delay() {

        final Handler h = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
            //    Log.d("TimerExample", "Going for... " + chosen);
                if ( movieTask.getMovieDataArray() != null)
                    movieItemArray = movieTask.getMovieDataArray();


                if ( movieItemArray.size() > 0 ) {

                    detailFragment.setMovieItem(movieItemArray.get(0));
                        getActivity().setTitle(movieItemArray.get(0).getTitle() + " ("
                                + movieItemArray.get(0).getDate().substring(0, 4) + ")");
                        chosen = true;

                }

                if (!chosen)
                    h.postDelayed(this, 0);
            }
        };
        h.postDelayed(runnable, 1000);
        if (chosen)
            h.removeCallbacks(runnable);

    }

    public void updateMovies() {

        prefs = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String choice =  prefs.getString("Choice", "Popular");
        if (  choice.equals("Popular")){

            String MOVIE_BASE_URL = "http://api.themoviedb.org/3/movie/popular?";
            movieTask = new FetchMovieTask(getActivity(), MOVIE_BASE_URL, MOVIE_DATA, gridview);
            movieTask.execute();
            if (detailFragment != null)
                delay();
        }
        else if ( choice.equals("TopRated")){

            String MOVIE_BASE_URL = "http://api.themoviedb.org/3/movie/top_rated?";
            movieTask = new FetchMovieTask(getActivity(), MOVIE_BASE_URL, MOVIE_DATA, gridview);
            movieTask.execute();
            if (detailFragment != null)
                delay();
        }
        else if ( choice.equals("Favorites")) {
            movieItemArray = movieDBOperation.getFavoritesMovies();
            gridview.setAdapter(new CustomGridAdapter(getActivity() ,movieItemArray ));
        }
        else if ( choice.equals("WatchList")){
            movieItemArray = movieDBOperation.getWatchList();
        gridview.setAdapter(new CustomGridAdapter(getActivity() ,movieItemArray ));
    }

    }

    public void setDetailFragment(MovieDetailFragment detailFragment) {
        this.detailFragment = detailFragment;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        if ( movieItemArray.size() > 0 ) {
            outState.putParcelableArrayList("moviesItem", movieItemArray);
            outState.putParcelable("movieItem", movieItemArray.get(curPosition));
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        ;
        if (id == R.id.action_refresh) {
            updateMovies();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onResume() {

        updateMovies();
        super.onResume();
    }

}
