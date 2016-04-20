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

        if (view == null) {
            LayoutInflater inflater = mContext.getLayoutInflater();
            switch (groupPosition) {
                case 0:
                    view = inflater.inflate(R.layout.movie_trailer,null);
                    break;
                case 1:
                    view = inflater.inflate(R.layout.movie_review,null);
                    break;
            }

        }

        switch (groupPosition) {
            case 0:
                final Trailer trailer = (Trailer)getChild(0,childPosition);
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
                Review review = (Review)getChild(1,childPosition);
                textView = (TextView)view.findViewById(R.id.reviewText);
                textView.setText(review.getContent());
                break;
        }


        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }


    public void updateChildren(String group, List<?> childData) {
        mChildren.put(group,childData);
    }
}
