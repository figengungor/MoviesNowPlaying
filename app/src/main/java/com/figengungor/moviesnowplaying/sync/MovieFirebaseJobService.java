package com.figengungor.moviesnowplaying.sync;


import android.content.Context;
import android.os.AsyncTask;

import com.figengungor.moviesnowplaying.utilities.NotificationUtils;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import java.lang.ref.WeakReference;

/**
 * Created by figengungor on 12/4/2017.
 */

public class MovieFirebaseJobService extends JobService {

    private AsyncTask<JobParameters, Void, JobParameters> mFetchMoviesTask;

    @Override
    public boolean onStartJob(final JobParameters jobParameters) {
        mFetchMoviesTask = new FetchMoviesJobTask(new WeakReference<JobService>(this));
        mFetchMoviesTask.execute(jobParameters);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        if (mFetchMoviesTask != null) {
            mFetchMoviesTask.cancel(true);
        }
        return true;
    }

    private static class FetchMoviesJobTask extends AsyncTask<JobParameters, Void, JobParameters> {
        private final WeakReference<JobService> jobService;

        FetchMoviesJobTask(WeakReference<JobService> jobService) {
            this.jobService = jobService;
        }

        @Override
        protected JobParameters doInBackground(JobParameters... params) {
            Context context = jobService.get().getApplicationContext();
            MovieSyncTask.syncMovies(context);
            jobService.get().jobFinished(params[0], false);
            return null;
        }

        @Override
        protected void onPostExecute(JobParameters jobParameters) {
            NotificationUtils.notifyUser(jobService.get().getApplicationContext());
            MovieSyncUtils.scheduleFirebaseJobDispatcherSync(jobService.get().getApplicationContext());
            jobService.get().jobFinished(jobParameters, false);
        }
    }
}
