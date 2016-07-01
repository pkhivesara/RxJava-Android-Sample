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
	@Bind(R.id.textView)
	TextView textView;

	@OnClick(R.id.refresh_button)
	public void makeCall() {
		showUsers();
	}


	CompositeSubscription compositeSubscription;
	StringBuilder stringBuilder;
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
		stringBuilder = new StringBuilder();
		ButterKnife.bind(this, view);
		githubSuggestionsFragmentPresenter = new GithubSuggestionsFragmentPresenter(this,compositeSubscription);
		showUsers();
		return view;
	}

	private void showUsers() {
		stringBuilder.setLength(0);
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
		stringBuilder.append(githubUsers.login).append(" ");
		textView.setText(stringBuilder);
	}
}
