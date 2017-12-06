package com.figengungor.moviesnowplaying.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.figengungor.moviesnowplaying.R;

import org.threeten.bp.DayOfWeek;

/**
 * Created by figengungor on 12/1/2017.
 */

public class MoviePreferences {

    public static String getPreferredLanguage(Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String keyForLanguage = context.getString(R.string.pref_language_key);
        String defaultLanguage = context.getString(R.string.pref_language_default);
        return sp.getString(keyForLanguage, defaultLanguage);
    }

    public static String getPreferredRegion(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String keyForRegion = context.getString(R.string.pref_region_key);
        String defaultRegion = context.getString(R.string.pref_region_default);
        return sp.getString(keyForRegion, defaultRegion);
    }

    public static DayOfWeek getPreferredNotificationDay(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String keyForNotificationDay = context.getString(R.string.pref_notification_day_key);
        String defaultNotificationDayStr = context.getString(R.string.pref_notification_day_default);
        int defaultNotificationDay = Integer.parseInt(sp.getString(keyForNotificationDay, defaultNotificationDayStr));
        return DayOfWeek.of(defaultNotificationDay);
    }
}
