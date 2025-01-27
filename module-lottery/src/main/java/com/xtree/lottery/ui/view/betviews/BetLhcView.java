package com.xtree.lottery.ui.view.betviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.Observable;

import com.xtree.base.vo.UserMethodsResponse;
import com.xtree.lottery.data.source.request.LotteryBetRequest;
import com.xtree.lottery.databinding.LayoutBetLhcBinding;
import com.xtree.lottery.ui.lotterybet.model.LotteryBetsModel;
import com.xtree.lottery.ui.view.viewmodel.BetLhcViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class BetLhcView extends BetBaseView {

    private LayoutBetLhcBinding binding;

    public BetLhcView(@NonNull Context context) {
        this(context, null);
    }

    public BetLhcView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        binding = LayoutBetLhcBinding.inflate(LayoutInflater.from(context), this, true);
        initView();
    }

    @Override
    public void clearBet() {

    }

    /**
     * 保留投注数据
     */
    private void setBetData() {
        List<Map<String, String>> lotteryNumbs = binding.getM().lotteryNumbs.get();

        LotteryBetsModel betsModel = getModel();
        if (!lotteryNumbs.isEmpty() && betsModel != null) {
            ArrayList<LotteryBetRequest.BetOrderData> orderList = new ArrayList<>();
            for (Map<String, String> lotteryNumb : lotteryNumbs) {
                LotteryBetRequest.BetOrderData betOrderData = new LotteryBetRequest.BetOrderData();
                betOrderData.setCodes(lotteryNumb.get("num"));
                betOrderData.setDesc(betsModel.getTitle());
                betOrderData.setMenuid(lotteryNumb.get("menuid"));
                betOrderData.setMethodid(lotteryNumb.get("methodid"));
                betOrderData.setType(betsModel.getMenuMethodLabelData().getType());
                orderList.add(betOrderData);
            }
            betCodes.set(lotteryNumbs);

            betData.set(orderList);
        } else {
            betData.set(null);
        }
    }

    private void initView() {
        BetLhcViewModel model = new BetLhcViewModel();
        binding.setM(model);

        binding.getM().lotteryNumbs.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                setBetData();
            }
        });
    }

    @Override
    public void setModel(LotteryBetsModel model, UserMethodsResponse.DataDTO.PrizeGroupDTO prizeGroup) {
        super.setModel(model, prizeGroup);
        binding.getM().initData(model, prizeGroup);
    }

}
