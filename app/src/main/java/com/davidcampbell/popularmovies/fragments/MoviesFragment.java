package com.davidcampbell.popularmovies.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

 * Use the {@link MoviesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MoviesFragment extends Fragment {
    private static String LOG_TAG = MoviesFragment.class.getSimpleName();
    private GridView posterGrid;
    private MovieAdapter mGridArrayAdapter;

    public MoviesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     * @return A new instance of fragment MoviesFragment.
     */
    public static MoviesFragment newInstance() {
        MoviesFragment fragment = new MoviesFragment();

        return fragment;
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
                intent.putExtra("movie",movie) ;
                startActivity(intent);
            }
        });


        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    @Override
    public void onStart() {
        super.onStart();
        new FetchPopularMoviesTask().execute();

    }


    @Override
    public void onDetach() {
        super.onDetach();
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
            Log.d(LOG_TAG, "New Movies loaded");
        }
    }


}
