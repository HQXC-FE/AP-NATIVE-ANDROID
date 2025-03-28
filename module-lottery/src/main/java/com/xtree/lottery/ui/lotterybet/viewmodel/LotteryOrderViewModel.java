package com.xtree.lottery.ui.lotterybet.viewmodel;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.drake.brv.BindingAdapter;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.xtree.base.mvvm.recyclerview.BaseDatabindingAdapter;
import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.base.utils.ClickUtil;
import com.xtree.base.widget.MsgDialog;
import com.xtree.base.widget.TipDialog;
import com.xtree.lottery.R;
import com.xtree.lottery.data.LotteryRepository;
import com.xtree.lottery.data.config.Lottery;
import com.xtree.lottery.data.source.request.LotteryBetRequest;
import com.xtree.lottery.ui.lotterybet.LotteryBetConfirmDialogFragment;
import com.xtree.lottery.ui.lotterybet.model.LotteryOrderModel;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import me.xtree.mvvmhabit.base.BaseViewModel;

/**
 * Created by KAKA on 2024/5/10.
 * Describe: 彩种投注订单详情viewModel
 */
public class LotteryOrderViewModel extends BaseViewModel<LotteryRepository> {

    public final MutableLiveData<ArrayList<BindModel>> datas = new MutableLiveData<>(new ArrayList<>());
    public final MutableLiveData<ArrayList<Integer>> itemType = new MutableLiveData<>(
            new ArrayList<Integer>() {
                {
                    add(R.layout.item_lottery_order);
                }
            });
    public final ArrayList<BindModel> bindModels = new ArrayList<>();
    //中奖通知
    public MutableLiveData<Boolean> winNotifi = new MutableLiveData<>(true);
    //是否是六合彩
    public MutableLiveData<Boolean> isLhc = new MutableLiveData<>(false);
    //选中的订单数
    public MutableLiveData<String> orderNums = new MutableLiveData<>();
    //共几注
    public MutableLiveData<String> betNums = new MutableLiveData<>();
    public LotteryBetsViewModel betsViewModel;
    //总金额
    public MutableLiveData<String> moneyNums = new MutableLiveData<>();
    private final Observer<List<LotteryOrderModel>> orderObserver = lotteryOrderModels -> {
        Lottery lottery = betsViewModel.lotteryLiveData.getValue();
        isLhc.setValue("lhc".equals(lottery.getLinkType()));
        if (lotteryOrderModels == null) {
            bindModels.clear();
            datas.setValue(bindModels);
            checkOrder();
            return;
        }

        bindModels.clear();
        for (LotteryOrderModel orderData : lotteryOrderModels) {
            StringBuilder stringBuilder = new StringBuilder();
            if (isLhc.getValue()) {
                stringBuilder
                        .append(orderData.getBetOrderData().getNums()).append("注 x ")
                        .append(orderData.getBetOrderData().getTimes()).append("倍 x ")
                        .append("元").append(" = ")
                        .append(BigDecimal.valueOf(orderData.getBetOrderData().getMoney()).toPlainString()).append("元");
            } else {
                stringBuilder
                        .append(orderData.getBetOrderData().getNums()).append("注 x ")
                        .append(orderData.getBetOrderData().getTimes()).append("倍 x ")
                        .append(orderData.getMoneyData().getMoneyModel().getName()).append(" = ")
                        .append(BigDecimal.valueOf(orderData.getBetOrderData().getMoney()).toPlainString()).append("元");
            }

            orderData.betMoney.set(stringBuilder.toString());
        }
        bindModels.addAll(lotteryOrderModels);
        datas.setValue(bindModels);
        checkOrder();
    };
    private WeakReference<FragmentActivity> mActivity = null;
    private BasePopupView pop;
    public BaseDatabindingAdapter.onBindListener onBindListener = new BaseDatabindingAdapter.onBindListener() {
        @Override
        public void onBind(@NonNull BindingAdapter.BindingViewHolder bindingViewHolder, @NonNull View view, int itemViewType) {

            if (itemViewType == R.layout.item_lottery_order) {
                //删除订单
                view.findViewById(R.id.item_lottery_order_delete).setOnClickListener(v -> {
                    if (ClickUtil.isFastClick()) {
                        return;
                    }
                    ArrayList<BindModel> bindModels = datas.getValue();
                    int modelPosition = bindingViewHolder.getModelPosition();
                    LotteryOrderModel model = (LotteryOrderModel) bindModels.get(modelPosition);

                    bindingViewHolder.getAdapter().getMutable().remove(modelPosition);
                    bindingViewHolder.getAdapter().notifyItemRemoved(modelPosition);

                    List<LotteryOrderModel> betOrders = betsViewModel.betCartOrdersLiveData.getValue();
                    betOrders.remove(model);
                    betsViewModel.betCartOrdersLiveData.setValue(new ArrayList<>(betOrders));

                    checkOrder();
                });
            }
        }

        @Override
        public void onItemClick(int modelPosition, int layoutPosition, int itemViewType) {
            LotteryOrderModel model = (LotteryOrderModel) bindModels.get(modelPosition);
            StringBuilder codeBuilder = new StringBuilder();
            codeBuilder.append(model.getBetOrderData().getDesc()).append("\n");
            showTipDialog(codeBuilder.toString());
        }
    };

