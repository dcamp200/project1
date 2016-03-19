package com.davidcampbell.popularmovies.webservices;

import com.davidcampbell.popularmovies.domain.Movie;

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
}
