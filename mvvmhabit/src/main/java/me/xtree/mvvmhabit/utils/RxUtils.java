package me.xtree.mvvmhabit.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;

import com.trello.rxlifecycle4.LifecycleProvider;
import com.trello.rxlifecycle4.LifecycleTransformer;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;
import me.xtree.mvvmhabit.http.BaseResponse;
import me.xtree.mvvmhabit.http.ExceptionHandle;
import me.xtree.mvvmhabit.http.ResponseThrowable;

/**
 * Created by goldze on 2017/6/19.
 * 有关Rx的工具类
 */
public class RxUtils {
    /**
     * 生命周期绑定
     *
     * @param lifecycle Activity
     */
    public static <T> LifecycleTransformer<T> bindToLifecycle(@NonNull Context lifecycle) {
        if (lifecycle instanceof LifecycleProvider) {
            return ((LifecycleProvider) lifecycle).bindToLifecycle();
        } else {
            throw new IllegalArgumentException("context not the LifecycleProvider type");
        }
    }

    /**
     * 生命周期绑定
     *
     * @param lifecycle Fragment
     */
    public static LifecycleTransformer bindToLifecycle(@NonNull Fragment lifecycle) {
        if (lifecycle instanceof LifecycleProvider) {
            return ((LifecycleProvider) lifecycle).bindToLifecycle();
        } else {
            throw new IllegalArgumentException("fragment not the LifecycleProvider type");
        }
    }

    /**
     * 生命周期绑定
     *
     * @param lifecycle Fragment
     */
    public static LifecycleTransformer bindToLifecycle(@NonNull LifecycleProvider lifecycle) {
        return lifecycle.bindToLifecycle();
    }

    /**
     * 线程调度器
     */
    public static <T> ObservableTransformer<T, T> schedulersTransformerObs() {
        return upstream -> upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }


    public static FlowableTransformer schedulersTransformer() {
        return upstream -> upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }


    public static io.reactivex.rxjava3.core.FlowableTransformer schedulersTransformer1() {
        return upstream -> upstream.subscribeOn(io.reactivex.rxjava3.schedulers.Schedulers.io()).observeOn(io.reactivex.rxjava3.android.schedulers.AndroidSchedulers.mainThread());

    }

    public static <T> ObservableTransformer<T, T> exceptionTransformerObservable() {
        return new ObservableErrorTransformer();
    }

    private static class ErrorTransformer<T> implements FlowableTransformer {
        @Override
        public Publisher apply(Flowable upstream) {
            //onErrorResumeNext当发生错误的时候，由另外一个Observable来代替当前的Observable并继续发射数据
            return (Flowable<T>) upstream
                    //.map(new HandleFuc<T>())
                    .onErrorResumeNext(new HttpResponseFunc<T>());
        }
    }

    public static FlowableTransformer exceptionTransformer() {
        return new ErrorTransformer();
    }


    public static <T> FlowableTransformer<T, T> schedulersTransformer2() {
        return upstream -> upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }


    private static class ObservableErrorTransformer<T> implements ObservableTransformer {

        @Override
        public ObservableSource apply(Observable upstream) {
            //onErrorResumeNext当发生错误的时候，由另外一个Observable来代替当前的Observable并继续发射数据
            return (Observable<T>) upstream.map(new HandleFuc<T>()).onErrorResumeNext(new HttpResponseFunc<T>());
        }
    }

    private static class HttpResponseFuncObservable<T> implements Function<Throwable, Observable<T>> {
        @Override
        public Observable<T> apply(Throwable t) {
            return Observable.error(ExceptionHandle.handleException(t));
        }
    }

    private static class HttpResponseFunc<T> implements Function<Throwable, Flowable<T>> {
        @Override
        public Flowable<T> apply(Throwable t) {
            return Flowable.error(ExceptionHandle.handleException(t));
        }
    }

    private static class HandleFuc<T> implements Function<BaseResponse<T>, T> {
        @Override
        public T apply(BaseResponse<T> response) {
            if (!response.isOk())
                throw new RuntimeException(!"".equals(response.getStatus() + "" + response.getMessage()) ? response.getMessage() : "");
            return response.getData();
        }
    }


    public static <T> void safeSubscribe(Flowable<BaseResponse<T>> flowable, CompositeDisposable compositeDisposable, DisposableSubscriber<T> subscriber) {
        Disposable disposable = flowable.compose(applySchedulersAndUnwrap())
                .subscribeWith(subscriber);
        compositeDisposable.add(disposable);
    }

    public static <T> FlowableTransformer<BaseResponse<T>, T> applySchedulersAndUnwrap() {
        return upstream ->
                upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .flatMap(response -> {
                            if (response.isOk()) {
                                return Flowable.just(response.getData());
                            } else {
                                return Flowable.error(new ResponseThrowable(response.getCode(), response.getMessage()));
                            }
                        }).onErrorResumeNext(new HttpResponseFunc<>());
    }


}