    public LotteryOrderViewModel(@NonNull Application application) {
        super(application);
    }

    public LotteryOrderViewModel(@NonNull Application application, LotteryRepository model) {
        super(application, model);
    }

    public void initData(FragmentActivity mActivity) {
        setActivity(mActivity);
        betsViewModel.betCartOrdersLiveData.observeForever(orderObserver);
    }

    /**
     * 检查注单，更新信息
     */
    private void checkOrder() {
        int totalBet = 0;
        BigDecimal totalMoney = BigDecimal.ZERO;
        for (BindModel binmodel : bindModels) {
            LotteryOrderModel orderModel = (LotteryOrderModel) binmodel;
            LotteryBetRequest.BetOrderData betOrderData = orderModel.getBetOrderData();
            totalBet += betOrderData.getNums();
            totalMoney = totalMoney.add(BigDecimal.valueOf(betOrderData.getMoney()));
        }

        orderNums.setValue(String.valueOf(bindModels.size()));
        moneyNums.setValue(totalMoney.toPlainString());
        betNums.setValue(String.valueOf(totalBet));
    }

    private void setActivity(FragmentActivity mActivity) {
        this.mActivity = new WeakReference<>(mActivity);
    }

    /**
     * 提示弹窗
     */
    public void showTipDialog(String msg) {
        MsgDialog dialog = new MsgDialog(mActivity.get(), "已选号码", msg, true, new TipDialog.ICallBack() {
            @Override
            public void onClickLeft() {

            }

            @Override
            public void onClickRight() {
                if (pop != null) {
                    pop.dismiss();
                }
            }
        });

        pop = new XPopup.Builder(mActivity.get())
                .dismissOnTouchOutside(true)
                .dismissOnBackPressed(true)
                .asCustom(dialog).show();
    }

    public void goChasing() {
        ArrayList<BindModel> bindModels = datas.getValue();
        for (BindModel bindModel : bindModels) {
            LotteryOrderModel model = (LotteryOrderModel) bindModel;
            model.getBetOrderData().setTimes(1);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder
                    .append(model.getBetOrderData().getNums()).append("注 x ")
                    .append(model.getBetOrderData().getTimes()).append("倍 x ")
                    .append(model.getMoneyData().getMoneyModel().getName()).append(" = ")
                    .append(BigDecimal.valueOf(model.getBetOrderData().getMoney()).toPlainString()).append("元");
            model.betMoney.set(stringBuilder.toString());
        }
        for (LotteryOrderModel lotteryOrderModel : betsViewModel.betOrdersLiveData.getValue()) {
            lotteryOrderModel.getBetOrderData().setTimes(1);
        }
        datas.setValue(bindModels);

        checkOrder();
    }

    public void bet() {

        ArrayList<LotteryBetRequest.BetOrderData> betOrderData = new ArrayList<>();

        for (BindModel binmodel : bindModels) {
            LotteryOrderModel orderModel = (LotteryOrderModel) binmodel;
            betOrderData.add(orderModel.getBetOrderData());
        }

        LotteryBetConfirmDialogFragment.show(mActivity.get(), betOrderData);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        betsViewModel.betCartOrdersLiveData.removeObserver(orderObserver);
        betsViewModel = null;
        if (mActivity != null) {
            mActivity.clear();
            mActivity = null;
        }
    }
}
