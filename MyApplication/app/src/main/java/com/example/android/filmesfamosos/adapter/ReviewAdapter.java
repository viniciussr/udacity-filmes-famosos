package com.example.android.filmesfamosos.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.filmesfamosos.R;
import com.example.android.filmesfamosos.utilities.ReviewResult;

import java.util.List;

/**
 * Created by vinicius.rocha on 1/20/18.
 */

public class ReviewAdapter extends BaseAdapter {

    private final List<ReviewResult> reviews;
    private final Activity activity;

    public ReviewAdapter(List<ReviewResult> reviews, Activity activity) {
        this.reviews = reviews;
        this.activity = activity;
    }


    @Override
    public int getCount() {
        return reviews.size();
    }

    @Override
    public Object getItem(int position) {
        return reviews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = activity.getLayoutInflater().inflate(R.layout.review_film,parent,false);
        ReviewResult review = reviews.get(position);
        ((TextView) view.findViewById(R.id.review_author)).setText(review.getAuthor());
        ((TextView) view.findViewById(R.id.review_content)).setText(review.getContent());
        return view;
    }
}
