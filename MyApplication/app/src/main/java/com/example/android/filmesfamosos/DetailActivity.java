package com.example.android.filmesfamosos;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.filmesfamosos.utilities.MovieResult;
import com.example.android.filmesfamosos.utilities.NetworkUtils;
import com.example.android.filmesfamosos.utilities.ReviewResult;
import com.example.android.filmesfamosos.utilities.TrailerResult;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        MovieResult movieResult = (MovieResult) getIntent().getExtras().getParcelable(getResources().getString(R.string.intent_detail_put_extra));

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

        loadTrailers(movieResult.getTrailers());
        loadReviews(movieResult.getReviews());
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

    private void loadReviews(List<ReviewResult> reviews) {
        LinearLayout reviewList = (LinearLayout) findViewById(R.id.review_list);
        if (reviews.size() > 0) {
            for(ReviewResult review : reviews){
                LayoutInflater inflater =(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View reviewView = inflater.inflate(R.layout.review_film, null);
                ((TextView) reviewView.findViewById(R.id.review_author)).setText(review.getAuthor());
                ((TextView) reviewView.findViewById(R.id.review_content)).setText(review.getContent());
                reviewList.addView(reviewView);
            }
        }
    }

    private void loadTrailers(List<TrailerResult> trailers) {
        LinearLayout trailerList = (LinearLayout) findViewById(R.id.trailer_list);
        if (trailers.size() > 0) {
            for(TrailerResult trailer : trailers){
                LayoutInflater inflater =(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View trailerView = inflater.inflate(R.layout.trailer_film, null);
                ((ImageView) trailerView.findViewById(R.id.trailer_button)).setImageResource(R.drawable.play_btn);
                ((TextView) trailerView.findViewById(R.id.trailer_title)).setText(trailer.getName());
                trailerList.addView(trailerView);
            }
        }
    }


}
