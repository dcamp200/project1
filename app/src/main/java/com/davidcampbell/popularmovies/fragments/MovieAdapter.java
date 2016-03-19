package com.davidcampbell.popularmovies.fragments;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.davidcampbell.popularmovies.R;
import com.davidcampbell.popularmovies.domain.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * PopularMovies
 * MovieAdapter - Custom ArrayAdapter
 * Reference : http://developer.android.com/guide/topics/ui/layout/gridview.html#example
 * Created by david on 2016-03-13.
 */
public class MovieAdapter extends ArrayAdapter<Movie> {
    private static String LOG_TAG = MovieAdapter.class.getSimpleName();
    private static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/w185/";
    private final Context mContext;
    private List<Movie> mMovies;
    private LayoutInflater mLayoutInflater;


    public MovieAdapter(Context context, List<Movie> movies) {
        super(context, R.layout.movie_poster_item, movies);
        Log.d(LOG_TAG, "Initializing Grid Adapter...");
        this.mContext = context;
        this.mMovies = movies;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mMovies.size();
    }

    @Override
    public Movie getItem(int position) {
        return mMovies.get(position);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Log.d(LOG_TAG, "Adding new movie to grid...");
        View gridItemView = mLayoutInflater.inflate(R.layout.movie_poster_item, parent, false);
        ImageView moviePoster = (ImageView) gridItemView.findViewById(R.id.posterImage);

        Movie movie = mMovies.get(position);
        String posterUrl = BASE_IMAGE_URL + movie.getPoster_path();
        Log.d(LOG_TAG, "Poster url = " + posterUrl);
        Picasso.with(mContext).load(posterUrl).into(moviePoster);
        return gridItemView;
    }
}
