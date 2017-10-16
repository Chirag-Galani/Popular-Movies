package com.example.chirag.project2.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.example.chirag.project2.R;
import com.example.chirag.project2.model.Movie;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class PrefManager {
    private static final String FAV_LIST = "FAV_LIST ";
    Context _context;
    static SharedPreferences pref;

    private static final String PREF_NAME = "Project2";
    int PRIVATE_MODE = 0;
    static SharedPreferences.Editor editor;

    public PrefManager(Context context) {
        this._context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);

    }

    public static  boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public static void toggleFavourite(Context context, Movie movie) {
        try {

            ArrayList<Movie> favouriteList;
            favouriteList = getFavouriteList();
            if (favouriteList == null)
                favouriteList = new ArrayList<Movie>();
            if (isFavourite(movie)) {
                Toast.makeText(context, context.getResources().getString(R.string.removed_from_fav), Toast.LENGTH_SHORT).show();
                if (positionFavourite(movie) != -1) {
                    favouriteList.remove(positionFavourite(movie));
                }
            } else {
                Toast.makeText(context, context.getResources().getString(R.string.add_to_fav), Toast.LENGTH_SHORT).show();
                favouriteList.add(0, movie);


            }


            editor = pref.edit();
            Gson gson = new Gson();
            editor.putString(FAV_LIST, gson.toJson(favouriteList));
            // save changes
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
//            return null;
        }
    }

    public static boolean isFavourite(Movie movie) {
        try {
            ArrayList<Movie> favouriteList;
            favouriteList = getFavouriteList();
            if (favouriteList == null)
                return false;
            else {
                for (int i = 0; i < favouriteList.size(); i++) {
                    if (favouriteList.get(i).getId().equals(movie.getId())) {
                        return true;
                    }
                }
                return false;

            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public static ArrayList<Movie> getFavouriteList() {
        try {
            List<Movie> favouriteList;// = new ArrayList<Movie>();

            if (pref.contains(FAV_LIST)) {
                String json = pref.getString(FAV_LIST, null);
                if (json != null) {
                    Gson gson = new Gson();
                    Movie[] favArry = gson.fromJson(json, Movie[].class);

                    favouriteList = Arrays.asList(favArry);
                    favouriteList = new ArrayList<Movie>(favouriteList);

                    return (ArrayList<Movie>) favouriteList;
                }
            }
            return new ArrayList<Movie>();
        } catch (Exception e) {

            e.printStackTrace();
            return new ArrayList<Movie>();
        }

    }

    public static void clearFavourite() {
        if (pref.getString(FAV_LIST, null) != null) {
            editor.putString(FAV_LIST, null);

            // save changes
            editor.commit();
        }
    }

    public static int positionFavourite(Movie movie) {
        try {
            ArrayList<Movie> favouriteList;
            favouriteList = getFavouriteList();
            if (favouriteList == null)
                return -1;
            else {
                for (int i = 0; i < favouriteList.size(); i++) {
                    if (favouriteList.get(i).getId().equals(movie.getId())) {
                        return i;
                    }
                }

                return -1;

            }
        } catch (Exception e) {
            e.printStackTrace();

            return -1;
        }
    }
}

