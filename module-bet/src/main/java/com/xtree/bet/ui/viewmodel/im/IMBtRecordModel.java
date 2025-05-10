package com.xtree.bet.ui.viewmodel.im;

import android.app.Application;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.xtree.base.net.HttpCallBack;
import com.xtree.base.utils.CfLog;
import com.xtree.bet.bean.request.im.GetBetListReq;
import com.xtree.bet.bean.request.im.GetStatement;
import com.xtree.bet.bean.response.fb.BtResultInfo;
import com.xtree.bet.bean.response.im.Wager;
import com.xtree.bet.data.BetRepository;
import com.xtree.bet.ui.viewmodel.TemplateBtRecordModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by daniel
 */

public class IMBtRecordModel extends TemplateBtRecordModel {

    public Map<String, BtResultInfo> mOrderMap = new HashMap<>();

    public IMBtRecordModel(@NonNull Application application, BetRepository repository) {
        super(application, repository);
    }

    /**
     * 投注记录接口
     */
    public void betRecord(boolean isSettled) {
        mIsSettled = isSettled;
        CfLog.i("betRecord     " + isSettled + "");
        if (isSettled) {
            getBetList();
        } else {
            getStatement();
        }
    }

    public void getBetList() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        GetBetListReq btRecordReq = new GetBetListReq();
        btRecordReq.setBetConfirmationStatus(list);

        launchFlow(model.getIMApiService().getBetList(
                btRecordReq), new HttpCallBack<List<Wager>>() {
            @Override
            public void onResult(List<Wager> btRecordRsp) {
                CfLog.i("betRecord     " + new Gson().toJson(btRecordRsp));
                //btRecordTimeDate.postValue(btRecordTimeList);
            }

        });
    }

    public void getStatement() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = sdf.format(calendar.getTime());
        GetStatement req = new GetStatement();
        req.setStartDate(formattedDate);
        req.setEndDate(formattedDate);
        launchFlow(model.getIMApiService().getStatement(
                req), new HttpCallBack<List<Wager>>() {
            @Override
            public void onResult(List<Wager> btRecordRsp) {
                //btRecordTimeDate.postValue(btRecordTimeList);
            }

        });
    }

    /**
     * 批量获取订单提前结算报价
     */
    public void cashOutPrice() {

    }

    /**
     * 提前结算下注
     *
     * @param orderId                订单ID
     * @param cashOutStake           提前结算本金
     * @param unitCashOutPayoutStake 提前结算报价接口返回的"单位提前结算价格"
     * @param acceptOddsChange       是否接受下注时真实价格低于下注价格(false:不接受价格变低 true:接受价格变低下注)
     * @param parlay                 是否串关
     */
    public void cashOutPricebBet(String orderId, double cashOutStake, double unitCashOutPayoutStake, boolean acceptOddsChange, boolean parlay) {

    }

    /**
     * 按提前结算订单ID查询提前结算订单金额及状态
     *
     * @param id 提前结算订单ID
     */
    public void getCashOutsByIds(String id) {

    }

}
