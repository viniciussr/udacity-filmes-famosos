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
    private static final int POSTER_SIZE = 400;
    private static final int MIN_COLUMS = 2;

    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private FilmAdapter filmAdapter;

    private TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;

    private static String TMDB_API_KEY;

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

        TMDB_API_KEY = getResources().getString(R.string.apiKey);

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

        if (id == R.id.most_popular) {
            invalidateData();
            getSupportLoaderManager().restartLoader(FILM_MOST_POPULAR_LOADER_ID, null, this);
            return true;
        }

        if (id == R.id.top_rated) {
            invalidateData();
            getSupportLoaderManager().restartLoader(FILM_TOP_RATED_LOADER_ID, null, this);
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

        return new AsyncTaskLoader<ArrayList<MovieResult>>(this) {

            ArrayList<MovieResult> results = new ArrayList<MovieResult>();

            @Override
            protected void onStartLoading() {
                if (results.size() > 0) {
                    deliverResult(results);
                } else {
                    mLoadingIndicator.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            @Override
            public ArrayList<MovieResult> loadInBackground() {

                try {
                    loadMovies();
                    return results;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @NonNull
            private MovieResult parseMovieResult(JSONObject jsonMovieObject, List<TrailerResult> trailers, List<ReviewResult> reviews) throws JSONException {
                return new MovieResult(
                        jsonMovieObject.getString(getResources().getString(R.string.response_title)),
                        Integer.parseInt(jsonMovieObject.getString(getResources().getString(R.string.response_id))),
                        jsonMovieObject.getString(getResources().getString(R.string.response_vote_average)),
                        jsonMovieObject.getString(getResources().getString(R.string.response_poster_path)),
                        jsonMovieObject.getString(getResources().getString(R.string.response_release_date)),
                        jsonMovieObject.getString(getResources().getString(R.string.response_overview)),
                        trailers,
                        reviews
                        );
            }

            private void loadMovies() throws Exception {
                URL requestUrl = null;

                switch (id) {
                    case FILM_MOST_POPULAR_LOADER_ID:
                        requestUrl = NetworkUtils.buildFilmUrl(getResources().getString(R.string.path_popular), null, TMDB_API_KEY);
                        break;
                    case FILM_TOP_RATED_LOADER_ID:
                        requestUrl = NetworkUtils.buildFilmUrl(getResources().getString(R.string.path_top_rated), null, TMDB_API_KEY);
                        break;
                }


                JSONObject jsonObject = new JSONObject(NetworkUtils.getResponseFromHttpUrl(requestUrl));
                JSONArray array = (JSONArray) jsonObject.get(getResources().getString(R.string.response_results));
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonMovieObject = array.getJSONObject(i);
                    List<TrailerResult> trailers = loadTrailers(jsonMovieObject.getString(getResources().getString(R.string.response_id)));
                    List<ReviewResult> reviews = loadReviews(jsonMovieObject.getString(getResources().getString(R.string.response_id)));
                    MovieResult movieResult = parseMovieResult(jsonMovieObject,trailers, reviews);
                    results.add(movieResult);
                }

            }

            private List<TrailerResult> loadTrailers(String id) throws Exception {
                URL requestUrl = null;
                requestUrl = NetworkUtils.buildFilmUrl(id + "/" + getResources().getString(R.string.path_videos), null, TMDB_API_KEY);

                JSONObject jsonObject = new JSONObject(NetworkUtils.getResponseFromHttpUrl(requestUrl));
                JSONArray array = (JSONArray) jsonObject.get(getResources().getString(R.string.response_results));
                List<TrailerResult> listTrailers = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    listTrailers.add(parseTrailersResult(array.getJSONObject(i)));
                }
                return listTrailers;
            }

            @NonNull
            private TrailerResult parseTrailersResult(JSONObject jsonObject) throws JSONException {

                return new TrailerResult(
                        jsonObject.getString(getResources().getString(R.string.trailers_id)),
                        jsonObject.getString(getResources().getString(R.string.trailers_iso639)),
                        jsonObject.getString(getResources().getString(R.string.trailers_iso3166)),
                        jsonObject.getString(getResources().getString(R.string.trailers_key)),
                        jsonObject.getString(getResources().getString(R.string.trailers_name)),
                        jsonObject.getString(getResources().getString(R.string.trailers_site)),
                        Integer.parseInt(jsonObject.getString(getResources().getString(R.string.trailers_size))),
                        jsonObject.getString(getResources().getString(R.string.trailers_type)));

            }

            private List<ReviewResult> loadReviews(String id) throws Exception {
                URL requestUrl = null;
                requestUrl = NetworkUtils.buildFilmUrl(id + "/" + getResources().getString(R.string.path_reviews), null, TMDB_API_KEY);
                JSONObject jsonObject = new JSONObject(NetworkUtils.getResponseFromHttpUrl(requestUrl));
                JSONArray array = (JSONArray) jsonObject.get(getResources().getString(R.string.response_results));
                List<ReviewResult> listTrailers = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    listTrailers.add(parseReviewResult(array.getJSONObject(i)));
                }
                return listTrailers;
            }

            @NonNull
            private ReviewResult parseReviewResult(JSONObject jsonObject) throws JSONException {
                return new ReviewResult(
                        jsonObject.getString(getResources().getString(R.string.reviews_id)),
                        jsonObject.getString(getResources().getString(R.string.reviews_author)),
                        jsonObject.getString(getResources().getString(R.string.reviews_content)),
                        jsonObject.getString(getResources().getString(R.string.reviews_url)));
            }

            public void deliverResult(ArrayList<MovieResult> results) {
                this.results = results;
                super.deliverResult(results);
            }
        };
    }
}
