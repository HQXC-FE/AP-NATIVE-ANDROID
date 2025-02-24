package com.xtree.live.ui.main.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;

import com.xtree.live.data.LiveRepository;

import me.xtree.mvvmhabit.base.BaseViewModel;

// 这里必须要使用 原来的 LiveRepository ，不然即使使用 BaseModel也会崩溃。没用使用也不能删掉，否则崩溃
public class LiveDetailHomeViewModel extends BaseViewModel<LiveRepository> {
    public LiveDetailHomeViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveDetailHomeViewModel(@NonNull Application application, LiveRepository model) {
        super(application, model);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCleared() {
        super.onCleared();

    }

    

}
