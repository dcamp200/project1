package com.davidcampbell.popularmovies.fragments;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.davidcampbell.popularmovies.R;
import com.davidcampbell.popularmovies.domain.Review;

import java.util.List;

/**
 * PopularMovies
 * MovieAdapter - Custom ArrayAdapter
 * Reference : http://developer.android.com/guide/topics/ui/layout/gridview.html#example
 * Created by david on 2016-03-13.
 */
public class ReviewAdapter extends ArrayAdapter<Review> {
    private static String LOG_TAG = ReviewAdapter.class.getSimpleName();
    private final Context mContext;
    private List<Review> mReviews;
    private LayoutInflater mLayoutInflater;


    public ReviewAdapter(Context context, List<Review> reviews) {
        super(context, R.layout.movie_review, reviews);
        Log.d(LOG_TAG, "Initializing Review Adapter...");
        this.mContext = context;
        this.mReviews = reviews;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        Log.d(LOG_TAG, "Reviews Adapter count:" + mReviews.size());
        return mReviews.size();
    }

    @Override
    public Review getItem(int position) {
        return mReviews.get(position);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ReviewViewHolder reviewViewHolder;

        if (convertView == null) {
            Log.d(LOG_TAG, "Adding new review viewholder...");
            convertView = mLayoutInflater.inflate(R.layout.movie_review, parent, false);
            reviewViewHolder = new ReviewViewHolder();
            reviewViewHolder.reviewText = (TextView) convertView.findViewById(R.id.reviewText);
            reviewViewHolder.author = (TextView) convertView.findViewById(R.id.author);
            convertView.setTag(reviewViewHolder);
        } else {
            Log.d(LOG_TAG, "Re-using existing viewholder...");
            reviewViewHolder = (ReviewViewHolder) convertView.getTag();
        }
        Review review = mReviews.get(position);
        Log.d(LOG_TAG, "Adding new review to list..." + review.getContent());
        reviewViewHolder.reviewText.setText(review.getContent());
        reviewViewHolder.author.setText(review.getAuthor());
        return convertView;
    }


    /**
     * ViewHolder design pattern
     * @link http://developer.android.com/training/improving-layouts/smooth-scrolling.html
     */
    static class ReviewViewHolder {
        TextView reviewText;
        TextView author;
    }
}
