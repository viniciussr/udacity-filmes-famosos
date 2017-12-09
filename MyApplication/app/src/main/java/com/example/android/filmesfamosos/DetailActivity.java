package com.example.android.filmesfamosos;

import android.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    private TextView titleFilm;
    private ImageView filmIcon;
    private TextView year;
    private TextView time;
    private TextView score;
    private TextView synopsisFilm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        titleFilm = (TextView) findViewById(R.id.title_film);
        filmIcon = (ImageView) findViewById(R.id.film_image);
        year = (TextView) findViewById(R.id.year);
        time = (TextView) findViewById(R.id.time);
        score = (TextView) findViewById(R.id.score);
        synopsisFilm = (TextView) findViewById(R.id.synopsis_film);

        titleFilm.setText("INTERSTELLAR");
        filmIcon.setImageResource(R.drawable.imagetest);
        year.setText("2015");
        time.setText("120min");
        score.setText("8/10");
        synopsisFilm.setText("Every child comes into the world full of promise, and nome more so. he is the gifted, special, a prodigy");

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
