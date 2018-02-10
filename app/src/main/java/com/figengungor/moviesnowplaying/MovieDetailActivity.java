package com.figengungor.moviesnowplaying;

import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.figengungor.moviesnowplaying.data.MovieContract;
import com.figengungor.moviesnowplaying.databinding.ActivityMovieDetailBinding;
import com.figengungor.moviesnowplaying.utilities.ImageUtils;
import com.figengungor.moviesnowplaying.utilities.MovieDateUtils;

public class MovieDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {


    private static final int ID_MOVIE_DETAIL_LOADER = 2;
    private static final String TAG = MovieDetailActivity.class.getSimpleName();
    private ActivityMovieDetailBinding mDetailBinding;
    private Uri mUri;

    public static final String[] MOVIE_DETAIL_PROJECTION = {
            MovieContract.MovieEntry.COLUMN_ID,
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_POSTER_PATH,
            MovieContract.MovieEntry.COLUMN_BACKDROP_PATH,
            MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
            MovieContract.MovieEntry.COLUMN_OVERVIEW,
            MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE
    };

    public static final int INDEX_MOVIE_ID = 0;
    public static final int INDEX_MOVIE_TITLE = 1;
    public static final int INDEX_MOVIE_POSTER_PATH = 2;
    public static final int INDEX_MOVIE_BACKDROP_PATH = 3;
    public static final int INDEX_MOVIE_RELEASE_DATE = 4;
    public static final int INDEX_MOVIE_OVERVIEW = 5;
    public static final int INDEX_VOTE_AVERAGE = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mUri = getIntent().getData();
        if (mUri == null)
            throw new NullPointerException("Uri for MovieDetailActivity cannot be null");

        getSupportLoaderManager().initLoader(ID_MOVIE_DETAIL_LOADER, null, this);

        mDetailBinding.shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDetailBinding.shareButton.getTag() != null)
                    shareMovie((int) mDetailBinding.shareButton.getTag());
            }
        });


    }

    private void shareMovie(int movieId) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "https://www.themoviedb.org/movie/" + movieId);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getString(R.string.choosesharingplatform)));
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
        switch (loaderId) {
            case ID_MOVIE_DETAIL_LOADER:

                return new CursorLoader(
                        this,
                        mUri,
                        MOVIE_DETAIL_PROJECTION,
                        null,
                        null,
                        null
                );
            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        boolean cursorHasValidData = false;
        if (data != null && data.moveToFirst()) {
            cursorHasValidData = true;
        }

        if (!cursorHasValidData) {
            return;
        }

        int movieId = data.getInt(INDEX_MOVIE_ID);
        mDetailBinding.shareButton.setTag(movieId);

        String posterPath = data.getString(INDEX_MOVIE_POSTER_PATH);
        ImageUtils.loadImageUrl(posterPath, mDetailBinding.imageViewPoster, ImageUtils.ImageType.POSTER);

        String backdropPath = data.getString(INDEX_MOVIE_BACKDROP_PATH);
        ImageUtils.loadImageUrl(backdropPath, mDetailBinding.imageViewBackDrop, ImageUtils.ImageType.BACKDROP);

        String title = data.getString(INDEX_MOVIE_TITLE);
        mDetailBinding.textViewTitle.setText(title);

        String releaseDate = data.getString(INDEX_MOVIE_RELEASE_DATE);
        mDetailBinding.textViewReleaseDate.setText(MovieDateUtils.getFriendlyReleaseDate(releaseDate));

        String overview = data.getString(INDEX_MOVIE_OVERVIEW);
        mDetailBinding.textViewOverview.setText(overview);

        float voteAverage = data.getFloat(INDEX_VOTE_AVERAGE);
        mDetailBinding.textViewVoteAverage.setText(String.valueOf(voteAverage));


        mDetailBinding.ratingBar.setRating(voteAverage);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
