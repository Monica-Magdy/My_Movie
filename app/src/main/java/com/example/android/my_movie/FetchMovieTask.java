package com.example.android.my_movie;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Lenovo on 9/16/2016.
 */
public class FetchMovieTask extends AsyncTask<String,Void, String[]>
{
    private final String LOG_TAG=FetchMovieTask.class.getSimpleName();
    public FetchMovieTask() {
    }

    private String[] getMovieDatafromJson(String MovieJsonStr)
            throws JSONException
    {
        JSONObject MovieJson = new JSONObject(MovieJsonStr);

        JSONArray MovieArray = MovieJson.getJSONArray( "results");

        String[] resultStrs = new String[MovieArray.length()];
        for (int i=0;i<MovieArray.length();i++)
        {
            Movie movie=new Movie();

            String Original_Title;
            String Poster_path;
            String releaseDate;
            String overview;
            Double voteAverage;
            int id;

            JSONObject MovieInfo = MovieArray.getJSONObject(i);//ygeb curly bracket rkm i gwa el results

            Poster_path=MovieInfo.getString("poster_path");
            movie.setPoster_path(Poster_path);
            MovieFragment.mThumbnails.add("http://image.tmdb.org/t/p/w185"+movie.getPoster_path());

            id=MovieInfo.getInt("id");
            movie.setId(id);
            MovieFragment.Ids.add(movie.getId());

            Original_Title=MovieInfo.getString("original_title");

            overview=MovieInfo.getString("overview");

            voteAverage=MovieInfo.getDouble( "vote_average");

            releaseDate=MovieInfo.getString("release_date");
            movie.setDETAILS(Original_Title+"\n"+overview+"\n"+"Vote Average: "+voteAverage+"\n"+"Release Date: "+releaseDate);
            MovieFragment.movieArrayList.add(movie);
            resultStrs[i]=MovieFragment.movieArrayList.get(i).getDETAILS();
        }
        return resultStrs;
    }

    protected  String[] doInBackground(String...params)
    {
        if(params.length==0)
            return  null;

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String MovieJsonStr = null;


        try{
            String Base_URL=params[0];
            URL url = new URL(Base_URL);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                MovieJsonStr = null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                MovieJsonStr = null;
            }
            MovieJsonStr = buffer.toString();

        }
        catch (Exception e)
        {
            Log.e(LOG_TAG, "Error ", e);
            return null;
        }
        finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        try {
            return getMovieDatafromJson(MovieJsonStr);
        }
        catch (JSONException e)
        {
            Log.e(LOG_TAG,e.getMessage(),e);
            e.printStackTrace();
        }

        return null;
    }

    protected void onPostExecute(String[] result)
    {
        if(result!=null)
        {
            MovieFragment.imageAdapter.updateMovies(MovieFragment.mThumbnails);
            MovieFragment.movieAdapter.clear();
            MovieFragment.imageAdapter.notifyDataSetChanged();
            for(String Str:result)
                MovieFragment.movieAdapter.add(Str);


        }
    }

}