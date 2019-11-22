package com.honeyapp.room;

import androidx.room.Dao;

import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;


@Dao
public interface ArticleDao {
    @Insert
    void insert(NewsDetails newsDetails);


    @Query("SELECT * FROM articles")
    List<NewsDetails> loadAllArticles();


    @Query("DELETE FROM articles")
    void nukeTable();
}
