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
import com.example.chirag.project2.adapters.TrailerAdapter;
import com.example.chirag.project2.model.Trailer;
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


public class TrailerActivity extends AppCompatActivity {
    Context context;
    List<Trailer> trailerList;
    ListView listView;
    long movie_id;
    TrailerAdapter adapter1;
    String movieName;
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
            context = this;
            Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
            setSupportActionBar(toolbar);

            // Show the Up button in the action bar.
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }


            trailerList = new ArrayList<Trailer>();
            Intent intent = getIntent();
            Bundle extras = intent.getExtras();
            TextView textView_heading=(TextView)findViewById(R.id.textView_heading);

            if(savedInstanceState!=null)
            {
                movie_id =  savedInstanceState.getLong("ID_MOVIE_SELECTED");
                movieName =  savedInstanceState.getString("NAME_MOVIE_SELECTED");
            }
            else if (extras != null) {

                movie_id =  intent.getLongExtra("ID_MOVIE_SELECTED",(long)00);
                movieName =  intent.getStringExtra("NAME_MOVIE_SELECTED");

            }

            textView_heading.setText(context.getResources().getString(R.string.trailers_of)+" "+movieName);

            listView = (ListView) findViewById(R.id.list_view);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://www.youtube.com/watch?v="+trailerList.get(position).getYoutube_path()));
                    startActivity(intent);

                }
            });

            if(!PrefManager.isNetworkAvailable(context))
            {
                textView_heading.setText(R.string.no_internet);
            }
            else
            new FetchTrailer().execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("ID_MOVIE_SELECTED",movie_id);
        outState.putString("NAME_MOVIE_SELECTED",movieName);
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


    class FetchTrailer extends AsyncTask<Void, Void, Void> {
        String url1 = "http://api.themoviedb.org/3/movie/"+movie_id+"/videos?api_key=eaa79560709dc209a84e4932660338ed";

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
                conn = (HttpURLConnection) url.openConnection();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            try
            {
            if(conn.getResponseCode() == HttpsURLConnection.HTTP_OK){
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

            trailerList.clear();

            if (response != null) {
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    JSONArray jsonArray = jsonObj.getJSONArray("results");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject in = jsonArray.getJSONObject(i);


                        String provider, youtube_path,title;
                        String trailer_id;
                        provider = in.getString("site");
                        youtube_path= in.getString("key");
                        trailer_id = in.getString("id");
                        title = in.getString("name");
                        Trailer trailer = new Trailer(title, provider, youtube_path, trailer_id);
                        trailerList.add(trailer);
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
            adapter1 = new TrailerAdapter(context, trailerList);
            if (adapter1 != null)
                listView.setAdapter(adapter1);
        }
    }
}