/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.davidcampbell.popularmovies.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashSet;

import static com.davidcampbell.popularmovies.model.MovieContract.MovieEntry;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class TestMovieDbHelper {

    public static final String LOG_TAG = TestMovieDbHelper.class.getSimpleName();

    private static Context mContext;

    // Since we want each test to start with a clean slate
    void deleteTheDatabase() {
        mContext.deleteDatabase(MovieDbHelper.DATABASE_NAME);
    }

    @BeforeClass
    public static void init() {
        mContext = InstrumentationRegistry.getTargetContext();
    }

    /*
        This function gets called before each test is executed to delete the database.  This makes
        sure that we always have a clean test.
     */
    @Before
    public void setUp() {
        deleteTheDatabase();
    }


    @Test
    public void testCreateDb() throws Throwable {
        // build a HashSet of all of the table names we wish to look for
        // Note that there will be another table in the DB that stores the
        // Android metadata (db version information)
        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(MovieContract.MovieEntry.TABLE_NAME);

        //mContext.deleteDatabase(MovieDbHelper.DATABASE_NAME);

        SQLiteDatabase db = new MovieDbHelper(this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        // have we created the tables we want?
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: This means that the database has not been created correctly", c.moveToFirst());

        String tableName;
        // verify that the tables have been created
        do {
            tableName = c.getString(0);
            Log.d(LOG_TAG, "Table: " + tableName);
            tableNameHashSet.remove(tableName);
        } while (c.moveToNext());

        // if this fails, it means that your database doesn't contain both the location entry
        // and weather entry tables
        assertTrue("Error: Your database was created without both the location entry and weather entry tables",
                tableNameHashSet.isEmpty());
        db.close();

    }


    @Test
    public void testColumns() {
        SQLiteDatabase db = new MovieDbHelper(this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        // now, do our tables contain the correct columns?
        Cursor c = db.rawQuery("PRAGMA table_info(" + MovieContract.MovieEntry.TABLE_NAME + ")", null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> movieColumnHashSet = new HashSet<String>();
        movieColumnHashSet.add(MovieContract.MovieEntry._ID);
        movieColumnHashSet.add(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH);
        movieColumnHashSet.add(MovieContract.MovieEntry.COLUMN_MOVIE_ID);
        movieColumnHashSet.add(MovieContract.MovieEntry.COLUMN_ORIGINAL_LANGUAGE);
        movieColumnHashSet.add(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE);
        movieColumnHashSet.add(MovieContract.MovieEntry.COLUMN_OVERVIEW);
        movieColumnHashSet.add(MovieContract.MovieEntry.COLUMN_POPULARITY);
        movieColumnHashSet.add(MovieContract.MovieEntry.COLUMN_POSTER_PATH);
        movieColumnHashSet.add(MovieContract.MovieEntry.COLUMN_RELEASE_DATE);
        movieColumnHashSet.add(MovieContract.MovieEntry.COLUMN_TITLE);
        movieColumnHashSet.add(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE);
        movieColumnHashSet.add(MovieContract.MovieEntry.COLUMN_VOTE_COUNT);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            Log.d(LOG_TAG, "Column name:" + columnName);
            movieColumnHashSet.remove(columnName);
        } while(c.moveToNext());

        // if this fails, it means that your database doesn't contain all of the required movie
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required movie entry columns",
                movieColumnHashSet.isEmpty());
        db.close();
    }


   public static long insertMovie() {
        // First step: Get reference to writable database
        // If there's an error in those massive SQL table creation Strings,
        // errors will be thrown here when you try to get a writable database.
        MovieDbHelper dbHelper = new MovieDbHelper(InstrumentationRegistry.getTargetContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Second Step: Create ContentValues of what you want to insert
        ContentValues testValues = TestMovieFactory.getContentValues(TestMovieFactory.getMovieInstance());

        // Third Step: Insert ContentValues into database and get a row ID back
        long rowId = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, testValues);

        // Verify we got a row back.
        assertTrue(rowId != -1);
        db.close();
        return rowId;
    }

    @Test
    public void testInsertMovie() {
        // First step: Get reference to writable database
        // If there's an error in those massive SQL table creation Strings,
        // errors will be thrown here when you try to get a writable database.
        MovieDbHelper dbHelper = new MovieDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Second Step: Insert ContentValues into database and get a row ID back
        long rowId = insertMovie();

        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
        // the round trip.

        // Fourth Step: Query the database and receive a Cursor back
        // A cursor is your primary interface to the query results.
        Cursor cursor = db.query(
                MovieContract.MovieEntry.TABLE_NAME,  // Table to Query
                null, // all columns
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );

        // Move the cursor to a valid database row and check to see if we got any records back
        // from the query
        assertTrue( "Error: No Records returned from query", cursor.moveToFirst() );

        // Fifth Step: Validate data in resulting Cursor with the original ContentValues
        validateCursorResults(cursor);

        // Move the cursor to demonstrate that there is only one record in the database
        assertFalse( "Error: More than one record returned from query", cursor.moveToNext() );

        // Sixth Step: Close Cursor and Database
        cursor.close();
        db.close();
    }

    @Test
    public void testDeleteMovie() {
        MovieDbHelper dbHelper = new MovieDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        insertMovie();
        long count = db.delete(
                MovieContract.MovieEntry.TABLE_NAME,
                null,
                null
        );

        assertEquals( 1, count );

        db.close();
    }


    public static void validateCursorResults(Cursor cursor) {
        int colNum = cursor.getColumnIndex(MovieEntry.COLUMN_TITLE);
        assertEquals(TestMovieFactory.TITLE,cursor.getString(colNum));
        colNum = cursor.getColumnIndex(MovieEntry.COLUMN_BACKDROP_PATH);
        assertEquals(TestMovieFactory.BACKDROP_PATH,cursor.getString(colNum));
        colNum = cursor.getColumnIndex(MovieEntry.COLUMN_MOVIE_ID);
        assertEquals(TestMovieFactory.MOVIE_ID,cursor.getInt(colNum));
        colNum = cursor.getColumnIndex(MovieEntry.COLUMN_ORIGINAL_LANGUAGE);
        assertEquals(TestMovieFactory.LANGUAGE,cursor.getString(colNum));
        colNum = cursor.getColumnIndex(MovieEntry.COLUMN_ORIGINAL_TITLE);
        assertEquals(TestMovieFactory.ORGINAL_TITLE,cursor.getString(colNum));
        colNum = cursor.getColumnIndex(MovieEntry.COLUMN_OVERVIEW);
        assertEquals(TestMovieFactory.OVERVIEW,cursor.getString(colNum));
        colNum = cursor.getColumnIndex(MovieEntry.COLUMN_POSTER_PATH);
        assertEquals(TestMovieFactory.POSTER_PATH,cursor.getString(colNum));
        colNum = cursor.getColumnIndex(MovieEntry.COLUMN_POPULARITY);
        assertEquals(TestMovieFactory.POPULARITY,cursor.getDouble(colNum));
        colNum = cursor.getColumnIndex(MovieEntry.COLUMN_VOTE_AVERAGE);
        assertEquals(TestMovieFactory.VOTE_AVERAGE,cursor.getDouble(colNum));
        colNum = cursor.getColumnIndex(MovieEntry.COLUMN_VOTE_COUNT);
        assertEquals(TestMovieFactory.VOTE_COUNT,cursor.getInt(colNum));
        colNum = cursor.getColumnIndex(MovieEntry.COLUMN_RELEASE_DATE);
        assertEquals(TestMovieFactory.RELEASE_DATE,cursor.getString(colNum));
    }
}
