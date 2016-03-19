package com.davidcampbell.popularmovies.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.davidcampbell.popularmovies.R;
import com.davidcampbell.popularmovies.domain.Movie;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 */
public class MoviesDetailFragment extends Fragment {
    private static final String LOG_TAG = MoviesDetailFragment.class.getSimpleName();
    private static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/w185/";
    @Bind(R.id.poster) ImageView moviePoster;
    @Bind(R.id.movieName) TextView movieName;
    @Bind(R.id.releasedate) TextView releaseDate;
    @Bind(R.id.userRating) TextView userRating;

    public MoviesDetailFragment() {
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
        Log.d(LOG_TAG, "Inflating details fragment...");
        View rootView = inflater.inflate(R.layout.fragment_movies_detail, container, false);
        ButterKnife.bind(this,rootView);
        Movie movie  = (Movie) getActivity().getIntent().getSerializableExtra("movie");
        Log.d(LOG_TAG, "Movie.." + movie);
        String year = movie.getRelease_date().substring(0,4);
        String description = movie.getOverview();
        ((TextView)rootView.findViewById(R.id.year)).setText(year);
        ((TextView)rootView.findViewById(R.id.description)).setText(description);
        movieName.setText(movie.getOriginal_title());
        releaseDate.setText(movie.getRelease_date());
        userRating.setText(Double.toString(movie.getVote_average()));
        String posterUrl = BASE_IMAGE_URL + movie.getPoster_path();
        Picasso.with(getActivity()).load(posterUrl).into(moviePoster);

        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
