package com.davidcampbell.popularmovies.model;

import android.content.ContentValues;

import com.davidcampbell.popularmovies.domain.Movie;

/**
 * PopularMovies
 * Created by david on 2016-04-29.
 */
public class TestMovieFactory {
    public static String BACKDROP_PATH = "/n1y094tVDFATSzkTnFxoGZ1qNsG.jpg";
    public static String POSTER_PATH = "/inVq3FRqcYIRl2la8iZikYYxFNR.jpg";
    public static String LANGUAGE = "en";
    public static String TITLE    = "Deadpool";
    public static String ORGINAL_TITLE = "Deadpool 2";
    public static String RELEASE_DATE = "2016-02-09";
    public static String OVERVIEW = "Based upon Marvel Comics’ most unconventional anti-hero, DEADPOOL tells the origin story of former Special Forces operative turned mercenary Wade Wilson, who after being subjected to a rogue experiment that leaves him with accelerated healing powers, adopts the alter ego Deadpool. Armed with his new abilities and a dark, twisted sense of humor, Deadpool hunts down the man who nearly destroyed his life.";
    public static int MOVIE_ID = 293660;
    public static int VOTE_COUNT = 1952;
    public static double POPULARITY = 47.105346;
    public static double VOTE_AVERAGE = 7.22;


   public static Movie getMovieInstance() {
       Movie m = new Movie();

       m.setBackdrop_path(BACKDROP_PATH);
       m.setId(MOVIE_ID);
       m.setOriginal_language(LANGUAGE);
       m.setOriginal_title(ORGINAL_TITLE);
       m.setOverview(OVERVIEW);
       m.setPopularity(POPULARITY);
       m.setPoster_path(POSTER_PATH);
       m.setRelease_date(RELEASE_DATE);
       m.setTitle(TITLE);
       m.setVote_average(VOTE_AVERAGE);
       m.setVote_count(VOTE_COUNT);

       return m;
   }

    public static ContentValues getContentValues(Movie movie) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH,movie.getBackdrop_path());
        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID,movie.getId());
        contentValues.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_LANGUAGE,movie.getOriginal_language());
        contentValues.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE,movie.getOriginal_title());
        contentValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW,movie.getOverview());
        contentValues.put(MovieContract.MovieEntry.COLUMN_POPULARITY,movie.getPopularity());
        contentValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH,movie.getPoster_path());
        contentValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE,movie.getRelease_date());
        contentValues.put(MovieContract.MovieEntry.COLUMN_TITLE,movie.getTitle());
        contentValues.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE,movie.getVote_average());
        contentValues.put(MovieContract.MovieEntry.COLUMN_VOTE_COUNT,movie.getVote_count());
        return contentValues;
    }
}
