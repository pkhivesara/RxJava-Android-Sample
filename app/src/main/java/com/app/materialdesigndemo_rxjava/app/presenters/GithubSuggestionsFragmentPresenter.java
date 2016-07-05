package com.app.materialdesigndemo_rxjava.app.presenters;


import com.app.materialdesigndemo_rxjava.app.model.GithubUsers;
import com.app.materialdesigndemo_rxjava.app.network.RestWebClient;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class GithubSuggestionsFragmentPresenter {
	GithubSuggestionsFragmentPresenterInterface githubSuggestionsFragmentPresenterInterface;
	CompositeSubscription compositeSubscription;

	public GithubSuggestionsFragmentPresenter(GithubSuggestionsFragmentPresenterInterface githubSuggestionsFragmentPresenterInterface, CompositeSubscription compositeSubscription) {
		this.githubSuggestionsFragmentPresenterInterface = githubSuggestionsFragmentPresenterInterface;
		this.compositeSubscription = compositeSubscription;
	}

	public void getGithubUsers() {
		compositeSubscription.add(RestWebClient.get().getGithubUsers(generateRandomYear()).
				take(3).
				subscribeOn(Schedulers.newThread()).
				observeOn(AndroidSchedulers.mainThread()).
				subscribe(githubUsers -> githubSuggestionsFragmentPresenterInterface.setUserDetails(githubUsers)));
	}

	private String generateRandomYear() {
		return Double.toString(Math.floor(Math.random() * 500));
	}

	public void onDestroyView() {
		compositeSubscription.unsubscribe();
	}

	public interface GithubSuggestionsFragmentPresenterInterface {

		void setUserDetails(List<GithubUsers> githubUsers);

	}
}
