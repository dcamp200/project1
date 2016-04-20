package com.davidcampbell.popularmovies.webservices;

import com.davidcampbell.popularmovies.domain.Movie;
import com.davidcampbell.popularmovies.domain.Review;
import com.davidcampbell.popularmovies.domain.Trailer;

import java.util.List;

/**
 * PopularMovies
 * Created by david on 2016-03-13.
 */
public interface MovieWebService {

    /**
     * Method to retrieve popular movies
     * @return List<Movie>  a list of {@link Movie} objects
     */
    List<Movie> getPopularMovies();

    /**
     * Method to retrieve top rated movies
     * @return List<Movie>  a list of {@link Movie} objects
     */
    List<Movie> getTopRatedMovies();


    /**
     * Method to return a movie's associated trailers
     * @param movieId
     * @return List<Trailer> A list of {@link Trailer} objects
     */
    List<Trailer> getTrailers(long movieId);

    /**
     * Method to return a movie's associated reviews
     * @param movieId
     * @return List<Review> A list of {@link Review} objects
     */
    List<Review> getReviews(long movieId);
}
