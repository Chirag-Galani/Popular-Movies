package com.example.chirag.project2.model;

import android.os.Parcel;
import android.os.Parcelable;


public class Review implements Parcelable {
    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel parcel) {
            return new Review(parcel);
        }

        @Override
        public Review[] newArray(int i) {
            return new Review[i];
        }

    };
    String author, content,url;
    String review_id;

    public Review(String url, String author, String content, String review_id) {
        this.author = author;
        this.url= url;
        this.content = content;
        this.review_id = review_id;
     }

    private Review(Parcel in) {
        author = in.readString();
        content = in.readString();
        url=in.readString();
        review_id= in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String toString() {
        return  author + "--" + content + "--"+url+ "--"+review_id;
//        return name + "--" + popularity + "--" + vote_average;

    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(author);
        parcel.writeString(content);
        parcel.writeString(review_id);
        parcel.writeString(url);
    }

    public String getId() {
        return review_id;
    }

    public void setId(String review_id) {
        this.review_id = review_id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
