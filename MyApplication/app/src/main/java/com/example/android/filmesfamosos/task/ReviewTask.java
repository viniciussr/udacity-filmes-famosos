package com.example.android.filmesfamosos.task;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;

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

/**
 * Created by vinicius.rocha on 1/20/18.
 */

public class ReviewTask extends AsyncTask<String , Void, ArrayList<ReviewResult>> {

    private static String TMDB_API_KEY;
    private MovieResult movie;
    private Context context;

    public ReviewTask(Context context, MovieResult movie) {
        this.context = context;
        this.movie = movie;
        TMDB_API_KEY = context.getString(R.string.apiKey);
    }

    @Override
    protected ArrayList<ReviewResult> doInBackground(String... params) {

        try {
            URL requestUrl = null;
            requestUrl = NetworkUtils.buildFilmUrl(movie.getId() + "/" + context.getString(R.string.path_reviews), null, TMDB_API_KEY);
            JSONObject jsonObject = new JSONObject(NetworkUtils.getResponseFromHttpUrl(requestUrl));
            JSONArray array = (JSONArray) jsonObject.get(context.getString(R.string.response_results));
            ArrayList<ReviewResult> listReviews = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                listReviews.add(parseReviewResult(array.getJSONObject(i)));
            }
            return listReviews;
        } catch (Exception e) {
            return null;
        }

    }

    @NonNull
    private ReviewResult parseReviewResult(JSONObject jsonObject) throws JSONException {
        return new ReviewResult(
                jsonObject.getString(context.getString(R.string.reviews_id)),
                jsonObject.getString(context.getString(R.string.reviews_author)),
                jsonObject.getString(context.getString(R.string.reviews_content)),
                jsonObject.getString(context.getString(R.string.reviews_url)));
    }

    @Override
    protected void onPostExecute(ArrayList<ReviewResult> results) {
        movie.setReviews(results);
    }
}
