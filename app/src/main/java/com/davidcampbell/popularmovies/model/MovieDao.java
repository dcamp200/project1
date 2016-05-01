package com.davidcampbell.popularmovies.model;

import com.davidcampbell.popularmovies.domain.Movie;

import java.util.List;

/**
 * PopularMovies
 * Created by david on 2016-04-30.
 */
public interface MovieDao {

    void addToFavorites(Movie movie);
    void removeFromFavorites(Movie movie);
    boolean checkIfFavorite(Movie movie);
    List<Movie> findAll();
}
