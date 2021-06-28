package com.arvin.bracelet.common.rx;

import androidx.annotation.NonNull;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class RxCallback<T> implements Observer<T> {

    @Override
    public void onSubscribe(@NonNull Disposable d) {
    }

    @Override
    public abstract void onNext(@NonNull T t);

    @Override
    public void onError(@NonNull Throwable e) {
    }

    @Override
    public void onComplete() {
    }

    public void onFinally() {
    }


    public static <T> void onSubscribe(final RxCallback<T> callback, @NonNull Disposable d) {
        if (callback != null) {
            callback.onSubscribe(d);
        }
    }

    public static <T> void onNext(final RxCallback<T> callback, @NonNull T t) {
        if (callback != null) {
            callback.onNext(t);
        }
    }

    public static <T> void onError(final RxCallback<T> callback, @NonNull Throwable e) {
        if (callback != null) {
            callback.onError(e);
        }
    }

    public static <T> void onComplete(final RxCallback<T> callback) {
        if (callback != null) {
            callback.onComplete();
        }
    }

    public static <T> void onFinally(final RxCallback<T> callback) {
        if (callback != null) {
            callback.onFinally();
        }
    }
}