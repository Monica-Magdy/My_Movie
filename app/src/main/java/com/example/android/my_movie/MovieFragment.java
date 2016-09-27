package com.example.android.my_movie;


import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.util.ArrayList;


public class MovieFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static ImageAdapter imageAdapter;
    int ID;
    public static ArrayList<String>mThumbnails=new ArrayList<>();
    public static ArrayList<String>cursorThumbnails=new ArrayList<>();
    public static ArrayList<String>movieDetails=new ArrayList<>();
    public static ArrayList<String>keys=new ArrayList<>();
    public static ArrayList<Integer>Ids=new ArrayList<>();
    public static ArrayList<Integer>Ids2=new ArrayList<>();
    public static ArrayList<String>names=new ArrayList<>();
    public static ArrayAdapter<String> movieAdapter;
    public static ArrayList<String>Reviews=new ArrayList<>();
    public static ArrayList<String> trailers=new ArrayList<>();
    public static  ArrayList<Movie> movieArrayList=new ArrayList<>();
    private static final int MY_LOADER_ID=0;
    FavouriteAdapter mFavouriteAdapter;
    String details ;
    String pic ;
    public static GridView gridview;
    Callback mcallback;//object from interface
    public static int mPosition = GridView.INVALID_POSITION;
    public static final String SELECTED_KEY = "selected_position";

    private static final String[] MOVIE_COLUMNS={
            MovieContract.MovieEntry.TABLE_NAME+"."+MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_MovieID,
            MovieContract.MovieEntry.COLUMN_DETAILS,
            MovieContract.MovieEntry.COLUMN_Poster_Path
    };

    static final int COL_MOVIE_ID = 0;
    static final int COL_MOVIE_API_ID = 1;//MOVIE ID IN THE API
    static final int COL_MOVIE_DETAILS= 2;
    static final int COL_MOVIE_POSTER_PATH= 3;
   static boolean gridViewEmpty=true;
    public MovieFragment() {setRetainInstance(true);

    }

    @Override
    public void onResume() {
        super.onResume();
        movieArrayList.clear();
        mThumbnails.clear();
        Ids.clear();
        movieAdapter.clear();
       imageAdapter.updateMovies(mThumbnails);
       imageAdapter.notifyDataSetChanged();
        movieAdapter.notifyDataSetChanged();
       // if(mPosition!= GridView.INVALID_POSITION)
           // gridview.smoothScrollToPosition(mPosition);
    }

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }




    @Override
   public void onStart() {
        super.onStart();
        sort();

    }

    public  void getTrailers(int ID)
    {
        trailers.clear();
        FetchTrailerTask TrailerTask=new FetchTrailerTask();
        try {
            TrailerTask.onPostExecute(TrailerTask.execute("http://api.themoviedb.org/3/movie/" +ID + "/videos?api_key=????").get());
        }
        catch (Exception e){Log.e("Error:","is",e);
        }


    }
    public void getReviews(int ID)
    {
        Reviews.clear();
        FetchReviewTask RevTask=new FetchReviewTask();
        try {

            RevTask.onPostExecute(RevTask.execute("http://api.themoviedb.org/3/movie/"+ID+"/reviews?api_key=????").get());
        }
        catch (Exception e){Log.e("Error:","is",e);
        }

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_movie, container, false);
        if(savedInstanceState == null || !savedInstanceState.containsKey("Movies")) {

        }
        else {

            movieArrayList = savedInstanceState.getParcelableArrayList("Movies");
            SharedPreferences sharedPrefs= PreferenceManager.getDefaultSharedPreferences(getActivity());
            String SortType=sharedPrefs.getString(getString(R.string.pref_sort_key),getString(R.string.pref_sort_most_popular));
            if(SortType.equals(getString(R.string.pref_sort_top_rated))||SortType.equals(getString(R.string.pref_sort_most_popular)))
            {
                mThumbnails.clear();
                movieAdapter.clear();
                Ids.clear();
                for(int i=0;i<movieArrayList.size();i++)
                {
                    mThumbnails.add("http://image.tmdb.org/t/p/w185"+movieArrayList.get(i).getPoster_path());
                    movieAdapter.add(movieArrayList.get(i).getDETAILS());
                    Ids.add(movieArrayList.get(i).getId());

                }
                gridview.setAdapter(imageAdapter);
               imageAdapter.updateMovies(mThumbnails);
                imageAdapter.notifyDataSetChanged();
            }
            else if(SortType.equals(getString(R.string.pref_sort_favourites))){
                cursorThumbnails.clear();
                movieDetails.clear();
                Ids2.clear();
                for(int i=0;i<movieArrayList.size();i++)
                {
                    cursorThumbnails.add(movieArrayList.get(i).getPoster_path());
                    movieDetails.add(movieArrayList.get(i).getDETAILS());
                    Ids2.add(movieArrayList.get(i).getId());
                }
            }
        }
        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            mPosition = savedInstanceState.getInt(SELECTED_KEY);

        }


        imageAdapter=new ImageAdapter(getActivity(),mThumbnails);
        movieAdapter =
                new ArrayAdapter<String>(
                        getActivity(), // The current context (this activity)
                        R.layout.grid_movie_item, // The name of the layout ID.
                        R.id.iv_movie_poster, // The ID of the textview to populate.
                        new ArrayList<String>());

        gridview = (GridView) rootView.findViewById(R.id.gridview);
        gridview.setAdapter(imageAdapter);
        if(mPosition!= GridView.INVALID_POSITION){
            gridview.setSelection(mPosition);
           }
          gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                if (!Ids.isEmpty()) {
                    ID = Ids.get(position);
                    getTrailers(ID);
                    getReviews(ID);
                     details = movieAdapter.getItem(position);
                     pic = mThumbnails.get(position);

                    mcallback.onItemSelected(pic,details,trailers,keys,Reviews,ID);
                }
                else {
                    Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                    if (cursor != null) {
                        ID=Ids2.get(position);
                        getTrailers(ID);
                        getReviews(ID);

                        pic=cursorThumbnails.get(position);

                        String det=movieDetails.get(position);

                        mcallback.onItemSelected(pic,det,trailers,keys,Reviews,ID);
                        }
                }
               mPosition=position;
            }
        });
        return rootView;
    }

    private void sort()
    {
        movieArrayList.clear();
        SharedPreferences sharedPrefs= PreferenceManager.getDefaultSharedPreferences(getActivity());
        String SortType=sharedPrefs.getString(getString(R.string.pref_sort_key),getString(R.string.pref_sort_most_popular));
        if(SortType.equals(getString(R.string.pref_sort_top_rated)))
        {
            mThumbnails.clear();
            movieAdapter.clear();
            Ids.clear();
            gridview.setAdapter(imageAdapter);
            FetchMovieTask movieTask=new FetchMovieTask();
            movieTask.execute("http://api.themoviedb.org/3/movie/top_rated?api_key=????");

        }else if(SortType.equals(getString(R.string.pref_sort_most_popular))) {
            mThumbnails.clear();
            movieAdapter.clear();
            Ids.clear();
            gridview.setAdapter(imageAdapter);
            FetchMovieTask movieTask=new FetchMovieTask();
            movieTask.execute("http://api.themoviedb.org/3/movie/popular?api_key=????");
        }
            else if(SortType.equals("favourites")){
            cursorThumbnails.clear();
            movieDetails.clear();
            Ids2.clear();
                getLoaderManager().initLoader(MY_LOADER_ID, null, this);
                mFavouriteAdapter=new FavouriteAdapter(getActivity(),null,0);
                Uri MOVIE_URI=MovieContract.MovieEntry.CONTENT_URI;
                Cursor cur=getActivity().getContentResolver().query(MOVIE_URI,null,null,null,null);
                gridview.setAdapter(mFavouriteAdapter);
            for(int i=0;i<Ids2.size();i++){
                Movie movie=new Movie();
                movie.setPoster_path(cursorThumbnails.get(i));
                movie.setDETAILS(movieDetails.get(i));
                movie.setId(Ids2.get(i));
                movieArrayList.add(movie);
            }
            }
        else
            Log.d("Error","Sort type not found:"+SortType);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("Movies",movieArrayList);//it adds the moviearraylist to the bundle
        if(mPosition!=GridView.INVALID_POSITION)
        {
            outState.putInt(SELECTED_KEY, mPosition);
        }

        super.onSaveInstanceState(outState);//it saves some info about app
    }



    public Loader<Cursor> onCreateLoader(int i, Bundle bundle)
    {
        Uri MOVIE_URI=MovieContract.MovieEntry.CONTENT_URI;
        return new CursorLoader(getContext(),MOVIE_URI,MOVIE_COLUMNS,null,null,null);
    }
    public void onLoadFinished(Loader<Cursor> cursorLoader,Cursor cursor)
    {
        if(cursor.getPosition()<movieArrayList.size())
        mFavouriteAdapter.swapCursor(cursor);
       if(mPosition!= GridView.INVALID_POSITION)
           gridview.smoothScrollToPosition(mPosition);

    }

    public  void onLoaderReset(Loader<Cursor> cursorLoader)
    {
        mFavouriteAdapter.swapCursor(null);
    }


    public void setCallBack(Callback callBack)
    {
        mcallback=callBack;
    }
}
