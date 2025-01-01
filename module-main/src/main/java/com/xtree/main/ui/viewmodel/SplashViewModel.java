package com.xtree.main.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.net.HttpCallBack;
import com.xtree.base.utils.CfLog;
import com.xtree.base.vo.FBService;
import com.xtree.base.vo.PMService;
import com.xtree.main.data.MainRepository;

import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.http.BusinessException;
import me.xtree.mvvmhabit.utils.RxUtils;
import me.xtree.mvvmhabit.utils.SPUtils;

/**
 * Created by marquis on 2023/11/24.
 */
public class SplashViewModel extends BaseViewModel<MainRepository> {
    public MutableLiveData<Void> reNewViewModel = new MutableLiveData<>();
    public MutableLiveData<Void> noWebData = new MutableLiveData<>();

    public SplashViewModel(@NonNull Application application, MainRepository model) {
        super(application, model);
    }


}
