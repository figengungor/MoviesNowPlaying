package com.figengungor.moviesnowplaying;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.jakewharton.threetenabp.AndroidThreeTen;

/**
 * Created by figengungor on 12/3/2017.
 */

public class MoviesNowPlayingApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
        AndroidThreeTen.init(this);
    }
}
