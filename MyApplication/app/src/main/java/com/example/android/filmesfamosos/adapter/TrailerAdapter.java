package com.example.android.filmesfamosos.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.filmesfamosos.R;
import com.example.android.filmesfamosos.utilities.TrailerResult;

import java.util.List;

/**
 * Created by vinicius.rocha on 1/20/18.
 */

public class TrailerAdapter extends BaseAdapter {

    private final List<TrailerResult> trailers;
    private final Activity activity;

    public TrailerAdapter(List<TrailerResult> trailers, Activity activity) {
        this.trailers = trailers;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return trailers.size();
    }

    @Override
    public Object getItem(int position) {
        return trailers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = activity.getLayoutInflater().inflate(R.layout.trailer_film,parent,false);
        TrailerResult trailer = trailers.get(position);
        ((ImageView) view.findViewById(R.id.trailer_button)).setImageResource(R.drawable.play_btn);
        ((TextView) view.findViewById(R.id.trailer_title)).setText(trailer.getName());
        return view;
    }
}
