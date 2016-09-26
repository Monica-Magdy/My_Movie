package com.example.android.my_movie;

import java.util.ArrayList;

/**
 * Created by Lenovo on 9/18/2016.
 */
public interface Callback {
    /**
     * DetailFragmentCallback for when an item has been selected.
     */
    public void onItemSelected(String posterPath, String info, ArrayList trailers, ArrayList keys, ArrayList reviews,int id);
}