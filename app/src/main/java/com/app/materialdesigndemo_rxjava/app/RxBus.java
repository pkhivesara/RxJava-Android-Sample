package com.app.materialdesigndemo_rxjava.app;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

public class RxBus {

	private final Subject<Object, Object> rxBus = new SerializedSubject<>(PublishSubject.create());

	public Observable<Object> toObservable() {
		return rxBus;
	}
	public void post(Object event) {
		rxBus.onNext(event);
	}
}
