package com.davidcampbell.popularmovies.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.davidcampbell.popularmovies.R;
import com.davidcampbell.popularmovies.domain.Movie;
import com.davidcampbell.popularmovies.domain.Review;
import com.davidcampbell.popularmovies.domain.Trailer;
import com.davidcampbell.popularmovies.model.MovieDao;
import com.davidcampbell.popularmovies.model.SQLLiteMovieDao;
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
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 */
public class MoviesDetailFragment extends Fragment {
    private static final String LOG_TAG = MoviesDetailFragment.class.getSimpleName();
    private static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/w185/";
    private static final String YOUTUBE_BASE_URL = "http://www.youtube.com/watch?v=";
    private static final String TRAILERS = "Trailers";
    private static final String REVIEWS = "Reviews";
    @Bind(R.id.poster) ImageView moviePoster;
    @Bind(R.id.movieName) TextView movieName;
    @Bind(R.id.runtime) TextView runtime;
    @Bind(R.id.releasedate) TextView releaseDate;
    @Bind(R.id.userRating) TextView userRating;
    @Bind(R.id.year) TextView yearView;
    @Bind(R.id.description) TextView descriptionView;
    @Bind(R.id.favButton) ImageButton favButton;

    private ShareActionProvider mShareActionProvider;
    private TrailerAdapter mTrailerAdapter;
    private ReviewAdapter mReviewAdapter;
    private MovieDao movieDao;
    private Movie mMovie;
    private String mTrailerUrl;
    private Bitmap favourited;
    private Bitmap notFavourited;

    private boolean dualPane = false;

    @Bind(R.id.reviewsAndTrailerslist)ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;



    public static MoviesDetailFragment newInstance(Movie movie, boolean dualPane) {
        MoviesDetailFragment fragment = new MoviesDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("movie", movie);
        args.putBoolean("dualPane", dualPane);
        fragment.setArguments(args);
        return fragment;
    }


    public MoviesDetailFragment() {
        // Required empty public constructor
    }

    public boolean isDualPane() {
        return dualPane;
    }

    public void setDualPane(boolean dualPane) {
        this.dualPane = dualPane;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMovie = (Movie)getArguments().getSerializable("movie");
            dualPane = getArguments().getBoolean("dualPane");
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(LOG_TAG, "Inflating details fragment...");
        movieDao = new SQLLiteMovieDao(getActivity());
        View rootView = inflater.inflate(R.layout.fragment_movies_detail, container, false);
        ButterKnife.bind(this,rootView);
        favourited = BitmapFactory.decodeResource(getResources(), R.drawable.icon_favorite_active);
        notFavourited = BitmapFactory.decodeResource(getResources(), R.drawable.icon_favorite_default);


        Map<String, List<?>> children = new HashMap<>();
        children.put("Trailers", new ArrayList<>());
        children.put("Reviews", new ArrayList<>());
        expandableListAdapter = new ExpandableListAdapter(this.getActivity(), Arrays.asList(new String[] {TRAILERS, REVIEWS}), children);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setFocusable(false);


        if (!dualPane) {
            mMovie = (Movie) getActivity().getIntent().getSerializableExtra("movie");
        }

        if (mMovie != null) {
            new FetchMovie().execute(mMovie.getId());
            new FetchMovieTrailersTask().execute(mMovie.getId());
            new FetchMovieReviewsTask().execute(mMovie.getId());
            Log.d(LOG_TAG, "Movie.." + mMovie);
            yearView.setText(mMovie.getRelease_date().substring(0,4));
            descriptionView.setText(mMovie.getOverview());
            movieName.setText(mMovie.getOriginal_title());
            releaseDate.setText(mMovie.getRelease_date());
            userRating.setText(Double.toString(mMovie.getVote_average()));
            String posterUrl = BASE_IMAGE_URL + mMovie.getPoster_path();
            Picasso.with(getActivity()).load(posterUrl).into(moviePoster);
           if (movieDao.checkIfFavorite(mMovie)) {
               favButton.setImageBitmap(favourited);
           } else {
               favButton.setImageBitmap(notFavourited);
           }
        }


        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //movieDao = new SQLLiteMovieDao(getActivity());
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.detailfragment, menu);

        // Retrieve the share menu item
        MenuItem menuItem = menu.findItem(R.id.action_share);

        // Get the provider and hold onto it to set/change the share intent.
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);


        if (mShareActionProvider != null) {
            if (mTrailerUrl != null) {
                mShareActionProvider.setShareIntent(createShareTrailerIntent(mTrailerUrl));
            }
        }
    }



    private Intent createShareTrailerIntent(String trailerUrl) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, trailerUrl);
        return shareIntent;
    }


    @OnClick(R.id.favButton)
    public void onClickFavorite() {
        Log.d(LOG_TAG, "Fav button clicked");
        if(movieDao.checkIfFavorite(mMovie)) {
            movieDao.removeFromFavorites(mMovie);
            favButton.setImageBitmap(notFavourited);
        } else {
            movieDao.addToFavorites(mMovie);
            favButton.setImageBitmap(favourited);
        }
    }

    class FetchMovie extends AsyncTask<Long, Void, Movie> {

        @Override
        protected Movie doInBackground(Long... params) {
            long movieId = params[0];
            Log.d(LOG_TAG, "Fetching movie details for id " + movieId);
            MovieWebService movieWebService = new RetrofitMovieWebService();
            Movie movie = movieWebService.getMovie(movieId);
            return movie;
        }

        @Override
        protected void onPostExecute(Movie movie) {
            super.onPostExecute(movie);
            Log.d(LOG_TAG, "Returned " + movie);
            if (movie != null) {
                runtime.setText(movie.getRuntime() + " mins");
            }

        }
    }

    class FetchMovieTrailersTask extends AsyncTask<Long, Void, List<Trailer>> {

        @Override
        protected List<Trailer> doInBackground(Long... params) {
            long movieId = params[0];
            Log.d(LOG_TAG, "Fetching movie trailers for id " + movieId);
            MovieWebService movieWebService = new RetrofitMovieWebService();
            List<Trailer> trailers = movieWebService.getTrailers(movieId);
            if (trailers.size() > 0) {
                mTrailerUrl = YOUTUBE_BASE_URL + trailers.get(0).getKey();
                if (mShareActionProvider != null) {
                    mShareActionProvider.setShareIntent(createShareTrailerIntent(mTrailerUrl));
                }
            }
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
            for(Review review:reviews) {
                review.setMovieTitle(mMovie.getTitle());
            }
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
