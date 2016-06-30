package com.app.materialdesigndemo_rxjava.app.presenters;

import android.graphics.Color;
import android.text.TextUtils;
import android.widget.EditText;

import com.jakewharton.rxbinding.widget.RxTextView;
import static android.util.Patterns.EMAIL_ADDRESS;


public class LoginFragmentPresenter {
	LoginFragmentPresenterInterface loginFragmentPresenterInterface;

	public LoginFragmentPresenter(LoginFragmentPresenterInterface loginFragmentPresenterInterface) {
		this.loginFragmentPresenterInterface = loginFragmentPresenterInterface;

	}

	public void setupListenersForViews(EditText usernameEditText, EditText passwordEditText) {
		RxTextView.textChanges(usernameEditText).
				map(charSequence -> charSequence.toString()).
				map(s -> !TextUtils.isEmpty(s) && EMAIL_ADDRESS.matcher(s).matches()).
				map(aBoolean -> aBoolean ? Color.BLUE : Color.RED).
				subscribe(integer -> loginFragmentPresenterInterface.setEmailTextColor(integer));

		RxTextView.textChanges(passwordEditText).
				map(CharSequence::toString).
				map(s -> !TextUtils.isEmpty(s) && s.length()>4).
				map(aBoolean -> aBoolean ? Color.BLUE : Color.RED).
				subscribe(integer -> loginFragmentPresenterInterface.setPasswordTextColor(integer));

	}

	public interface LoginFragmentPresenterInterface {
		void setEmailTextColor(int color);

		void setPasswordTextColor(int color);
	}
}
