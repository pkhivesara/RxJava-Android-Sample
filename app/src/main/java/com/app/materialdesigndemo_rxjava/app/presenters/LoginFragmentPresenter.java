package com.app.materialdesigndemo_rxjava.app.presenters;

import android.database.Observable;
import android.graphics.Color;
import android.graphics.Interpolator;
import android.text.TextUtils;
import android.widget.EditText;

import com.jakewharton.rxbinding.widget.RxTextView;

import rx.Subscriber;
import rx.functions.Func1;
import rx.functions.Func2;

import static android.util.Patterns.EMAIL_ADDRESS;


public class LoginFragmentPresenter {
	LoginFragmentPresenterInterface loginFragmentPresenterInterface;

	public LoginFragmentPresenter(LoginFragmentPresenterInterface loginFragmentPresenterInterface) {
		this.loginFragmentPresenterInterface = loginFragmentPresenterInterface;

	}

	public void setupListenersForViews(EditText usernameEditText, EditText passwordEditText) {
		rx.Observable<Boolean> userNameObservableValid = RxTextView.textChanges(usernameEditText).
				map(new Func1<CharSequence, String>() {
					@Override
					public String call(CharSequence charSequence) {
						return charSequence.toString();
					}
				}).
				map(new Func1<String, Boolean>() {
					@Override
					public Boolean call(String charSequence) {
						return !TextUtils.isEmpty(charSequence) && EMAIL_ADDRESS.matcher(charSequence).matches();
					}
				});
		userNameObservableValid.map(new Func1<Boolean, Integer>() {
			@Override
			public Integer call(Boolean aBoolean) {
				return aBoolean ? Color.BLUE : Color.RED;
			}
		}).subscribe(integer1 -> loginFragmentPresenterInterface.setEmailTextColor(integer1));

		rx.Observable<Boolean> passwordObservableValid = RxTextView.textChanges(passwordEditText).
				map(new Func1<CharSequence, String>() {
					@Override
					public String call(CharSequence charSequence) {
						return charSequence.toString();
					}
				}).
				map(new Func1<String, Boolean>() {
					@Override
					public Boolean call(String s) {
						return !TextUtils.isEmpty(s) && s.length() > 4;

					}
				});

		passwordObservableValid.map(new Func1<Boolean, Integer>() {
			@Override
			public Integer call(Boolean aBoolean) {
				return aBoolean ? Color.BLUE : Color.RED;
			}
		}).subscribe(integer -> loginFragmentPresenterInterface.setPasswordTextColor(integer));

		rx.Observable.combineLatest(userNameObservableValid, passwordObservableValid, new Func2<Boolean, Boolean, Boolean>() {

			@Override
			public Boolean call(Boolean aBoolean, Boolean aBoolean2) {
				return aBoolean && aBoolean2;
			}

		}).subscribe(new Subscriber<Boolean>() {
			@Override
			public void onCompleted() {

			}

			@Override
			public void onError(Throwable e) {

			}

			@Override
			public void onNext(Boolean aBoolean) {
				loginFragmentPresenterInterface.handleButtonState(aBoolean);
			}
		});
	}

	public interface LoginFragmentPresenterInterface {
		void setEmailTextColor(int color);

		void setPasswordTextColor(int color);

		void handleButtonState(boolean value);
	}
}
