package com.figengungor.moviesnowplaying.utilities;

import android.content.Context;
import android.util.Log;

import com.figengungor.moviesnowplaying.data.MoviePreferences;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.temporal.TemporalAdjusters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static org.threeten.bp.temporal.ChronoUnit.SECONDS;
import static org.threeten.bp.temporal.ChronoUnit.MINUTES;

/**
 * Created by figengungor on 12/2/2017.
 */

public class MovieDateUtils {

    private static final String TAG = MovieDateUtils.class.getSimpleName();

    public static String getFriendlyReleaseDate(String releaseDate) {
        SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat newFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        try {
            Date date = oldFormat.parse(releaseDate);
            return newFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static int getSyncIntervalSeconds(Context context) {
        DayOfWeek nextDay = MoviePreferences.getPreferredNotificationDay(context);
        //TODO: Make hour and minute user preference
        int hour = 12;
        int minute = 30;
        LocalDate nowDate = LocalDate.now();
        LocalDateTime nowTime = LocalDateTime.now();
        LocalDate notificationDay;

        boolean isItToday = nextDay == nowDate.getDayOfWeek();
        boolean isNotificationTimePassed = MINUTES.between(nowTime, nowDate.atTime(hour, minute))<0;

        if(isItToday && !isNotificationTimePassed ) {
            notificationDay = nowDate.with(
                    TemporalAdjusters.nextOrSame(nextDay));
        } else {
            notificationDay = nowDate.with(
                    TemporalAdjusters.next(nextDay));
        }

        int seconds = (int)SECONDS.between(nowTime, notificationDay.atTime(hour,minute));
        Log.d(TAG, "interval seconds: " + seconds);
        return seconds;

    }
}
