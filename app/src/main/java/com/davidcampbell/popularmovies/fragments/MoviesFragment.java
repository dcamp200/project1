package com.davidcampbell.popularmovies.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.davidcampbell.popularmovies.MovieDetailActivity;
import com.davidcampbell.popularmovies.R;
import com.davidcampbell.popularmovies.domain.Movie;
import com.davidcampbell.popularmovies.webservices.MovieWebService;
import com.davidcampbell.popularmovies.webservices.RetrofitMovieWebService;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoviesFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static String LOG_TAG = MoviesFragment.class.getSimpleName();
    public enum Order {MOST_POPULAR,TOP_RATED}

    @Bind(R.id.posterGrid) GridView mPosterGrid;
    private MovieAdapter mGridArrayAdapter;

    public MoviesFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);
        ButterKnife.bind(this,rootView);
        mGridArrayAdapter = new MovieAdapter(this.getActivity(), new ArrayList<Movie>());
        mPosterGrid.setAdapter(mGridArrayAdapter);
        return rootView;
    }

    @OnItemClick(R.id.posterGrid)
    void showMovieDetails(int position) {
        Movie movie = mGridArrayAdapter.getItem(position);
        Log.d(LOG_TAG, "Showing details of movie :" + movie);
        Intent intent = new Intent(MoviesFragment.this.getActivity(), MovieDetailActivity.class);
        intent.putExtra("movie", movie);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "On resume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "On pause");
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "On start");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "On stop");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(LOG_TAG, "On attach");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        String order = sharedPreferences.getString(getString(R.string.pref_sortOrder_key), getString(R.string.pref_sort_order_default));
        fetchMovies(order);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(LOG_TAG, "On detach");
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        pref.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.d(LOG_TAG, "Preference change..." + key);
        if (key.equals(getString(R.string.pref_sortOrder_key))) {
            String order = sharedPreferences.getString(getString(R.string.pref_sortOrder_key), getString(R.string.pref_sort_order_default));
            Log.d(LOG_TAG, "New Sort order: Changed to:" + order);
            fetchMovies(order);
        }
    }

    private void fetchMovies(String order) {
        new FetchMoviesTask().execute(order);

    }

    class FetchMoviesTask extends AsyncTask<String, Void, List<Movie>> {

        @Override
        protected List<Movie> doInBackground(String... params) {
            String order = params[0];
            Log.d(LOG_TAG, "Fetching movies based on " + order);
            MovieWebService movieWebService = new RetrofitMovieWebService();
            switch(order) {
                case "Most Popular":
                    Log.d(LOG_TAG, "Fetching popular movies...");
                    return movieWebService.getPopularMovies();
                case "Top Rated":
                    Log.d(LOG_TAG, "Fetching top rated movies...");
                    return movieWebService.getTopRatedMovies();
                default:
                    return null;
            }
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            super.onPostExecute(movies);
            Log.d(LOG_TAG, "Returned " + movies.size() + " movies.");
            mGridArrayAdapter.clear();
            mGridArrayAdapter.addAll(movies);
            mGridArrayAdapter.notifyDataSetChanged();
            Log.d(LOG_TAG, "New Movies loaded");
        }
    }


}
