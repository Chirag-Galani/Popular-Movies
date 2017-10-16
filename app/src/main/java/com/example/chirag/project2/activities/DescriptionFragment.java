package com.example.chirag.project2.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chirag.project2.R;
import com.example.chirag.project2.model.Movie;
import com.example.chirag.project2.utils.PrefManager;
import com.squareup.picasso.Picasso;

/**
 * A fragment representing a single Movie detail screen.
 * This fragment is either contained in a {@link MainGridActivity}
 * in two-pane mode (on tablets) or a {@link DescriptionActivity}
 * on handsets.
 */
public class DescriptionFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM = "MOVIE_SELECTED";

    private Movie movieItem;
    private Context context;
    ImageView imageViewPoster;
    TextView textViewTitle, textViewDescription, textViewVoteAverage, textViewReleaseDate, textView_language, textView_popularity;
    Button buttonWatchTrailer, buttonReview;


    public DescriptionFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();

        if (savedInstanceState != null) {
            movieItem = savedInstanceState.getParcelable(ARG_ITEM);
        } else if (getArguments().containsKey(ARG_ITEM)) {
            movieItem = getArguments().getParcelable(ARG_ITEM);
        }
/*
        Bundle arguments = new Bundle();
        arguments.putParcelable(DescriptionFragment.ARG_ITEM,
                movieItem);
        DescriptionFragment fragment = new DescriptionFragment();
        fragment.setArguments(arguments);

        getFragmentManager().beginTransaction()
                .replace(R.id.movie_detail_container, fragment)
                .commit();

*/
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_description, container, false);

        if (movieItem != null) {

            imageViewPoster = (ImageView) rootView.findViewById(R.id.imageView_poster);
            textViewVoteAverage = (TextView) rootView.findViewById(R.id.textView_voteAverage);
            textViewDescription = (TextView) rootView.findViewById(R.id.textView_description);
            textViewReleaseDate = (TextView) rootView.findViewById(R.id.textViewReleaseDate);
            textViewTitle = (TextView) rootView.findViewById(R.id.textView_review_title);
            textView_language = (TextView) rootView.findViewById(R.id.textView_original_language);
            textView_popularity= (TextView) rootView.findViewById(R.id.textViewPopularity);
            buttonWatchTrailer = (Button) rootView.findViewById(R.id.button_watch_trailer);
            buttonReview = (Button) rootView.findViewById(R.id.button_readReview);
//            textViewOther= (TextView) findViewById(R.id.textView_other);
            initializeViews();

        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ARG_ITEM,movieItem);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            movieItem = savedInstanceState.getParcelable(ARG_ITEM);
            if(movieItem!=null)
                initializeViews();
        }

    }
    private void initializeViews()
    {
        Picasso.with(context)
                .load("http://image.tmdb.org/t/p/w500/" + movieItem.getPoster_path())
//                .fit()
                .into(imageViewPoster);
        textViewTitle.setText(movieItem.getTitle());
        textViewVoteAverage.setText("" + movieItem.getVote_average());
        textViewDescription.setText(movieItem.getOverview());
        textView_language.setText(movieItem.getOriginal_language());
        textViewVoteAverage.setText(""+movieItem.getVote_average());
        textViewReleaseDate.setText(movieItem.getRelease_date());
        textView_popularity.setText(""+movieItem.getPopularity());

        buttonWatchTrailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(context, TrailerActivity.class);
                myIntent.putExtra("ID_MOVIE_SELECTED", movieItem.getId()); //Optional parameters
                myIntent.putExtra("NAME_MOVIE_SELECTED", movieItem.getTitle()); //Optional parameters
                startActivity(myIntent);
            }
        });

        buttonReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(context, ReviewActivity.class);
                myIntent.putExtra("ID_MOVIE_SELECTED", movieItem.getId()); //Optional parameters
                myIntent.putExtra("NAME_MOVIE_SELECTED", movieItem.getTitle()); //Optional parameters
                startActivity(myIntent);
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        inflater.inflate(R.menu.menu_fav_desc, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem fave = menu.findItem(R.id.add);
        MenuItem unfave = menu.findItem(R.id.remove);

        Boolean isFav = new PrefManager(context).isFavourite(movieItem);
        if (fave != null || unfave != null) {
            fave.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            unfave.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            fave.setVisible(!isFav);
            unfave.setVisible(isFav);


        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.add:
                new PrefManager(context).toggleFavourite(context, movieItem);
                getActivity().invalidateOptionsMenu();
                return true;

            case R.id.remove:
                new PrefManager(context).toggleFavourite(context, movieItem);
                getActivity().invalidateOptionsMenu();
                return true;


        }

        return super.onOptionsItemSelected(item);
    }


}



