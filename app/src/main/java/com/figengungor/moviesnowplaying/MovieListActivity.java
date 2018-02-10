package com.figengungor.moviesnowplaying;


import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.figengungor.moviesnowplaying.data.ErrorEvent;
import com.figengungor.moviesnowplaying.data.MovieContract;
import com.figengungor.moviesnowplaying.databinding.ActivityMovieListBinding;
import com.figengungor.moviesnowplaying.sync.MovieSyncUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class MovieListActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler, LoaderManager.LoaderCallbacks<Cursor> {

    public static final String[] MOVIE_LIST_PROJECTION = {
            MovieContract.MovieEntry.COLUMN_ID,
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_POSTER_PATH,
            MovieContract.MovieEntry.COLUMN_RELEASE_DATE
    };

    public static final int INDEX_MOVIE_ID = 0;
    public static final int INDEX_MOVIE_TITLE = 1;
    public static final int INDEX_MOVIE_POSTER_PATH = 2;
    public static final int INDEX_MOVIE_RELEASE_DATE = 3;


    private static final int ID_MOVIE_LOADER = 1;
    private static final String TAG = MovieListActivity.class.getSimpleName();
    private int mPosition = RecyclerView.NO_POSITION;

    private MovieAdapter mMovieAdapter;
    private ActivityMovieListBinding mMovieListBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMovieListBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_list);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        mMovieListBinding.recyclerView.addItemDecoration(dividerItemDecoration);
        mMovieListBinding.recyclerView.setHasFixedSize(true);

        mMovieAdapter = new MovieAdapter(this, this);
        mMovieListBinding.recyclerView.setAdapter(mMovieAdapter);

        EventBus.getDefault().register(this);

        showLoading();

        getSupportLoaderManager().initLoader(ID_MOVIE_LOADER, null, this);

        MovieSyncUtils.initialize(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movie_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.action_refresh:
                MovieSyncUtils.startImmediateSync(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(int movieId, View poster, View title, View releaseDate) {
        Pair<View, String> p1 = Pair.create(poster, getString(R.string.poster_transition_name));
        Pair<View, String> p2 = Pair.create(title, getString(R.string.title_transition_name));
        Pair<View, String> p3 = Pair.create(releaseDate, getString(R.string.releaseDate_transition_name));
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, p1, p2, p3);

        startActivity(new Intent(this, MovieDetailActivity.class)
                .setData(MovieContract.MovieEntry.buildMovieUriWithId(movieId)), options.toBundle());
    }

    private void showLoading() {
        mMovieListBinding.recyclerView.setVisibility(View.INVISIBLE);
        mMovieListBinding.errorLayout.errorView.setVisibility(View.INVISIBLE);
        mMovieListBinding.progressBar.setVisibility(View.VISIBLE);
    }

    private void showMovieDataView() {
        mMovieListBinding.recyclerView.setVisibility(View.VISIBLE);
        mMovieListBinding.progressBar.setVisibility(View.INVISIBLE);
        mMovieListBinding.errorLayout.errorView.setVisibility(View.INVISIBLE);
    }

    private void showErrorView() {
        mMovieListBinding.recyclerView.setVisibility(View.INVISIBLE);
        mMovieListBinding.progressBar.setVisibility(View.INVISIBLE);
        mMovieListBinding.errorLayout.errorView.setVisibility(View.VISIBLE);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
        switch (loaderId) {
            case ID_MOVIE_LOADER:
                return new CursorLoader(
                        this,
                        MovieContract.MovieEntry.CONTENT_URI,
                        MOVIE_LIST_PROJECTION,
                        null,
                        null,
                        MovieContract.MovieEntry.COLUMN_RELEASE_DATE + " DESC");
            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mMovieAdapter.swapCursor(data);
        if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
        mMovieListBinding.recyclerView.smoothScrollToPosition(mPosition);
        if (data.getCount() != 0) showMovieDataView();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMovieAdapter.swapCursor(null);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ErrorEvent event) {
        /* Do something */
        Log.e(TAG, "onMessageEvent: " + event.getMessage());
        mMovieListBinding.errorLayout.textViewErrorMessage.setText(event.getMessage());
        showErrorView();
    }

    public void onRetry(View view) {
        MovieSyncUtils.startImmediateSync(this);
    }
}
