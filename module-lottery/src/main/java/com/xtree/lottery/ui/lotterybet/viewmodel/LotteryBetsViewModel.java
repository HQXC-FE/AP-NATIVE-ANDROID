package com.xtree.lottery.ui.lotterybet.viewmodel;

import android.app.Application;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;

import com.google.android.material.tabs.TabLayout;
import com.xtree.lottery.data.config.Lottery;
import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.base.net.HttpCallBack;
import com.xtree.lottery.data.LotteryRepository;
import com.xtree.lottery.data.source.request.BonusNumbersRequest;
import com.xtree.lottery.data.source.request.LotteryBetRequest;
import com.xtree.lottery.data.source.response.BalanceResponse;
import com.xtree.lottery.data.source.response.BonusNumbersResponse;
import com.xtree.lottery.data.source.response.MenuMethodsResponse;
import com.xtree.lottery.data.source.response.UserMethodsResponse;
import com.xtree.lottery.ui.lotterybet.LotteryBetConfirmDialogFragment;
import com.xtree.lottery.ui.lotterybet.LotteryOrderDialogFragment;
import com.xtree.lottery.ui.lotterybet.LotteryPlayCollectionDialogFragment;
import com.xtree.lottery.ui.lotterybet.data.LotteryMoneyData;
import com.xtree.lottery.ui.lotterybet.model.LotteryBetsModel;
import com.xtree.lottery.ui.lotterybet.model.LotteryOrderModel;
import com.xtree.lottery.ui.lotterybet.model.LotteryPlayCollectionModel;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.utils.ToastUtils;

/**
 * Created by KAKA on 2024/5/1.
 * Describe: 彩种投注viewModel
 */
public class LotteryBetsViewModel extends BaseViewModel<LotteryRepository> implements TabLayout.OnTabSelectedListener {
    public LotteryBetsViewModel(@NonNull Application application) {
        super(application);
    }

    public LotteryBetsViewModel(@NonNull Application application, LotteryRepository model) {
        super(application, model);
    }

    private MenuMethodsResponse menuMethods;
    private UserMethodsResponse userMethods;
    public final ArrayList<BindModel> playModels = new ArrayList<BindModel>();
    public final ArrayList<LotteryBetsModel> betModels = new ArrayList<LotteryBetsModel>();
    private WeakReference<FragmentActivity> mActivity = null;
    public ObservableField<ArrayList<String>> tabs = new ObservableField<>(new ArrayList<>());
    public MutableLiveData<LotteryBetsModel> currentBetModel = new MutableLiveData<LotteryBetsModel>();
    public MutableLiveData<BonusNumbersResponse> bonusNumbersLiveData = new MutableLiveData<>();
    //投注金额
    public MutableLiveData<LotteryMoneyData> moneyLiveData = new MutableLiveData<>();
    //余额
    public MutableLiveData<String> balanceData = new MutableLiveData<>();
    //奖金玩法
    public MutableLiveData<UserMethodsResponse.DataDTO.PrizeGroupDTO> prizeData = new MutableLiveData<>();
    //投注订单集
    public MutableLiveData<ArrayList<LotteryOrderModel>> betOrdersLiveData = new MutableLiveData<>(new ArrayList<>());
    //当前有效投注项
    public MutableLiveData<LotteryBetRequest.BetOrderData> betLiveData = new MutableLiveData<>();
    //彩票信息
    public MutableLiveData<Lottery> lotteryLiveData = new MutableLiveData<>();

    public void initData(FragmentActivity mActivity, Lottery lottery) {
        setActivity(mActivity);
        lotteryLiveData.setValue(lottery);
        getMenuMethods();
        getUserBalance();
    }

    private void setActivity(FragmentActivity mActivity) {
        this.mActivity = new WeakReference<>(mActivity);
    }

    /**
     * 初始化玩法数据
     */
    private void initPlayCollection() {
        List<UserMethodsResponse.DataDTO> userLabels = userMethods.getData();
        List<MenuMethodsResponse.DataDTO.LabelsDTO> menuLabels = menuMethods.getData().getLabels();
        for (MenuMethodsResponse.DataDTO.LabelsDTO label : menuLabels) {
            if (label != null && label.getLabels() != null) {
                for (MenuMethodsResponse.DataDTO.LabelsDTO.Labels1DTO labels1DTO : label.getLabels()) {
                    LotteryPlayCollectionModel model = new LotteryPlayCollectionModel();
                    MenuMethodsResponse.DataDTO.LabelsDTO.Labels1DTO la = new MenuMethodsResponse.DataDTO.LabelsDTO.Labels1DTO();
                    la.setTitle(labels1DTO.getTitle());
                    la.setLabels(new ArrayList<>());
                    model.setLabel(la);
                    for (MenuMethodsResponse.DataDTO.LabelsDTO.Labels1DTO.Labels2DTO labels2DTO : labels1DTO.getLabels()) {
                        for (UserMethodsResponse.DataDTO um : userLabels) {
                            if (Objects.equals(labels2DTO.getMenuid(), um.getMenuid()) && Objects.equals(labels2DTO.getMethodid(), um.getMethodid())) {
                                model.getLabel().getLabels().add(labels2DTO);
                                model.setUserMethods(um);
                                break;
                            }
                        }
                    }
                    if (model.getLabel().getLabels().size() > 0) {
                        playModels.add(model);
                    }
                }
            }
        }

        //默认第一条选中
        LotteryPlayCollectionModel m = (LotteryPlayCollectionModel) playModels.get(0);
        m.getLabel().getLabels().get(0).setUserPlay(true);

        initTabs();
    }

