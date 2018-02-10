package com.figengungor.moviesnowplaying.utilities;

import android.widget.ImageView;

import com.figengungor.moviesnowplaying.R;
import com.squareup.picasso.Picasso;

/**
 * Created by figengungor on 12/1/2017.
 */

public class ImageUtils {

    public enum ImageType {
        POSTER,
        BACKDROP
    }

    private static final String IMAGE_URL = "http://image.tmdb.org/t/p/";
    private static final String PATH_POSTER_W185 = "w185";
    private static final String PATH_BACKDROP_W780 = "w780";
    private static final String POSTER_URL = IMAGE_URL + PATH_POSTER_W185;
    private static final String BACKDROP_URL = IMAGE_URL + PATH_BACKDROP_W780;


    public static void loadImageUrl(String imagePath, ImageView imageView, ImageType imageType) {
        String url;
        int placeholderResId;
        switch (imageType) {
            case POSTER:
                url = POSTER_URL + imagePath;
                placeholderResId = R.drawable.placeholder_poster;
                break;
            case BACKDROP:
                url = BACKDROP_URL + imagePath;
                placeholderResId = R.drawable.placeholder_backdrop;
                break;
            default:
                throw new UnsupportedOperationException("ImageType not supported");
        }

        Picasso.with(imageView.getContext())
                .load(url)
                .noFade()
                .placeholder(placeholderResId)
                .into(imageView);
    }


}
