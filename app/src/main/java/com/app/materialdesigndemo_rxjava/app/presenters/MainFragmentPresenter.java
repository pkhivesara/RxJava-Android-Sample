package com.app.materialdesigndemo_rxjava.app.presenters;

import android.support.v7.widget.SearchView;
import android.util.Pair;
import com.app.materialdesigndemo_rxjava.app.model.GifsData;
import com.app.materialdesigndemo_rxjava.app.model.RandomGifs;
import com.app.materialdesigndemo_rxjava.app.network.RestWebClient;
import com.jakewharton.rxbinding.support.v7.widget.RxSearchView;

import rx.android.schedulers.AndroidSchedulers;

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

    private void makeRetrofitCall() {
        RestWebClient.get().getTrendingGifs("dc6zaTOxFJmzC")
                .flatMap(gifsData1 -> RestWebClient.get().getRandomGifs("dc6zaTOxFJmzC"), Pair::create)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(gifsDataRandomGifsPair -> mainFragmentPresenterInterface.showTrendingList(gifsDataRandomGifsPair));

    }


    public void startDebounceListenerForSearch(SearchView searchView) {
        RxSearchView.queryTextChangeEvents(searchView)
                .debounce(400, TimeUnit.MILLISECONDS, Schedulers.newThread())
                .skip(2)
                .map(searchViewQueryTextEvent -> searchViewQueryTextEvent.queryText().toString())
                .observeOn(Schedulers.newThread())
                .flatMap(s -> RestWebClient.get().getSearchedGifs(s, "dc6zaTOxFJmzC"))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(gifsData -> mainFragmentPresenterInterface.displaySearchedGifsList(gifsData));

    }

    public interface MainFragmentPresenterInterface {
        void showTrendingList(Pair<GifsData, RandomGifs> gifsData);

        void displaySearchedGifsList(GifsData gifsData);
    }


}
