package com.app.materialdesigndemo_rxjava.app.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.app.materialdesigndemo_rxjava.app.R;
import com.app.materialdesigndemo_rxjava.app.model.GithubUsers;
import com.app.materialdesigndemo_rxjava.app.presenters.GithubSuggestionsFragmentPresenter;
import com.app.materialdesigndemo_rxjava.app.presenters.LoginFragmentPresenter;
import com.app.materialdesigndemo_rxjava.app.presenters.MainFragmentPresenter;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class GithubSuggestionsFragment extends Fragment implements GithubSuggestionsFragmentPresenter.GithubSuggestionsFragmentPresenterInterface {
	@Bind(R.id.textView)
	TextView textView;



	@OnClick(R.id.refresh_button)
	public void makeCall(){
		showUsers();
	}

	StringBuilder stringBuilder;
	GithubSuggestionsFragmentPresenter githubSuggestionsFragmentPresenter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_github, container, false);
		stringBuilder = new StringBuilder();
		ButterKnife.bind(this, view);
		githubSuggestionsFragmentPresenter = new GithubSuggestionsFragmentPresenter(this);
		showUsers();
		return view;
	}

	private void showUsers() {
		stringBuilder.setLength(0);
		githubSuggestionsFragmentPresenter.getGithubUsers();
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
