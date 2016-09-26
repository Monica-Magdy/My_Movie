package com.example.android.my_movie;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Lenovo on 9/18/2016.
 */
public  class DetailFragment extends Fragment {
    ListAdapter mVideoAdapter;
    ListAdapter mReviewsAdapter;

    ImageView image ;

    TextView detailText;
    TextView trailerText;
    TextView ReviewText;
    ListView listView ;
    ListView l2;

    private static final int DETAIL_LOADER=1;
    private String movie;
    private ArrayList<String> KEY;
    private ArrayList<String> videos;
    private ArrayList<String>reviews;
    private int movie_id;
    ImageAdapter i;
    int flag=0;
    public static ArrayList<Movie> mArrayList;
    private static final String[] MOVIE_Details_COLUMNS={
            MovieContract.MovieEntry.TABLE_NAME+"."+MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_MovieID,
            MovieContract.MovieEntry.COLUMN_DETAILS,
            MovieContract.MovieEntry.COLUMN_Poster_Path
    };

    static final int COL_MOVIE_ID = 0;
    static final int COL_MOVIE_API_ID = 1;//MOVIE ID IN THE API
    static final int COL_MOVIE_DETAILS= 2;
    static final int COL_MOVIE_POSTER_PATH= 3;
    //private final Context mContext;
    public DetailFragment() {
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null || !savedInstanceState.containsKey("Movi")) {

        }
        else {
            mArrayList=savedInstanceState.getParcelableArrayList("Movi");
        }

        }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ActionBar.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();

        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() -1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

         mArrayList=new ArrayList<>();
        image = (ImageView) rootView.findViewById(R.id.imageview);
        final ImageButton b=(ImageButton) rootView.findViewById(R.id.favorite);
        detailText= ((TextView) rootView.findViewById(R.id.detail_text));
        listView = (ListView) rootView.findViewById(R.id.trailersList);
        trailerText=(TextView) rootView.findViewById(R.id.trailersTV) ;
        ReviewText=(TextView) rootView.findViewById(R.id.ReviewsTV);
        l2=(ListView) rootView.findViewById(R.id.reviewsList);
        Intent intent = getActivity().getIntent();
        Movie m=new Movie();
        if (intent != null &&intent.hasExtra(Intent.EXTRA_TEXT)) {
            final String  strImage= intent.getStringExtra("Poster path");
            m.setPoster_path(strImage);
            Picasso.with(getContext()).load(m.getPoster_path()).placeholder(R.mipmap.ic_launcher).into(image);
            movie=intent.getStringExtra(intent.EXTRA_TEXT);
            m.setDETAILS(movie);
            videos=intent.getStringArrayListExtra("Videos");
           // trailerText.setPaintFlags(trailerText.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);


            KEY=intent.getStringArrayListExtra("Keys");
            reviews=intent.getStringArrayListExtra("Reviews");
            movie_id=intent.getIntExtra("MovieID",0);
            m.setId(movie_id);
            Cursor favCursor=getContext().getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,new String[]{MovieContract.MovieEntry._ID},MovieContract.MovieEntry.COLUMN_MovieID+"=?",new String[]{Integer.toString(movie_id)},null);
            if (favCursor.moveToFirst()) {
                b.setImageResource(R.drawable.button_pressed);
                flag=1;
            }
            else {
                b.setImageResource(R.drawable.button_normal);
            }
            mVideoAdapter=new ArrayAdapter<String>( getActivity(), // The current context (this activity)
                    R.layout.trailer_list_item, // The name of the layout ID.
                    R.id.name, // The ID of the textview to populate.
                    videos);
            mReviewsAdapter=new ArrayAdapter<String>(getActivity(),R.layout.review_list_item,R.id.list_item_reviews_textview,reviews);
            detailText.setText(movie);
            if(!mVideoAdapter.isEmpty())
                trailerText.setText("Trailers:");
            if(!mReviewsAdapter.isEmpty())
                ReviewText.setText("Reviews:");
            listView.setAdapter(mVideoAdapter);
            l2.setAdapter(mReviewsAdapter);
            setListViewHeightBasedOnChildren(listView);
            setListViewHeightBasedOnChildren(l2);
            listView.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View v,
                                                int position, long id) {
                            String URL="http://www.youtube.com/watch?v="+KEY.get(position);

                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
                            startActivity(intent);
                        }
                    }
            );

            b.setOnClickListener(new View.OnClickListener() {
                                     public void onClick(View v) {
                                         if(flag==0){
                                             long Insertedrow=addFavourite(movie_id,movie,strImage);
                                             b.setImageResource(R.drawable.button_pressed);
                                             flag=1;
                                         }
                                         else
                                         {
                                             removeFavourite(movie_id);
                                             int position= MovieFragment.Ids2.indexOf(movie_id);
                                             MovieFragment.cursorThumbnails.remove(position);
                                             MovieFragment.movieDetails.remove(position);
                                             MovieFragment.Ids2.remove(position);
                                             b.setImageResource(R.drawable.button_normal);
                                             flag=0;
                                         }

                                     }
                                 }

            );
        }
        else
        {
            Bundle arguments = getArguments();
            if(arguments!=null)
            {
                final String imgPath=arguments.getString("Poster path");
                m.setPoster_path(imgPath);
                Picasso.with(getContext()).load(imgPath).placeholder(R.mipmap.ic_launcher).into(image);
                movie=arguments.getString(intent.EXTRA_TEXT);
                m.setDETAILS(movie);
                videos=arguments.getStringArrayList("Videos");
                KEY=arguments.getStringArrayList("Keys");
                reviews=arguments.getStringArrayList("Reviews");
                movie_id=arguments.getInt("MovieID");
                m.setId(movie_id);
                Cursor favCursor=getContext().getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,new String[]{MovieContract.MovieEntry._ID},MovieContract.MovieEntry.COLUMN_MovieID+"=?",new String[]{Integer.toString(movie_id)},null);
                if (favCursor.moveToFirst()) {
                    b.setImageResource(R.drawable.button_pressed);
                    flag=1;
                }
                else {
                    b.setImageResource(R.drawable.button_normal);
                }
                mVideoAdapter=new ArrayAdapter<String>( getActivity(), // The current context (this activity)
                        R.layout.trailer_list_item, // The name of the layout ID.
                        R.id.name, // The ID of the textview to populate.
                        videos);
                mReviewsAdapter=new ArrayAdapter<String>(getActivity(),R.layout.review_list_item,R.id.list_item_reviews_textview,reviews);
                detailText.setText(movie);
                listView.setAdapter(mVideoAdapter);
                l2.setAdapter(mReviewsAdapter);
                if(!mVideoAdapter.isEmpty())
                    trailerText.setText("Trailers:");
                if(!mReviewsAdapter.isEmpty())
                    ReviewText.setText("Reviews:");
                setListViewHeightBasedOnChildren(listView);
                setListViewHeightBasedOnChildren(l2);
                listView.setOnItemClickListener(
                        new AdapterView.OnItemClickListener() {
                            public void onItemClick(AdapterView<?> parent, View v,
                                                    int position, long id) {
                                String URL="http://www.youtube.com/watch?v="+KEY.get(position);

                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
                                startActivity(intent);
                            }
                        }
                );

                b.setOnClickListener(new View.OnClickListener() {
                                         public void onClick(View v) {
                                             if(flag==0){
                                                 long Insertedrow=addFavourite(movie_id,movie,imgPath);
                                                 b.setImageResource(R.drawable.button_pressed);
                                                 flag=1;
                                             }
                                             else
                                             {
                                                 removeFavourite(movie_id);
                                                 int position= MovieFragment.Ids2.indexOf(movie_id);
                                                 MovieFragment.cursorThumbnails.remove(position);
                                                 MovieFragment.movieDetails.remove(position);
                                                 MovieFragment.Ids2.remove(position);
                                                 b.setImageResource(R.drawable.button_normal);
                                                 flag=0;
                                             }

                                         }
                                     }

                );
            }
        }
        mArrayList.add(m);
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("Movi",mArrayList);//it adds the moviearraylist to the bundle
        super.onSaveInstanceState(outState);//it saves some info about app
    }

    public  long addFavourite(Integer MovieID, String Details, String PosterPath) {
        long Id;
        Cursor movieCursor=getContext().getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,new String[]{MovieContract.MovieEntry._ID},MovieContract.MovieEntry.COLUMN_MovieID+"=?",new String[]{Integer.toString(MovieID)},null);
        if (movieCursor.moveToFirst()) {
            int movieIdIndex=movieCursor.getColumnIndex(MovieContract.MovieEntry._ID);
            Id=movieCursor.getLong(movieIdIndex);
        }
        else {
            // Now that the content provider is set up, inserting rows of data is pretty simple.
            // First create a ContentValues object to hold the data you want to insert.
            ContentValues movieValues = new ContentValues();
            // Then add the data, along with the corresponding name of the data type,
            // so the content provider knows what kind of value is being inserted.
            movieValues.put(MovieContract.MovieEntry.COLUMN_MovieID, MovieID);
            movieValues.put(MovieContract.MovieEntry.COLUMN_DETAILS,Details);
            movieValues.put(MovieContract.MovieEntry.COLUMN_Poster_Path,PosterPath);
            Uri InsertedUri=getContext().getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI,movieValues);
            Id= ContentUris.parseId(InsertedUri);//ContentUris.parseId converts InsertedUri into a long
        }
        movieCursor.close();
        return Id;
    }

    public void removeFavourite(Integer MovieID) {

        String where=MovieContract.MovieEntry.COLUMN_MovieID+"=?";
        getContext().getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI,where,new String[]{Integer.toString(MovieID)});
    }
}
