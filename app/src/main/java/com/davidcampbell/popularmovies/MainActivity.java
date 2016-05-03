package com.davidcampbell.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.davidcampbell.popularmovies.fragments.MoviesFragment;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private boolean mDualPane = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);

        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
        MoviesFragment moviesFragment = new MoviesFragment();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, moviesFragment)
                    .commit();
        }

        if (findViewById(R.id.details_container) != null) {
            // two pane
            mDualPane = true;
            moviesFragment.setDualPane(true);
//            MoviesDetailFragment moviesDetailFragment = MoviesDetailFragment.newInstance(null, mDualPane);
//            moviesDetailFragment.setDualPane(true);
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.details_container, moviesDetailFragment)
//                    .commit();
        } else {
            mDualPane = false;
        }



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
