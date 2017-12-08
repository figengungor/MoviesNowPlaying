package com.figengungor.moviesnowplaying.utilities;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.figengungor.moviesnowplaying.BuildConfig;
import com.figengungor.moviesnowplaying.data.MoviePreferences;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by figengungor on 12/1/2017.
 */

public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String TMDB_BASE_URL = "https://api.themoviedb.org/3";

    private static final String MOVIE_PATH = "movie";

    private static final String NOW_PLAYING_PATH = "now_playing";

    private static Uri NOW_PLAYING_MOVIE_URI = Uri.parse(TMDB_BASE_URL).buildUpon()
            .appendPath(MOVIE_PATH)
            .appendPath(NOW_PLAYING_PATH)
            .build();

    private static final String API_KEY_PARAM = "api_key";
    private static final String LANGUAGE_PARAM = "language"; //default en-US
    private static final String REGION_PARAM = "region";

    private static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";
    private static final String GZIP_ENCODING = "gzip";


    public static URL getUrl(Context context) {
        Uri movieQueryUri = NOW_PLAYING_MOVIE_URI.buildUpon()
                .appendQueryParameter(API_KEY_PARAM, BuildConfig.TMDB_API_KEY)
                .appendQueryParameter(LANGUAGE_PARAM, MoviePreferences.getPreferredLanguage(context))
                .appendQueryParameter(REGION_PARAM, MoviePreferences.getPreferredRegion(context))
                .build();

        try {
            URL movieQueryUrl = new URL((movieQueryUri.toString()));
            Log.v(TAG, "URL: " + movieQueryUrl);
            return movieQueryUrl;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setUseCaches(false);
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            String response = null;
            if (hasInput) {
                response = scanner.next();
            }
            scanner.close();
            return response;
        } finally {
            urlConnection.disconnect();
        }
    }


}
