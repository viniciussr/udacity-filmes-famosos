package com.example.android.filmesfamosos.com.example.android.filmesfamosos.utilities;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.filmesfamosos.R;

public class FilmAdapter extends RecyclerView.Adapter<FilmAdapter.FilmAdapterViewHolder>{

    private String[] mFilmData;

    private final FilmAdapterOnClickHandler mClickHandler;

    public FilmAdapter(FilmAdapterOnClickHandler clickHandler){
        mClickHandler = clickHandler;
    }


    public interface FilmAdapterOnClickHandler {
        void onClick(String weatherForDay);
    }

    @Override
    public FilmAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.films_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem,parent,shouldAttachToParentImmediately);
        return new FilmAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FilmAdapterViewHolder holder, int position) {
        String film = mFilmData[position];

        //Atualiza com as informaçoes
        holder.mFilmTextView.setText(film);
    }

    @Override
    public int getItemCount() {
        if(mFilmData == null) return 0;
        return  mFilmData.length;
    }

    public class FilmAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView mFilmTextView;

        public FilmAdapterViewHolder(View view) {
            super(view);
            mFilmTextView = (TextView) view.findViewById(R.id.film_data);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            String filmInfo = mFilmData[adapterPosition];
            mClickHandler.onClick(filmInfo);
        }


    }

}
