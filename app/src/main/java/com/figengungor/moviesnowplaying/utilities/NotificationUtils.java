package com.figengungor.moviesnowplaying.utilities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.figengungor.moviesnowplaying.MovieListActivity;
import com.figengungor.moviesnowplaying.R;

/**
 * Created by figengungor on 12/4/2017.
 */

public class NotificationUtils {

    private static final int MOVIE_NOTIFICATION_ID = 1;
    private static final String MOVIE_NOTIFICATION_CHANNEL_ID = "movie_channel";
    private static final int MOVIE_PENDING_INTENT_REQUEST_CODE = 1;

    public static void notifyUser(Context context) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel notificationChannel = new NotificationChannel(
                    MOVIE_NOTIFICATION_CHANNEL_ID,
                    context.getString(R.string.movie_notification_channel_name),
                    NotificationManager.IMPORTANCE_DEFAULT);

            notificationManager.createNotificationChannel(notificationChannel);

        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, MOVIE_NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_star_black_24dp)
                .setContentTitle(context.getString(R.string.movie_notification_content_title))
                .setContentText(context.getString(R.string.movie_notification_content_text))
                .setContentIntent(contentIntent(context))
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        }

        notificationManager.notify(MOVIE_NOTIFICATION_ID, notificationBuilder.build());

    }

    private static PendingIntent contentIntent(Context context) {
        Intent startActivity = new Intent(context, MovieListActivity.class);

        return PendingIntent.getActivity(
                context,
                MOVIE_PENDING_INTENT_REQUEST_CODE,
                startActivity,
                PendingIntent.FLAG_UPDATE_CURRENT);

    }

}
