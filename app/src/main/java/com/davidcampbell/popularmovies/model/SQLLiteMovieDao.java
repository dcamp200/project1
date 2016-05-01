package com.davidcampbell.popularmovies.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.davidcampbell.popularmovies.domain.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * PopularMovies
 * Created by david on 2016-04-30.
 */
public class SQLLiteMovieDao implements MovieDao {
    private static final String LOG_TAG = SQLLiteMovieDao.class.getSimpleName();
    private static final String sMovieIdSelection = MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ? ";
    private Context mContext;


    public SQLLiteMovieDao(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void addToFavorites(Movie movie) {
        Log.d(LOG_TAG, "Adding movie " + movie.getTitle() + " to favorites");
        Uri uri = mContext.getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, getContentValues(movie));
    }

    @Override
    public void removeFromFavorites(Movie movie) {
        Log.d(LOG_TAG, "Removing movie " + movie.getTitle() + " from favorites");
        int count = mContext.getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI,
                                            sMovieIdSelection,
                                            new String[]{Long.toString(movie.getId())});
    }

    @Override
    public boolean checkIfFavorite(Movie movie) {
        Cursor cursor = mContext.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                null,
                sMovieIdSelection,
                new String[]{Long.toString(movie.getId())},
                null);
        boolean result = (cursor.getCount() == 1);
        cursor.close();
        return result;
    }

    @Override
    public List<Movie> findAll() {
        List<Movie> results = new ArrayList<>();
        Cursor cursor = mContext.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null);

        Movie movie = null;
        try {
            while (cursor.moveToNext()) {
                movie = new Movie();
                int colNum = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE);
                movie.setTitle(cursor.getString(colNum));
                colNum = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH);
                movie.setBackdrop_path(cursor.getString(colNum));
                colNum = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID);
                movie.setId(cursor.getInt(colNum));
                colNum = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_ORIGINAL_LANGUAGE);
                movie.setOriginal_language(cursor.getString(colNum));
                colNum = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE);
                movie.setOriginal_title(cursor.getString(colNum));
                colNum = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW);
                movie.setOverview(cursor.getString(colNum));
                colNum = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_PATH);
                movie.setPoster_path(cursor.getString(colNum));
                colNum = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POPULARITY);
                movie.setPopularity(cursor.getDouble(colNum));
                colNum = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE);
                movie.setVote_average(cursor.getDouble(colNum));
                colNum = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE_COUNT);
                movie.setVote_count(cursor.getInt(colNum));
                colNum = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE);
                movie.setRelease_date(cursor.getString(colNum));

                results.add(movie);
            }
        }finally {
            cursor.close();
        }

        return results;
    }


    private static ContentValues getContentValues(Movie movie) {
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
