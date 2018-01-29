package com.example.android.filmesfamosos.task;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;

import com.example.android.filmesfamosos.R;
import com.example.android.filmesfamosos.utilities.MovieResult;
import com.example.android.filmesfamosos.utilities.NetworkUtils;
import com.example.android.filmesfamosos.utilities.TrailerResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;

/**
 * Created by vinicius.rocha on 1/20/18.
 */

public class TrailerTask  extends AsyncTask<String , Void, String> {
    private static String TMDB_API_KEY;
    private MovieResult movie;
    private Context context;

    public TrailerTask(Context context, MovieResult movie) {
        this.context = context;
        this.movie = movie;
        TMDB_API_KEY = context.getString(R.string.apiKey);
    }

    protected String doInBackground(String... params) {

        try{
            URL requestUrl = null;
            requestUrl = NetworkUtils.buildFilmUrl(movie.getId() + "/" + context.getString(R.string.path_videos), null, TMDB_API_KEY);

            return NetworkUtils.getResponseFromHttpUrl(requestUrl);
//            JSONObject jsonObject = new JSONObject();
//            JSONArray array = (JSONArray) jsonObject.get(context.getString(R.string.response_results));
//            ArrayList<TrailerResult> listTrailers = new ArrayList<>();
//            for (int i = 0; i < array.length(); i++) {
//                listTrailers.add(parseTrailersResult(array.getJSONObject(i)));
//            }
//            return listTrailers;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    @NonNull
    private TrailerResult parseTrailersResult(JSONObject jsonObject) throws JSONException {

        return new TrailerResult(
                jsonObject.getString(context.getString(R.string.trailers_id)),
                jsonObject.getString(context.getString(R.string.trailers_iso639)),
                jsonObject.getString(context.getString(R.string.trailers_iso3166)),
                jsonObject.getString(context.getString(R.string.trailers_key)),
                jsonObject.getString(context.getString(R.string.trailers_name)),
                jsonObject.getString(context.getString(R.string.trailers_site)),
                Integer.parseInt(jsonObject.getString(context.getString(R.string.trailers_size))),
                jsonObject.getString(context.getString(R.string.trailers_type)));

    }

    @Override
    protected void onPostExecute(String results) {
        movie.setTrailers(results);
    }

}
