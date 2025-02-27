package com.xtree.live.chat;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.xtree.live.data.source.httpnew.BaseRepository;
import com.xtree.live.data.source.httpnew.RepositoryManager;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

public  class Subscription {
    private static CompositeDisposable mCompositeDisposable;
    // RxJava取消注册，以避免内存泄露
    public void onUnSubscribe() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
        }
    }

    public static CompositeDisposable getComposite(){
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        return mCompositeDisposable;
    }

    public final RepositoryManager getApiStores(){
        return RepositoryManager.getInstance();
    }


    public <T>void addSubscription(Observable<T> observable, DisposableObserver<T> observer) {
        getComposite().add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(observer));
    }
    public <T>void addSubscription(Observable<T> single, Consumer<T> observer) {
        getComposite().add(single.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(observer));
    }



    public RequestBody getRequestBody(JSONObject jsonObject) {
        return RequestUtils.getRequestBody(jsonObject);
    }

    public RequestBody getRequestBody(JsonObject jsonObject) {
        return RequestUtils.getRequestBody(jsonObject);
    }

    public RequestBody getRequestBody(Map<String, Object> map) {
        return RequestUtils.getRequestBody(map);
    }

}

