package com.xtree.lottery.ui.lotterybet.viewmodel;

import android.app.Application;
import android.text.TextUtils;
import android.view.View;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.xtree.base.mvvm.ExKt;
import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.base.net.HttpCallBack;
import com.xtree.base.utils.CfLog;
import com.xtree.base.vo.UserMethodsResponse;
import com.xtree.lottery.data.LotteryDataManager;
import com.xtree.lottery.data.LotteryRepository;
import com.xtree.lottery.data.config.Lottery;
import com.xtree.lottery.data.source.request.LotteryBetRequest;
import com.xtree.lottery.data.source.response.BalanceResponse;
import com.xtree.lottery.data.source.response.MenuMethodsResponse;
import com.xtree.lottery.data.source.vo.MenuMethodsData;
import com.xtree.lottery.rule.BettingEntryRule;
import com.xtree.lottery.rule.betting.data.RulesEntryData;
import com.xtree.lottery.ui.lotterybet.LotteryBetConfirmDialogFragment;
import com.xtree.lottery.ui.lotterybet.LotteryOrderDialogFragment;
import com.xtree.lottery.ui.lotterybet.LotteryPlayCollectionDialogFragment;
import com.xtree.lottery.ui.lotterybet.data.LotteryMoneyData;
import com.xtree.lottery.ui.lotterybet.model.LotteryBetsModel;
import com.xtree.lottery.ui.lotterybet.model.LotteryBetsPrizeGroup;
import com.xtree.lottery.ui.lotterybet.model.LotteryBetsTotal;
import com.xtree.lottery.ui.lotterybet.model.LotteryOrderModel;
import com.xtree.lottery.ui.lotterybet.model.LotteryPlayCollectionModel;
import com.xtree.lottery.ui.viewmodel.LotteryViewModel;
import com.xtree.lottery.utils.AnimUtils;
import com.xtree.lottery.utils.EventConstant;
import com.xtree.lottery.utils.EventVo;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.bus.event.SingleLiveData;
import me.xtree.mvvmhabit.utils.KLog;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;

/**
 * Created by KAKA on 2024/5/1.
 * Describe: 彩种投注viewModel
 */
public class LotteryBetsViewModel extends BaseViewModel<LotteryRepository> implements TabLayout.OnTabSelectedListener {
    public final ArrayList<BindModel> playModels = new ArrayList<BindModel>();
    public final ArrayList<LotteryBetsModel> betModels = new ArrayList<LotteryBetsModel>();
    public ObservableField<ArrayList<String>> tabs = new ObservableField<>(new ArrayList<>());
    public MutableLiveData<LotteryBetsModel> currentBetModel = new MutableLiveData<LotteryBetsModel>();
    //投注金额
    public MutableLiveData<LotteryMoneyData> moneyLiveData = new MutableLiveData<>();
    //余额
    public MutableLiveData<String> balanceData = new MutableLiveData<>();
    //奖金玩法
    public MutableLiveData<UserMethodsResponse.DataDTO.PrizeGroupDTO> prizeData = new MutableLiveData<>();
    //投注订单集
    public MutableLiveData<ArrayList<LotteryOrderModel>> betOrdersLiveData = new MutableLiveData<>();
    //购物车投注订单集
    public MutableLiveData<List<LotteryOrderModel>> betCartOrdersLiveData = new MutableLiveData<>();
    //当前有效投注项
    public SingleLiveData<List<LotteryBetRequest.BetOrderData>> betLiveData = new SingleLiveData<>();
    //清除投注框事件
    public SingleLiveData<String> clearBetEvent = new SingleLiveData<>();
    //彩票信息
    public MutableLiveData<Lottery> lotteryLiveData = new MutableLiveData<>();
    //是否显示金额
    public MutableLiveData<Boolean> moneyView = new MutableLiveData<>();
    //奖金组和投注数据组合
    public MediatorLiveData<LotteryBetsPrizeGroup> combinedPrizeBetLiveData = new MediatorLiveData<>();
    //投注数和总金额
    public MediatorLiveData<LotteryBetsTotal> betTotalLiveData = new MediatorLiveData<>();
    public LotteryViewModel lotteryViewModel;

