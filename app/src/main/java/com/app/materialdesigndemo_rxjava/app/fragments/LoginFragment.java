package com.app.materialdesigndemo_rxjava.app.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.app.materialdesigndemo_rxjava.app.R;
import com.app.materialdesigndemo_rxjava.app.presenters.LoginFragmentPresenter;
import butterknife.Bind;
import butterknife.ButterKnife;


public class LoginFragment extends android.support.v4.app.Fragment implements LoginFragmentPresenter.LoginFragmentPresenterInterface {
LoginFragmentPresenter loginFragmentPresenter;

	@Bind(R.id.user_name_edit_text)
	EditText userNameEditText;

	@Bind(R.id.password_edit_text)
	EditText passwordEditText;

	@Bind(R.id.login_button)
	Button loginButton;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_login, container, false);

		ButterKnife.bind(this, view);
		setHasOptionsMenu(true);
		loginFragmentPresenter = new LoginFragmentPresenter(this);
		loginFragmentPresenter.setupListenersForViews(userNameEditText,passwordEditText);
		return view;
	}


	public static LoginFragment newInstance() {
		return new LoginFragment();
	}

	@Override
	public void setEmailTextColor(int color) {
		userNameEditText.setTextColor(color);
	}

	@Override
	public void setPasswordTextColor(int color) {
		passwordEditText.setTextColor(color);
	}

	@Override
	public void handleButtonState(boolean value) {
		loginButton.setEnabled(value);
	}
}
