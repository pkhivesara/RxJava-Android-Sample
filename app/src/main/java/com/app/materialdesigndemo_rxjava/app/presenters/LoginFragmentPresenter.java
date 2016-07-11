package com.app.materialdesigndemo_rxjava.app.presenters;

import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import com.jakewharton.rxbinding.widget.RxTextView;
import rx.Observable;
import rx.Subscriber;
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



    /*Java 8 style implementation of retrywhen operator

    In the following method the retryWhen is executed because:
   * 1. The input observable triggers the onError callback.
   * 2. The resultant observable emits a value successfully since the throwable IS A instance of IllegalStateException */
    public void makeADummyCallToTestRetryWhenOperator() {
        Observable<String> dummyResponse = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("Dummy call made");
                subscriber.onNext("Dummy call number 2 made");
                subscriber.onError(new IllegalStateException());
            }
        }).retryWhen(observable -> observable.flatMap(o -> o instanceof IllegalStateException ?
                Observable.just(new Object()) : Observable.error(o)).take(4));

        subscribeToDummyObservable(dummyResponse);
    }


    private void subscribeToDummyObservable(Observable<String> dummyResponse) {
        dummyResponse.subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Log.d("#######", "OnCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.d("#######", "OnError:  + " + e);
            }

            @Override
            public void onNext(String s) {
                Log.d("#######", s);
            }
        });
    }

    public interface LoginFragmentPresenterInterface {
        void setEmailTextColor(int color);

        void setPasswordTextColor(int color);

        void handleButtonState(boolean value);
    }

    /*
    In the following method the retryWhen is not executed because of:
   * 1. The repeatWhen function terminates with a error notification, which gets triggered since throwable IS NOT an instanceOf
   * NetworkOnMainThreadException(). If the instanceOf check is against IllegalStateException, then retryWhen operator does execute,
   * since the resultant observable emits a value.

    public void makeADummyCallToTestRetryWhenOperator() {
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
                                if (throwable instanceof NetworkOnMainThreadException) {
                                    return Observable.just(new Object());
                                } else {
                                    return Observable.error(throwable);
                                }
                            }
                        }).take(4);
                    }
                });
        subscribeToDummyObservable(dummyResponse);
    }

    */

    /*In the following method the repeatWhen is executed because of two reasons:
   * 1. The observable.create triggers with the onCompleted callback.
   * 2. The resultant observable emits a value after a delay of 200 millisecond.
    public void makeADummyCallToTestRepeatWhenIsExecutedSuccessfully() {
        Observable<String> dummyResponse = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("Dummy call made");
                subscriber.onNext("Dummy call number 2 made");
                subscriber.onCompleted();
            }
        }).repeatWhen(observable -> RestWebClient.get().getGithubUsers("2009").delay(200, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.newThread()));

        subscribeToDummyObservable(dummyResponse);
    }
    */

    /*In the following method the repeatWhen is never executed because of two reasons:
    * 1. The observable.create triggers the onError callback.
    * 2. The resultant observable is terminated with an error callback.
    public void makeADummyCallToTestRepeatWhenIsUnSuccessfulBecauseOfTwoError() {
        Observable<String> dummyResponse = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("Dummy call made");
                subscriber.onNext("Dummy call number 2 made");
                subscriber.onError(new IllegalStateException());
            }
        }).repeatWhen(observable -> Observable.error(new IllegalStateException()));

        subscribeToDummyObservable(dummyResponse);
    }
    */

    /*In the following method the repeatWhen is never executed because:
    * 1. The resultant observable is terminated with an error callback, despite the input observable executing the onCompleted callback.
    public void makeADummyCallToTestRepeatWhenIsUnSuccessful() {
        Observable<String> dummyResponse = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("Dummy call made");
                subscriber.onNext("Dummy call number 2 made");
                subscriber.onCompleted();
            }
        }).repeatWhen(observable -> Observable.error(new IllegalStateException()));

        subscribeToDummyObservable(dummyResponse);
    }
    */

}
