package com.app.materialdesigndemo_rxjava.app.presenters;


import com.app.materialdesigndemo_rxjava.app.model.GithubUsers;
import com.app.materialdesigndemo_rxjava.app.network.RestWebClient;

import java.util.List;
import java.util.Random;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class GithubSuggestionsFragmentPresenter {
	GithubSuggestionsFragmentPresenterInterface githubSuggestionsFragmentPresenterInterface;
	CompositeSubscription compositeSubscription;
	Observable<List<GithubUsers>> cachedObservable;

	public GithubSuggestionsFragmentPresenter(GithubSuggestionsFragmentPresenterInterface githubSuggestionsFragmentPresenterInterface, CompositeSubscription compositeSubscription) {
		this.githubSuggestionsFragmentPresenterInterface = githubSuggestionsFragmentPresenterInterface;
		this.compositeSubscription = compositeSubscription;
	}

	public void getGithubUsers() {
		cachedObservable = RestWebClient.get().getGithubUsers(generateRandomYear()).
				cache();
		compositeSubscription.add(cachedObservable.
				flatMap(githubUsersList -> Observable.from(githubUsersList)).
				take(3).
				toList().
				subscribeOn(Schedulers.newThread()).
				observeOn(AndroidSchedulers.mainThread()).
				subscribe(githubUsers -> githubSuggestionsFragmentPresenterInterface.setUserDetails(githubUsers)));
	}
	public void getRandomUser(int position){
		Random r = new Random();
		int Low = 0;
		int High = 30;
		int Result = r.nextInt(High-Low) + Low;
		compositeSubscription.add(cachedObservable.flatMap(githubUsersList1 -> Observable.from(githubUsersList1))
		.elementAt(Result).subscribe(githubUsersList -> githubSuggestionsFragmentPresenterInterface.setRandomUser(position, githubUsersList)));
	}

	private String generateRandomYear() {
		return Double.toString(Math.floor(Math.random() * 500));
	}

	public void onDestroyView() {
		compositeSubscription.unsubscribe();
	}

	public interface GithubSuggestionsFragmentPresenterInterface {

		void setUserDetails(List<GithubUsers> githubUsers);

		void setRandomUser(int position, GithubUsers githubUsers);

	}
}
