package com.figengungor.moviesnowplaying;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.figengungor.moviesnowplaying.utilities.ImageUtils;
import com.figengungor.moviesnowplaying.utilities.MovieDateUtils;

/**
 * Created by figengungor on 12/1/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private final Context mContext;
    private final MovieAdapterOnClickHandler mClickHandler;
    private Cursor mCursor;

    public interface MovieAdapterOnClickHandler {
        void onClick(int movieId, View poster, View title, View releaseDate);
    }

    public MovieAdapter(Context context, MovieAdapterOnClickHandler clickHandler) {
        this.mContext = context;
        this.mClickHandler = clickHandler;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_movie_list, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {

        mCursor.moveToPosition(position);

        String title = mCursor.getString(MovieListActivity.INDEX_MOVIE_TITLE);
        String releaseDate = mCursor.getString(MovieListActivity.INDEX_MOVIE_RELEASE_DATE);
        String posterPath = mCursor.getString(MovieListActivity.INDEX_MOVIE_POSTER_PATH);

        holder.title.setText(title);
        holder.releaseDate.setText(MovieDateUtils.getFriendlyReleaseDate(releaseDate));
        ImageUtils.loadImageUrl(posterPath, holder.poster, ImageUtils.ImageType.POSTER);

    }

    @Override
    public int getItemCount() {
        if(mCursor == null) return 0;
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        this.mCursor = newCursor;
        notifyDataSetChanged();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView title;
        final TextView releaseDate;
        final ImageView poster;


        public MovieViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textViewTitle);
            releaseDate = itemView.findViewById(R.id.textViewReleaseDate);
            poster = itemView.findViewById(R.id.imageViewPoster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mCursor.moveToPosition(position);
            int movieId = mCursor.getInt(MovieListActivity.INDEX_MOVIE_ID);
            mClickHandler.onClick(movieId, poster, title, releaseDate);
        }
    }

}
