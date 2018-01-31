package com.example.android.filmesfamosos.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by vinicius.rocha on 12/4/17.
 */

public class NetworkUtils {


    private static final int READ_TIMEOUT_MILISECONDS = 10000;
    private static final int CONNECT_TIMEOUT_MILISECONDS= 15000;
    private static final String URL_API_MOVIE = "https://api.themoviedb.org/3/movie/";
    private static final String URL_TMDB_IMAGE = "http://image.tmdb.org/t/p/w185//";
    private static final String REQUEST_METHOD = "GET";
    private static final String REQUEST_PROPERTY = "Accept";
    private static final String REQUEST_PROPERTY_VALUE = "application/json";
    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final String API_KEY = "?api_key=";
    private static final String QUERY = "&query=";



    public static URL buildFilmUrl(String path, String query, String key) throws IOException {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(URL_API_MOVIE);

        stringBuilder.append(path);
        stringBuilder.append( API_KEY + key);
        if (query != null) {
            stringBuilder.append(QUERY + query);
        }

        return new URL(stringBuilder.toString());
    }

    public static URL buildImageFilmUrl(String query) throws IOException {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(URL_TMDB_IMAGE);
        stringBuilder.append(query);

        return new URL(stringBuilder.toString());
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        InputStream stream = null;
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(READ_TIMEOUT_MILISECONDS );
            connection.setConnectTimeout(CONNECT_TIMEOUT_MILISECONDS);
            connection.setRequestMethod(REQUEST_METHOD);
            connection.addRequestProperty(REQUEST_PROPERTY, REQUEST_PROPERTY_VALUE);
            connection.setDoInput(true);
            connection.connect();

            stream = connection.getInputStream();
            return stringify(stream);
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }

    public static String stringify(InputStream stream) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream,DEFAULT_CHARSET );
        BufferedReader bufferedReader = new BufferedReader(reader);
        return bufferedReader.readLine();
    }

}
