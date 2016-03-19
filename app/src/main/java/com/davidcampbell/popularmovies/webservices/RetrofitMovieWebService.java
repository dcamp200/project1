package com.davidcampbell.popularmovies.webservices;

import android.util.Log;

import com.davidcampbell.popularmovies.BuildConfig;
import com.davidcampbell.popularmovies.domain.Movie;

import java.util.Collections;
import java.util.List;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.http.GET;

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




    /** RetroFit interfaces **/
    public interface MovieDBRestService {

        // e.g. http://api.themoviedb.org/3/movie/popular?api_key
        @GET("/3/movie/popular")
        MoviesResponse getPopularMovies();

        // e.g. http://api.themoviedb.org/3/movie/top_rated?api_key
        @GET("/3/movie/top_rated")
        MoviesResponse getTopRatedMovies();

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
}
