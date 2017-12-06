package com.figengungor.moviesnowplaying.sync;


import android.content.Context;
import android.os.AsyncTask;

import com.figengungor.moviesnowplaying.utilities.NotificationUtils;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

/**
 * Created by figengungor on 12/4/2017.
 */

public class MovieFirebaseJobService extends JobService {

    private AsyncTask<Void, Void, Void> mFetchMoviesTask;

    @Override
    public boolean onStartJob(final JobParameters jobParameters) {
        mFetchMoviesTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Context context = getApplicationContext();
                MovieSyncTask.syncMovies(context);
                jobFinished(jobParameters, false);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                NotificationUtils.notifyUser(getApplicationContext());
                MovieSyncUtils.scheduleFirebaseJobDispatcherSync(getApplicationContext());
                jobFinished(jobParameters, false);
            }
        };

        mFetchMoviesTask.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        if(mFetchMoviesTask != null) {
            mFetchMoviesTask.cancel(true);
        }
        return true;
    }
}
