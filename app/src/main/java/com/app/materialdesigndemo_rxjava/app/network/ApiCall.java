package com.app.materialdesigndemo_rxjava.app.network;


import com.app.materialdesigndemo_rxjava.app.model.GifsData;
import com.app.materialdesigndemo_rxjava.app.model.RandomGifs;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

public interface ApiCall {

    @GET("/v1/gifs/trending")
    Observable<GifsData> getTrendingGifs(@Query("api_key") String api_key);

    @GET("/v1/gifs/random")
    Observable<RandomGifs> getRandomGifs(@Query("api_key") String api_key);

    @GET("/v1/gifs/search")
    Observable<GifsData> getSearchedGifs(@Query("q") String search_string, @Query("api_key") String api_key);




}
