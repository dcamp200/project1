package com.davidcampbell.popularmovies.model;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * PopularMovies
 * Created by david on 2016-04-25.
 */
public class MovieDbHelper extends SQLiteOpenHelper {
    private static final String LOG_TAG = MovieDbHelper.class.getSimpleName();
    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "movie.db";
    final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +
            MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            MovieContract.MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
            MovieContract.MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
            MovieContract.MovieEntry.COLUMN_BACKDROP_PATH + " TEXT NOT NULL, " +
            MovieContract.MovieEntry.COLUMN_ORIGINAL_LANGUAGE + " TEXT NOT NULL, " +
            MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL, " +
            MovieContract.MovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
            MovieContract.MovieEntry.COLUMN_POPULARITY + " REAL NOT NULL, " +
            MovieContract.MovieEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
            MovieContract.MovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
            MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE + " REAL NOT NULL, " +
            MovieContract.MovieEntry.COLUMN_VOTE_COUNT + " INTEGER NOT NULL); ";



    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public MovieDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
