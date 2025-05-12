package com.xtree.bet.ui.viewmodel.pm;

import android.app.Application;

import androidx.annotation.NonNull;

import com.xtree.base.net.HttpCallBack;
import com.xtree.bet.bean.request.pm.BtCarCgReq;
import com.xtree.bet.bean.request.pm.BtCarReq;
import com.xtree.bet.bean.request.pm.BtReq;
import com.xtree.bet.bean.request.pm.OrderDetail;
import com.xtree.bet.bean.request.pm.SeriesOrder;
import com.xtree.bet.bean.response.pm.BtConfirmInfo;
import com.xtree.bet.bean.response.pm.BtResultInfo;
import com.xtree.bet.bean.response.pm.BtResultOptionInfo;
import com.xtree.bet.bean.response.pm.CgOddLimitInfo;
import com.xtree.bet.bean.response.pm.PlayTypeInfo;
import com.xtree.bet.bean.response.pm.SeriesOrderInfo;
import com.xtree.bet.bean.ui.BetConfirmOption;
import com.xtree.bet.bean.ui.BetConfirmOptionPm;
import com.xtree.bet.bean.ui.BtResult;
import com.xtree.bet.bean.ui.BtResultPm;
import com.xtree.bet.bean.ui.CgOddLimit;
import com.xtree.bet.bean.ui.CgOddLimitPm;
import com.xtree.bet.bean.ui.PlayType;
import com.xtree.bet.bean.ui.PlayTypePm;
import com.xtree.bet.constant.SPKey;
import com.xtree.bet.data.BetRepository;
import com.xtree.bet.ui.viewmodel.TemplateBtCarViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import com.xtree.base.http.BusinessException;
import com.xtree.base.utils.RxUtils;
import com.xtree.base.utils.SPUtils;
import com.xtree.base.utils.ToastUtils;

/**
 * Created by marquis
 */

public class PMBtCarViewModel extends TemplateBtCarViewModel {

    private List<BetConfirmOption> mSearchBetConfirmOptionList;

    public PMBtCarViewModel(@NonNull Application application, BetRepository repository) {
        super(application, repository);
    }

    @Override
    public int getOrderBy(int index) {
        return index == 1 ? 2 : 1;
    }

    @Override
    public int getOrderByPosition(int orderBy) {
        return orderBy == 1 ? 0 : 1;
    }