    private MenuMethodsData menuMethods;
    //    private UserMethodsResponse userMethods;
    private WeakReference<FragmentActivity> mActivity = null;

    public LotteryBetsViewModel(@NonNull Application application) {
        super(application);
    }

    public LotteryBetsViewModel(@NonNull Application application, LotteryRepository model) {
        super(application, model);
    }

    public void initData(FragmentActivity mActivity, Lottery lottery) {
        setActivity(mActivity);
        lotteryLiveData.setValue(lottery);
        initMethods(lottery);
        getUserBalance(null);
        combinedPrizeBetLiveData.addSource(currentBetModel, betModel -> createLotteryBetsPrizeGroup(false));
        combinedPrizeBetLiveData.addSource(prizeData, prizeGroup -> createLotteryBetsPrizeGroup(true));
        betTotalLiveData.addSource(betLiveData, betOrders -> calBetOrdersNums());
        moneyView.setValue(!"lhc".equals(lottery.getLinkType()));
    }

    private void calBetOrdersNums() {
        List<LotteryBetRequest.BetOrderData> betOrderDataList = betLiveData.getValue();
        if (betOrderDataList != null) {
            int nums = 0;
            BigDecimal money = BigDecimal.ZERO;
            for (LotteryBetRequest.BetOrderData betOrderData : betOrderDataList) {
                nums += betOrderData.getNums();
                money = money.add(BigDecimal.valueOf(betOrderData.getMoney()));
            }
            betTotalLiveData.setValue(new LotteryBetsTotal(nums, money.toPlainString()));
        } else {
            betTotalLiveData.setValue(null);
        }
    }

    //组合投注数据和奖金组
    private void createLotteryBetsPrizeGroup(boolean isPrize) {
        LotteryBetsModel betModel = currentBetModel.getValue();
        UserMethodsResponse.DataDTO.PrizeGroupDTO prizeGroup = prizeData.getValue();

        if (betModel != null) {
            // Update combinedLiveData when both values are available
            combinedPrizeBetLiveData.setValue(new LotteryBetsPrizeGroup(betModel, prizeGroup, isPrize));
        }
    }

    private void setActivity(FragmentActivity mActivity) {
        this.mActivity = new WeakReference<>(mActivity);
    }

    /**
     * 初始化玩法数据
     */
    private void initPlayCollection(Lottery lottery) {
        playModels.clear();
//        List<UserMethodsResponse.DataDTO> userLabels = userMethods.getData();
        List<MenuMethodsData.LabelsDTO> menuLabels = menuMethods.getLabels();

        boolean hasUsePlay = false;
        String methods = SPUtils.getInstance().getString(lottery.getAlias(), "");

        for (MenuMethodsData.LabelsDTO label : menuLabels) {
            if (label != null && label.getLabels() != null) {
                for (MenuMethodsData.LabelsDTO.Labels1DTO labels1DTO : label.getLabels()) {
                    if ("lhc".equals(lottery.getLinkType())) {
                        if (!"特码".equals(label.getDyTitle())) {
                            continue;
                        }
                    }
                    LotteryPlayCollectionModel model = new LotteryPlayCollectionModel();
                    model.setLottery(lottery);
                    model.setMenulabel(label);
                    MenuMethodsData.LabelsDTO.Labels1DTO la = new MenuMethodsData.LabelsDTO.Labels1DTO();
                    la.setTitle(labels1DTO.getTitle());
                    la.setDyTitle(labels1DTO.getDyTitle());
                    la.setLabels(new ArrayList<>());
                    model.setLabel(la);
                    for (MenuMethodsData.LabelsDTO.Labels1DTO.Labels2DTO labels2DTO : labels1DTO.getLabels()) {
                        if (!TextUtils.isEmpty(methods) && ExKt.includes(Arrays.asList(methods.split(",")), labels2DTO.getMenuid())) {
                            hasUsePlay = true;
                            labels2DTO.setUserPlay(true);
                        }
                        model.putUserMethods(labels2DTO.getMenuid() + labels2DTO.getMethodid(), new UserMethodsResponse.DataDTO(labels2DTO.getMenuid(), labels2DTO.getMethodid(), labels2DTO.getName(), labels2DTO.getPrizeLevel(), labels2DTO.getPrizeGroup(), labels2DTO.getCateTitle(), labels2DTO.getIsMultiple(), labels2DTO.getRelationMethods()));
                        model.getLabel().getLabels().add(labels2DTO);
                    }
//                    if (model.getLabel().getLabels().size() > 0) {
                    playModels.add(model);
//                    }
                }
            } else {
            }
        }

        if (!hasUsePlay && !playModels.isEmpty()) {
            //如果没有默认玩法 则默认第一条选中
            LotteryPlayCollectionModel m = (LotteryPlayCollectionModel) playModels.get(0);
            m.getLabel().getLabels().get(0).setUserPlay(true);
            SPUtils.getInstance().put(lottery.getAlias(), m.getLabel().getLabels().get(0).getMenuid());
        }
        CfLog.e("耗时:" + System.currentTimeMillis() / 1000);
        initTabs();
    }

