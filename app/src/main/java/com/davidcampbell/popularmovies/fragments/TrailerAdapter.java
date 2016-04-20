package com.davidcampbell.popularmovies.fragments;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.davidcampbell.popularmovies.R;
import com.davidcampbell.popularmovies.domain.Trailer;

import java.util.List;

/**
 * PopularMovies
 * MovieAdapter - Custom ArrayAdapter
 * Reference : http://developer.android.com/guide/topics/ui/layout/gridview.html#example
 * Created by david on 2016-03-13.
 */
public class TrailerAdapter extends ArrayAdapter<Trailer> {
    private static String LOG_TAG = TrailerAdapter.class.getSimpleName();
    private final Context mContext;
    private List<Trailer> mTrailers;
    private LayoutInflater mLayoutInflater;


    public TrailerAdapter(Context context, List<Trailer> trailers) {
        super(context, R.layout.movie_trailer, trailers);
        Log.d(LOG_TAG, "Initializing Trailer Adapter...");
        this.mContext = context;
        this.mTrailers = trailers;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mTrailers.size();
    }

    @Override
    public Trailer getItem(int position) {
        return mTrailers.get(position);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            Log.d(LOG_TAG, "Adding new trailer viewholder...");
            convertView = mLayoutInflater.inflate(R.layout.movie_trailer, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.trailerName = (TextView) convertView.findViewById(R.id.trailerName);
            convertView.setTag(viewHolder);
        } else {
            Log.d(LOG_TAG, "Re-using existing viewholder...");
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Trailer trailer = mTrailers.get(position);
        viewHolder.trailerName.setText(trailer.getName());
        Log.d(LOG_TAG, "Adding new trailer view to list..." + trailer.getName());
        return convertView;
    }


    /**
     * ViewHolder design pattern
     * @link http://developer.android.com/training/improving-layouts/smooth-scrolling.html
     */
    static class ViewHolder {
        TextView trailerName;
    }
}
