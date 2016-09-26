package com.example.android.my_movie;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
/**
 * Created by Lenovo on 9/9/2016.
 */
public class MovieContract {
    public static final String CONTENT_AUTHORITY ="com.example.android.my_movie";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_Movie= "movies";

    public static final class MovieEntry implements BaseColumns {
        public static final String TABLE_NAME = "movies";
        public static final String COLUMN_Poster_Path="Poster_Path";
        public static final String COLUMN_DETAILS="Details";
        public static final String COLUMN_MovieID="MovieID";
        public static final Uri CONTENT_URI =//Use buildUpon() to obtain a builder representing an existing URI.
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_Movie).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE +"/"+CONTENT_AUTHORITY +"/"+PATH_Movie;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE +"/"+CONTENT_AUTHORITY +"/"+PATH_Movie;

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String getPosterPath(Uri uri) {
            String POSTERString = uri.getQueryParameter(COLUMN_Poster_Path);
            if (null != POSTERString && POSTERString.length() > 0)
                return POSTERString;
            else
                return null;
        }
        public static String getDETAILS(Uri uri) {
            String detailsString = uri.getQueryParameter(COLUMN_DETAILS);
            if (null != detailsString && detailsString.length() > 0)
                return detailsString;
            else
                return null;
        }
        public static int getmovieId(Uri uri) {
            String IDString = uri.getQueryParameter(COLUMN_MovieID);
            if (null != IDString && IDString.length() > 0)
                return Integer.parseInt(IDString);
            else
                return -1;
        }
        public static int getmovieAtposition(Uri uri) {
            String id = uri.getQueryParameter(BaseColumns._ID);
            if (null != id && id.length() > 0)
                return Integer.parseInt(id);
            else
                return -1;
        }
    }
}
