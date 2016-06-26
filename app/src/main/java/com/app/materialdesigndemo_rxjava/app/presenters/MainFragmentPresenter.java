package com.app.materialdesigndemo_rxjava.app.presenters;

import android.util.Log;
import android.util.Pair;
import com.app.materialdesigndemo_rxjava.app.model.GifsData;
import com.app.materialdesigndemo_rxjava.app.model.RandomGifs;
import com.app.materialdesigndemo_rxjava.app.network.RestWebClient;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Pratik on 6/26/16.
 */
public class MainFragmentPresenter {
    MainFragmentPresenterInterface mainFragmentPresenterInterface;

    public MainFragmentPresenter(MainFragmentPresenterInterface mainFragmentPresenterInterface) {
        this.mainFragmentPresenterInterface = mainFragmentPresenterInterface;
        loadDataFromGiphysApi();
    }

    private void loadDataFromGiphysApi() {
        makeRetrofitCall();
    }

//    private void makeRetrofitCall() {
//        RestWebClient.get().getTrendingGifs("dc6zaTOxFJmzC")
//                .flatMap(gifsData1 -> RestWebClient.get().getRandomGifs("dc6zaTOxFJmzC"), Pair::create)
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(gifsDataRandomGifsPair -> mainFragmentPresenterInterface.showTrendingList(gifsDataRandomGifsPair));
//
//    }

    private void makeRetrofitCall() {
        RestWebClient.get().getTrendingGifs("dc6zaTOxFJmzC")
                .flatMap(new Func1<GifsData, Observable<RandomGifs>>() {
                    @Override
                    public Observable<RandomGifs> call(GifsData gifsData) {
                        return RestWebClient.get().getRandomGifs("dc6zaTOxFJmzC");
                    }
                }, (gifsData, randomGifs) -> Pair.create(gifsData, randomGifs))
                .subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                new Subscriber<Pair<GifsData, RandomGifs>>() {
                    @Override
                    public void onCompleted() {
                        Log.e("#######", "observable has terminated");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("#######", e.toString());
                    }

                    @Override
                    public void onNext(Pair<GifsData, RandomGifs> gifsDataRandomGifsPair) {
                        mainFragmentPresenterInterface.showTrendingList(gifsDataRandomGifsPair);
                    }
                }
        );
    }

    public interface MainFragmentPresenterInterface {
        void showTrendingList(Pair<GifsData, RandomGifs> gifsData);
    }


}
