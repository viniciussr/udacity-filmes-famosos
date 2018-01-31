package com.example.android.filmesfamosos.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

/**
 * Created by vinicius.rocha on 1/25/18.
 */

public class MovieProvider extends ContentProvider {

    public static final int CODE_MOVIE = 100;
    public static final int CODE_MOVIE_ID = 101;
    private static final String UNKNOWN_URI = "Unknown uri: ";

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private MovieDbHelper movieDbHelper;
    @Override
    public boolean onCreate() {
        movieDbHelper = new MovieDbHelper(getContext());
        return true;
    }

    private static UriMatcher buildUriMatcher(){
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        String authority = MovieContract.CONTENT_AUTHORITY;
        matcher.addURI(authority,MovieContract.PATH,CODE_MOVIE);
        matcher.addURI(authority, MovieContract.PATH + "/#", CODE_MOVIE_ID);
        return matcher;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        switch (sUriMatcher.match(uri)){
            case CODE_MOVIE_ID:
                builder.setTables(MovieContract.MovieEntry.TABLE_NAME);
                builder.appendWhere(MovieContract.MovieEntry.ID + " = " +
                        uri.getLastPathSegment());
                cursor = builder.query(movieDbHelper.getReadableDatabase(),
                        projection,selection,selectionArgs, null,null,sortOrder);
                break;
            case CODE_MOVIE:
                builder.setTables(MovieContract.MovieEntry.TABLE_NAME);
                cursor = builder.query(movieDbHelper.getReadableDatabase(),projection,selection,selectionArgs, null,null,sortOrder);
                break;
            default:
                throw new UnsupportedOperationException(UNKNOWN_URI + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case CODE_MOVIE:
                return MovieContract.MovieEntry.CONTENT_TYPE;
            case CODE_MOVIE_ID:
                return MovieContract.MovieEntry.CONTENT_ITEM_TYPE;
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        SQLiteDatabase db = movieDbHelper.getWritableDatabase();
        Uri returnUri;
        switch (sUriMatcher.match(uri)) {
            case CODE_MOVIE:
                    long id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, values);
                    if (id != 0) {
                        returnUri = MovieContract.MovieEntry.buildMovieUri(id);
                    } else {
                        throw new android.database.SQLException("Failed to insert row into " + uri);
                    }
                break;
            default:
                throw new UnsupportedOperationException(UNKNOWN_URI + uri);

        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        final SQLiteDatabase db = movieDbHelper.getWritableDatabase();
        int rowsDeleted;
        switch (sUriMatcher.match(uri)){
            case CODE_MOVIE:
                rowsDeleted = db.delete(MovieContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException(UNKNOWN_URI + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new RuntimeException(
                "Not implementing");
    }

}
