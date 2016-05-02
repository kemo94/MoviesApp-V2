package com.example.kemos.fainalmoviemalapp;


import android.content.Context;
import android.net.ConnectivityManager;

/** Utility help function class**/
public abstract  class Utility {
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String SIMILAR_MOVIES = "SIMILAR_MOVIES";
    public static final String  MOVIE_TRAILER = "movie_trailer";
    public static final  String MOVIE_REVIEW = "movie_review";
     public static final  String MOVIE_DATA = "movie_data";

    public static boolean isNetworkAvailable(Context context){

        ConnectivityManager connectivityManager = ((ConnectivityManager)  context.getSystemService(
                Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo()!= null && connectivityManager.getActiveNetworkInfo().isConnected();

    }
}
