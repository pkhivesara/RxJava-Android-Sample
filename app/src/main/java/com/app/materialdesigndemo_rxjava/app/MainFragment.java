package com.app.materialdesigndemo_rxjava.app;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Pratik on 6/25/16.
 */
public class MainFragment extends Fragment {
    GiphyApiTrendingObservable giphyApiTrendingObservable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);
        loadDataFromGiphysApi();
        // giphyApiTrendingObservable = new GiphyApiTrendingObservable();
        return view;

    }

    private void loadDataFromGiphysApi() {

            makeRetrofitCall().subscribe(resultSubscriber);
    }

    private Observable<GifsData> makeRetrofitCall() {
        return RestWebClient.get().getTrendingGifs("dc6zaTOxFJmzC")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());

    }

    Subscriber<GifsData> resultSubscriber = new Subscriber<GifsData>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(GifsData gifsData) {

        }
    };

    public static MainFragment newInstance() {
        return new MainFragment();
    }

}
