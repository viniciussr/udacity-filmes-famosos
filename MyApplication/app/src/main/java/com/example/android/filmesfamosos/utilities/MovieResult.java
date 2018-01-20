package com.example.android.filmesfamosos.utilities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by vinicius.rocha on 12/9/17.
 */

public class MovieResult implements Parcelable {

    private String originalTitle;
    private int id;
    private String voteAverage;
    private String posterPath;
    private String releaseDate;
    private String overview;
    private List<TrailerResult> trailers;
    private List<ReviewResult> reviews;


    public MovieResult(String originalTitle, int id, String voteAverage, String posterPath, String releaseDate, String overview ) {
        this.originalTitle = originalTitle;
        this.id = id;
        this.voteAverage = voteAverage;
        this.posterPath = posterPath;
        this.releaseDate = releaseDate;
        this.overview = overview;
    }


    private MovieResult(Parcel in){
        this.originalTitle = in.readString();
        this.id = in.readInt();
        this.voteAverage = in.readString();
        this.posterPath = in.readString();
        this.releaseDate = in.readString();
        this.overview = in.readString();
        this.setTrailers(in.readArrayList(TrailerResult.class.getClassLoader()));
        this.setReviews(in.readArrayList(ReviewResult.class.getClassLoader()));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(originalTitle);
        dest.writeInt(id);
        dest.writeString(voteAverage);
        dest.writeString(posterPath);
        dest.writeString(releaseDate);
        dest.writeString(overview);
        dest.writeList(getTrailers());
        dest.writeList(getReviews());
    }


    public static final Parcelable.Creator<MovieResult> CREATOR = new Parcelable.Creator<MovieResult>() {
        @Override
        public MovieResult createFromParcel(Parcel in) {
            return new MovieResult(in);
        }

        @Override
        public MovieResult[] newArray(int size) {
            return new MovieResult[size];
        }
    };

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

    public List<TrailerResult> getTrailers() {
        return trailers;
    }

    public List<ReviewResult> getReviews() {
        return reviews;
    }


    public void setTrailers(List<TrailerResult> trailers) {
        this.trailers = trailers;
    }

    public void setReviews(List<ReviewResult> reviews) {
        this.reviews = reviews;
    }
}
