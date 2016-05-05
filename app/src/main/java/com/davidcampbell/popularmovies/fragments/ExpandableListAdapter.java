package com.davidcampbell.popularmovies.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.davidcampbell.popularmovies.MovieReviewActivity;
import com.davidcampbell.popularmovies.R;
import com.davidcampbell.popularmovies.domain.Review;
import com.davidcampbell.popularmovies.domain.Trailer;

import java.util.List;
import java.util.Map;

/**
 * Created by davidcampbell on 2016-04-19.
 */
public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private static final String LOG_TAG = ExpandableListAdapter.class.getSimpleName();
    private List<String> mGroups;
    private List<Trailer> trailers;
    private List<Review> reviews;

    private Map<String,List<?>> mChildren;
    private Activity mContext;

    public ExpandableListAdapter(Activity context, List<String> groups, Map<String, List<?>> children) {
        this.mChildren = children;
        this.mContext = context;
        this.mGroups = groups;
    }

    @Override
    public int getGroupCount() {
        return mGroups.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return mChildren.get(mGroups.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return mGroups.get(i);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mChildren.get(mGroups.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_group,null);
        }
        TextView textView = (TextView)view.findViewById(R.id.listHeader);
        Log.d(LOG_TAG, "Adding new group header:" + mGroups.get(i));
        textView.setText(mGroups.get(i));
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean b, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = mContext.getLayoutInflater();

        switch (groupPosition) {
            case 0:
                final Trailer trailer = (Trailer)getChild(0,childPosition);
                view = inflater.inflate(R.layout.movie_trailer,null);
                TextView textView = (TextView)view.findViewById(R.id.trailerName);
                textView.setText(trailer.getName());
                ImageButton playButton = (ImageButton)view.findViewById(R.id.playButton);
                playButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://" + trailer.getKey()));
                        mContext.startActivity(intent);
                    }
                });
                break;
            case 1:
                final Review review = (Review)getChild(1,childPosition);
                view = inflater.inflate(R.layout.movie_review,null);
                TextView author = (TextView)view.findViewById(R.id.author);
                author.setText(review.getAuthor());
                textView = (TextView)view.findViewById(R.id.reviewText);
                textView.setText(review.getContent());
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startReviewActivity(review);
                    }
                });
                TextView readmore = (TextView)view.findViewById(R.id.more);
                readmore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startReviewActivity(review);
                    }
                });
                break;
        }


        return view;
    }

    private void startReviewActivity(Review review) {
        Intent intent = new Intent(mContext, MovieReviewActivity.class);
        intent.putExtra("review", review);
        mContext.startActivity(intent);
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }


    public void updateChildren(String group, List<?> childData) {
        mChildren.put(group,childData);
    }
}
