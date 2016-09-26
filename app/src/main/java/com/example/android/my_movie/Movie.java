package com.example.android.my_movie;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Lenovo on 8/18/2016.
 */
public class Movie implements Parcelable {

    String poster_path;
    int id;
    String DETAILS;

    public Movie()
    {

    }

    private Movie(Parcel in)
    {
        poster_path=in.readString();
        id=in.readInt();
        DETAILS=in.readString();
    }//bnfs el tarteeb ely 3mlt beh write to parcel


    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getDETAILS() {
        return DETAILS;
    }

    public void setDETAILS(String DETAILS) {
        this.DETAILS = DETAILS;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(poster_path);
        parcel.writeInt(id);
        parcel.writeString(DETAILS);

    }
    public final Parcelable.Creator<Movie> CREATOR=new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int i) {
            return new Movie[i];
        }
    };
}
