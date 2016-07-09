package com.app.materialdesigndemo_rxjava.app.presenters;

import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import com.jakewharton.rxbinding.widget.RxTextView;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

import java.util.concurrent.TimeUnit;

import static android.util.Patterns.EMAIL_ADDRESS;


public class LoginFragmentPresenter {
    LoginFragmentPresenterInterface loginFragmentPresenterInterface;

    public LoginFragmentPresenter(LoginFragmentPresenterInterface loginFragmentPresenterInterface) {
        this.loginFragmentPresenterInterface = loginFragmentPresenterInterface;

    }

    public void setupListenersForViews(EditText usernameEditText, EditText passwordEditText) {

        /*Set email address field color based on boolean observable*/
        rx.Observable<Boolean> userNameObservableValid = RxTextView.textChanges(usernameEditText).
                map(CharSequence::toString).
                map(charSequence -> !TextUtils.isEmpty(charSequence) && EMAIL_ADDRESS.matcher(charSequence).matches());
        userNameObservableValid.map(aBoolean -> aBoolean ? Color.BLUE : Color.RED).subscribe(integer1 -> loginFragmentPresenterInterface.setEmailTextColor(integer1));

        /*Set password field color based on boolean observable*/
        rx.Observable<Boolean> passwordObservableValid = RxTextView.textChanges(passwordEditText).
                map(CharSequence::toString).
                map(s -> !TextUtils.isEmpty(s) && s.length() > 4);
        passwordObservableValid.map(aBoolean -> aBoolean ? Color.BLUE : Color.RED).subscribe(integer -> loginFragmentPresenterInterface.setPasswordTextColor(integer));


         /*Set button state based on an boolean observable from email and password observables*/
        rx.Observable.combineLatest(userNameObservableValid, passwordObservableValid, (aBoolean, aBoolean2) -> aBoolean && aBoolean2)
                .subscribe(aBoolean1 -> loginFragmentPresenterInterface.handleButtonState(aBoolean1));
    }

    public void makeADummyCallToTestRetryWhenOperator(){
        Observable<String> dummyResponse = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("Dummy call made");
                subscriber.onNext("Dummy call number 2 made");
                subscriber.onError(new IllegalStateException());
            }
        })

        .retryWhen(new Func1<Observable<? extends Throwable>, Observable<?>>() {
            @Override
            public Observable<?> call(Observable<? extends Throwable> observable) {
              return observable.flatMap(new Func1<Throwable, Observable<?>>() {
                  @Override
                  public Observable<?> call(Throwable throwable) {
                      if(throwable instanceof IllegalStateException){
                          return Observable.just(new Object());
                      }else{
                          return Observable.error(throwable);
                      }
                  }
              }).take(4);
            }
        });

        dummyResponse.subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Log.d("#######","OnCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.d("#######","OnError:  + "+ e);
            }

            @Override
            public void onNext(String s) {
                Log.d("#######",s);
            }
        });
    }

    public interface LoginFragmentPresenterInterface {
        void setEmailTextColor(int color);

        void setPasswordTextColor(int color);

        void handleButtonState(boolean value);
    }
}
