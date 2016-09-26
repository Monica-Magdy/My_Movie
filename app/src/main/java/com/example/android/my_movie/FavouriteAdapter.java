package com.example.android.my_movie;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by Lenovo on 9/16/2016.
 */
public class FavouriteAdapter extends CursorAdapter {
    public FavouriteAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view= LayoutInflater.from(context).inflate(R.layout.grid_movie_item,parent,false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ImageView imageView=(ImageView)view;
        String ImageURl=posterURL(cursor);
        String details=getDetails(cursor);
        int id=MovieId(cursor);
        //Log.e("url",ImageURl);
        if(!MovieFragment.cursorThumbnails.contains(ImageURl))
        MovieFragment.cursorThumbnails.add(ImageURl);
        if(!MovieFragment.movieDetails.contains(details))
            MovieFragment.movieDetails.add(details);
        if(!MovieFragment.Ids2.contains(id))
            MovieFragment.Ids2.add(id);

        Picasso.with(mContext).load(ImageURl).placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher).into(imageView);
    }

   public String posterURL(Cursor cursor)
    {
        String FavPosterPath=cursor.getString(MovieFragment.COL_MOVIE_POSTER_PATH);
        return FavPosterPath;
    }
    public String getDetails(Cursor cursor)
    {
        String Favdetails=cursor.getString(MovieFragment.COL_MOVIE_DETAILS);
        return Favdetails;
    }
    public int  MovieId(Cursor cursor)
    {
        String id=cursor.getString(MovieFragment.COL_MOVIE_API_ID);
        return Integer.parseInt(id);
    }
}
