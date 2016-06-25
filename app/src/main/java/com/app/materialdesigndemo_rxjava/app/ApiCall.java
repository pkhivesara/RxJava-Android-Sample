package com.app.materialdesigndemo_rxjava.app;


import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

import java.util.List;

public interface ApiCall {

    @GET("/v1/gifs/trending")
    Observable<GifsData> getTrendingGifs(@Query("api_key") String api_key);


}