    public void initTabs() {

        ArrayList<String> tabList = new ArrayList<>();
        betModels.clear();
        for (BindModel playModel : playModels) {
            LotteryPlayCollectionModel m = (LotteryPlayCollectionModel) playModel;
            for (MenuMethodsData.LabelsDTO.Labels1DTO.Labels2DTO label : m.getLabel().getLabels()) {
                if (label.isUserPlay()) {
                    String title;
                    if ("lhc".equals(m.getLottery().getLinkType())) {
                        title = m.getMenulabel().getTitle() + "-" + m.getLabel().getDyTitle();
                    } else {
                        title = m.getLabel().getTitle() + "-" + label.getName();
                    }

                    tabList.add(title);
                    LotteryBetsModel lotteryBetsModel = new LotteryBetsModel(title, m.getMenulabel(), label, m.getUserMethod(label.getMenuid() + label.getMethodid()));
                    betModels.add(lotteryBetsModel);
                }
            }
        }
        tabs.set(tabList);
    }

    private void initMethods(Lottery lottery) {
        CfLog.e("耗时:" + System.currentTimeMillis() / 1000);
        Map<String, MenuMethodsData> staticLotteryMethodsData = LotteryDataManager.INSTANCE.getStaticLotteryMethodsData();
        if (staticLotteryMethodsData != null) {
            MenuMethodsData menuMethodsData = staticLotteryMethodsData.get(lottery.getAlias());
            UserMethodsResponse dynamicUserMethodsData = LotteryDataManager.INSTANCE.getDynamicUserMethods();
            CfLog.e("耗时:" + System.currentTimeMillis() / 1000);
            //先使用本地数据初始化玩法
            if (menuMethodsData != null && dynamicUserMethodsData != null) {

                //静态和动态数据替换
                // 转换 methods-dynamic 数据
                List<UserMethodsResponse.DataDTO> dy = (dynamicUserMethodsData.getData()).stream()
                        .map(item -> {
//                            item.put("menuid", Integer.valueOf(item.get("menuid").toString()));
                            return item;
                        })
                        .collect(Collectors.toList());

                // 处理每个彩票数据
                Iterator<Map.Entry<String, MenuMethodsData>> mainIterator = staticLotteryMethodsData.entrySet().iterator();
                CfLog.e("耗时:" + System.currentTimeMillis() / 1000);
                // dy提前转成Map，避免每次遍历
                Map<String, UserMethodsResponse.DataDTO> dyMap = dy.stream()
                        .filter(Objects::nonNull)
                        .collect(Collectors.toMap(UserMethodsResponse.DataDTO::getMenuid, Function.identity(), (a, b) -> a));

                while (mainIterator.hasNext()) {
                    Map.Entry<String, MenuMethodsData> next = mainIterator.next();
                    MenuMethodsData value = next.getValue();
                    List<MenuMethodsData.LabelsDTO> labels = value.getLabels();

                    if (labels == null || labels.stream().noneMatch(Objects::nonNull)) {
                        CfLog.e("移除玩法:" + value);
                        mainIterator.remove();
                        continue;
                    }

                    Iterator<MenuMethodsData.LabelsDTO> iterator = labels.iterator();
                    while (iterator.hasNext()) {
                        MenuMethodsData.LabelsDTO label = iterator.next();
                        List<MenuMethodsData.LabelsDTO.Labels1DTO> labels1DTOS = label.getLabels();

                        if (labels1DTOS == null || labels1DTOS.stream().noneMatch(Objects::nonNull)) {
                            CfLog.e("移除玩法:" + label);
                            iterator.remove();
                            continue;
                        }

                        Iterator<MenuMethodsData.LabelsDTO.Labels1DTO> labels1DTOIterator = labels1DTOS.iterator();
                        while (labels1DTOIterator.hasNext()) {
                            MenuMethodsData.LabelsDTO.Labels1DTO labels1DTO = labels1DTOIterator.next();
                            List<MenuMethodsData.LabelsDTO.Labels1DTO.Labels2DTO> labels2DTOS = labels1DTO.getLabels();

                            if (labels2DTOS == null || labels2DTOS.stream().noneMatch(Objects::nonNull)) {
                                CfLog.e("移除玩法:" + labels1DTO);
                                labels1DTOIterator.remove();
                                continue;
                            }

                            Iterator<MenuMethodsData.LabelsDTO.Labels1DTO.Labels2DTO> labels2DTOIterator = labels2DTOS.iterator();
                            while (labels2DTOIterator.hasNext()) {
                                MenuMethodsData.LabelsDTO.Labels1DTO.Labels2DTO labels2DTO = labels2DTOIterator.next();
                                UserMethodsResponse.DataDTO dyMethod = dyMap.get(labels2DTO.getMenuid());

                                if (dyMethod == null) {
                                    CfLog.e("移除玩法:" + labels2DTO);
                                    labels2DTOIterator.remove();
                                } else {
                                    labels2DTO.setPrizeLevel(dyMethod.getPrizeLevel());
                                    labels2DTO.setPrizeGroup(dyMethod.getPrizeGroup());
                                    labels2DTO.setGroupName(labels1DTO.getTitle());
                                    labels2DTO.setCateName(label.getTitle());
                                    labels2DTO.setLotteryid(value.getLotteryid());
                                }
                            }
                        }
                    }
                }
                CfLog.e("耗时:" + System.currentTimeMillis() / 1000);
                //静态和动态数据替换
                menuMethods = menuMethodsData;
//                userMethods = dynamicUserMethodsData;
            }
            //加载网络数据初始化玩法
            getMenuMethods(lottery);
        }
        CfLog.e("耗时:" + System.currentTimeMillis() / 1000);
    }

//    private void getUserMethods(Lottery lottery) {
//        Disposable disposable = model.getUserMethodsData().subscribeWith(new HttpCallBack<UserMethodsResponse>() {
//            @Override
//            public void onResult(UserMethodsResponse response) {
//                if (response.getData() != null && menuMethods != null) {
//                    LotteryDataManager.INSTANCE.setDynamicUserMethods(response);
//                    userMethods = response;
//                    initPlayCollection(lottery);
//                }
//
//            }
//        });
//        addSubscribe(disposable);
//    }

