package com.example.chirag.project2.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int i) {
            return new Movie[i];
        }

    };
    String poster_path, overview, release_date, original_title, title;
    String backdrop_path, original_language;
    Boolean adult, video;
    double vote_average, popularity;
    long vote_count, id;

    public Movie(String poster_path, Boolean adult, String overview, String release_date, long id, String original_title, String original_language, String title, String backdrop_path, double popularity, long vote_count, boolean video, double vote_average) {
        this.poster_path = poster_path;
        this.adult = adult;
        this.overview = overview;
        this.release_date = release_date;
        this.id = id;
        this.original_title = original_title;
        this.original_language = original_language;
        this.title = title;
        this.backdrop_path = backdrop_path;
        this.popularity = popularity;
        this.vote_count = vote_count;
        this.video = video;
        this.vote_average = vote_average;
    }

    private Movie(Parcel in) {
        poster_path = in.readString();
        adult = in.readByte() != 0;
        overview = in.readString();
        release_date = in.readString();
        id = in.readLong();
        original_title = in.readString();
        original_language = in.readString();
        title = in.readString();
        backdrop_path = in.readString();
        popularity = in.readDouble();
        vote_count = in.readLong();
        video = in.readByte() != 0;
        vote_average = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String toString() {
        return  poster_path+ "--" +adult+ "--" +overview+ "--" + release_date+ "--" +  id+ "--" + original_title+ "--" +
                original_language+ "--" + title+ "--" +backdrop_path+ "--" + popularity+ "--" + vote_count+ "--" +video+ "--" +vote_average;
//        return name + "--" + popularity + "--" + vote_average;

    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(poster_path);
        parcel.writeByte((byte) (adult ? 1 : 0));     //if myBoolean == true, byte == 1 /* http://stackoverflow.com/a/7089687 */
        parcel.writeString(overview);
        parcel.writeString(release_date);
        parcel.writeLong(id);
        parcel.writeString(original_title);
        parcel.writeString(original_language);
        parcel.writeString(title);
        parcel.writeString(backdrop_path);
        parcel.writeDouble(popularity);
        parcel.writeLong(vote_count);
        parcel.writeByte((byte) (video ? 1 : 0));     //if myBoolean == true, byte == 1 /* http://stackoverflow.com/a/7089687 */
        parcel.writeDouble(vote_average);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public Boolean getAdult() {
        return adult;
    }

    public void setAdult(Boolean adult) {
        this.adult = adult;
    }

    public Boolean getVideo() {
        return video;
    }

    public void setVideo(Boolean video) {
        this.video = video;
    }

    public double getVote_average() {
        return vote_average;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public long getVote_count() {
        return vote_count;
    }

    public void setVote_count(long id) {
        this.vote_count = vote_count;
    }

}
