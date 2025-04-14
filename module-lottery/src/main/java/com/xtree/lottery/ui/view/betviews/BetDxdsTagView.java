package com.xtree.lottery.ui.view.betviews;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.Observable;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.GridLayoutManager;

import com.xtree.base.vo.UserMethodsResponse;
import com.xtree.lottery.data.config.Lottery;
import com.xtree.lottery.data.source.request.LotteryBetRequest;
import com.xtree.lottery.databinding.LayoutBetDxdstagBinding;
import com.xtree.lottery.rule.betting.data.RulesEntryData;
import com.xtree.lottery.ui.lotterybet.model.LotteryBetsModel;
import com.xtree.lottery.ui.view.viewmodel.BetDxdsTagViewModel;

import java.util.ArrayList;

/**
 * Created by KAKA on 2024/5/6.
 * Describe: 非数字标签投注视图
 */
public class BetDxdsTagView extends BetBaseView {

    private LayoutBetDxdstagBinding binding;

    public BetDxdsTagView(@NonNull Context context) {
        super(context);
        binding = LayoutBetDxdstagBinding.inflate(LayoutInflater.from(context), this, true);
        initView();
    }

    public BetDxdsTagView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        binding = LayoutBetDxdstagBinding.inflate(LayoutInflater.from(context), this, true);
        initView();
    }

    private void initView() {

        BetDxdsTagViewModel model = new BetDxdsTagViewModel();
        model.layoutManager.set(new GridLayoutManager(getContext(), 4));
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
        String codes = binding.getModel().codesData.get();
        LotteryBetsModel betsModel = getModel();
        if (!TextUtils.isEmpty(codes) && betsModel != null) {
            LotteryBetRequest.BetOrderData betOrderData = new LotteryBetRequest.BetOrderData();
            betOrderData.setCodes(codes);
            betOrderData.setDesc(betsModel.getTitle());
            betOrderData.setMenuid(betsModel.getMenuMethodLabelData().getMenuid());
            betOrderData.setMethodid(betsModel.getMenuMethodLabelData().getMethodid());
            betOrderData.setType(betsModel.getMenuMethodLabelData().getSelectarea().getType());

            ArrayList<LotteryBetRequest.BetOrderData> orderList = new ArrayList<>();
            orderList.add(betOrderData);
            betCodes.set(binding.getModel().reFormatCode(codes));
            betData.set(orderList);
        } else {
            betData.set(null);
        }
    }

    private void initTip() {
        LotteryBetsModel model = getModel();
        String methoddesc = model.getMenuMethodLabelData().getMethoddesc();
        String methodexample = model.getMenuMethodLabelData().getMethodexample();
        String methodhelp = model.getMenuMethodLabelData().getMethodhelp();

        binding.betDxdstagTip.setVisibility(methoddesc == null || methoddesc.isEmpty() ? GONE : VISIBLE);
        binding.betDxdstagExample.setVisibility(methodexample == null || methodexample.isEmpty() ? GONE : VISIBLE);
        binding.betDxdstagHelp.setVisibility(methodhelp == null || methodhelp.isEmpty() ? GONE : VISIBLE);

        if (!TextUtils.isEmpty(methoddesc)) {
            binding.betDxdstagTip.setText(String.valueOf(Html.fromHtml(methoddesc)));
        }
        binding.betDxdstagExample.setOnClickListener(v -> showTipDialog(String.valueOf(Html.fromHtml(methodexample))));
        binding.betDxdstagHelp.setOnClickListener(v -> showTipDialog(String.valueOf(Html.fromHtml(methodhelp))));
    }

    @Override
    public void setModel(LiveData<RulesEntryData.RulesResultData> rulesResultDataLiveData, LotteryBetsModel model, UserMethodsResponse.DataDTO.PrizeGroupDTO prizeGroup, Lottery lottery) {
        super.setModel(rulesResultDataLiveData, model, prizeGroup, lottery);
        binding.getModel().initData(model);
        initTip();
    }

    @Override
    public void clearBet() {
        binding.getModel().clearBet();
    }
}
