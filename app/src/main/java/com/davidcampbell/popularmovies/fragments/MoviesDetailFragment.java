package com.davidcampbell.popularmovies.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.davidcampbell.popularmovies.R;
import com.davidcampbell.popularmovies.domain.Movie;
import com.davidcampbell.popularmovies.domain.Review;
import com.davidcampbell.popularmovies.domain.Trailer;
import com.davidcampbell.popularmovies.webservices.MovieWebService;
import com.davidcampbell.popularmovies.webservices.RetrofitMovieWebService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private static final String TRAILERS = "Trailers";
    private static final String REVIEWS = "Reviews";
    @Bind(R.id.poster) ImageView moviePoster;
    @Bind(R.id.movieName) TextView movieName;
    @Bind(R.id.releasedate) TextView releaseDate;
    @Bind(R.id.userRating) TextView userRating;
    @Bind(R.id.year) TextView yearView;
    @Bind(R.id.description) TextView descriptionView;

    private TrailerAdapter mTrailerAdapter;
    private ReviewAdapter mReviewAdapter;

    @Bind(R.id.reviewsAndTrailerslist)ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;


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

        Map<String, List<?>> children = new HashMap<>();
        children.put("Trailers", new ArrayList<>());
        children.put("Reviews", new ArrayList<>());
        expandableListAdapter = new ExpandableListAdapter(this.getActivity(), Arrays.asList(new String[] {TRAILERS, REVIEWS}), children);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setFocusable(false);

        Movie movie  = (Movie) getActivity().getIntent().getSerializableExtra("movie");
        new FetchMovieTrailersTask().execute(movie.getId());
        new FetchMovieReviewsTask().execute(movie.getId());
        Log.d(LOG_TAG, "Movie.." + movie);
        yearView.setText(movie.getRelease_date().substring(0,4));
        descriptionView.setText(movie.getOverview());
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



    class FetchMovieTrailersTask extends AsyncTask<Long, Void, List<Trailer>> {

        @Override
        protected List<Trailer> doInBackground(Long... params) {
            long movieId = params[0];
            Log.d(LOG_TAG, "Fetching movie details for id " + movieId);
            MovieWebService movieWebService = new RetrofitMovieWebService();
            List<Trailer> trailers = movieWebService.getTrailers(movieId);
            return trailers;
        }

        @Override
        protected void onPostExecute(List<Trailer> trailers) {
            super.onPostExecute(trailers);
            Log.d(LOG_TAG, "Returned " + trailers.size() + " trailers.");
            expandableListAdapter.updateChildren(TRAILERS,trailers);
            expandableListAdapter.notifyDataSetChanged();
        }
    }

    class FetchMovieReviewsTask extends AsyncTask<Long, Void, List<Review>> {

        @Override
        protected List<Review> doInBackground(Long... params) {
            long movieId = params[0];
            Log.d(LOG_TAG, "Fetching movie reviews for id " + movieId);
            MovieWebService movieWebService = new RetrofitMovieWebService();
            List<Review> reviews = movieWebService.getReviews(movieId);
            return reviews;
        }

        @Override
        protected void onPostExecute(List<Review> reviews) {
            super.onPostExecute(reviews);
            Log.d(LOG_TAG, "Returned " + reviews.size() + " reviews.");
            expandableListAdapter.updateChildren(REVIEWS,reviews);
            expandableListAdapter.notifyDataSetChanged();
        }
    }
}
