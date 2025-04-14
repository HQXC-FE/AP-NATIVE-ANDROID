package com.xtree.lottery.ui.lotterybet.viewmodel;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.net.HttpCallBack;
import com.xtree.base.vo.ProfileVo;
import com.xtree.base.widget.MsgDialog;
import com.xtree.base.widget.TipDialog;
import com.xtree.lottery.data.LotteryRepository;
import com.xtree.lottery.data.config.Lottery;
import com.xtree.lottery.data.source.request.LotteryBetRequest;
import com.xtree.lottery.data.source.response.BalanceResponse;
import com.xtree.lottery.data.source.response.HandicapResponse;
import com.xtree.lottery.rule.betting.data.RulesEntryData;
import com.xtree.lottery.ui.lotterybet.LotteryChipSettingDialogFragment;
import com.xtree.lottery.ui.lotterybet.data.LotteryHandicapPrizeData;
import com.xtree.lottery.ui.lotterybet.model.LotteryBetsModel;
import com.xtree.lottery.ui.lotterybet.model.LotteryBetsTotal;
import com.xtree.lottery.ui.viewmodel.LotteryViewModel;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.bus.event.SingleLiveData;
import me.xtree.mvvmhabit.http.BusinessException;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;

/**
 * Created by KAKA on 2024/5/16.
 * Describe: 盘口玩法ViewModel
 */
public class LotteryHandicapViewModel extends BaseViewModel<LotteryRepository> implements TabLayout.OnTabSelectedListener {

