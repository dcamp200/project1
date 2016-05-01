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

import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.davidcampbell.popularmovies.model.MovieContract.MovieEntry;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class TestMovieProvider {

    public static final String LOG_TAG = TestMovieProvider.class.getSimpleName();
    private static Context mContext;

    @BeforeClass
    public static void init() {
        mContext = InstrumentationRegistry.getTargetContext();
    }


    public void deleteAllRecords() {
        mContext.getContentResolver().delete(
                MovieEntry.CONTENT_URI,
                null,
                null
        );

        Cursor cursor = mContext.getContentResolver().query(
                MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from Movie table during delete", 0, cursor.getCount());
        cursor.close();

    }

    // Since we want each test to start with a clean slate, run deleteAllRecords
    // in setUp (called by the test runner before each test).
    @Before
    public void setUp() throws Exception {
        deleteAllRecords();
    }

    /*
        This test checks to make sure that the content provider is registered correctly.
     */
    @Test
    public void testProviderRegistry() {
        PackageManager pm = mContext.getPackageManager();

        // We define the component name based on the package name from the context and the
        // MovieProvider class.
        ComponentName componentName = new ComponentName(mContext.getPackageName(),
                MovieProvider.class.getName());
        try {
            // Fetch the provider info using the component name from the PackageManager
            // This throws an exception if the provider isn't registered.
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);

            // Make sure that the registered authority matches the authority from the Contract.
            assertEquals("Error: MovieProvider registered with authority: " + providerInfo.authority +
                    " instead of authority: " + MovieContract.CONTENT_AUTHORITY,
                    providerInfo.authority, MovieContract.CONTENT_AUTHORITY);
        } catch (PackageManager.NameNotFoundException e) {
            // I guess the provider isn't registered correctly.
            assertTrue("Error: MovieProvider not registered at " + mContext.getPackageName(), false);
        }
    }

    /*
        This test doesn't touch the database.  It verifies that the ContentProvider returns
        the correct type for each type of URI that it can handle.
     */
    public void testGetType() {
        // content://com.davidcampbell.popularmovies/movie/
        String type = mContext.getContentResolver().getType(MovieEntry.CONTENT_URI);
        // vnd.android.cursor.dir/com.davidcampbell.popularmovies/movie
        assertEquals("Error: the MovieEntry CONTENT_URI should return WeatherEntry.CONTENT_TYPE", MovieEntry.CONTENT_TYPE, type);

        String testMovieId = "293660";
        // content://com.davidcampbell.popularmovies/movie/293660
        type = mContext.getContentResolver().getType(MovieEntry.buildMovieUri(293660));
        // vnd.android.cursor.item/com.davidcampbell.popularmovies/movie/293660
        assertEquals("Error: the MovieEntry CONTENT_URI with movie id should return MovieEntry.CONTENT_ITEM_TYPE", MovieEntry.CONTENT_ITEM_TYPE, type);
    }


    /*
        This test uses the database directly to insert and then uses the ContentProvider to
        read out the data.
     */
    @Test
    public void testBasicWeatherQuery() {
        // insert our test records into the database
        long rowId = TestMovieDbHelper.insertMovie();
        assertTrue("Unable to Insert MovieEntry into the Database", rowId != -1);

        // Test the basic content provider query
        Cursor movieCursor = mContext.getContentResolver().query(
                MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        // Move the cursor to a valid database row and check to see if we got any records back
        // from the query
        assertTrue( "Error: No Records returned from query", movieCursor.moveToFirst() );

        // Make sure we get the correct cursor out of the database
        TestMovieDbHelper.validateCursorResults(movieCursor);
    }


    /*
        This test uses the provider to insert and then update the data. Uncomment this test to
        see if your update location is functioning correctly.
     */
    @Test
    public void testUpdateMovie() {
        // Create a new map of values, where column names are the keys
        ContentValues values = TestMovieFactory.getContentValues(TestMovieFactory.getMovieInstance());

        Uri movieUri = mContext.getContentResolver().insert(MovieEntry.CONTENT_URI, values);
        long movieId = ContentUris.parseId(movieUri);

        // Verify we got a row back.
        assertTrue(movieId != -1);
        Log.d(LOG_TAG, "New row id: " + movieId);

        ContentValues updatedValues = new ContentValues(values);
        updatedValues.put(MovieEntry._ID, movieId);
        updatedValues.put(MovieEntry.COLUMN_TITLE, "Star Wars");

        int count = mContext.getContentResolver().update(
                MovieEntry.CONTENT_URI, updatedValues, MovieEntry._ID + "= ?",
                new String[] { Long.toString(movieId)});
        assertEquals(count, 1);

    }



    @Test
    public void testDeleteRecord() {
        // insert our test records into the database
        long rowId = TestMovieDbHelper.insertMovie();
        assertTrue("Unable to Insert MovieEntry into the Database", rowId != -1);
        Uri movieUri = MovieEntry.buildMovieUri(rowId);
        int count = mContext.getContentResolver().delete(movieUri, null, null);
        assertEquals(1,count);
    }



}
