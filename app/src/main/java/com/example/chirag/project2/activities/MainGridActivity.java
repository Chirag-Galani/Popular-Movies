package com.example.chirag.project2.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chirag.project2.R;
import com.example.chirag.project2.model.Movie;
import com.example.chirag.project2.utils.PrefManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * An activity representing a list of Movies. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link DescriptionActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class MainGridActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private static List<Movie> moviesArrayList;
    private List<Movie> favMovieList;
    private List<Movie> originalMoviesArrayList; //Original will contain the unsorted list as receieved
    private TextView textView_title;
    private static boolean mTwoPane;
    private static Context context;
    RecyclerView recyclerView;
    private GridLayoutManager lLayout;
    private int POPULARITY_MODE = 0;
    private int RATING_MODE = 1;
    private int FAVOURITE_MODE = 2;
    private int mode = POPULARITY_MODE; //
    private String ORIGINAL_MOVIE_ARRAY = "ORIGINAL_MOVIE_ARRAY";
    private String SORT_MODE = "SORT_MODE";
    FrameLayout containerFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            context = this;
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            toolbar.setTitle(getTitle());

            recyclerView = (RecyclerView) findViewById(R.id.movie_list);

            lLayout = new GridLayoutManager(this,2);
            recyclerView.setLayoutManager(lLayout);


            if (findViewById(R.id.movie_detail_container) != null) {
                // The detail container view will be present only in the
                // large-screen layouts (res/values-w900dp).
                // If this view is present, then the
                // activity should be in two-pane mode.
                mTwoPane = true;
                containerFrame = (FrameLayout) findViewById(R.id.movie_detail_container);

            }

            if(!mTwoPane && getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                lLayout.setSpanCount(3);
            }

            if (savedInstanceState != null) {
                originalMoviesArrayList = savedInstanceState.getParcelableArrayList(ORIGINAL_MOVIE_ARRAY);
                mode=savedInstanceState.getInt(SORT_MODE);
            } else {
                originalMoviesArrayList = new ArrayList<Movie>();
            }

            moviesArrayList = new ArrayList<Movie>();
            textView_title = (TextView) findViewById(R.id.textView_title_grid);

            if (!PrefManager.isNetworkAvailable(context)) {
                textView_title.setText(R.string.no_internet);
            } else {
                if (mode == POPULARITY_MODE)
                    new FetchMovie().execute(true);
                else if (mode == RATING_MODE)
                    new FetchMovie().execute(false);
                else
                    favouriteMode();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            originalMoviesArrayList = savedInstanceState.getParcelableArrayList(ORIGINAL_MOVIE_ARRAY);
            mode = savedInstanceState.getInt(SORT_MODE);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(ORIGINAL_MOVIE_ARRAY, (ArrayList<? extends Parcelable>) originalMoviesArrayList);
        outState.putInt(SORT_MODE, mode);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        int a=recyclerView.getVerticalScrollbarPosition();
        if(!mTwoPane && newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE ) {
            lLayout.setSpanCount(3);

        }else
        {

            lLayout.setSpanCount(2);

        }
        recyclerView.setVerticalScrollbarPosition(a);
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        if (mode == FAVOURITE_MODE) {
            favouriteMode();
        }
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mTwoPane) {
            containerFrame.setVisibility(View.INVISIBLE);
        }

        switch (item.getItemId()) {
            case R.id.action_sort__highest_rated:

                textView_title.setText(R.string.sorted_by_rating);
                if (!PrefManager.isNetworkAvailable(context)) {
                    textView_title.setText(R.string.no_internet);
                } else
                    new FetchMovie().execute(false);

                return true;

            case R.id.action_sort_most_popular:
                textView_title.setText(R.string.sorted_by_popularity);
                if (!PrefManager.isNetworkAvailable(context)) {
                    textView_title.setText(R.string.no_internet);
                } else
                    new FetchMovie().execute(true);
                return true;

            case R.id.action_show_favourites:
                mode = FAVOURITE_MODE;
                favouriteMode();
                return true;
        }


        return super.onOptionsItemSelected(item);
    }

    public void favouriteMode() {

        moviesArrayList = new PrefManager(this).getFavouriteList();
        if (moviesArrayList.isEmpty())
            textView_title.setText(R.string.no_favourites_found);
        else {
            textView_title.setText(R.string.sorted_by_favourite);
        }
        setupRecyclerView(recyclerView, moviesArrayList);

    }

    class FetchMovie extends AsyncTask<Boolean, Void, Void> {
        String url1 = "http://api.themoviedb.org/3/discover/movies?api_key=eaa79560709dc209a84e4932660338ed";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Boolean... arg0) {
            if (arg0[0]) {
                url1 = "http://api.themoviedb.org/3/movie/popular?api_key=eaa79560709dc209a84e4932660338ed";
                mode = POPULARITY_MODE;
            }
            else if (!arg0[0]) {
                url1 = "http://api.themoviedb.org/3/movie/top_rated?api_key=eaa79560709dc209a84e4932660338ed";
                mode = RATING_MODE;
            }
            String response = "";
            URL url = null;
            try {
                url = new URL(url1);
            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            }


            HttpURLConnection conn = null;
            try {
                conn = (HttpURLConnection) url.openConnection();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setDoInput(true);
            try {
                conn.setRequestMethod("POST");
            } catch (ProtocolException e) {
                e.printStackTrace();
            }

            conn.setDoOutput(true);
            OutputStream os = null;
            try {
                os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.flush();
                writer.close();
                os.close();
                int responseCode = conn.getResponseCode();
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        response += line;
                    }
                } else {
                    response = "";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            moviesArrayList.clear();


            if (response != null) {
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    JSONArray jsonArray = jsonObj.getJSONArray("results");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject in = jsonArray.getJSONObject(i);

                        String poster_path, overview, release_date, original_title, title;
                        String backdrop_path, original_language;
                        Boolean adult, video;
                        double vote_average, popularity;
                        long vote_count, id;
                        poster_path = in.getString("poster_path");
                        adult = in.getBoolean("adult");
                        overview = in.getString("overview");
                        release_date = in.getString("release_date");
                        id = in.getLong("id");
                        original_title = in.getString("original_title");
                        original_language = in.getString("original_language");
                        title = in.getString("title");
                        backdrop_path = in.getString("backdrop_path");
                        popularity = in.getDouble("popularity");
                        vote_count = in.getLong("vote_count");
                        video = in.getBoolean("video");
                        vote_average = in.getDouble("vote_average");
                        Movie movie = new Movie(poster_path, adult, overview, release_date, id, original_title, original_language, title, backdrop_path, popularity, vote_count, video, vote_average);
                        moviesArrayList.add(movie);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {

            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            originalMoviesArrayList = moviesArrayList;
            Collections.sort(moviesArrayList, new Comparator<Movie>() {
                public int compare(Movie a1, Movie a2) {
                    if (a1.getPopularity() > a2.getPopularity()) {
                        return -1;
                    }
                    if (a1.getPopularity() < a2.getPopularity()) {
                        return 1;
                    }
                    return 0;
                }
            });
            setupRecyclerView(recyclerView, moviesArrayList);

        }
    }


    public void setupRecyclerView(@NonNull RecyclerView recyclerView, List<Movie> new_MoviesArrayList) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(new_MoviesArrayList));


    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private List<Movie> movieList;

        public SimpleItemRecyclerViewAdapter(List<Movie> items) {
            movieList = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.grid_item_card, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            movieList = moviesArrayList;
            holder.mItem = movieList.get(position);
            holder.name.setText(movieList.get(position).getTitle());
            holder.icon.setScaleType(ImageView.ScaleType.CENTER_CROP);//FIT_XY spoils it
            Picasso.with(context)
                    .load("http://image.tmdb.org/t/p/w154/" + movieList.get(position).getPoster_path())
                    .fit()
                    .into(holder.icon);
            holder.name.setVisibility(View.VISIBLE);
            holder.icon.setVisibility(View.VISIBLE);
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putParcelable(DescriptionFragment.ARG_ITEM, movieList.get(position));
                        DescriptionFragment fragment = new DescriptionFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.movie_detail_container, fragment)
                                .commit();
                        containerFrame.setVisibility(View.VISIBLE);
                    } else {
                        Intent intent = new Intent(context, DescriptionActivity.class);
                        intent.putExtra(DescriptionFragment.ARG_ITEM, (Parcelable) moviesArrayList.get(position));
                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return movieList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public Movie mItem;
            public final TextView name;
            public final ImageView icon;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                name = (TextView) view.findViewById(R.id.textView_review_title);
                icon = (ImageView) view.findViewById(R.id.icon);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + name.getText() + "'";
            }
        }
    }
}
