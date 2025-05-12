package com.xtree.bet.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;

import com.xtree.bet.contract.BetContract;
import com.xtree.bet.data.BetRepository;

import io.reactivex.disposables.Disposable;
import com.xtree.base.base.BaseViewModel;
import com.xtree.base.bus.RxBus;
import com.xtree.base.bus.event.SingleLiveData;

/**
 * Created by marquis
 */

public class BtDetailOptionViewModel extends BaseViewModel<BetRepository> {
    private Disposable mSubscription;
    public SingleLiveData<BetContract> betContractListData = new SingleLiveData<>();

    public BtDetailOptionViewModel(@NonNull Application application, BetRepository repository) {
        super(application, repository);
    }

    public void addSubscription() {
        mSubscription = RxBus.getDefault().toObservable(BetContract.class)
                .subscribe(betContract -> {
                    betContractListData.postValue(betContract);
                });
        addSubscribe(mSubscription);
    }
}
