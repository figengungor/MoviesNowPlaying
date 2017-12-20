package com.figengungor.moviesnowplaying.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import com.figengungor.moviesnowplaying.R;
import com.figengungor.moviesnowplaying.data.ErrorEvent;
import com.figengungor.moviesnowplaying.data.MovieContract;
import com.figengungor.moviesnowplaying.utilities.NetworkUtils;
import com.figengungor.moviesnowplaying.utilities.TmdbJsonUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URL;

/**
 * Created by figengungor on 12/1/2017.
 */

public class MovieSyncTask {

    synchronized public static void syncMovies(Context context) {

        try {
            URL movieRequestUri = NetworkUtils.getUrl(context);
            String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestUri);
            ContentValues[] movieValues = TmdbJsonUtils.getMovieContentValuesFromJson(context, jsonMovieResponse);

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
            if(e instanceof SocketTimeoutException) {
                EventBus.getDefault().post(new ErrorEvent(context.getString(R.string.error_connection_timeout)));
            }
            else if(e instanceof IOException) {
                EventBus.getDefault().post(new ErrorEvent(context.getString(R.string.error_no_internet_connection)));
            }
            e.printStackTrace();
        }

    }

}
