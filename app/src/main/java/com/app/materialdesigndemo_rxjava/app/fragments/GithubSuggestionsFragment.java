package com.app.materialdesigndemo_rxjava.app.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.app.materialdesigndemo_rxjava.app.R;
import com.app.materialdesigndemo_rxjava.app.model.GithubUsers;
import com.app.materialdesigndemo_rxjava.app.presenters.GithubSuggestionsFragmentPresenter;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.subscriptions.CompositeSubscription;


public class GithubSuggestionsFragment extends Fragment implements GithubSuggestionsFragmentPresenter.GithubSuggestionsFragmentPresenterInterface {
	@Bind(R.id.first_user_name)
	TextView firstUserNameTextView;

	@Bind(R.id.second_user_name)
	TextView secondUserNameTextView;

	@Bind(R.id.third_user_name)
	TextView thirdUserNameTextView;

	@OnClick(R.id.refresh_button)
	public void makeCall() {
		showUsers();
	}


	CompositeSubscription compositeSubscription;
	GithubSuggestionsFragmentPresenter githubSuggestionsFragmentPresenter;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		compositeSubscription = new CompositeSubscription();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_github, container, false);
		ButterKnife.bind(this, view);
		githubSuggestionsFragmentPresenter = new GithubSuggestionsFragmentPresenter(this,compositeSubscription);
		showUsers();
		return view;
	}

	private void showUsers() {
		githubSuggestionsFragmentPresenter.getGithubUsers();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		ButterKnife.unbind(this);
		githubSuggestionsFragmentPresenter.onDestroyView();
	}

	public static GithubSuggestionsFragment newInstance() {
		return new GithubSuggestionsFragment();
	}

	@Override
	public void setUserDetails(GithubUsers githubUsers) {
		firstUserNameTextView.setText(githubUsers.login);
		secondUserNameTextView.setText(githubUsers.login);
		thirdUserNameTextView.setText(githubUsers.login);
	}
}
