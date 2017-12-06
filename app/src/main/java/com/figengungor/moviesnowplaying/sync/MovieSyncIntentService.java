package com.figengungor.moviesnowplaying.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by figengungor on 12/1/2017.
 */

public class MovieSyncIntentService extends IntentService {

    public MovieSyncIntentService() {
        super("MovieSyncIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        MovieSyncTask.syncMovies(this);
    }
}
