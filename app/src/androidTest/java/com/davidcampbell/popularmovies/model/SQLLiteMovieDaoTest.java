package com.davidcampbell.popularmovies.model;

import android.content.ContentResolver;
import android.database.Cursor;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;

import com.davidcampbell.popularmovies.domain.Movie;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Set;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * PopularMovies
 * Created by david on 2016-04-30.
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
public class SQLLiteMovieDaoTest {

    @Before
    public void setup() {
        InstrumentationRegistry.getTargetContext().getContentResolver().delete(
                MovieContract.MovieEntry.CONTENT_URI,
                null,
                null
        );
    }

    @Test
    public void testAddToFavorites() throws Exception {
        Movie movie = TestMovieFactory.getMovieInstance();
        addFavorite(movie);
    }

    private void addFavorite(Movie movie) {
        SQLLiteMovieDao dao = new SQLLiteMovieDao(InstrumentationRegistry.getTargetContext());
        dao.addToFavorites(movie);
        verifyAdd();
    }

    @Test
    public void testRemoveFromFavorites() throws Exception {
        Movie movie = TestMovieFactory.getMovieInstance();
        addFavorite(movie);
        SQLLiteMovieDao dao = new SQLLiteMovieDao(InstrumentationRegistry.getTargetContext());
        dao.removeFromFavorites(movie);
        verifyDelete();
    }

    @Test
    public void testCheckIfFavorite() throws Exception {
        Movie movie = TestMovieFactory.getMovieInstance();
        addFavorite(movie);
        SQLLiteMovieDao dao = new SQLLiteMovieDao(InstrumentationRegistry.getTargetContext());
        assertTrue(dao.checkIfFavorite(movie));
    }

    @Test
    public void testCheckIfNotFavorite() throws Exception {
        Movie movie = TestMovieFactory.getMovieInstance();
        addFavorite(movie);
        SQLLiteMovieDao dao = new SQLLiteMovieDao(InstrumentationRegistry.getTargetContext());
        movie.setId(999999);
        assertFalse(dao.checkIfFavorite(movie));
    }

    @Test
    public void testFindAllMovies() throws Exception {
        Movie movie = TestMovieFactory.getMovieInstance();
        addFavorite(movie);
        SQLLiteMovieDao dao = new SQLLiteMovieDao(InstrumentationRegistry.getTargetContext());
        Set<Movie> movies =  dao.findAll();
        assertEquals(1, movies.size());
    }



    private void verifyAdd() {
        ContentResolver contentResolver = InstrumentationRegistry.getTargetContext().getContentResolver();
        String selection = MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ? ";
        String[] selectionArgs = new String[]{Long.toString(TestMovieFactory.MOVIE_ID)};
        Cursor cursor = contentResolver.query(MovieContract.MovieEntry.CONTENT_URI,null,selection,selectionArgs,null);
        assertTrue(cursor.moveToFirst());
        TestMovieDbHelper.validateCursorResults(cursor);
        cursor.close();
    }

    private void verifyDelete() {
        ContentResolver contentResolver = InstrumentationRegistry.getTargetContext().getContentResolver();
        String selection = MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ? ";
        String[] selectionArgs = new String[]{Long.toString(TestMovieFactory.MOVIE_ID)};
        Cursor cursor = contentResolver.query(MovieContract.MovieEntry.CONTENT_URI,null,selection,selectionArgs,null);
        assertFalse(cursor.moveToFirst());
        cursor.close();
    }
}