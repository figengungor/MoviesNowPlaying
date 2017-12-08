package com.figengungor.moviesnowplaying.sync;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.Log;

import com.figengungor.moviesnowplaying.data.MovieContract;
import com.figengungor.moviesnowplaying.utilities.MovieDateUtils;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

/**
 * Created by figengungor on 12/2/2017.
 */

public class MovieSyncUtils {

    private static final String TAG = MovieSyncUtils.class.getSimpleName();
    private static final String MOVIE_SYNC_TAG = "movie-sync";
    private static boolean sInitialized;

    public static void scheduleFirebaseJobDispatcherSync(@NonNull final Context context) {
        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);
        int SYNC_INTERVAL_SECONDS = MovieDateUtils.getSyncIntervalSeconds(context);
        Log.d(TAG, "interval seconds: " + SYNC_INTERVAL_SECONDS);
        int SYNC_FLEXTIME_SECONDS = SYNC_INTERVAL_SECONDS + (int)TimeUnit.MINUTES.toSeconds(5);
        Job syncMoviesJob = dispatcher.newJobBuilder()
                .setService(MovieFirebaseJobService.class)
                .setTag(MOVIE_SYNC_TAG)
                .setConstraints(Constraint.ON_UNMETERED_NETWORK)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(
                        SYNC_INTERVAL_SECONDS,
                        SYNC_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))
                .setReplaceCurrent(true)
                .build();

        dispatcher.schedule(syncMoviesJob);
    }


    synchronized public static void initialize(@NonNull final Context context) {
        if(sInitialized) return;
        sInitialized = true;

        scheduleFirebaseJobDispatcherSync(context);

        final Thread checkForEmpty = new Thread(new Runnable() {
            @Override
            public void run() {
                String[] projection = {MovieContract.MovieEntry._ID};

                Cursor cursor = context.getContentResolver().query(
                        MovieContract.MovieEntry.CONTENT_URI,
                        projection,
                        null,
                        null,
                        null
                );

                if(cursor == null || cursor.getCount() == 0) {
                    startImmediateSync(context);
                }

                cursor.close();
            }
        });

        checkForEmpty.start();
    }

    public static void startImmediateSync(@NonNull final Context context) {
        Intent intentToSyncImmediately = new Intent(context, MovieSyncIntentService.class);
        context.startService(intentToSyncImmediately);
    }

}
