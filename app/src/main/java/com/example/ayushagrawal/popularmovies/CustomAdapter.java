package com.example.ayushagrawal.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ayushagrawal on 9/6/16.
 */
public class CustomAdapter extends BaseAdapter {

    Context context;
    ArrayList<Movie> movieList = new ArrayList<Movie>();

    public CustomAdapter(Context context,ArrayList<Movie> movieList) {
        this.context = context;
        this.movieList = movieList;
    }

    @Override
    public int getCount() {
        return movieList.size();
    }

    @Override
    public Movie getItem(int position) {
        return movieList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position + 100000000;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.grid_item_image, parent, false);
        }
        Movie movieDb = getItem(position);
        ImageView imageViewcustom = (ImageView) convertView.findViewById(R.id.customImageView);
            Picasso.with(context).load("https://image.tmdb.org/t/p/w185" + movieDb.getPoster_path())
                    .placeholder(R.drawable.poster_place_holder)
                    .into(imageViewcustom);

        return convertView;
    }
}
