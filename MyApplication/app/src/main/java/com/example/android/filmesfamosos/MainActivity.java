package com.example.android.filmesfamosos;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.filmesfamosos.com.example.android.filmesfamosos.utilities.FilmAdapter;

public class MainActivity extends AppCompatActivity implements FilmAdapter.FilmAdapterOnClickHandler{

    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private FilmAdapter filmAdapter;

    private TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_film);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        filmAdapter = new FilmAdapter(this);
        mRecyclerView.setAdapter(filmAdapter);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);



    }

    @Override
    public void onClick(String weatherForDay) {

    }
}
