package com.xtree.bet.ui.viewmodel.factory;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.xtree.bet.data.BetRepository;
import com.xtree.bet.data.PMInjection;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import me.xtree.mvvmhabit.base.BaseModel;

/**
 * Created by marquis on 2023/11/22.
 */
public class IMAppViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    @SuppressLint("StaticFieldLeak")
    private static volatile IMAppViewModelFactory INSTANCE;
    private static Application mApplication;
    private final BetRepository mRepository;

    public static IMAppViewModelFactory getInstance(Application application) {
        if (INSTANCE == null) {
            synchronized (IMAppViewModelFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new IMAppViewModelFactory(application, PMInjection.provideHomeRepository(false));
                }
            }
        }
        return INSTANCE;
    }

    @VisibleForTesting
    public static void destroyInstance() {
        INSTANCE = null;
    }

    private IMAppViewModelFactory(Application application, BetRepository repository) {
        this.mApplication = application;
        this.mRepository = repository;
    }

    public static void init() {
        synchronized (IMAppViewModelFactory.class) {
            INSTANCE = new IMAppViewModelFactory(mApplication, PMInjection.provideHomeRepository(true));
        }
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        try {
            Class<T> clazz = (Class<T>) Class.forName(modelClass.getName());
            Constructor constructor = clazz.getConstructor(Application.class, BetRepository.class);
            return (T) constructor.newInstance(mApplication, mRepository);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                 InstantiationException | InvocationTargetException e) {
            return null;
        }
    }

    public BaseModel getRepository() {
        return mRepository;
    }
}
