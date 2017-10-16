package com.example.chirag.project2.activities;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.chirag.project2.R;
import com.example.chirag.project2.adapters.ReviewAdapter;
import com.example.chirag.project2.model.Review;
import com.example.chirag.project2.utils.PrefManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;


public class ReviewActivity extends AppCompatActivity {
    Context context;
    List<Review> reviewList;
    ListView listView;
    long movie_id;
    String movieName;
    ReviewAdapter adapter1;

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        try {
            Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
            setSupportActionBar(toolbar);
            // Show the Up button in the action bar.
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }


            context = this;
            reviewList = new ArrayList<Review>();
            Intent intent = getIntent();
            Bundle extras = intent.getExtras();
            TextView textView_heading = (TextView) findViewById(R.id.textView_heading);
            if (savedInstanceState != null) {
                movie_id = savedInstanceState.getLong("ID_MOVIE_SELECTED");
                movieName = savedInstanceState.getString("NAME_MOVIE_SELECTED");
            } else if (extras != null) {

                movie_id = intent.getLongExtra("ID_MOVIE_SELECTED", (long) 00);
                movieName = intent.getStringExtra("NAME_MOVIE_SELECTED");
            }
            textView_heading.setText(context.getResources().getString(R.string.review_of)+" "+ movieName);

            listView = (ListView) findViewById(R.id.list_view);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(reviewList.get(position).getUrl()));
                    startActivity(intent);

                }
            });

            if (!PrefManager.isNetworkAvailable(context)) {
                textView_heading.setText(R.string.no_internet);
            } else
                new FetchReview().execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("ID_MOVIE_SELECTED", movie_id);
        outState.putString("NAME_MOVIE_SELECTED", movieName);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            movie_id = savedInstanceState.getLong("ID_MOVIE_SELECTED");
            movieName = savedInstanceState.getString("NAME_MOVIE_SELECTED");
    }
}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    class FetchReview extends AsyncTask<Void, Void, Void> {
        String url1 = "http://api.themoviedb.org/3/movie/"+movie_id+"/reviews?api_key=eaa79560709dc209a84e4932660338ed";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            String response = "";
            URL url = null;
            try {
                url = new URL(url1);
            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            }


            HttpURLConnection conn = null;
            try {
                if (url != null) {
                    conn = (HttpURLConnection) url.openConnection();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            try
            {
                if (conn != null) {
                    if(conn.getResponseCode() == HttpsURLConnection.HTTP_OK){
                        String line;
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        while ((line = br.readLine()) != null) {
                            response += line;
                        }
                    } else {
                        response = "";
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            reviewList.clear();

            if (response != null) {
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    JSONArray jsonArray = jsonObj.getJSONArray("results");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject in = jsonArray.getJSONObject(i);

                        String author, content,review_url;
                        String review_id;



                        author = in.getString("author");
                        review_id= in.getString("id");
                        content = in.getString("content");
                        review_url = in.getString("url");
                        Review review = new Review(review_url, author, content, review_id);
                        reviewList.add(review);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            adapter1 = new ReviewAdapter(context, reviewList);
            if (adapter1 != null)
                listView.setAdapter(adapter1);
        }
    }

}