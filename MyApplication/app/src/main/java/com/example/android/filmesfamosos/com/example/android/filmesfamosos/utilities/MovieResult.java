package com.example.android.filmesfamosos.com.example.android.filmesfamosos.utilities;

import java.io.Serializable;

/**
 * Created by vinicius.rocha on 12/9/17.
 */

public class MovieResult implements Serializable {

    private String originalTitle;
    private int id;
    private String voteAverage;
    private String posterPath;
    private String releaseDate;
    private String overview;

    public MovieResult(String originalTitle, int id, String voteAverage, String posterPath, String releaseDate, String overview) {
        this.originalTitle = originalTitle;
        this.id = id;
        this.voteAverage = voteAverage;
        this.posterPath = posterPath;
        this.releaseDate = releaseDate;
        this.overview = overview;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public int getId() {
        return id;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getOverview() {
        return overview;
    }

}
