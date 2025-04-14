package com.xtree.lottery.ui.view.betviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.Observable;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.xtree.base.vo.UserMethodsResponse;
import com.xtree.lottery.data.config.Lottery;
import com.xtree.lottery.databinding.LayoutBetHandicap1Binding;
import com.xtree.lottery.rule.betting.data.RulesEntryData;
import com.xtree.lottery.ui.lotterybet.model.LotteryBetsModel;
import com.xtree.lottery.ui.view.viewmodel.BetHandicap1ViewModel;

/**
 * Created by KAKA on 2024/5/20.
 * Describe: 盘口投注 整合视图
 */
public class BetHandicap1View extends BetBaseView {

    private LayoutBetHandicap1Binding binding;

    public BetHandicap1View(@NonNull Context context) {
        super(context);
        binding = LayoutBetHandicap1Binding.inflate(LayoutInflater.from(context), this, true);
        initView();
    }

    public BetHandicap1View(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        binding = LayoutBetHandicap1Binding.inflate(LayoutInflater.from(context), this, true);
        initView();
    }

    private void initView() {

        BetHandicap1ViewModel model = new BetHandicap1ViewModel();
        binding.setModel(model);

        binding.getModel().codesData.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                setBetData();
            }
        });
    }

    /**
     * 保留投注数据
     */
    private void setBetData() {
        betCodes.set(binding.getModel().codesData.get());
        betData.set(binding.getModel().codesData.get());
    }

    @Override
    public void setModel(LiveData<RulesEntryData.RulesResultData> rulesResultDataLiveData, LotteryBetsModel model, UserMethodsResponse.DataDTO.PrizeGroupDTO prizeGroup, Lottery lottery) {
        super.setModel(rulesResultDataLiveData, model, prizeGroup, lottery);
        binding.getModel().initData(model);

        switch (model.getHandicapMethodData().getCategory()) {
            case "整合":
            case "炸金花":
            case "龙虎斗":
                binding.getModel().layoutManager.set(new LinearLayoutManager(getContext()));
                break;
            case "牛牛":
            case "百家乐":
            case "梭哈":
                binding.getModel().layoutManager.set(new GridLayoutManager(getContext(), 3));
                break;
            default:
                break;
        }

    }

    @Override
    public void clearBet() {
        binding.getModel().clearBet();
    }
}
