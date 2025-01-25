package com.xtree.lottery.ui.view.betviews;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.Observable;

import com.xtree.base.vo.UserMethodsResponse;
import com.xtree.lottery.data.source.request.LotteryBetRequest;
import com.xtree.lottery.databinding.LayoutBetDxdsBinding;
import com.xtree.lottery.ui.lotterybet.model.LotteryBetsModel;
import com.xtree.lottery.ui.view.viewmodel.BetDxdsViewModel;

import java.util.ArrayList;

/**
 * Created by KAKA on 2024/5/3.
 * Describe: 非数字投注视图
 */
public class BetDxdsView extends BetBaseView {

    private LayoutBetDxdsBinding binding;

    public BetDxdsView(@NonNull Context context) {
        super(context);
        binding = LayoutBetDxdsBinding.inflate(LayoutInflater.from(context), this, true);
        initView();
    }

    public BetDxdsView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        binding = LayoutBetDxdsBinding.inflate(LayoutInflater.from(context), this, true);
        initView();
    }

    private void initView() {
        BetDxdsViewModel model = new BetDxdsViewModel();
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
        //判断是否包含有效数据
        String numbs = codes.replaceAll("[|,&]", "");
        if (!TextUtils.isEmpty(numbs) && betsModel != null) {
            LotteryBetRequest.BetOrderData betOrderData = new LotteryBetRequest.BetOrderData();
            betOrderData.setCodes(codes);
            betOrderData.setDesc(betsModel.getTitle());
            betOrderData.setMenuid(betsModel.getMenuMethodLabelData().getMenuid());
            betOrderData.setMethodid(betsModel.getMenuMethodLabelData().getMethodid());
            betOrderData.setType(betsModel.getMenuMethodLabelData().getSelectarea().getType());

            ArrayList<LotteryBetRequest.BetOrderData> orderList = new ArrayList<>();
            orderList.add(betOrderData);

            if (codes.contains("|")) {
                betCodes.set(binding.getModel().reFormatCode(codes));
            } else {
                betCodes.set(binding.getModel().reFormatCode2(codes));
            }
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

        binding.betDxdsTip.setVisibility(methoddesc == null || methoddesc.isEmpty() ? GONE : VISIBLE);
        binding.betDxdsExample.setVisibility(methodexample == null || methodexample.isEmpty() ? GONE : VISIBLE);
        binding.betDxdsHelp.setVisibility(methodhelp == null || methodhelp.isEmpty() ? GONE : VISIBLE);

        if (!TextUtils.isEmpty(methoddesc)) {
            binding.betDxdsTip.setText(String.valueOf(Html.fromHtml(methoddesc)));
        }
        binding.betDxdsExample.setOnClickListener(v -> showTipDialog(String.valueOf(Html.fromHtml(methodexample))));
        binding.betDxdsHelp.setOnClickListener(v -> showTipDialog(String.valueOf(Html.fromHtml(methodhelp))));
    }

    @Override
    public void setModel(LotteryBetsModel model, UserMethodsResponse.DataDTO.PrizeGroupDTO prizeGroup) {
        super.setModel(model, prizeGroup);
        binding.getModel().initData(model);
        initTip();
    }

    @Override
    public void clearBet() {
        binding.getModel().clearBet();
    }
}
