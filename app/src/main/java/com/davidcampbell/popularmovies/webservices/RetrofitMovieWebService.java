package com.davidcampbell.popularmovies.webservices;

import android.util.Log;

import com.davidcampbell.popularmovies.BuildConfig;
import com.davidcampbell.popularmovies.domain.Movie;
import com.davidcampbell.popularmovies.domain.Review;
import com.davidcampbell.popularmovies.domain.Trailer;

import java.util.Collections;
import java.util.List;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * PopularMovies
 * Created by david on 2016-03-13.
 */
public class RetrofitMovieWebService implements MovieWebService {
    private static final String LOG_TAG = RetrofitMovieWebService.class.getSimpleName();
    private String endPoint = "http://api.themoviedb.org";
    private RestAdapter movieDBRestAdapter;
    private MovieDBRestService movieDBRestService;
    private MovieDBRequestInterceptor movieDBRequestInterceptor = new MovieDBRequestInterceptor();



    public RetrofitMovieWebService() {
        movieDBRestAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setRequestInterceptor(movieDBRequestInterceptor)
                .setEndpoint(endPoint).build();
        movieDBRestService = movieDBRestAdapter.create(MovieDBRestService.class);
    }


    @Override
    public List<Movie> getPopularMovies() {
        MoviesResponse moviesResponse = movieDBRestService.getPopularMovies();
        return (moviesResponse != null) ? moviesResponse.getResults() : Collections.<Movie>emptyList();
    }


    @Override
    public List<Movie> getTopRatedMovies() {
        MoviesResponse moviesResponse = movieDBRestService.getTopRatedMovies();
        return (moviesResponse != null) ? moviesResponse.getResults() : Collections.<Movie>emptyList();
    }

    @Override
    public List<Review> getReviews(long movieId) {
        ReviewsResponse reviewsResponse = movieDBRestService.getMovieReviews(movieId);
        return (reviewsResponse != null) ? reviewsResponse.getResults() : Collections.<Review>emptyList();
    }

    @Override
    public List<Trailer> getTrailers(long movieId) {
        TrailerResponse trailerResponse = movieDBRestService.getMovieTrailers(movieId);
        return (trailerResponse != null) ? trailerResponse.getResults() : Collections.<Trailer>emptyList();
    }


    /** RetroFit interfaces **/
    public interface MovieDBRestService {

        // e.g. http://api.themoviedb.org/3/movie/popular?api_key
        @GET("/3/movie/popular")
        MoviesResponse getPopularMovies();

        // e.g. http://api.themoviedb.org/3/movie/top_rated?api_key
        @GET("/3/movie/top_rated")
        MoviesResponse getTopRatedMovies();

        // e.g. http://api.themoviedb.org/3/movie/{id}/videos?api_key
        @GET("/3/movie/{id}/videos")
        TrailerResponse getMovieTrailers(@Path("id") long id);

        // e.g. http://api.themoviedb.org/3/movie/{id}/reviews?api_key
        @GET("/3/movie/{id}/reviews")
        ReviewsResponse getMovieReviews(@Path("id") long id);

    }

    /**
     * This class is used to insert the api key on each transaction.
     * It intercepts client requests before they are sent to the
     * endpoint.
     * It needs to be set on the RestAdapter Builder
     * Example usage:
     * RestAdapter restAdapter = new RestAdapter.Builder()
     *               .setRequestInterceptor(requestInterceptor)
     */
    class MovieDBRequestInterceptor implements RequestInterceptor {

        @Override
        public void intercept(RequestFacade request) {
            Log.d(LOG_TAG, "Adding api key:[" + BuildConfig.MOVIEDB_API_KEY + "]");
            request.addQueryParam("api_key",BuildConfig.MOVIEDB_API_KEY);
        }

    };



    /**
     * MoviesResponse
     * Class used in movies query response.
     */
    class MoviesResponse {
        private int page;
        private List<Movie> results;

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public List<Movie> getResults() {
            return results;
        }

        public void setResults(List<Movie> results) {
            this.results = results;
        }
    }


    /**
     * TrailersResponse
     * Helper class used to model trailers query response
     */
    class TrailerResponse {
        private int id;
        private List<Trailer> results;

        public TrailerResponse() {
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public List<Trailer> getResults() {
            return results;
        }

        public void setResults(List<Trailer> results) {
            this.results = results;
        }
    }


    class ReviewsResponse {
        private int id;
        private int page;
        private int totalPages;
        private int totalresults;
        private List<Review> results;

        public ReviewsResponse() {
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getTotalPages() {
            return totalPages;
        }

        public void setTotalPages(int totalPages) {
            this.totalPages = totalPages;
        }

        public int getTotalresults() {
            return totalresults;
        }

        public void setTotalresults(int totalresults) {
            this.totalresults = totalresults;
        }

        public List<Review> getResults() {
            return results;
        }

        public void setResults(List<Review> results) {
            this.results = results;
        }
    }
}
