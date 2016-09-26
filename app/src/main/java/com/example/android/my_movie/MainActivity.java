package com.example.android.my_movie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements Callback {

    Boolean mTwoPane;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(findViewById(R.id.movie_detail_container)!=null) {
            mTwoPane=true;
        }
        else{
            mTwoPane=false;
        }
        MovieFragment movieFragment = new MovieFragment();
        movieFragment.setCallBack(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_movie,movieFragment).commit();
}

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.moviefragment, menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            DetailFragment df = (DetailFragment)getSupportFragmentManager().findFragmentById(R.id.movie_detail_container);
            if ( null != df ) {
                df = new DetailFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.movie_detail_container, df).commit();
            }
            startActivity(new Intent(this,SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onItemSelected(String posterPath, String info, ArrayList trailers,ArrayList keys,ArrayList reviews,int id) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            DetailFragment detailFragment=new DetailFragment();
            Bundle extras=new Bundle();
            extras.putString("Poster path",posterPath);
            extras.putString(Intent.EXTRA_TEXT,info);
            extras.putStringArrayList("Videos",trailers);
            extras.putStringArrayList("Keys",keys);
            extras.putStringArrayList("Reviews",reviews);
            extras.putInt("MovieID",id);
            detailFragment.setArguments(extras);
            getSupportFragmentManager().beginTransaction().replace(R.id.movie_detail_container,detailFragment).commit();
        } else {
            Intent i = new Intent(this, DetailsActivity.class);

             i.putExtra("Poster path",posterPath);
             i.putExtra(Intent.EXTRA_TEXT,info);
             i.putStringArrayListExtra("Videos",trailers);
             i.putStringArrayListExtra("Keys",keys);
             i.putStringArrayListExtra("Reviews",reviews);
             i.putExtra("MovieID",id);
            startActivity(i);
        }
    }
    @Override
    public void onResume()
    {
        super.onResume();

    }
}
