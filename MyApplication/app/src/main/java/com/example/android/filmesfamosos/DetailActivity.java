package com.example.android.filmesfamosos;

import android.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.filmesfamosos.com.example.android.filmesfamosos.utilities.MovieResult;
import com.example.android.filmesfamosos.com.example.android.filmesfamosos.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        MovieResult movieResult = (MovieResult) getIntent().getSerializableExtra(getResources().getString(R.string.intent_detail_put_extra));

        ((TextView) findViewById(R.id.title_film)).setText(movieResult.getOriginalTitle());
        ((TextView) findViewById(R.id.year)).setText( movieResult.getReleaseDate().substring(0,4));
        ((TextView) findViewById(R.id.score)).setText(movieResult.getVoteAverage()+"/10");
        ((TextView) findViewById(R.id.synopsis_film)).setText(movieResult.getOverview());

        try {
            Picasso.with(this).load(NetworkUtils.buildImageFilmUrl(movieResult.getPosterPath()).toString()).into((ImageView) findViewById(R.id.film_image));
        } catch (Exception e) {
            e.printStackTrace();
        }

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

}
