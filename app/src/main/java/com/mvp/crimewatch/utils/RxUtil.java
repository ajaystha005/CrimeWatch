package com.mvp.crimewatch.utils;

import java.util.concurrent.Callable;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by ajayshrestha on 3/18/17.
 */

public class RxUtil {


    public static <T> Observable<T> makeObservable(final Callable<T> func) {
        return Observable.create(
                new Observable.OnSubscribe<T>() {
                    @Override
                    public void call(Subscriber<? super T> subscriber) {
                        try {
                            T observed = func.call();
                            if (null != observed) {
                                subscriber.onNext(observed);
                            }
                            subscriber.onCompleted();
                        } catch (Exception ex) {
                            subscriber.onError(ex);
                        }
                    }
                });
    }
}
