package com.xtree.lottery.ui.lotterybet.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.net.HttpCallBack;
import com.xtree.base.vo.ProfileVo;
import com.xtree.lottery.data.LotteryRepository;
import com.xtree.lottery.data.config.Lottery;
import com.xtree.lottery.data.source.request.BonusNumbersRequest;
import com.xtree.lottery.data.source.request.LotteryBetRequest;
import com.xtree.lottery.data.source.response.BalanceResponse;
import com.xtree.lottery.data.source.response.BonusNumbersResponse;
import com.xtree.lottery.data.source.response.HandicapResponse;
import com.xtree.lottery.ui.lotterybet.LotteryChipSettingDialogFragment;
import com.xtree.lottery.ui.lotterybet.data.LotteryHandicapPrizeData;
import com.xtree.lottery.ui.lotterybet.model.LotteryBetsModel;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.http.BaseResponse;
import me.xtree.mvvmhabit.http.BusinessException;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;

/**
 * Created by KAKA on 2024/5/16.
 * Describe: 盘口玩法ViewModel
 */
public class LotteryHandicapViewModel extends BaseViewModel<LotteryRepository> implements TabLayout.OnTabSelectedListener {

    public LotteryHandicapViewModel(@NonNull Application application) {
        super(application);
    }

    public LotteryHandicapViewModel(@NonNull Application application, LotteryRepository model) {
        super(application, model);
    }

    private HandicapResponse handicapMethods;
    public final ArrayList<LotteryBetsModel> betModels = new ArrayList<LotteryBetsModel>();
    private WeakReference<FragmentActivity> mActivity = null;
    public ObservableField<ArrayList<String>> tabs = new ObservableField<>(new ArrayList<>());
    public MutableLiveData<LotteryBetsModel> currentBetModel = new MutableLiveData<LotteryBetsModel>();
    public MutableLiveData<BonusNumbersResponse> bonusNumbersLiveData = new MutableLiveData<>();
    //投注金额
    public MutableLiveData<String> moneyLiveData = new MutableLiveData<>("2");
    //余额
    public MutableLiveData<String> balanceData = new MutableLiveData<>();
    //奖金玩法返点
    public MutableLiveData<LotteryHandicapPrizeData> prizeData = new MutableLiveData<>(new LotteryHandicapPrizeData("0%", 2));
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
    //返点数据
    public static ArrayList<LotteryHandicapPrizeData> prizeMap = new ArrayList<>();

    public void initData(FragmentActivity mActivity, Lottery lottery) {
        setActivity(mActivity);
        lotteryLiveData.setValue(lottery);
        getHandicapMethods();
        getUserBalance();
        initPrize();
    }

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

    private void setActivity(FragmentActivity mActivity) {
        this.mActivity = new WeakReference<>(mActivity);
    }

    /**
     * 初始化玩法数据
     */
    public void initTabs() {
        getBonusNumbers();

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
     * 获取往期开奖号码
     */
    public void getBonusNumbers() {
        Disposable disposable = model.getBonusNumbersData(String.valueOf(lotteryLiveData.getValue().getId()),new BonusNumbersRequest())
                .subscribeWith(new HttpCallBack<BonusNumbersResponse>() {
                    @Override
                    public void onResult(BonusNumbersResponse response) {
                        if (response != null && response.getData() != null) {
                            bonusNumbersLiveData.setValue(response);
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
    public void handicapBet() {

        List<LotteryBetRequest.BetOrderData> betOrders = betLiveData.getValue();

        if (betOrders == null || betOrders.isEmpty()) {
            ToastUtils.showError("请选择投注号码");
            return;
        }

        LotteryBetRequest lotteryBetRequest = new LotteryBetRequest();


        int money = 0;
        int nums = 0;
        for (LotteryBetRequest.BetOrderData data : betOrders) {
            money += data.getMoney();
            nums += data.getNums();
        }

        if (money == 0 || nums == 0) {
            ToastUtils.showError("请选择注单和投注金额");
            return;
        }

        lotteryBetRequest.setLtProject(betOrders);
        lotteryBetRequest.setLotteryid(lotteryLiveData.getValue().getId());
        lotteryBetRequest.setCurmid(lotteryLiveData.getValue().getCurmid());
        lotteryBetRequest.setLtIssueStart(bonusNumbersLiveData.getValue().getData().get(0).getIssue());
        lotteryBetRequest.setLtProject(betOrders);
        lotteryBetRequest.setLtTotalMoney(money);
        lotteryBetRequest.setLtTotalNums(nums);
        lotteryBetRequest.setPlaySource(6);

        HashMap<String, Object> params = new HashMap<>();
        params.put("lt_trace_if", "no");

        model.bet(lotteryBetRequest, params).subscribe(new HttpCallBack<BaseResponse>() {
            @Override
            public void onResult(BaseResponse response) {
                if (response != null && response.getCode() == 10000) {

                }
            }

            @Override
            public void onFail(BusinessException t) {
                super.onFail(t);
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
