package com.davidcampbell.popularmovies;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.davidcampbell.popularmovies.domain.Review;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MovieReviewActivity extends AppCompatActivity {
    private static final String LOG_TAG = MovieReviewActivity.class.getSimpleName();
    @Bind(R.id.reviewContent) TextView reviewTextView;
    @Bind(R.id.reviewAuthor) TextView authorTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_review);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        Review review = (Review)getIntent().getSerializableExtra("review");
        reviewTextView.setText(review.getContent());
        authorTextView.setText("A movie review by " + review.getAuthor());

        toolbar.setTitle("Popular Movies Review: " + review.getMovieTitle());
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
