package com.app.materialdesigndemo_rxjava.app.presenters;

import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import com.app.materialdesigndemo_rxjava.app.model.GifsData;
import com.app.materialdesigndemo_rxjava.app.model.RandomGifs;
import com.app.materialdesigndemo_rxjava.app.network.RestWebClient;
import com.jakewharton.rxbinding.support.v7.widget.RxSearchView;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Action2;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import java.util.concurrent.TimeUnit;

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



    Action1<GifsData> OnNextAction = gifsData -> mainFragmentPresenterInterface.displaySearchedGifsList(gifsData);

    Action1<Throwable> onErrorAction = throwable -> Log.d("#######",throwable.toString());



    public void startDebounceListenerForSearch(SearchView searchView){
        RxSearchView.queryTextChanges(searchView).debounce(400,TimeUnit.MILLISECONDS).
                observeOn(AndroidSchedulers.mainThread()).
                skip(2).
                subscribeOn(Schedulers.newThread()).
                map(searchViewQueryTextEvent -> searchViewQueryTextEvent.toString()).
                flatMap(s -> RestWebClient.get().getSearchedGifs(s,"dc6zaTOxFJmzC")).
                subscribe(OnNextAction,onErrorAction);


    }

    public interface MainFragmentPresenterInterface {
        void showTrendingList(Pair<GifsData, RandomGifs> gifsData);

        void displaySearchedGifsList(GifsData gifsData);
    }


}
