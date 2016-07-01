package com.app.materialdesigndemo_rxjava.app.presenters;


import com.app.materialdesigndemo_rxjava.app.model.GithubUsers;
import com.app.materialdesigndemo_rxjava.app.network.RestWebClient;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.internal.schedulers.ScheduledAction;
import rx.schedulers.Schedulers;

public class GithubSuggestionsFragmentPresenter {
	GithubSuggestionsFragmentPresenterInterface githubSuggestionsFragmentPresenterInterface;

	public GithubSuggestionsFragmentPresenter(GithubSuggestionsFragmentPresenterInterface githubSuggestionsFragmentPresenterInterface) {
		this.githubSuggestionsFragmentPresenterInterface = githubSuggestionsFragmentPresenterInterface;
	}

	public void getGithubUsers() {
		RestWebClient.get().getGithubUsers(generateRandomYear()).
				flatMap(githubUsersList -> Observable.from(githubUsersList)).
				take(3).
				subscribeOn(Schedulers.newThread()).
				observeOn(AndroidSchedulers.mainThread()).
				subscribe(githubUsers -> githubSuggestionsFragmentPresenterInterface.setUserDetails(githubUsers));
	}

	private String generateRandomYear(){
		return Double.toString(Math.floor(Math.random() * 500));
	}

	public interface GithubSuggestionsFragmentPresenterInterface {

		void setUserDetails(GithubUsers githubUsers);

	}
}
