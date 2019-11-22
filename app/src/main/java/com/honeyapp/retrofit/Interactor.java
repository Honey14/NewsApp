package com.honeyapp.retrofit;


import com.honeyapp.pojo.HeadStart;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Interactor {

    @GET("everything")
    Observable<HeadStart> getNews(
            @Query("q") String bitcoin,
            @Query("from") String date,
            @Query("sortBy") String publishedAt,
            @Query("apiKey") String apiKey
    );

}
