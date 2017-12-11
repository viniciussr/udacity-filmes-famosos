package com.example.android.filmesfamosos;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.filmesfamosos.com.example.android.filmesfamosos.utilities.MovieResult;
import com.example.android.filmesfamosos.com.example.android.filmesfamosos.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FilmAdapter extends RecyclerView.Adapter<FilmAdapter.FilmAdapterViewHolder> {

    private final Context mContext;
    private ArrayList<MovieResult> mFilmData;

    private final FilmAdapterOnClickHandler mClickHandler;

    public FilmAdapter(Context context, FilmAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }


    public interface FilmAdapterOnClickHandler {
        void onClick(MovieResult movie);
    }

    @Override
    public FilmAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.card_film_list;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new FilmAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FilmAdapterViewHolder holder, int position) {
        MovieResult film = mFilmData.get(position);

        try {
            Picasso.with(holder.itemView.getContext()).load(NetworkUtils.buildImageFilmUrl(film.getPosterPath()).toString()).placeholder(R.drawable.user_placeholder)
                    .error(R.drawable.user_placeholder_error).into(holder.mFilmTextView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (mFilmData == null) return 0;
        return mFilmData.size();
    }

    public void setFilmData(ArrayList<MovieResult> data) {
        mFilmData = data;
        notifyDataSetChanged();
    }

    public class FilmAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ImageView mFilmTextView;

        public FilmAdapterViewHolder(View view) {
            super(view);
            mFilmTextView = (ImageView) view.findViewById(R.id.film_icon);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            MovieResult filmInfo = mFilmData.get(adapterPosition);
            mClickHandler.onClick(filmInfo);
        }


    }

}
