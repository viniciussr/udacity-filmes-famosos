package com.example.android.filmesfamosos.db;

/**
 * Created by vinicius.rocha on 1/23/18.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.filmesfamosos.db.MovieContract.MovieEntry;

public class MovieDbHelper extends SQLiteOpenHelper {

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                    MovieEntry.ID + " INTEGER PRIMARY KEY AUTOINCREMENT " + COMMA_SEP +
                    MovieEntry.TITLE + TEXT_TYPE + COMMA_SEP +
                    MovieEntry.POSTER + TEXT_TYPE + COMMA_SEP +
                    MovieEntry.OVERVIEW + TEXT_TYPE + COMMA_SEP +
                    MovieEntry.VOTE_AVERAGE + TEXT_TYPE + COMMA_SEP +
                    MovieEntry.RELEASE_DATE + TEXT_TYPE +  COMMA_SEP +
                    MovieEntry.REVIEWS + TEXT_TYPE +COMMA_SEP +
                    MovieEntry.TRAILERS + TEXT_TYPE + " )";


    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME;

    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "movie.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