    /**
     * 投注前查询指定玩法赔率
     */
    public void batchBetMatchMarketOfJumpLine(List<BetConfirmOption> betConfirmOptionList) {
        mSearchBetConfirmOptionList = betConfirmOptionList;
        BtCarReq btCarReq = new BtCarReq();
        btCarReq.setCuid();
        List<BtCarReq.BetMatchMarket> betMatchMarketList = new ArrayList<>();
        for (BetConfirmOption betConfirmOption : betConfirmOptionList) {
            BtCarReq.BetMatchMarket betMatchMarket = new BtCarReq.BetMatchMarket();
            betMatchMarket.setMatchInfoId(betConfirmOption.getMatch().getId());
            betMatchMarket.setMarketId(Long.valueOf(betConfirmOption.getPlayTypeId()));
            if (betConfirmOption.getOption() == null) {
                //初始化投注弹窗是，option有可能为空
                return;
            }
            betMatchMarket.setOddsId(betConfirmOption.getOption().getId());
            betMatchMarket.setPlayId(betConfirmOption.getPlayType().getId());//betConfirmOption.getPlayType().getId()  这个值就是hpid
            betMatchMarket.setMatchType(betConfirmOption.getOptionList().getMatchType());
            betMatchMarket.setSportId(Integer.valueOf(betConfirmOption.getMatch().getSportId()));
            betMatchMarket.setPlaceNum(betConfirmOption.getPlaceNum());

            PlayType playType = betConfirmOption.getPlayType();
            if (!(playType instanceof PlayTypePm)) {
                ToastUtils.showLong("数据错误，请重新打开投注弹窗");
                return;
            }

            PlayTypeInfo playTypeInfo = ((PlayTypePm) playType).getPlayTypeInfo();
            String chpid = "";
            if (playTypeInfo.topKey != null) {
                chpid = playTypeInfo.topKey;
            } else {
                chpid = betConfirmOption.getPlayType().getId();
            }
            if (!chpid.isEmpty()) {
                betMatchMarket.setChpid(chpid);
            }
            betMatchMarketList.add(betMatchMarket);
        }
        btCarReq.setIdList(betMatchMarketList);
        queryMarketMaxMinBetMoney(betConfirmOptionList);
        Disposable disposable = (Disposable) model.getPMApiService().batchBetMatchMarketOfJumpLine(btCarReq)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<List<BtConfirmInfo>>() {
                    @Override
                    public void onResult(List<BtConfirmInfo> btConfirmInfoList, BusinessException exception) {
                        if (btConfirmInfoList == null || btConfirmInfoList.isEmpty()) {
                            btConfirmInfoDate.postValue(new ArrayList<>());
                            return;
                        }
                        List<BetConfirmOption> mBetConfirmOptionList = new ArrayList<>();
                        for (BtConfirmInfo btConfirmInfo : btConfirmInfoList) {
                            mBetConfirmOptionList.add(new BetConfirmOptionPm(btConfirmInfo, ""));
                        }
                        btConfirmInfoDate.postValue(mBetConfirmOptionList);
                    }

                    @Override
                    public void onError(Throwable t) {
                        //super.onError(t);
                        if (t instanceof BusinessException) {
                            BusinessException error = (BusinessException) t;
                            if (error.code == HttpCallBack.CodeRule.CODE_401026 || error.code == HttpCallBack.CodeRule.CODE_401013) {
                                batchBetMatchMarketOfJumpLine(betConfirmOptionList);
                            }
                        }
                    }
                });
        addSubscribe(disposable);
    }

    /**
     * 查询最大最小投注金额
     */
    private void queryMarketMaxMinBetMoney(List<BetConfirmOption> betConfirmOptionList) {
        BtCarCgReq btCarCgReq = new BtCarCgReq();
        btCarCgReq.setCuid();
        List<BtCarCgReq.OrderMaxBetMoney> orderMaxBetMonieList = new ArrayList<>();
        for (BetConfirmOption betConfirmOption : betConfirmOptionList) {
            BtCarCgReq.OrderMaxBetMoney orderMaxBetMoney = new BtCarCgReq.OrderMaxBetMoney();
            orderMaxBetMoney.setOpenMiltSingle(0);
            orderMaxBetMoney.setPlayId(betConfirmOption.getPlayType().getId());
            orderMaxBetMoney.setPlayOptionId(betConfirmOption.getOption().getId());
            orderMaxBetMoney.setMatchType(betConfirmOption.getOptionList().getMatchType());
            orderMaxBetMoney.setMarketId(betConfirmOption.getPlayTypeId());
            orderMaxBetMoney.setMatchId(betConfirmOption.getMatch().getId());
            orderMaxBetMoney.setOddsValue(betConfirmOption.getOption().getRealOdd() * 100000);
            orderMaxBetMonieList.add(orderMaxBetMoney);
        }
        btCarCgReq.setOrderMaxBetMoney(orderMaxBetMonieList);

        Disposable disposable = (Disposable) model.getPMApiService().queryMarketMaxMinBetMoney(btCarCgReq)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<List<CgOddLimitInfo>>() {
                    @Override
                    public void onResult(List<CgOddLimitInfo> cgOddLimitInfos) {

                        List<CgOddLimit> cgOddLimitInfoList = new ArrayList<>();
                        if (betConfirmOptionList.size() == 1) {
                            CgOddLimitInfo cgOddLimitInfo = cgOddLimitInfos.get(0);
                            cgOddLimitInfo.seriesOdds = String.valueOf(betConfirmOptionList.get(0).getOption().getRealOdd());
                            cgOddLimitInfo.type = "1";
                            cgOddLimitInfoList.add(new CgOddLimitPm(cgOddLimitInfo));
                        } else {
                            if (!cgOddLimitInfos.isEmpty()) {
                                for (CgOddLimitInfo cgOddLimitInfo : cgOddLimitInfos) {
                                    cgOddLimitInfoList.add(new CgOddLimitPm(cgOddLimitInfo));
                                }
                            }
                        }

                        cgOddLimitDate.postValue(cgOddLimitInfoList);
                    }

                    @Override
                    public void onError(Throwable t) {
                        //super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    /**
     * 单关投注
     */
    public void singleBet(List<BetConfirmOption> betConfirmOptionList, List<CgOddLimit> cgOddLimitList, int acceptOdds) {
        betMultiple(betConfirmOptionList, cgOddLimitList, acceptOdds);
    }

    /**
     * 串关投注
     */
    public void betMultiple(List<BetConfirmOption> betConfirmOptionList, List<CgOddLimit> cgOddLimitList, int acceptOdds) {
        if (betConfirmOptionList.isEmpty() || cgOddLimitList.isEmpty()) {
            return;
        }
        BtReq btReq = new BtReq();
        btReq.setCuid();
        btReq.setAcceptOdds(acceptOdds);
        List<SeriesOrder> seriesOrders = new ArrayList<>();
        for (CgOddLimit cgOddLimit : cgOddLimitList) {
            if (cgOddLimit.getBtAmount() > 0) {
                SeriesOrder seriesOrder = new SeriesOrder();
                seriesOrder.setSeriesSum(cgOddLimit.getBtCount());
                seriesOrder.setSeriesType(cgOddLimit.getCgType());
                seriesOrder.setFullBet(0);

                List<OrderDetail> orderDetailList = new ArrayList<>();
                for (BetConfirmOption betConfirmOption : betConfirmOptionList) {
                    OrderDetail orderDetail = new OrderDetail();
                    orderDetail.setBetAmount(cgOddLimit.getBtAmount());
                    orderDetail.setMarketId(betConfirmOption.getPlayTypeId());
                    orderDetail.setMatchId(Long.valueOf(betConfirmOption.getMatchId()));
                    orderDetail.setMatchType(betConfirmOption.getOptionList().getMatchType());
                    orderDetail.setOddFinally(String.valueOf(betConfirmOption.getOption().getUiShowOdd()));
                    orderDetail.setOdds((long) betConfirmOption.getOption().getBodd());
                    orderDetail.setPlayId(Long.valueOf(betConfirmOption.getPlayType().getId()));
                    orderDetail.setPlayOptionsId(betConfirmOption.getOption().getId());
                    orderDetail.setPlaceNum(betConfirmOption.getPlaceNum());
                    int marketType = SPUtils.getInstance().getInt(SPKey.BT_MATCH_LIST_ODDTYPE, 1);
                    orderDetail.setMarketTypeFinally(marketType == 1 ? "EU" : "HK");
                    orderDetailList.add(orderDetail);
                    PlayTypePm playTypePm = (PlayTypePm) betConfirmOption.getPlayType();
                    PlayTypeInfo playTypeInfo = playTypePm.getPlayTypeInfo();
                    String chpid = "";
                    if (playTypeInfo.topKey != null) {
                        chpid = playTypeInfo.topKey;
                    } else {
                        chpid = betConfirmOption.getPlayType().getId();
                    }
                    if (!chpid.isEmpty()) {
                        btReq.setChpid(chpid);
                    }
                }
                seriesOrder.setOrderDetailList(orderDetailList);
                seriesOrders.add(seriesOrder);
            }
        }
        btReq.setSeriesOrders(seriesOrders);
        btReq.setCuid();
        if (seriesOrders.isEmpty()) {
            noBetAmountDate.call();
            return;
        }

        Disposable disposable = (Disposable) model.getPMApiService().bet(btReq)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<BtResultInfo>() {
                    @Override
                    public void onResult(BtResultInfo btResultInfo) {
                        List<BtResult> btResultList = new ArrayList<>();
                        if (btResultInfo.seriesOrderRespList != null) {
                            for (SeriesOrderInfo seriesOrderInfo : btResultInfo.seriesOrderRespList) {
                                btResultList.add(new BtResultPm(seriesOrderInfo));
                            }
                        } else {
                            BtResultOptionInfo btResultOptionInfo = btResultInfo.orderDetailRespList.get(0);
                            SeriesOrderInfo seriesOrderInfo = new SeriesOrderInfo();
                            seriesOrderInfo.maxWinAmount = btResultOptionInfo.maxWinMoney;
                            seriesOrderInfo.orderNo = btResultOptionInfo.orderNo;
                            seriesOrderInfo.orderStatusCode = Integer.valueOf(btResultOptionInfo.orderStatusCode);
                            seriesOrderInfo.betAmount = btResultOptionInfo.betMoney;
                            btResultList.add(new BtResultPm(seriesOrderInfo));
                        }
                        btResultInfoDate.postValue(btResultList);
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        if (t instanceof BusinessException) {
                            if (((BusinessException) t).code == HttpCallBack.CodeRule.CODE_400467) {
                                batchBetMatchMarketOfJumpLine(mSearchBetConfirmOptionList);
                            }
                        }
                    }
                });
        addSubscribe(disposable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
