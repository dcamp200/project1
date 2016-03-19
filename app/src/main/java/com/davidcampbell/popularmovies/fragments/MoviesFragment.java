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
import android.widget.AdapterView;
import android.widget.GridView;

import com.davidcampbell.popularmovies.MovieDetailActivity;
import com.davidcampbell.popularmovies.R;
import com.davidcampbell.popularmovies.domain.Movie;
import com.davidcampbell.popularmovies.webservices.MovieWebService;
import com.davidcampbell.popularmovies.webservices.RetrofitMovieWebService;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoviesFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static String LOG_TAG = MoviesFragment.class.getSimpleName();

    private GridView posterGrid;
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

        posterGrid = (GridView)rootView.findViewById(R.id.posterGrid);

        mGridArrayAdapter = new MovieAdapter(this.getActivity(), new ArrayList<Movie>());
        posterGrid.setAdapter(mGridArrayAdapter);

        posterGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Movie movie = mGridArrayAdapter.getItem(position);
                Log.d(LOG_TAG, "Showing details of movie :" + movie);
                Intent intent = new Intent(MoviesFragment.this.getActivity(), MovieDetailActivity.class);
                intent.putExtra("movie", movie);
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(LOG_TAG,"On resume");
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
        if(order.equals("Most Popular")) {
            new FetchPopularMoviesTask().execute();
        } else if(order.equals("Top Rated")) {
            new FetchTopRatedMoviesTask().execute();
        }
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
        Log.d(LOG_TAG,"Preference change..." + key);
        if (key.equals(getString(R.string.pref_sortOrder_key))) {
            String order = sharedPreferences.getString(getString(R.string.pref_sortOrder_key), getString(R.string.pref_sort_order_default));
            Log.d(LOG_TAG, "New Sort order: Changed to:" + order);
            if(order.equals("Most Popular")) {
                new FetchPopularMoviesTask().execute();
            } else if(order.equals("Top Rated")) {
                new FetchTopRatedMoviesTask().execute();
            }
        }
    }


    class FetchPopularMoviesTask extends AsyncTask<Void, Void, List<Movie>> {

        @Override
        protected List<Movie> doInBackground(Void... params) {
            Log.d(LOG_TAG, "Fetching popular movies...");
            MovieWebService movieWebService = new RetrofitMovieWebService();
            List<Movie> popularMovies = movieWebService.getPopularMovies();
            return popularMovies;
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            super.onPostExecute(movies);
            Log.d(LOG_TAG, "Returned " + movies.size() + " popular movies.");
            mGridArrayAdapter.clear();
            mGridArrayAdapter.addAll(movies);
            mGridArrayAdapter.notifyDataSetChanged();
            Log.d(LOG_TAG, "Popular Movies loaded");
        }
    }

    class FetchTopRatedMoviesTask extends AsyncTask<Void, Void, List<Movie>> {

        @Override
        protected List<Movie> doInBackground(Void... params) {
            Log.d(LOG_TAG, "Fetching top rated movies...");
            MovieWebService movieWebService = new RetrofitMovieWebService();
            List<Movie> topRatedMovies = movieWebService.getTopRatedMovies();
            return topRatedMovies;
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            super.onPostExecute(movies);
            Log.d(LOG_TAG, "Returned " + movies.size() + " top rated movies.");
            mGridArrayAdapter.clear();
            mGridArrayAdapter.addAll(movies);
            mGridArrayAdapter.notifyDataSetChanged();
            Log.d(LOG_TAG, "Top Rated Movies loaded");
        }
    }


}
