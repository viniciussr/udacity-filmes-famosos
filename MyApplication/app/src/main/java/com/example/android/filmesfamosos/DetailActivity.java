package com.example.android.filmesfamosos;

import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.filmesfamosos.db.MovieContract;
import com.example.android.filmesfamosos.utilities.MovieResult;
import com.example.android.filmesfamosos.utilities.NetworkUtils;
import com.example.android.filmesfamosos.utilities.ReviewResult;
import com.example.android.filmesfamosos.utilities.TrailerResult;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private MovieResult movieResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        movieResult = (MovieResult) getIntent().getExtras().getParcelable(getResources().getString(R.string.intent_detail_put_extra));

        ((TextView) findViewById(R.id.title_film)).setText(movieResult.getOriginalTitle());
        ((TextView) findViewById(R.id.year)).setText(movieResult.getReleaseDate().substring(0, 4));
        ((TextView) findViewById(R.id.score)).setText(movieResult.getVoteAverage() + "/10");
        ((TextView) findViewById(R.id.synopsis_film)).setText(movieResult.getOverview());

        try {
            Picasso.with(this).load(NetworkUtils.buildImageFilmUrl(movieResult.getPosterPath()).toString()).placeholder(R.drawable.picasa_icon)
                    .error(R.drawable.picasa_icon).into((ImageView) findViewById(R.id.film_image));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            loadTrailers(movieResult.getTrailers());
            loadReviews(movieResult.getReviews());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ImageButton favorite = ((ImageButton) findViewById(R.id.favorite));
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                button.setSelected(!button.isSelected());
                if (button.isSelected()) {
                    addFavorites();
                } else {
                    deleteFavorites();
                }
            }
        });
        toggleFavorite(favorite);

        getSupportActionBar().setHomeButtonEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadReviews(String reviews) throws JSONException {
        LinearLayout reviewList = (LinearLayout) findViewById(R.id.review_list);

        JSONObject jsonList = new JSONObject(reviews);
        JSONArray array = (JSONArray) jsonList.get(getString(R.string.response_results));
        for (int i = 0; i < array.length(); i++) {
            JSONObject jsonObject = array.getJSONObject(i);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View reviewView = inflater.inflate(R.layout.review_film, null);
            ((TextView) reviewView.findViewById(R.id.review_author)).setText(jsonObject.getString(getString(R.string.reviews_author)));
            ((TextView) reviewView.findViewById(R.id.review_content)).setText(jsonObject.getString(getString(R.string.reviews_content)));
            reviewList.addView(reviewView);
        }
    }


    private void loadTrailers(String trailers) throws JSONException {
        LinearLayout trailerList = (LinearLayout) findViewById(R.id.trailer_list);

        JSONObject jsonList = new JSONObject(trailers);
        final JSONArray array = (JSONArray) jsonList.get(getString(R.string.response_results));
        for (int i = 0; i < array.length(); i++) {
            JSONObject jsonObject = array.getJSONObject(i);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View trailerView = inflater.inflate(R.layout.trailer_film, null);
            ((ImageView) trailerView.findViewById(R.id.trailer_button)).setImageResource(R.drawable.play_btn);
            ((TextView) trailerView.findViewById(R.id.trailer_title)).setText(jsonObject.getString(getString(R.string.trailers_name)));
            final String trailerKey = jsonObject.getString(getString(R.string.trailers_key));
            trailerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent youTubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + trailerKey));
                    Intent webIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(String.format(getString(R.string.youtube_link), trailerKey)));
                    try {
                        startActivity(youTubeIntent);
                    } catch (ActivityNotFoundException ex) {
                        startActivity(webIntent);
                    }
                }
            });

            trailerList.addView(trailerView);
        }

    }

    private void addFavorites() {

        Uri uri = MovieContract.MovieEntry.CONTENT_URI;
        ContentResolver resolver = getContentResolver();
        ContentValues values = new ContentValues();
        values.clear();

        values.put(MovieContract.MovieEntry.ID, movieResult.getId());
        values.put(MovieContract.MovieEntry.TITLE, movieResult.getOriginalTitle());
        values.put(MovieContract.MovieEntry.POSTER, movieResult.getPosterPath());
        values.put(MovieContract.MovieEntry.OVERVIEW, movieResult.getOverview());
        values.put(MovieContract.MovieEntry.VOTE_AVERAGE, movieResult.getVoteAverage());
        values.put(MovieContract.MovieEntry.RELEASE_DATE, movieResult.getReleaseDate());
        values.put(MovieContract.MovieEntry.REVIEWS, movieResult.getReviews());
        values.put(MovieContract.MovieEntry.TRAILERS, movieResult.getTrailers());

        resolver.insert(uri, values);
    }


    private void deleteFavorites() {

        Uri uri = MovieContract.MovieEntry.CONTENT_URI;
        ContentResolver resolver = getContentResolver();

        resolver.delete(uri,MovieContract.MovieEntry.ID + " = ?",new String[]{movieResult.getId()+""});

    }

    private boolean checkFavorites(){

        Uri uri = MovieContract.MovieEntry.buildMovieUri(movieResult.getId());
        ContentResolver resolver = getContentResolver();
        Cursor cursor = null;
        try {
            cursor = resolver.query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst())
                return true;
        } finally {
            if(cursor != null)
                cursor.close();
        }
        return false;
    }

    private void toggleFavorite(ImageButton favorite){

        boolean inFavorites = checkFavorites();
        favorite.setSelected(inFavorites);
    }
}
