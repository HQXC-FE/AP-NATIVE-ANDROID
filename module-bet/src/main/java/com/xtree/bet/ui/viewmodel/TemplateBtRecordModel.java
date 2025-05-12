package com.xtree.bet.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;

import com.xtree.bet.bean.ui.BtRecordTime;
import com.xtree.bet.data.BetRepository;

import java.util.ArrayList;
import java.util.List;

import com.xtree.base.bus.event.SingleLiveData;

/**
 * Created by marquis
 */

public abstract class TemplateBtRecordModel extends BaseBtViewModel implements BtRecordModel {
    public List<String> mOrderIdList = new ArrayList<>();
    public boolean mIsSettled;
    public SingleLiveData<List<BtRecordTime>> btRecordTimeDate = new SingleLiveData<>();
    public SingleLiveData<List<Void>> btUpdateCashOutPrice = new SingleLiveData<>();
    public SingleLiveData<String> btUpdateCashOutBet = new SingleLiveData<>();
    public SingleLiveData<Boolean> btUpdateCashOutStatus = new SingleLiveData<>();

    public TemplateBtRecordModel(@NonNull Application application, BetRepository repository) {
        super(application, repository);
    }

}
