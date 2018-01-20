package com.example.android.filmesfamosos.task;


import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;
import android.view.View;
import android.widget.ProgressBar;

import com.example.android.filmesfamosos.R;
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

/**
 * Created by vinicius.rocha on 1/20/18.
 */

public class MovieTask extends AsyncTaskLoader<ArrayList<MovieResult>> {

    private int sortBy;
    private static String TMDB_API_KEY;
    private static final int FILM_MOST_POPULAR_LOADER_ID = 0;
    private static final int FILM_TOP_RATED_LOADER_ID = 1;

    public MovieTask(Context ctx, int sortBy) {
        super(ctx);
        this.sortBy = sortBy;
        TMDB_API_KEY = getContext().getString(R.string.apiKey);
    }

    ArrayList<MovieResult> results = new ArrayList<MovieResult>();

    @Override
    protected void onStartLoading() {
        if (results.size() > 0) {
            deliverResult(results);
        } else {
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
                jsonMovieObject.getString(getContext().getString(R.string.response_title)),
                Integer.parseInt(jsonMovieObject.getString(getContext().getString(R.string.response_id))),
                jsonMovieObject.getString(getContext().getString(R.string.response_vote_average)),
                jsonMovieObject.getString(getContext().getString(R.string.response_poster_path)),
                jsonMovieObject.getString(getContext().getString(R.string.response_release_date)),
                jsonMovieObject.getString(getContext().getString(R.string.response_overview))
        );
    }

    private void loadMovies() throws Exception {
        URL requestUrl = null;

        switch (sortBy) {
            case FILM_MOST_POPULAR_LOADER_ID:
                requestUrl = NetworkUtils.buildFilmUrl(getContext().getString(R.string.path_popular), null, TMDB_API_KEY);
                break;
            case FILM_TOP_RATED_LOADER_ID:
                requestUrl = NetworkUtils.buildFilmUrl(getContext().getString(R.string.path_top_rated), null, TMDB_API_KEY);
                break;
        }

        JSONObject jsonObject = new JSONObject(NetworkUtils.getResponseFromHttpUrl(requestUrl));
        JSONArray array = (JSONArray) jsonObject.get(getContext().getString(R.string.response_results));
        for (int i = 0; i < array.length(); i++) {
            JSONObject jsonMovieObject = array.getJSONObject(i);

            MovieResult movieResult = parseMovieResult(jsonMovieObject,null, null);

            ReviewTask reviewTask = new ReviewTask(getContext(),movieResult);
            reviewTask.execute();

            TrailerTask trailerTask = new TrailerTask(getContext(),movieResult);
            trailerTask.execute();

            results.add(movieResult);
        }

    }

    public void deliverResult(ArrayList<MovieResult> results) {
        this.results = results;
        super.deliverResult(results);
    }

}
