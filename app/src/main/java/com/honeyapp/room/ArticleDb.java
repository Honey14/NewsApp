package com.honeyapp.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = NewsDetails.class, version = 1)
public abstract class ArticleDb extends RoomDatabase {
    public static final String DATABASE_NAME = "news_database";
    private static ArticleDb mInstance;

    public abstract ArticleDao articleDao();

    public static synchronized ArticleDb getInstance(Context context) {
        if (mInstance == null) {
            mInstance = Room.databaseBuilder(context.getApplicationContext(),
                    ArticleDb.class,
                    DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return mInstance;
    }

}
