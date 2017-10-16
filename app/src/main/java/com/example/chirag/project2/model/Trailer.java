package com.example.chirag.project2.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Trailer implements Parcelable {
    public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel parcel) {
            return new Trailer(parcel);
        }

        @Override
        public Trailer[] newArray(int i) {
            return new Trailer[i];
        }

    };
    String provider, youtube_path,title;
    String trailer_id;

    public Trailer(String title, String provider, String youtube_path, String trailer_id) {
        this.provider = provider;
        this.title= title;
        this.youtube_path = youtube_path;
        this.trailer_id = trailer_id;
     }

    private Trailer(Parcel in) {
        provider = in.readString();
        youtube_path = in.readString();
        title=in.readString();
        trailer_id= in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String toString() {
        return  provider + "--" + youtube_path + "--"+title;
//        return name + "--" + popularity + "--" + vote_average;

    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(provider);
        parcel.writeString(youtube_path);
        parcel.writeString(trailer_id);
        parcel.writeString(title);
    }

    public String getId() {
        return trailer_id;
    }

    public void setId(String id) {
        this.trailer_id = trailer_id;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getYoutube_path() {
        return youtube_path;
    }

    public void setYoutube_path(String youtube_path) {
        this.youtube_path = youtube_path;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
