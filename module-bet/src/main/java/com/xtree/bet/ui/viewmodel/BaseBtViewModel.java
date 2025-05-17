package com.xtree.bet.ui.viewmodel;

import static com.xtree.bet.ui.activity.MainActivity.KEY_PLATFORM;
import static com.xtree.bet.ui.activity.MainActivity.PLATFORM_FBXC;
import static com.xtree.bet.ui.activity.MainActivity.PLATFORM_PM;
import static com.xtree.bet.ui.activity.MainActivity.PLATFORM_PMXC;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.net.FBHttpCallBack;
import com.xtree.base.net.HttpCallBack;
import com.xtree.base.net.PMHttpCallBack;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.NumberUtils;
import com.xtree.base.vo.BalanceVo;
import com.xtree.base.vo.FBService;
import com.xtree.base.vo.PMService;
import com.xtree.bet.bean.response.fb.BalanceInfo;
import com.xtree.bet.data.BetRepository;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.bus.event.SingleLiveData;
import me.xtree.mvvmhabit.http.BaseResponse;
import me.xtree.mvvmhabit.utils.RxUtils;
import me.xtree.mvvmhabit.utils.SPUtils;

/**
 * Created by marquis
 */

public class BaseBtViewModel extends BaseViewModel<BetRepository> {
    public SingleLiveData<String> userBalanceData = new SingleLiveData<>();
    public SingleLiveData<Void> tokenInvalidEvent = new SingleLiveData<>();

    public BaseBtViewModel(@NonNull Application application, BetRepository model) {
        super(application, model);
    }

    public void getUserBalance() {

        Disposable disposable = (Disposable) model.getBaseApiService().getBalance()
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<BalanceVo>() {
                    @Override
                    public void onResult(BalanceVo vo) {
                        CfLog.d(vo.toString());
                        SPUtils.getInstance().put(SPKeyGlobal.WLT_CENTRAL_BLC, vo.balance);
                        userBalanceData.postValue(NumberUtils.formatDown(Double.valueOf(vo.balance), 2));
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    public void getGameTokenApi() {
        String mPlatform = SPUtils.getInstance().getString(KEY_PLATFORM);
        if (!TextUtils.equals(mPlatform, PLATFORM_PM) && !TextUtils.equals(mPlatform, PLATFORM_PMXC)) {
            getFBGameTokenApi();
        } else {
            getPMGameTokenApi();
        }
    }

    public void getFBGameTokenApi() {
        Flowable<BaseResponse<FBService>> flowable;
        String mPlatform = SPUtils.getInstance().getString(KEY_PLATFORM);
        if (TextUtils.equals(mPlatform, PLATFORM_FBXC)) {
            flowable = model.getBaseApiService().getFBXCGameTokenApi();
        } else {
            flowable = model.getBaseApiService().getFBGameTokenApi();
        }
        Disposable disposable = (Disposable) flowable
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<FBService>() {
                    @Override
                    public void onResult(FBService fbService) {
                        if (TextUtils.equals(mPlatform, PLATFORM_FBXC)) {
                            SPUtils.getInstance().put(SPKeyGlobal.FBXC_TOKEN, fbService.getToken());
                            SPUtils.getInstance().put(SPKeyGlobal.FBXC_API_SERVICE_URL, fbService.getForward().getApiServerAddress());
                        } else {
                            SPUtils.getInstance().put(SPKeyGlobal.FB_TOKEN, fbService.getToken());
                            SPUtils.getInstance().put(SPKeyGlobal.FB_API_SERVICE_URL, fbService.getForward().getApiServerAddress());
                        }

                        tokenInvalidEvent.call();
                    }

                    @Override
                    public void onError(Throwable t) {
                        //super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    public void getPMGameTokenApi() {
        Flowable<BaseResponse<PMService>> flowable;
        String mPlatform = SPUtils.getInstance().getString(KEY_PLATFORM);
        if (TextUtils.equals(mPlatform, PLATFORM_PMXC)) {
            flowable = model.getBaseApiService().getPMXCGameTokenApi();
        } else {
            flowable = model.getBaseApiService().getPMGameTokenApi();
        }
        Disposable disposable = (Disposable) flowable
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<PMService>() {
                    @Override
                    public void onResult(PMService pmService) {
                        if (TextUtils.equals(mPlatform, PLATFORM_PMXC)) {
                            SPUtils.getInstance().put(SPKeyGlobal.PMXC_TOKEN, pmService.getToken());
                            SPUtils.getInstance().put(SPKeyGlobal.PMXC_API_SERVICE_URL, pmService.getApiDomain());
                            SPUtils.getInstance().put(SPKeyGlobal.PMXC_IMG_SERVICE_URL, pmService.getImgDomain());
                            SPUtils.getInstance().put(SPKeyGlobal.PMXC_USER_ID, pmService.getUserId());
                        } else {
                            SPUtils.getInstance().put(SPKeyGlobal.PM_TOKEN, pmService.getToken());
                            SPUtils.getInstance().put(SPKeyGlobal.PM_API_SERVICE_URL, pmService.getApiDomain());
                            SPUtils.getInstance().put(SPKeyGlobal.PM_IMG_SERVICE_URL, pmService.getImgDomain());
                            SPUtils.getInstance().put(SPKeyGlobal.PM_USER_ID, pmService.getUserId());
                        }
                        tokenInvalidEvent.call();
                    }

                    @Override
                    public void onError(Throwable t) {
                        //super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

}
