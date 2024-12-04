package com.xtree.lottery.ui.viewmodel.factory;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;


import com.xtree.base.utils.CfLog;
import com.xtree.lottery.data.LotteryRepository;
import com.xtree.lottery.data.Injection;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import me.xtree.mvvmhabit.base.BaseModel;

public class AppViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    @SuppressLint("StaticFieldLeak")
    private static volatile AppViewModelFactory INSTANCE;
    private static Application mApplication;
    private final BaseModel mRepository;

    public static AppViewModelFactory getInstance(Application application) {
        if (INSTANCE == null) {
            synchronized (AppViewModelFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AppViewModelFactory(application, Injection.provideMainRepository(false));
                }
            }
        }
        return INSTANCE;
    }

    @VisibleForTesting
    public static void destroyInstance() {
        INSTANCE = null;
    }

    private AppViewModelFactory(Application application, LotteryRepository repository) {
        this.mApplication = application;
        this.mRepository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        try {
            Class<T> clazz = (Class<T>) Class.forName(modelClass.getName());
            Constructor constructor = clazz.getConstructor(Application.class, LotteryRepository.class);
            return (T) constructor.newInstance(mApplication, mRepository);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                 InstantiationException | InvocationTargetException e) {
            return null;
        }
    }

    public static void init() {
        synchronized (AppViewModelFactory.class) {
            INSTANCE = new AppViewModelFactory(mApplication, Injection.provideMainRepository(true));
            CfLog.e("AppViewModelFactory init");
        }
    }

    public BaseModel getmRepository() {
        return mRepository;
    }
}
