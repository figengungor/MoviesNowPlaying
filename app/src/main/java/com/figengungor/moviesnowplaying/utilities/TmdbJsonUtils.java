package com.figengungor.moviesnowplaying.utilities;

import android.content.ContentValues;
import android.util.Log;

import com.figengungor.moviesnowplaying.data.MovieContract.MovieEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by figengungor on 12/1/2017.
 */

public final class TmdbJsonUtils {

    private static final String TAG = TmdbJsonUtils.class.getSimpleName();

    //Movie details
    private static final String TMDB_RESULTS = "results";
    private static final String TMDB_POSTER_PATH = "poster_path";
    private static final String TMDB_ADULT = "adult";
    private static final String TMDB_OVERVIEW = "overview";
    private static final String TMDB_RELEASE_DATE = "release_date";
    private static final String TMDB_ID = "id";
    private static final String TMDB_ORIGINAL_TITLE = "original_title";
    private static final String TMDB_ORIGINAL_LANGUAGE = "original_language";
    private static final String TMDB_TITLE = "title";
    private static final String TMDB_BACKDROP_PATH = "backdrop_path";
    private static final String TMDB_POPULARITY = "popularity";
    private static final String TMDB_VOTE_COUNT = "vote_count";
    private static final String TMDB_VIDEO = "video";
    private static final String TMDB_VOTE_AVERAGE = "vote_average";


    //Status
    private static final String TMDB_STATUS_CODE= "status_code";
    private static final String TMDB_STATUS_MESSAGE = "status_message";
    private static final int TMDB_HTTP_OK_200 = 1;
    private static final int TMDB_HTTP_NOT_FOUND_404 = 6;



    public static ContentValues[] getMovieContentValuesFromJson(String movieJsonStr) throws JSONException {
        JSONObject movieJson = new JSONObject(movieJsonStr);

        /* Is there an error? */
        if (movieJson.has(TMDB_STATUS_CODE)) {
            int errorCode = movieJson.getInt(TMDB_STATUS_CODE);
            String errorMessage = movieJson.getString(TMDB_STATUS_MESSAGE);

            switch (errorCode) {
                case TMDB_HTTP_OK_200:
                    break;
                case TMDB_HTTP_NOT_FOUND_404:
                    /* Invalid id: The pre-requisite id is invalid or not found. */
                    Log.e(TAG, errorMessage);
                    return null;
                default:
                    /* Server probably down */
                    Log.e(TAG, errorMessage);
                    return null;
            }
        }

        JSONArray results = movieJson.getJSONArray(TMDB_RESULTS);
        int movieSize = results.length();
        ContentValues[] contentValues = new ContentValues[movieSize];
        for(int i=0; i<movieSize; i++) {
            JSONObject movieObj = results.getJSONObject(i);
            String posterPath = movieObj.optString(TMDB_POSTER_PATH);
            int adult = mapBooleanToIntForSqLite(movieObj.getBoolean(TMDB_ADULT));
            String overview = movieObj.getString(TMDB_OVERVIEW);
            String releaseDate = movieObj.getString(TMDB_RELEASE_DATE);
            int id = movieObj.getInt(TMDB_ID);
            String originalTitle = movieObj.getString(TMDB_ORIGINAL_TITLE);
            String originalLanguage = movieObj.getString(TMDB_ORIGINAL_LANGUAGE);
            String title = movieObj.getString(TMDB_TITLE);
            String backdropPath = movieObj.optString(TMDB_BACKDROP_PATH);
            double popularity = movieObj.getDouble(TMDB_POPULARITY);
            int voteCount = movieObj.getInt(TMDB_VOTE_COUNT);
            int video = mapBooleanToIntForSqLite(movieObj.getBoolean(TMDB_VIDEO));
            double voteAverage = movieObj.getDouble(TMDB_VOTE_AVERAGE);

            ContentValues cv = new ContentValues();
            cv.put(MovieEntry.COLUMN_POSTER_PATH, posterPath);
            cv.put(MovieEntry.COLUMN_ADULT, adult);
            cv.put(MovieEntry.COLUMN_OVERVIEW, overview);
            cv.put(MovieEntry.COLUMN_RELEASE_DATE, releaseDate);
            cv.put(MovieEntry.COLUMN_ID, id);
            cv.put(MovieEntry.COLUMN_ORIGINAL_TITLE, originalTitle);
            cv.put(MovieEntry.COLUMN_ORIGINAL_LANGUAGE, originalLanguage);
            cv.put(MovieEntry.COLUMN_TITLE, title);
            cv.put(MovieEntry.COLUMN_BACKDROP_PATH, backdropPath);
            cv.put(MovieEntry.COLUMN_POPULARITY, popularity);
            cv.put(MovieEntry.COLUMN_VOTE_COUNT, voteCount);
            cv.put(MovieEntry.COLUMN_VIDEO, video);
            cv.put(MovieEntry.COLUMN_VOTE_AVERAGE, voteAverage);

            contentValues[i] = cv;
        }

        return contentValues;

    }

    private static int mapBooleanToIntForSqLite(boolean value) {
        if(value) return 1;
        return 0;
    }
}
