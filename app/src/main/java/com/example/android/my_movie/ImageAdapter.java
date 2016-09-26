package com.example.android.my_movie;

/**
 * Created by Lenovo on 8/16/2016.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Lenovo on 8/13/2016.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    ArrayList<String> mThumbIds=new ArrayList<>();
    public ImageAdapter(Context c,ArrayList<String> Thumbnails) {
        mContext = c;
        mThumbIds=Thumbnails;
    }

    public int getCount() {
        return mThumbIds.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.grid_movie_item, null);}
            imageView = (ImageView) convertView.findViewById(R.id.iv_movie_poster);
        Picasso.with(mContext).load(mThumbIds.get(position)).placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher).into(imageView);
        return convertView;
    }

    public void updateMovies(ArrayList<String> movie_list) {
        mThumbIds = movie_list;
    }

}