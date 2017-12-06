package com.figengungor.moviesnowplaying.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import com.figengungor.moviesnowplaying.data.MovieContract;
import com.figengungor.moviesnowplaying.utilities.NetworkUtils;
import com.figengungor.moviesnowplaying.utilities.TmdbJsonUtils;

import java.net.URL;

/**
 * Created by figengungor on 12/1/2017.
 */

public class MovieSyncTask {

    synchronized public static void syncMovies(Context context) {

        try {
            URL movieRequestUri = NetworkUtils.getUrl(context);
            String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestUri);
            ContentValues[] movieValues = TmdbJsonUtils.getMovieContentValuesFromJson(jsonMovieResponse);

            if (movieValues != null && movieValues.length != 0) {
                ContentResolver movieContentResolver = context.getContentResolver();

                movieContentResolver.delete(
                        MovieContract.MovieEntry.CONTENT_URI,
                        null,
                        null);

                movieContentResolver.bulkInsert(
                        MovieContract.MovieEntry.CONTENT_URI,
                        movieValues);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