    //返点数据
    public static ArrayList<LotteryHandicapPrizeData> prizeMap = new ArrayList<>();
    public final ArrayList<LotteryBetsModel> betModels = new ArrayList<LotteryBetsModel>();
    public ObservableField<ArrayList<String>> tabs = new ObservableField<>(new ArrayList<>());
    public MutableLiveData<LotteryBetsModel> currentBetModel = new MutableLiveData<LotteryBetsModel>();
    //投注金额
    public MutableLiveData<String> moneyLiveData = new MutableLiveData<>("2");
    //余额
    public MutableLiveData<String> balanceData = new MutableLiveData<>();
    //奖金玩法返点
    public MutableLiveData<LotteryHandicapPrizeData> prizeData = new MutableLiveData<>(new LotteryHandicapPrizeData("0%", 2));
    //返点监听
    private final Observer<Boolean> prizeObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(Boolean aBoolean) {

            if (aBoolean) {
                prizeData.setValue(prizeMap.get(1));
                //切换返水设置时修改赔率
                if (currentBetModel.getValue() != null && currentBetModel.getValue().getHandicapMethodData() != null) {
                    if (currentBetModel.getValue().getHandicapMethodData().getGroups() != null) {
                        List<HandicapResponse.DataDTO.GroupsDTO> groups = currentBetModel.getValue().getHandicapMethodData().getGroups();
                        for (HandicapResponse.DataDTO.GroupsDTO group : groups) {
                            for (HandicapResponse.DataDTO.GroupsDTO.CodesDTO code : group.getCodes()) {
                                code.setRebate(false);
                            }
                        }
                    }
                }
            } else {
                prizeData.setValue(prizeMap.get(0));
                if (currentBetModel.getValue() != null && currentBetModel.getValue().getHandicapMethodData() != null) {
                    if (currentBetModel.getValue().getHandicapMethodData().getGroups() != null) {
                        List<HandicapResponse.DataDTO.GroupsDTO> groups = currentBetModel.getValue().getHandicapMethodData().getGroups();
                        for (HandicapResponse.DataDTO.GroupsDTO group : groups) {
                            for (HandicapResponse.DataDTO.GroupsDTO.CodesDTO code : group.getCodes()) {
                                code.setRebate(true);
                            }
                        }
                    }
                }
            }
        }
    };
    //是否启用奖金玩法
    public MutableLiveData<Boolean> prizeSwitchData = new MutableLiveData<>(false);
    //当前有效投注项
    public MutableLiveData<List<LotteryBetRequest.BetOrderData>> betLiveData = new MutableLiveData<>(new ArrayList<>());
    //显示选择筹码
    public MutableLiveData<Boolean> showChipSetting = new MutableLiveData<>(false);
    //筹码集
    public MutableLiveData<int[]> chips = new MutableLiveData<>(new int[]{10, 50, 100, 5000, 10000});
    //彩票信息
    public MutableLiveData<Lottery> lotteryLiveData = new MutableLiveData<>();
    //投注数和总金额
    public MediatorLiveData<LotteryBetsTotal> betTotalLiveData = new MediatorLiveData<>();
    //当前投注规则返回结果
    public SingleLiveData<RulesEntryData.RulesResultData> rulesResultDataLiveData = new SingleLiveData<>();
    //清除投注框事件
    public SingleLiveData<String> clearBetEvent = new SingleLiveData<>();
    public LotteryViewModel lotteryViewModel;
    private HandicapResponse handicapMethods;
    private WeakReference<FragmentActivity> mActivity = null;
    private BasePopupView popupView;

    public LotteryHandicapViewModel(@NonNull Application application) {
        super(application);
    }

    public LotteryHandicapViewModel(@NonNull Application application, LotteryRepository model) {
        super(application, model);
    }

    public void initData(FragmentActivity mActivity, Lottery lottery) {
        setActivity(mActivity);
        lotteryLiveData.setValue(lottery);
        getHandicapMethods();
        getUserBalance();
        initPrize();
        betTotalLiveData.addSource(betLiveData, betOrders -> calBetOrdersNums());
        betTotalLiveData.addSource(moneyLiveData, betOrders -> calBetOrdersNums());
    }

    private void initPrize() {
        String profile = SPUtils.getInstance().getString(SPKeyGlobal.HOME_PROFILE);
        ProfileVo profileVo = new Gson().fromJson(profile, ProfileVo.class);
        prizeMap.clear();
        prizeMap.add(new LotteryHandicapPrizeData("0%", 2));
        if (profileVo != null) {
            prizeMap.add(new LotteryHandicapPrizeData(profileVo.rebate_percentage, 1));
        }
        prizeSwitchData.observeForever(prizeObserver);
    }

    private void calBetOrdersNums() {
        List<LotteryBetRequest.BetOrderData> betOrderDataList = betLiveData.getValue();
        String moneyValue = moneyLiveData.getValue();
        if (betOrderDataList != null) {
            int nums = 0;
            BigDecimal money = BigDecimal.ZERO;
            for (LotteryBetRequest.BetOrderData betOrderData :
                    betOrderDataList) {
                nums += betOrderData.getNums();
                money = money.add(new BigDecimal(moneyValue));
            }
            betTotalLiveData.setValue(new LotteryBetsTotal(nums, money.toPlainString()));
        } else {
            betTotalLiveData.setValue(null);
        }
    }

    private void setActivity(FragmentActivity mActivity) {
        this.mActivity = new WeakReference<>(mActivity);
    }

    /**
     * 初始化玩法数据
     */
    public void initTabs() {


        List<HandicapResponse.DataDTO> handicapMethodsData = handicapMethods.getData();
        ArrayList<String> tabList = new ArrayList<>();
        betModels.clear();

        for (HandicapResponse.DataDTO handicapMethodsDatum : handicapMethodsData) {
            String title = handicapMethodsDatum.getCategory();

            //设置赔率
            if (handicapMethodsDatum.getGroups() != null) {
                for (HandicapResponse.DataDTO.GroupsDTO group : handicapMethodsDatum.getGroups()) {
                    for (HandicapResponse.DataDTO.GroupsDTO.CodesDTO code : group.getCodes()) {
                        code.setRebate(Boolean.FALSE.equals(prizeSwitchData.getValue()));
                    }
                }
            }
            LotteryBetsModel lotteryBetsModel = new LotteryBetsModel();
            lotteryBetsModel.setHandicapMethodData(handicapMethodsDatum);
            lotteryBetsModel.setTitle(title);

            tabList.add(title);
            betModels.add(lotteryBetsModel);
        }
        tabs.set(tabList);
    }

    private void getHandicapMethods() {
        Disposable disposable = model.getHandicapData(lotteryLiveData.getValue().getAlias())
                .subscribeWith(new HttpCallBack<HandicapResponse>() {
                    @Override
                    public void onResult(HandicapResponse response) {
                        if (response.getData() != null) {
                            handicapMethods = response;
                            initTabs();
                        }
                    }
                });
        addSubscribe(disposable);
    }

    /**
     * 获取用户余额
     */
    public void getUserBalance() {
        Disposable disposable = model.getUserBalance()
                .subscribeWith(new HttpCallBack<BalanceResponse>() {
                    @Override
                    public void onResult(BalanceResponse response) {
                        if (response != null && response.getData() != null) {
                            balanceData.setValue("¥ " + response.getData().getBalance());
                        }
                    }
                });
        addSubscribe(disposable);
    }

    /**
     * 快选金额
     */
    public void QuickPickAmount() {
        showChipSetting.setValue(Boolean.FALSE.equals(showChipSetting.getValue()));
    }

    /**
     * 筹码选择
     */
    public void pickChips(int money) {
        String value = moneyLiveData.getValue();

        if (value != null) {
            int m = Integer.parseInt(value);
            money += m;
        }
        moneyLiveData.setValue(String.valueOf(money));
        showChipSetting.setValue(false);
    }

    /**
     * 设置筹码
     */
    public void settingChips() {
        LotteryChipSettingDialogFragment.show(mActivity.get());
    }

    /**
     * 投注
     */
    public void handicapBet(View view) {

        List<LotteryBetRequest.BetOrderData> betOrders = betLiveData.getValue();

        if (lotteryViewModel == null || lotteryViewModel.currentIssueLiveData.getValue() == null) {
            ToastUtils.showError("获取投注期号失败");
            return;
        }

        if (betOrders == null || betOrders.isEmpty()) {
            ToastUtils.showError("请选择投注号码");
            return;
        }

        LotteryBetRequest lotteryBetRequest = new LotteryBetRequest();


        BigDecimal money = BigDecimal.ZERO;
        int nums = 0;
        for (LotteryBetRequest.BetOrderData data : betOrders) {
            money = money.add(BigDecimal.valueOf(data.getMoney()));
            nums += data.getNums();
        }

        if (money.compareTo(BigDecimal.ZERO) == 0 || nums == 0) {
            ToastUtils.showError("请选择注单和投注金额");
            return;
        }

        lotteryBetRequest.setLt_project(betOrders);
        lotteryBetRequest.setLotteryid(lotteryLiveData.getValue().getId());
        lotteryBetRequest.setCurmid(lotteryLiveData.getValue().getCurmid());
        lotteryBetRequest.setLt_issue_start(lotteryViewModel.currentIssueLiveData.getValue().getIssue());
        lotteryBetRequest.setLt_total_money(money.toPlainString());
        lotteryBetRequest.setLt_total_nums(nums);
        lotteryBetRequest.setPlay_source(6);

        HashMap<String, Object> params = new HashMap<>();
        params.put("lt_trace_if", "no");

        model.bet(lotteryBetRequest, params).subscribe(new HttpCallBack<Object>() {
            @Override
            public void onResult(Object response) {

                betLiveData.setValue(null);
                clearBetEvent.setValue(null);
                MsgDialog dialog = new MsgDialog(view.getContext(), "", "投注成功", true, new TipDialog.ICallBack() {
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

                popupView = new XPopup.Builder(view.getContext())
                        .dismissOnTouchOutside(true)
                        .dismissOnBackPressed(true)
                        .asCustom(dialog).show();
            }

            @Override
            public void onFail(BusinessException t) {
                super.onFail(t);
                Context realContext = view.getContext();
                while (realContext instanceof ContextWrapper && !(realContext instanceof Activity)) {
                    realContext = ((ContextWrapper) realContext).getBaseContext();
                }
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
                    ToastUtils.showError(t.message);
                }

            }
        });
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        LotteryBetsModel lotteryBetsModel = betModels.get(tab.getPosition());
        lotteryBetsModel.setPosition(tab.getPosition());
        currentBetModel.setValue(lotteryBetsModel);

        //切换玩法时根据返水设置修改赔率
        Boolean prizeSwitchDataValue = prizeSwitchData.getValue();
        for (HandicapResponse.DataDTO.GroupsDTO group : lotteryBetsModel.getHandicapMethodData().getGroups()) {
            for (HandicapResponse.DataDTO.GroupsDTO.CodesDTO code : group.getCodes()) {
                code.setRebate(Boolean.FALSE.equals(prizeSwitchDataValue));
            }
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mActivity != null) {
            mActivity.clear();
            mActivity = null;
        }
        prizeSwitchData.removeObserver(prizeObserver);
    }
}