    private void getMenuMethods(Lottery lottery) {
        Disposable disposable = model.getMenuMethodsData(lottery.getAlias()).subscribeWith(new HttpCallBack<MenuMethodsResponse>() {
            @Override
            public void onResult(MenuMethodsResponse response) {
                CfLog.e("耗时:" + System.currentTimeMillis() / 1000);
                if (response.getData() != null) {
                    MenuMethodsData menuMethodsRemote = response.getData();

                    int resSize = (int) menuMethodsRemote.getLabels().stream()
                            .filter(Objects::nonNull)
                            .flatMap(method -> Optional.ofNullable(method.getLabels()).orElse(Collections.emptyList()).stream())
                            .filter(Objects::nonNull)
                            .flatMap(label -> Optional.ofNullable(label.getLabels()).orElse(Collections.emptyList()).stream())
                            .filter(Objects::nonNull)
                            .count();
                    int cacheSize = (int) menuMethods.getLabels().stream()
                            .filter(Objects::nonNull)
                            .flatMap(method -> Optional.ofNullable(method.getLabels()).orElse(Collections.emptyList()).stream())
                            .filter(Objects::nonNull)
                            .flatMap(label -> Optional.ofNullable(label.getLabels()).orElse(Collections.emptyList()).stream())
                            .filter(Objects::nonNull)
                            .count();
                    if (resSize != cacheSize) {
                        // 缓存数据落后于实时数据
//                        $cache.remove('methods-dynamic');
                    }

                    if (menuMethods != null) { //替换本地数据
                        // 构建远程数据的映射表
                        Map<String, MenuMethodsData.LabelsDTO.Labels1DTO.Labels2DTO> labels2DTORemoteMap = new HashMap<>();
                        Map<String, MenuMethodsData.LabelsDTO.Labels1DTO> labels1DTORemoteMap = new HashMap<>();
                        Map<String, MenuMethodsData.LabelsDTO> labelsDTORemoteMap = new HashMap<>();
                        for (MenuMethodsData.LabelsDTO labelsDTORemote : menuMethodsRemote.getLabels()) {
                            if (labelsDTORemote.getLabels() != null) {
                                for (MenuMethodsData.LabelsDTO.Labels1DTO labels1DTORemote : labelsDTORemote.getLabels()) {
                                    if (labels1DTORemote.getLabels() != null) {
                                        for (MenuMethodsData.LabelsDTO.Labels1DTO.Labels2DTO labels2DTORemote : labels1DTORemote.getLabels()) {
                                            labels2DTORemoteMap.put(labels2DTORemote.getMenuid(), labels2DTORemote);
                                            labels1DTORemoteMap.put(labels2DTORemote.getMenuid(), labels1DTORemote);
                                            labelsDTORemoteMap.put(labels2DTORemote.getMenuid(), labelsDTORemote);
                                        }
                                    }
                                }
                            }
                        }

                        // 遍历本地数据并更新
                        if (menuMethods.getLabels() != null) {
                            for (MenuMethodsData.LabelsDTO labelsDTOLocal : menuMethods.getLabels()) {
                                if (labelsDTOLocal.getLabels() != null) {
                                    for (MenuMethodsData.LabelsDTO.Labels1DTO labels1DTOLocal : labelsDTOLocal.getLabels()) {
                                        if (labels1DTOLocal.getLabels() != null) {
                                            Iterator<MenuMethodsData.LabelsDTO.Labels1DTO.Labels2DTO> iterator = labels1DTOLocal.getLabels().iterator();
                                            while (iterator.hasNext()) {
                                                MenuMethodsData.LabelsDTO.Labels1DTO.Labels2DTO labels2DTOLocal = iterator.next();
                                                try {
                                                    String menuid = labels2DTOLocal.getMenuid();
                                                    if (labels2DTORemoteMap.get(menuid) != null) {
                                                        // 如果远程数据中存在对应的 menuid，则更新本地数据
                                                        MenuMethodsData.LabelsDTO.Labels1DTO.Labels2DTO labels2DTORemote = labels2DTORemoteMap.get(menuid);
                                                        MenuMethodsData.LabelsDTO.Labels1DTO labels1DTORemote = labels1DTORemoteMap.get(menuid);
                                                        MenuMethodsData.LabelsDTO labelsDTORemote = labelsDTORemoteMap.get(menuid);
                                                        labelsDTOLocal.setDyTitle(labelsDTORemote.getTitle());
                                                        labels1DTOLocal.setDyTitle(labels1DTORemote.getTitle());
                                                        labels2DTOLocal.setName(labels2DTORemote.getName());
                                                        labels2DTOLocal.setMethoddesc(labels2DTORemote.getMethoddesc());
                                                        labels2DTOLocal.setMethodexample(labels2DTORemote.getMethodexample());
                                                        labels2DTOLocal.setMethodhelp(labels2DTORemote.getMethodhelp());
                                                        labels2DTOLocal.setDescription(labels2DTORemote.getDescription());
                                                        labels2DTOLocal.setShowStr(labels2DTORemote.getShowStr());
                                                        labels2DTOLocal.setCodeSp(labels2DTORemote.getCodeSp());
                                                        labels2DTOLocal.setMoneyModes(labels2DTORemote.getMoneyModes());
                                                        if (TextUtils.isEmpty(labels2DTOLocal.getDefaultposition())) {
                                                            labels2DTOLocal.setDefaultposition(labels2DTORemote.getDefaultposition());
                                                        }
                                                        labels2DTOLocal.setCateTitle(labelsDTORemote.getTitle());
                                                        labels2DTOLocal.setGroupTitle(labels1DTORemote.getTitle());
                                                        if (labels2DTOLocal.getSelectarea() != null && labels2DTOLocal.getSelectarea().getLayout() != null) {
                                                            List<MenuMethodsData.LabelsDTO.Labels1DTO.Labels2DTO.SelectareaDTO.LayoutDTO> layoutDTOLocal = labels2DTOLocal.getSelectarea().getLayout();
                                                            List<MenuMethodsData.LabelsDTO.Labels1DTO.Labels2DTO.SelectareaDTO.LayoutDTO> layoutDTORemote = labels2DTORemote.getSelectarea().getLayout();
                                                            for (int index = 0; index < layoutDTOLocal.size(); index++) {
                                                                MenuMethodsData.LabelsDTO.Labels1DTO.Labels2DTO.SelectareaDTO.LayoutDTO layout = layoutDTOLocal.get(index);
                                                                layout.setTitle(layoutDTORemote.get(index).getTitle());
                                                            }
                                                        }
                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                    CfLog.e("移除" + new Gson().toJson(labels2DTOLocal));
                                                    iterator.remove();
                                                }
                                            }
                                        }

                                    }
                                }
                            }
                        }
                    } else {
                        menuMethods = menuMethodsRemote;
                    }

//                    UserMethodsResponse userMethodsData = LotteryDataManager.INSTANCE.getDynamicUserMethods();
//                    if (userMethodsData == null) {
//                        getUserMethods(lottery);
//                    } else {
//                        userMethods = userMethodsData;
//                        initPlayCollection(lottery);
//                    }
                    CfLog.e("耗时:" + System.currentTimeMillis() / 1000);
                    initPlayCollection(lottery);
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
        Disposable disposable = model.getUserBalance().subscribeWith(new HttpCallBack<BalanceResponse>() {
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

        List<UserMethodsResponse.DataDTO.PrizeGroupDTO> prizeGroup = currentBetModel.getValue().getUserMethodData().getPrizeGroup();

        if (prizeGroup == null) {
            ToastUtils.showError("无返点选项");
            return;
        }

        for (UserMethodsResponse.DataDTO.PrizeGroupDTO prizeGroupDTO : prizeGroup) {
            popupMenu.getMenu().add(prizeGroupDTO.getLabel());
        }

        // 设置菜单项点击事件
        popupMenu.setOnMenuItemClickListener(item -> {
            CharSequence title = item.getTitle();
            if (title != null) {
                for (UserMethodsResponse.DataDTO.PrizeGroupDTO prizeGroupDTO : currentBetModel.getValue().getUserMethodData().getPrizeGroup()) {
                    if (prizeGroupDTO.getLabel().contentEquals(title)) {
                        prizeData.setValue(prizeGroupDTO);
                        //保存选中的奖金组
                        SPUtils.getInstance().put(currentBetModel.getValue().getUserMethodData().getMenuid() + currentBetModel.getValue().getUserMethodData().getMethodid() + currentBetModel.getValue().getUserMethodData().getName(), prizeGroupDTO.getLabel());
                        return true;
                    }
                }
            }
            return true;
        });

        // 显示 PopupMenu
        popupMenu.show();
    }

    /**
     * 添加选号
     */
    public void saveBetOrder() {
        if (betLiveData.getValue() != null) {
            ArrayList<LotteryOrderModel> orderModels = new ArrayList<>(betCartOrdersLiveData.getValue() != null ? betCartOrdersLiveData.getValue() : Collections.emptyList());

            for (LotteryBetRequest.BetOrderData orderData : betLiveData.getValue()) {
                LotteryOrderModel lotteryOrderModel = new LotteryOrderModel();
                UserMethodsResponse.DataDTO.PrizeGroupDTO prize = prizeData.getValue();
                LotteryMoneyData money = moneyLiveData.getValue();
                orderData.setOmodel(prize.getValue());
                if (lotteryLiveData.getValue() != null && "lhc".equals(lotteryLiveData.getValue().getLinkType())) {
                    orderData.setMode(5);//六合彩写死mode
                    orderData.setTimes((int) orderData.getMoney());
                } else {
                    orderData.setMode(money.getMoneyModel().getModelId());
                    orderData.setTimes(money.getFactor());
                }

                lotteryOrderModel.setBetOrderData(orderData);
                if (!TextUtils.isEmpty(prize.getLabel())) {
                    lotteryOrderModel.setPrizeLabel(processPrize(prize.getLabel()));
                }
                lotteryOrderModel.setMoneyData(money);
                orderModels.add(lotteryOrderModel);
            }

            betCartOrdersLiveData.setValue(orderModels);
            betLiveData.setValue(null);
        }
    }

    private String processPrize(String targetLabel) {
        // 奖金为范围区间的时候
        if (targetLabel.contains("~")) {
            String[] parts = targetLabel.split(" ");
            String result = parts.length > 1 ? String.join(" ", Arrays.copyOfRange(parts, 1, parts.length)) : "";
            return "模式:" + result;
        }
        // 奖金不是范围区间的时候
        else {
            String[] firstSplit = targetLabel.split("-"); // 先按 "-" 分割，取第一部分
            String[] spaceSplit = firstSplit[0].split(" "); // 再按空格分割
            String result = spaceSplit.length > 1 ? spaceSplit[1] : ""; // 取第二个单词
            return "模式:" + result;
        }
    }

    /**
     * 一键投注
     */
    public void quickBet() {
        if (betLiveData.getValue() != null) {
            ArrayList<LotteryBetRequest.BetOrderData> betOrders = new ArrayList<>();
            for (LotteryBetRequest.BetOrderData orderData : betLiveData.getValue()) {
                UserMethodsResponse.DataDTO.PrizeGroupDTO prize = prizeData.getValue();
                LotteryMoneyData money = moneyLiveData.getValue();
                orderData.setOmodel(prize.getValue());
                orderData.setMoneyName(money.getMoneyModel().getName());
                if (lotteryLiveData.getValue() != null && "lhc".equals(lotteryLiveData.getValue().getLinkType())) {
                    orderData.setMode(5);
                } else {
                    orderData.setMode(money.getMoneyModel().getModelId());
                }
                betOrders.add(orderData);
            }
            LotteryBetConfirmDialogFragment.show(mActivity.get(), betOrders);
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        LotteryBetsModel lotteryBetsModel = betModels.get(tab.getPosition());
        currentBetModel.setValue(lotteryBetsModel);

        Lottery lottery = lotteryLiveData.getValue();
        UserMethodsResponse.DataDTO userMethodData = lotteryBetsModel.getUserMethodData();
        MenuMethodsData.LabelsDTO menuMethodLabel = lotteryBetsModel.getMenuMethodLabel();
        MenuMethodsData.LabelsDTO.Labels1DTO.Labels2DTO currentl2 = lotteryBetsModel.getMenuMethodLabelData();
        for (MenuMethodsData.LabelsDTO.Labels1DTO l1 : menuMethodLabel.getLabels()) {
            for (MenuMethodsData.LabelsDTO.Labels1DTO.Labels2DTO l2 : l1.getLabels()) {
                //设置当前玩法
                if (l2.getMenuid().equals(currentl2.getMenuid()) && l2.getMethodid().equals(currentl2.getMethodid())) {

                    RulesEntryData rulesEntryData = new RulesEntryData();
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
                    RulesEntryData.CurrentCategoryDTO currentCategoryDTO = new RulesEntryData.CurrentCategoryDTO();
                    currentCategoryDTO.setFlag(lottery.getAlias());
                    rulesEntryData.setCurrentMethod(currentMethodDTO);
                    rulesEntryData.setCurrentCategory(currentCategoryDTO);
                    KLog.i("RulesEntryData3", new Gson().toJson(rulesEntryData));
                    EventBus.getDefault().post(new EventVo(EventConstant.EVENT_RULES_ENTRY_DATA, rulesEntryData));
                }
            }
        }

        if (lotteryBetsModel.getUserMethodData().getPrizeGroup() != null && lotteryBetsModel.getUserMethodData().getPrizeGroup().size() > 0) {
            String defaultLabel = SPUtils.getInstance().getString(lotteryBetsModel.getUserMethodData().getMenuid() + lotteryBetsModel.getUserMethodData().getMethodid() + lotteryBetsModel.getUserMethodData().getName(), lotteryBetsModel.getUserMethodData().getPrizeGroup().get(0).getLabel());
            for (UserMethodsResponse.DataDTO.PrizeGroupDTO prizeGroupDTO : lotteryBetsModel.getUserMethodData().getPrizeGroup()) {
                if (prizeGroupDTO.getLabel().equals(defaultLabel)) {
                    prizeData.setValue(prizeGroupDTO);
                }
            }
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
    public void rule(RulesEntryData.BetDTO betDTO) {

        Lottery lottery = lotteryLiveData.getValue();
        LotteryBetsModel lotteryBetsModel = currentBetModel.getValue();
        UserMethodsResponse.DataDTO userMethodData = lotteryBetsModel.getUserMethodData();
        MenuMethodsData.LabelsDTO menuMethodLabel = lotteryBetsModel.getMenuMethodLabel();
        MenuMethodsData.LabelsDTO.Labels1DTO.Labels2DTO currentl2 = lotteryBetsModel.getMenuMethodLabelData();
        RulesEntryData rulesEntryData = new RulesEntryData();
        rulesEntryData.setType(lottery.getLinkType());

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

                UserMethodsResponse.DataDTO um = currentBetModel.getValue().getUserMethodData();
                if (l2.getMenuid().equals(um.getMenuid()) && l2.getMethodid().equals(um.getMethodid())) {
                    methodsDTO.setPrizeLevel(userMethodData.getPrizeLevel());
                    methodsDTO.setPrizeGroup(userMethodData.getPrizeGroup());
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

        BettingEntryRule.getInstance().startEngine(rulesEntryData, result -> {
            List<LotteryBetRequest.BetOrderData> betOrderlist = new ArrayList<>();
            for (RulesEntryData.SubmitDTO submitDTO : result) {
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
                    betOrderlist.add(betOrderData);
                }
            }

            if (betOrderlist.isEmpty()) {
                betLiveData.postValue(null);
            } else {
                betLiveData.postValue(betOrderlist);
            }
        });
    }

    /**
     * 清除单挑
     */
    public void doClear() {

        ArrayList<LotteryOrderModel> orderList = betOrdersLiveData.getValue();
        List<LotteryBetRequest.BetOrderData> curOrder = betLiveData.getValue();

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
            if (curOrder.get(0).isSolo()) {
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

    /**
     * 本地数据和远程的字段覆盖
     *
     * @param methodData
     * @param alias
     * @param res
     */
    private void processLocalMethodReplaceRemote(Map<String, Object> methodData, String
            alias, Map<String, Object> res) {

    }

}
