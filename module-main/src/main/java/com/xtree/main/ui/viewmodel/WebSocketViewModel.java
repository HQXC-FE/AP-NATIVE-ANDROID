package com.xtree.main.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.xtree.base.net.HttpCallBack;
import com.xtree.base.utils.CfLog;
import com.xtree.base.vo.MsgPersonInfoVo;
import com.xtree.base.vo.WsToken;
import com.xtree.main.data.MainRepository;

import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.bus.event.SingleLiveData;
import me.xtree.mvvmhabit.utils.RxUtils;

public class WebSocketViewModel extends BaseViewModel<MainRepository> {
    public SingleLiveData<WsToken> getWsTokenLiveData = new SingleLiveData<>();
    public MutableLiveData<MsgPersonInfoVo> liveDataMsgPersonInfo = new MutableLiveData<>();

    public WebSocketViewModel(@NonNull Application application) {
        super(application);
    }

    public WebSocketViewModel(@NonNull Application application, MainRepository model) {
        super(application, model);
    }

    public void getWsToken() {
        Disposable disposable = (Disposable) model.getApiService().getWsToken()
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<WsToken>() {
                    @Override
                    public void onResult(WsToken wsToken) {
                        CfLog.i("wsToken****** " + wsToken);
                        getWsTokenLiveData.setValue(wsToken);
                    }

                    @Override
                    public void onError(Throwable t) {
//                        CfLog.e(t.toString());
                    }
                });
        addSubscribe(disposable);
    }

    public void getMessagePerson(String id) {
        Disposable disposable = (Disposable) model.getApiService().getMessagePerson(id)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<MsgPersonInfoVo>() {
                    @Override
                    public void onResult(MsgPersonInfoVo vo) {
//                        CfLog.i(vo.toString());
                        liveDataMsgPersonInfo.setValue(vo);
                    }

                    @Override
                    public void onError(Throwable t) {
//                        CfLog.e("error, " + t.toString());
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }
}
