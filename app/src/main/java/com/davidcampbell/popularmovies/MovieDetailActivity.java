package com.davidcampbell.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.davidcampbell.popularmovies.fragments.MoviesDetailFragment;

public class MovieDetailActivity extends AppCompatActivity {
    private static final String LOG_TAG = MovieDetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "Showing movie details...");
        setContentView(R.layout.activity_movie_detail);
        MoviesDetailFragment moviesFragment = new MoviesDetailFragment();
        Log.d(LOG_TAG, "Adding movie details fragment...");
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, moviesFragment)
                    .commit();
        }

    }

}