    public void initTabs() {
        getBonusNumbers();
        ArrayList<String> tabList = new ArrayList<>();
        betModels.clear();
        for (BindModel playModel : playModels) {
            LotteryPlayCollectionModel m = (LotteryPlayCollectionModel) playModel;
            for (MenuMethodsResponse.DataDTO.LabelsDTO.Labels1DTO.Labels2DTO label : m.getLabel().getLabels()) {
                if (label.isUserPlay()) {
                    String title = m.getLabel().getTitle() + "-" + label.getName();
                    tabList.add(title);
                    LotteryBetsModel lotteryBetsModel = new LotteryBetsModel(title, label, m.getUserMethods());
                    betModels.add(lotteryBetsModel);
                }
            }
        }
        tabs.set(tabList);
    }

    private void getUserMethods() {
        Disposable disposable = model.getUserMethodsData()
                .subscribeWith(new HttpCallBack<UserMethodsResponse>() {
                    @Override
                    public void onResult(UserMethodsResponse response) {
                        if (response.getData() != null && menuMethods != null) {
                            userMethods = response;
                            initPlayCollection();
                        }

                    }
                });
        addSubscribe(disposable);
    }

    private void getMenuMethods() {
        Disposable disposable = model.getMenuMethodsData(lotteryLiveData.getValue().getAlias())
                .subscribeWith(new HttpCallBack<MenuMethodsResponse>() {
                    @Override
                    public void onResult(MenuMethodsResponse response) {
                        if (response.getData() != null) {
                            menuMethods = response;
                            getUserMethods();
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
     * 玩法收藏弹窗
     */
    public void showPlayCollection() {
        LotteryPlayCollectionDialogFragment.show(mActivity.get());
    }

    /**
     * 订单详情弹窗
     */
    public void showOrder() {
        LotteryOrderDialogFragment.show(mActivity.get());
    }

    /**
     * 选择金额模式
     */
    public void showPrizeMenu(View anchorView) {

        PopupMenu popupMenu = new PopupMenu(anchorView.getContext(), anchorView);

        List<UserMethodsResponse.DataDTO.PrizeGroupDTO> prizeGroup = currentBetModel
                .getValue()
                .getUserMethodData()
                .getPrizeGroup();

        if (prizeGroup == null) {
            ToastUtils.showError("无返点选项");
            return;
        }

        for (UserMethodsResponse.DataDTO.PrizeGroupDTO prizeGroupDTO : prizeGroup) {
            popupMenu.getMenu().add(prizeGroupDTO.getLabel());
        }

        // 设置菜单项点击事件
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                CharSequence title = item.getTitle();
                if (title != null) {
                    for (UserMethodsResponse.DataDTO.PrizeGroupDTO prizeGroupDTO : currentBetModel
                            .getValue()
                            .getUserMethodData()
                            .getPrizeGroup()) {
                        if (prizeGroupDTO.getLabel().contentEquals(title)) {
                            prizeData.setValue(prizeGroupDTO);
                            return true;
                        }
                    }
                }
                return true;
            }
        });

        // 显示 PopupMenu
        popupMenu.show();
    }

    /**
     * 添加选号
     */
    public void saveBetOrder() {
        if (betLiveData.getValue() != null) {
            ArrayList<LotteryOrderModel> orderModels = betOrdersLiveData.getValue();


            LotteryOrderModel lotteryOrderModel = new LotteryOrderModel();
            LotteryBetRequest.BetOrderData orderData = betLiveData.getValue();
            UserMethodsResponse.DataDTO.PrizeGroupDTO prize = prizeData.getValue();
            LotteryMoneyData money = moneyLiveData.getValue();

            orderData.setOmodel(prize.getValue());
            orderData.setMode(money.getMoneyModel().getModelId());
            orderData.setTimes(money.getFactor());

            lotteryOrderModel.setBetOrderData(orderData);
            lotteryOrderModel.setPrizeLabel(prize.getLabel());
            lotteryOrderModel.setMoneyData(money);

            orderModels.add(lotteryOrderModel);

            betOrdersLiveData.setValue(orderModels);
            betLiveData.setValue(null);
        }
    }

    /**
     * 一键投注
     */
    public void quickBet() {
        if (betLiveData.getValue() != null) {
            ArrayList<LotteryBetRequest.BetOrderData> betOrders = new ArrayList<>();
            LotteryBetRequest.BetOrderData orderData = betLiveData.getValue();
            UserMethodsResponse.DataDTO.PrizeGroupDTO prize = prizeData.getValue();
            LotteryMoneyData money = moneyLiveData.getValue();

            orderData.setOmodel(prize.getValue());
            orderData.setMode(money.getMoneyModel().getModelId());

            betOrders.add(orderData);

            LotteryBetConfirmDialogFragment.show(mActivity.get(), betOrders);
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        LotteryBetsModel lotteryBetsModel = betModels.get(tab.getPosition());
        currentBetModel.setValue(lotteryBetsModel);
        if (lotteryBetsModel.getUserMethodData().getPrizeGroup() != null) {
            prizeData.setValue(lotteryBetsModel.getUserMethodData().getPrizeGroup().get(0));
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
    }
}
