package com.example.android.filmesfamosos;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.filmesfamosos.adapter.FilmAdapter;
import com.example.android.filmesfamosos.task.MovieTask;
import com.example.android.filmesfamosos.utilities.MovieResult;
import com.example.android.filmesfamosos.utilities.NetworkUtils;
import com.example.android.filmesfamosos.utilities.ReviewResult;
import com.example.android.filmesfamosos.utilities.TrailerResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<ArrayList<MovieResult>>,
        FilmAdapter.FilmAdapterOnClickHandler {

    private static final int FILM_MOST_POPULAR_LOADER_ID = 0;
    private static final int FILM_TOP_RATED_LOADER_ID = 1;
    private static final int FAVORITES_LOADER_ID = 2;
    private static final int POSTER_SIZE = 400;
    private static final int MIN_COLUMS = 2;

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

        GridLayoutManager layoutManager
                = new GridLayoutManager(MainActivity.this, numberOfColumns());

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        filmAdapter = new FilmAdapter(this, this);
        mRecyclerView.setAdapter(filmAdapter);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        showLoading();

        if (isOnline()) {
            getSupportLoaderManager().initLoader(FILM_MOST_POPULAR_LOADER_ID, null, this);
        } else {
            showErrorMessage();
        }

    }

    private void showDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showLoading() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public void onClick(MovieResult movie) {
        Context context = this;
        Class detailActivity = DetailActivity.class;
        Intent intentDetailActivity = new Intent(context, detailActivity);
        intentDetailActivity.putExtra(getResources().getString(R.string.intent_detail_put_extra), movie);
        startActivity(intentDetailActivity);
    }

    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.most_popular:
                if (isOnline()) {
                    invalidateData();
                    getSupportLoaderManager().restartLoader(FILM_MOST_POPULAR_LOADER_ID, null, this);
                    return true;
                } else {
                    showErrorMessage();
                }
            case R.id.top_rated:
                if (isOnline()) {
                    invalidateData();
                    getSupportLoaderManager().restartLoader(FILM_TOP_RATED_LOADER_ID, null, this);
                    return true;
                } else {
                    showErrorMessage();
                }
            case R.id.favorites:
                invalidateData();
                getSupportLoaderManager().restartLoader(FAVORITES_LOADER_ID, null, this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void onLoadFinished(Loader<ArrayList<MovieResult>> loader, ArrayList<MovieResult> data) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        filmAdapter.setFilmData(data);
        if (null == data) {
            showErrorMessage();
        } else {
            showDataView();
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<MovieResult>> loader) {
        loader = null;
    }

    private void invalidateData() {
        filmAdapter.setFilmData(null);
    }

    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int nColumns = width / POSTER_SIZE;
        if (nColumns < MIN_COLUMS) return 2;
        return nColumns;
    }

    public Loader<ArrayList<MovieResult>> onCreateLoader(final int id, final Bundle loaderArgs) {

        return new MovieTask(this, id);

    }
}
