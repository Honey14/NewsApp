package com.honeyapp.room;

import android.os.Parcel;
import android.os.Parcelable;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "articles")
public class NewsDetails implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String description;
    private String name;
    private String publishedAt;
    private String urlToImage;

    public NewsDetails(String title, String description, String name, String publishedAt, String urlToImage) {
        this.title = title;
        this.description = description;
        this.name = name;
        this.publishedAt = publishedAt;
        this.urlToImage = urlToImage;
    }

    protected NewsDetails(Parcel in) {
        id = in.readInt();
        title = in.readString();
        description = in.readString();
        name = in.readString();
        publishedAt = in.readString();
        urlToImage = in.readString();
    }

    public static final Creator<NewsDetails> CREATOR = new Creator<NewsDetails>() {
        @Override
        public NewsDetails createFromParcel(Parcel in) {
            return new NewsDetails(in);
        }

        @Override
        public NewsDetails[] newArray(int size) {
            return new NewsDetails[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(name);
        parcel.writeString(publishedAt);
        parcel.writeString(urlToImage);
    }
}
