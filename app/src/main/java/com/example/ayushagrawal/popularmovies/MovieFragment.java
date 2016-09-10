package com.example.ayushagrawal.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.gson.Gson;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by ayushagrawal on 9/6/16.
 */
public class MovieFragment extends Fragment {

    GridView mMovieGrid;

    public MovieFragment(){

    }

    @Override
    public void onStart() {
        super.onStart();
        load_movies();
    }

    private void load_movies() {
        LoadMovies loadMovies = new LoadMovies();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String preference = prefs.getString(getString(R.string.preference_key),
                getString(R.string.preference_default_value));
        loadMovies.execute(preference);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_main, container, false);
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);

        mMovieGrid = (GridView) rootView.findViewById(R.id.topMovieGrid);

        return rootView;

    }

    public class LoadMovies extends AsyncTask<String, Void, ArrayList<Movie> > {

        @Override
        protected ArrayList<Movie> doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {

                final String APPID_PARAM = "api_key";
                final String QUERY_PARAM = "sort_by";
                final String SORTING_ORDER = ".desc";
                final String LOAD_MOVIE_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
                final String MOVIE_API_KEY = "";
                
                Uri builtUri = Uri.parse(LOAD_MOVIE_BASE_URL).buildUpon()
                        .appendQueryParameter(QUERY_PARAM, params[0] + SORTING_ORDER)
                        .appendQueryParameter(APPID_PARAM, MOVIE_API_KEY)
                        .build();

                URL url = new URL(builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                String results = IOUtils.toString(inputStream);
                ArrayList<Movie> movieArrayList =  getMoviesDataFromJSON(results);
                //inputStream.close();
                return movieArrayList;

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        private ArrayList<Movie> getMoviesDataFromJSON(String results) {
            ArrayList<Movie> movieArrayList = new ArrayList<Movie>();
            try {
                JSONObject mainObject = new JSONObject(results);

                JSONArray resultsArray = mainObject.getJSONArray("results");
                for (int i = 0; i < resultsArray.length(); i++) {
                    JSONObject indexObject = resultsArray.getJSONObject(i);
                    Movie indexMovie = new Movie();
                    indexMovie.setBackdrop_path(indexObject.getString("backdrop_path"));
                    indexMovie.setId(indexObject.getInt("id"));
                    indexMovie.setOriginal_title(indexObject.getString("original_title"));
                    indexMovie.setOverview(indexObject.getString("overview"));
                    indexMovie.setRelease_date(indexObject.getString("release_date"));
                    indexMovie.setPoster_path(indexObject.getString("poster_path"));
                    indexMovie.setPopularity(indexObject.getDouble("popularity"));
                    indexMovie.setTitle(indexObject.getString("title"));
                    indexMovie.setVote_average(indexObject.getInt("vote_average"));
                    indexMovie.setVote_count(indexObject.getInt("vote_count"));

                    movieArrayList.add(indexMovie); // Add each item to the list
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("Error", "JSON Error", e);
            }
            return movieArrayList;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movieArrayList) {
            if (!movieArrayList.isEmpty()) {
                load_adapter(movieArrayList);
            }

        }

        private void load_adapter(ArrayList<Movie> movieArrayList) {
            final CustomAdapter customAdapter = new CustomAdapter(getContext(),movieArrayList);
            mMovieGrid.setAdapter(customAdapter);

            mMovieGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Movie movie = customAdapter.getItem(position);

                    Intent intent = new Intent(getActivity(), DetailActivity.class)
                            .putExtra(Intent.EXTRA_TEXT, (new Gson()).toJson(movie));

                    startActivity(intent);
                }
            });

        }
    }
}
