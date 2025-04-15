package com.xtree.lottery.ui.lotterybet.viewmodel;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.xtree.base.mvvm.ExKt;
import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.base.net.HttpCallBack;
import com.xtree.base.utils.ClickUtil;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.base.widget.MsgDialog;
import com.xtree.base.widget.TipDialog;
import com.xtree.lottery.R;
import com.xtree.lottery.data.LotteryRepository;
import com.xtree.lottery.data.source.request.LotteryBetRequest;
import com.xtree.lottery.data.source.vo.IssueVo;
import com.xtree.lottery.ui.lotterybet.model.ChasingNumberRequestModel;
import com.xtree.lottery.ui.viewmodel.LotteryViewModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.http.BusinessException;
import me.xtree.mvvmhabit.utils.ToastUtils;

/**
 * Created by KAKA on 2024/11/5.
 * Describe:
 */
public class LotteryBetConfirmViewModel extends BaseViewModel<LotteryRepository> {

    public final MutableLiveData<ArrayList<BindModel>> datas = new MutableLiveData<>(new ArrayList<>());
    public final MutableLiveData<ArrayList<Integer>> itemType = new MutableLiveData<>(
            new ArrayList<Integer>() {
                {
                    add(R.layout.item_lottery_bet_confirm);
                }
            });
    public final ArrayList<BindModel> bindModels = new ArrayList<>();
    public final MutableLiveData<String> bonusNumberTitle = new MutableLiveData<>();
    public final MutableLiveData<String> totalMoney = new MutableLiveData<>();
    public final MutableLiveData<Boolean> containSolo = new MutableLiveData<>(false);
    //追号入参
    public final MutableLiveData<ChasingNumberRequestModel> chasingNumberParams = new MutableLiveData<>();
    public final MutableLiveData<IssueVo> issueLiveData = new MutableLiveData<>();
    public LotteryBetsViewModel betsViewModel;
    public LotteryViewModel lotteryViewModel;
    private BasePopupView popupView;

    public LotteryBetConfirmViewModel(@NonNull Application application) {
        super(application);
    }

    public LotteryBetConfirmViewModel(@NonNull Application application, LotteryRepository model) {
        super(application, model);
    }

    public void initData(FragmentActivity activity, ArrayList<LotteryBetRequest.BetOrderData> betList) {

        bindModels.clear();
        datas.getValue().clear();
        bonusNumberTitle.setValue("");
        issueLiveData.setValue(null);

        lotteryViewModel.currentIssueLiveData.observe(activity, new Observer<IssueVo>() {
            @Override
            public void onChanged(IssueVo issueVo) {
                if (issueVo != null) {
                    issueLiveData.setValue(issueVo);

                    bonusNumberTitle.setValue("确认要加入" + issueVo.getIssue() + "期?");
                }
            }
        });

        bindModels.addAll(betList);

        datas.setValue(bindModels);

        BigDecimal money = BigDecimal.ZERO;
        boolean solo = false;
        for (LotteryBetRequest.BetOrderData betOrderData : betList) {
            money = money.add(BigDecimal.valueOf(betOrderData.getMoney()));
            if (betOrderData.isSolo()) {
                solo = true;
            }
        }
        if (solo) {
            containSolo.setValue(true);
        }

        totalMoney.setValue(money.toPlainString());
    }

    /**
     * 取消
     */
    public void cancle() {
        finish();
    }

    /**
     * 投注
     */
    public void bet(View view) {
        if(ClickUtil.isFastClick()){
            return;
        }
        if (issueLiveData.getValue() == null) {
            ToastUtils.showError("期数数据错误");
            return;
        }

        LotteryBetRequest lotteryBetRequest = new LotteryBetRequest();
        ArrayList<LotteryBetRequest.BetOrderData> betOrders = new ArrayList<>();

        BigDecimal money = BigDecimal.ZERO;
        int nums = 0;
        for (BindModel bindModel : datas.getValue()) {
            LotteryBetRequest.BetOrderData data = (LotteryBetRequest.BetOrderData) bindModel;
            money = money.add(BigDecimal.valueOf(data.getMoney()));
            nums += data.getNums();
            betOrders.add(data);
        }
        lotteryBetRequest.setLt_project(betOrders);
        lotteryBetRequest.setLotteryid(betsViewModel.lotteryLiveData.getValue().getId());
        lotteryBetRequest.setCurmid(betsViewModel.lotteryLiveData.getValue().getCurmid());
        lotteryBetRequest.setLt_issue_start(issueLiveData.getValue().getIssue());
        lotteryBetRequest.setLt_total_money(money.toPlainString());
        lotteryBetRequest.setLt_total_nums(nums);
        lotteryBetRequest.setPlay_source(6);

        HashMap<String, Object> parmes = new HashMap<>();
        if (chasingNumberParams.getValue() != null && chasingNumberParams.getValue().getParmes() != null) {
            parmes.putAll(chasingNumberParams.getValue().getParmes());
        }
        model.bet(lotteryBetRequest, parmes).subscribe(new HttpCallBack<Object>() {
            @Override
            public void onResult(Object response) {
                chasingNumberParams.setValue(null);
                betsViewModel.betLiveData.setValue(null);
                betsViewModel.betOrdersLiveData.setValue(null);
                betsViewModel.betCartOrdersLiveData.setValue(null);
                betsViewModel.clearBetEvent.setValue(null);
                finish();
                ToastUtils.showSuccess("投注成功");
            }

            @Override
            public void onFail(BusinessException t) {
                super.onFail(t);

                Context realContext = view.getContext();
                while (realContext instanceof ContextWrapper && !(realContext instanceof Activity)) {
                    realContext = ((ContextWrapper) realContext).getBaseContext();
                }

                if (realContext instanceof Activity) {
                    Activity activity = (Activity) realContext;
                    // 继续你的逻辑

                    MsgDialog dialog = new MsgDialog(activity, "", t.message, true, new TipDialog.ICallBack() {
                        @Override
                        public void onClickLeft() {

                        }

                        @Override
                        public void onClickRight() {
                            if (popupView != null) {
                                popupView.dismiss();
                            }
                        }
                    });

                    popupView = new XPopup.Builder(activity)
                            .dismissOnTouchOutside(true)
                            .dismissOnBackPressed(true)
                            .asCustom(dialog).show();
                } else {
                    // 处理错误情况
                    ToastUtils.showError(t.message);
                }

            }

            @Override
            protected void onStart() {
                super.onStart();
                Activity activity = ExKt.getActivity(view);
                if (activity != null) {
                    LoadingDialog.show(activity);
                }
            }
        });
    }

    /**
     * 清除单挑
     */
    public void doClear() {

        BigDecimal money = BigDecimal.ZERO;

        for (int i = 0; i < bindModels.size(); i++) {
            LotteryBetRequest.BetOrderData m = (LotteryBetRequest.BetOrderData) bindModels.get(i);
            if (m.isSolo()) {
                bindModels.remove(bindModels.get(i));
            } else {
                money = money.add(BigDecimal.valueOf(m.getMoney()));
            }
        }

        //清除单挑取消选注
        betsViewModel.clearBetEvent.setValue(null);
        betsViewModel.doClear();

        if (bindModels.size() == 0) {
            finish();
            return;
        }

        containSolo.setValue(false);

        totalMoney.setValue(money.toPlainString());

        datas.setValue(bindModels);
    }
}
