package com.app.materialdesigndemo_rxjava.app;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/**
 * Created by Pratik on 6/25/16.
 */
public class RestWebClient {
    private static ApiCall REST_CLIENT;

    static {
        setupRestClient();
    }


    public static ApiCall get() {
        return REST_CLIENT;
    }

    private static void setupRestClient() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient();
        httpClient.interceptors().add(logging);


        String URL = "http://api.giphy.com/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();


        REST_CLIENT = retrofit.create(ApiCall.class);
    }
}