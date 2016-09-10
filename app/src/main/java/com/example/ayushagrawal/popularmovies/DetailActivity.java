package com.example.ayushagrawal.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

public class DetailActivity extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DetailFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public static class DetailFragment extends Fragment {

        public DetailFragment() {
            setHasOptionsMenu(true);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.content_detail, container, false);
            Intent intent = getActivity().getIntent();
            if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
                Movie movieIntent = (new Gson()).fromJson(intent.getStringExtra(Intent.EXTRA_TEXT), Movie.class);

                TextView titleText = (TextView) rootView.findViewById(R.id.detail_title);
                titleText.setText(movieIntent.getTitle());

                // Load image in Image View
                ImageView detailImage = (ImageView) rootView.findViewById(R.id.detail_image);
                Picasso.with(getContext()).load("https://image.tmdb.org/t/p/w185" + movieIntent.getPoster_path())
                        .placeholder(R.drawable.poster_place_holder)
                        .into(detailImage);

                // Load Release Date
                TextView releaseDateText = (TextView) rootView.findViewById(R.id.detail_release_date);
                releaseDateText.setText(movieIntent.getRelease_date());

                // Load Popularity
                TextView popularityText = (TextView) rootView.findViewById(R.id.detail_popularity);
                popularityText.setText("Popularity: " + movieIntent.getPopularity());

                //Load Overview
                TextView overviewText = (TextView) rootView.findViewById(R.id.detail_overview);
                overviewText.setText(movieIntent.getOverview());

                // Load Average
                TextView voteAverageText = (TextView) rootView.findViewById(R.id.detail_vote_average);
                voteAverageText.setText("Voter Average: " + movieIntent.getVote_average());

                // Load Vote Count
                TextView voteCountText = (TextView) rootView.findViewById(R.id.detail_vote_count);
                voteCountText.setText("Voter Count: " + movieIntent.getVote_count());

            }
            return rootView;
        }

    }
}
