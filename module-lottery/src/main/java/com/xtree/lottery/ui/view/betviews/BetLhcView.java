package com.xtree.lottery.ui.view.betviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.Observable;

import com.xtree.base.vo.UserMethodsResponse;
import com.xtree.lottery.databinding.LayoutBetLhcBinding;
import com.xtree.lottery.ui.lotterybet.model.LotteryBetsModel;
import com.xtree.lottery.ui.view.viewmodel.BetLhcViewModel;

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
        binding.getM().initData(model,prizeGroup);
    }

}
