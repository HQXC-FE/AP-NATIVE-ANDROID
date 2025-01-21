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
import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.base.net.HttpCallBack;
import com.xtree.base.vo.UserMethodsResponse;
import com.xtree.lottery.data.LotteryDataManager;
import com.xtree.lottery.data.LotteryRepository;
import com.xtree.lottery.data.config.Lottery;
import com.xtree.lottery.data.source.request.BonusNumbersRequest;
import com.xtree.lottery.data.source.request.LotteryBetRequest;
import com.xtree.lottery.data.source.response.BalanceResponse;
import com.xtree.lottery.data.source.response.BonusNumbersResponse;
import com.xtree.lottery.data.source.response.MenuMethodsResponse;
import com.xtree.lottery.data.source.vo.MenuMethodsData;
import com.xtree.lottery.rule.BettingEntryRule;
import com.xtree.lottery.rule.betting.data.RulesEntryData;
import com.xtree.lottery.ui.lotterybet.LotteryBetConfirmDialogFragment;
import com.xtree.lottery.ui.lotterybet.LotteryOrderDialogFragment;
import com.xtree.lottery.ui.lotterybet.LotteryPlayCollectionDialogFragment;
import com.xtree.lottery.ui.lotterybet.data.LotteryMoneyData;
import com.xtree.lottery.ui.lotterybet.model.LotteryBetsModel;
import com.xtree.lottery.ui.lotterybet.model.LotteryOrderModel;
import com.xtree.lottery.ui.lotterybet.model.LotteryPlayCollectionModel;
import com.xtree.lottery.utils.AnimUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.bus.event.SingleLiveData;
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

    private MenuMethodsData menuMethods;
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
    public SingleLiveData<LotteryBetRequest.BetOrderData> betLiveData = new SingleLiveData<>();
    //清除投注框事件
    public SingleLiveData<String> clearBetEvent = new SingleLiveData<>();
    //彩票信息
    public MutableLiveData<Lottery> lotteryLiveData = new MutableLiveData<>();

    public void initData(FragmentActivity mActivity, Lottery lottery) {
        setActivity(mActivity);
        lotteryLiveData.setValue(lottery);
        initMethods(lottery);
        getUserBalance(null);
    }

    private void setActivity(FragmentActivity mActivity) {
        this.mActivity = new WeakReference<>(mActivity);
    }

    /**
     * 初始化玩法数据
     */
    private void initPlayCollection() {
        playModels.clear();
        List<UserMethodsResponse.DataDTO> userLabels = userMethods.getData();
        List<MenuMethodsData.LabelsDTO> menuLabels = menuMethods.getLabels();

        boolean hasUsePlay = false;

        for (MenuMethodsData.LabelsDTO label : menuLabels) {
            if (label != null && label.getLabels() != null) {
                for (MenuMethodsData.LabelsDTO.Labels1DTO labels1DTO : label.getLabels()) {
                    LotteryPlayCollectionModel model = new LotteryPlayCollectionModel();
                    model.setMenulabel(label);
                    MenuMethodsData.LabelsDTO.Labels1DTO la = new MenuMethodsData.LabelsDTO.Labels1DTO();
                    la.setTitle(labels1DTO.getTitle());
                    la.setLabels(new ArrayList<>());
                    model.setLabel(la);
                    for (MenuMethodsData.LabelsDTO.Labels1DTO.Labels2DTO labels2DTO : labels1DTO.getLabels()) {
                        for (UserMethodsResponse.DataDTO um : userLabels) {
                            if (Objects.equals(labels2DTO.getMenuid(), um.getMenuid()) && Objects.equals(labels2DTO.getMethodid(), um.getMethodid())) {
                                //只取第一条玩法
                                if (!hasUsePlay && label.isIsdefault()) {
                                    hasUsePlay = true;
                                    labels2DTO.setUserPlay(true);
                                }
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

        if (!hasUsePlay && !playModels.isEmpty()) {
            //如果没有默认玩法 则默认第一条选中
            LotteryPlayCollectionModel m = (LotteryPlayCollectionModel) playModels.get(0);
            m.getLabel().getLabels().get(0).setUserPlay(true);
        }

        initTabs();
    }

    public void initTabs() {
        getBonusNumbers();
        ArrayList<String> tabList = new ArrayList<>();
        betModels.clear();
        for (BindModel playModel : playModels) {
            LotteryPlayCollectionModel m = (LotteryPlayCollectionModel) playModel;
            for (MenuMethodsData.LabelsDTO.Labels1DTO.Labels2DTO label : m.getLabel().getLabels()) {
                if (label.isUserPlay()) {
                    String title = m.getLabel().getTitle() + "-" + label.getName();
                    tabList.add(title);
                    LotteryBetsModel lotteryBetsModel = new LotteryBetsModel(title, m.getMenulabel(), label, m.getUserMethods());
                    betModels.add(lotteryBetsModel);
                }
            }
        }
        tabs.set(tabList);
    }

    private void initMethods(Lottery lottery) {

        Map<String, MenuMethodsData> lotteryMethodsData = LotteryDataManager.INSTANCE.getLotteryMethodsData();
        if (lotteryMethodsData != null) {
            MenuMethodsData menuMethodsData = lotteryMethodsData.get(lottery.getAlias());
            UserMethodsResponse userMethodsData = LotteryDataManager.INSTANCE.getUserMethods();

            //先使用本地数据初始化玩法
            if (menuMethodsData != null && userMethodsData != null) {
                menuMethods = menuMethodsData;
                userMethods = userMethodsData;
                initPlayCollection();
            }
        }
        //加载网络数据初始化玩法
        getMenuMethods();
    }

    private void getUserMethods() {
        Disposable disposable = model.getUserMethodsData()
                .subscribeWith(new HttpCallBack<UserMethodsResponse>() {
                    @Override
                    public void onResult(UserMethodsResponse response) {
                        if (response.getData() != null && menuMethods != null) {
                            LotteryDataManager.INSTANCE.setUserMethods(response);
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
                            menuMethods = response.getData();

                            UserMethodsResponse userMethodsData = LotteryDataManager.INSTANCE.getUserMethods();
                            if (userMethodsData == null) {
                                getUserMethods();
                            } else {
                                userMethods = userMethodsData;
                                initPlayCollection();
                            }
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
    public void getUserBalance(View view) {
        AnimUtils.rotateView(view);
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

    /**
     * 算法检测函数
     */
    public void rule(RulesEntryData.BetDTO betDTO){

        Lottery lottery = lotteryLiveData.getValue();
        LotteryBetsModel lotteryBetsModel = currentBetModel.getValue();
        UserMethodsResponse.DataDTO userMethodData = lotteryBetsModel.getUserMethodData();
        MenuMethodsData.LabelsDTO menuMethodLabel = lotteryBetsModel.getMenuMethodLabel();
        MenuMethodsData.LabelsDTO.Labels1DTO.Labels2DTO currentl2 = lotteryBetsModel.getMenuMethodLabelData();
        RulesEntryData rulesEntryData = new RulesEntryData();
        rulesEntryData.setType(lottery.getType());

        RulesEntryData.CurrentCategoryDTO currentCategoryDTO = new RulesEntryData.CurrentCategoryDTO();
        currentCategoryDTO.setFlag(lottery.getAlias());
        currentCategoryDTO.setName(menuMethodLabel.getTitle());

        ArrayList<RulesEntryData.CurrentCategoryDTO.CategoriesDTO> categoriesDTOS = new ArrayList<>();

        for (MenuMethodsData.LabelsDTO.Labels1DTO l1 : menuMethodLabel.getLabels()) {
            RulesEntryData.CurrentCategoryDTO.CategoriesDTO categoriesDTO = new RulesEntryData.CurrentCategoryDTO.CategoriesDTO();
            categoriesDTO.setGroupName(l1.getTitle());
            categoriesDTO.setDyTitle(l1.getTitle());
            ArrayList<RulesEntryData.CurrentCategoryDTO.CategoriesDTO.MethodsDTO> methodsDTOS = new ArrayList<>();
            for (MenuMethodsData.LabelsDTO.Labels1DTO.Labels2DTO l2 : l1.getLabels()) {
                //整理玩法
                RulesEntryData.CurrentCategoryDTO.CategoriesDTO.MethodsDTO methodsDTO = new RulesEntryData.CurrentCategoryDTO.CategoriesDTO.MethodsDTO();
                methodsDTO.setMenuid(l2.getMenuid());
                methodsDTO.setMethodid(l2.getMethodid());
                methodsDTO.setName(l2.getName());
                methodsDTO.setIsMultiple(l2.getIsMultiple());
                methodsDTO.setRelationMethods(l2.getRelationMethods());
                methodsDTO.setMaxcodecount(l2.getMaxcodecount());
                methodsDTO.setSelectarea(l2.getSelectarea());
                methodsDTO.setShowStr(l2.getShowStr());
                methodsDTO.setCodeSp(l2.getCodeSp());
                methodsDTO.setDefaultposition(l2.getDefaultposition());
                methodsDTO.setDesc(l2.getName());
                methodsDTO.setGroupName(l1.getTitle());
                methodsDTO.setCateName(menuMethodLabel.getTitle());
                methodsDTO.setLotteryId(String.valueOf(lottery.getId()));
                methodsDTO.setOriginalName(l2.getName());
                methodsDTO.setMethoddesc(l2.getMethoddesc());
                methodsDTO.setMethodexample(l2.getMethodexample());
                methodsDTO.setMethodhelp(l2.getMethodhelp());
                methodsDTO.setDescription(l2.getDescription());
                methodsDTO.setMoneyModes(l2.getMoneyModes());
                methodsDTO.setCateTitle(menuMethodLabel.getTitle());
                methodsDTO.setGroupTitle(l1.getTitle());
//                methodsDTO.setCurrent();
                methodsDTOS.add(methodsDTO);

                for (UserMethodsResponse.DataDTO um : userMethods.getData()) {
                    if (l2.getMenuid().equals(um.getMenuid()) && l2.getMethodid().equals(um.getMethodid())) {
                        methodsDTO.setPrizeLevel(userMethodData.getPrizeLevel());
                        methodsDTO.setPrizeGroup(userMethodData.getPrizeGroup());
                        break;
                    }
                }

                //设置当前玩法
                if (l2.getMenuid().equals(currentl2.getMenuid()) && l2.getMethodid().equals(currentl2.getMethodid())) {
                    RulesEntryData.CurrentMethodDTO currentMethodDTO = new RulesEntryData.CurrentMethodDTO();
                    currentMethodDTO.setMenuid(l2.getMenuid());
                    currentMethodDTO.setMethodid(l2.getMethodid());
                    currentMethodDTO.setName(l2.getName());
                    currentMethodDTO.setIsMultiple(l2.getIsMultiple());
                    currentMethodDTO.setRelationMethods(l2.getRelationMethods());
                    currentMethodDTO.setMaxcodecount(l2.getMaxcodecount());
                    currentMethodDTO.setSelectarea(l2.getSelectarea());
                    currentMethodDTO.setShowStr(l2.getShowStr());
                    currentMethodDTO.setCodeSp(l2.getCodeSp());
                    currentMethodDTO.setDefaultposition(l2.getDefaultposition());
                    currentMethodDTO.setDesc(l2.getName());
                    currentMethodDTO.setPrizeLevel(userMethodData.getPrizeLevel());
                    currentMethodDTO.setPrizeGroup(userMethodData.getPrizeGroup());
                    currentMethodDTO.setGroupName(l1.getTitle());
                    currentMethodDTO.setCateName(menuMethodLabel.getTitle());
                    currentMethodDTO.setLotteryId(String.valueOf(lottery.getId()));
                    currentMethodDTO.setOriginalName(l2.getName());
                    currentMethodDTO.setMethoddesc(l2.getMethoddesc());
                    currentMethodDTO.setMethodexample(l2.getMethodexample());
                    currentMethodDTO.setMethodhelp(l2.getMethodhelp());
                    currentMethodDTO.setDescription(l2.getDescription());
                    currentMethodDTO.setMoneyModes(l2.getMoneyModes());
                    currentMethodDTO.setCateTitle(menuMethodLabel.getTitle());
                    currentMethodDTO.setGroupTitle(l1.getTitle());
//                    currentMethodDTO.setTarget();
                    rulesEntryData.setCurrentMethod(currentMethodDTO);
                }
            }
            //设置子玩法
            categoriesDTO.setMethods(methodsDTOS);
            //设置一组玩法
            categoriesDTOS.add(categoriesDTO);
        }
        //设置玩法菜单
        currentCategoryDTO.setCategories(categoriesDTOS);
        //设置当前选中的玩法菜单
        rulesEntryData.setCurrentCategory(currentCategoryDTO);

        betDTO.setMethodid(currentl2.getMethodid());
        betDTO.setPrize(prizeData.getValue().getValue());
        RulesEntryData.BetDTO.DisplayDTO displayDTO = new RulesEntryData.BetDTO.DisplayDTO();
        displayDTO.setPrize(String.valueOf(prizeData.getValue().getLabel()));
        displayDTO.setPrizeLevel(userMethodData.getPrizeLevel());
        displayDTO.setPrizeGroup(userMethodData.getPrizeGroup());
        displayDTO.setMoneyModes(currentl2.getMoneyModes());
        betDTO.setDisplay(displayDTO);
        betDTO.setSubmit(new RulesEntryData.SubmitDTO());
        rulesEntryData.setBet(betDTO);
        RulesEntryData.SubmitDTO submitDTO = BettingEntryRule.getInstance().startEngine(rulesEntryData);

        if (submitDTO.getMoney() > 0 && submitDTO.getNums() > 0) {
            LotteryBetRequest.BetOrderData betOrderData = new LotteryBetRequest.BetOrderData();
            betOrderData.setMoney(submitDTO.getMoney());
            betOrderData.setOmodel(submitDTO.getOmodel());
            betOrderData.setCodes(submitDTO.getCodes());
            betOrderData.setTimes(submitDTO.getTimes());
            betOrderData.setDesc(submitDTO.getDesc());
            betOrderData.setMenuid(String.valueOf(submitDTO.getMenuid()));
            betOrderData.setMethodid(String.valueOf(submitDTO.getMethodid()));
            betOrderData.setNums(submitDTO.getNums());
            betOrderData.setPoschoose((String) submitDTO.getPoschoose());
            betOrderData.setSolo(submitDTO.isSolo());
            betOrderData.setType(submitDTO.getType());
            betLiveData.setValue(betOrderData);
        } else {
            betLiveData.setValue(null);
        }
    }

    /**
     * 清除单挑
     */
    public void doClear() {

        ArrayList<LotteryOrderModel> orderList = betOrdersLiveData.getValue();
        LotteryBetRequest.BetOrderData curOrder = betLiveData.getValue();

        if (orderList != null) {
            for (int i = 0; i < orderList.size(); i++) {
                LotteryOrderModel m = orderList.get(i);
                if (m.betOrderData.isSolo()) {
                    orderList.remove(m);
                }
            }
            betOrdersLiveData.setValue(orderList);
        }

        if (curOrder != null) {
            if (curOrder.isSolo()) {
                betLiveData.setValue(null);
            }
        }
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
