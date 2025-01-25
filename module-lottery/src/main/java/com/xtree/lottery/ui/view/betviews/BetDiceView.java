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
import com.xtree.lottery.databinding.LayoutBetDiceBinding;
import com.xtree.lottery.ui.lotterybet.model.LotteryBetsModel;
import com.xtree.lottery.ui.view.viewmodel.BetDiceViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by KAKA on 2024/5/3.
 * Describe: 快三骰子投注视图
 */
public class BetDiceView extends BetBaseView {

    private LayoutBetDiceBinding binding;

    public BetDiceView(@NonNull Context context) {
        this(context,null);
    }

    public BetDiceView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        binding = LayoutBetDiceBinding.inflate(LayoutInflater.from(context), this, true);
        initView();
    }

    private void initView() {
        BetDiceViewModel model = new BetDiceViewModel(getMethod());
        binding.setM(model);

        binding.getM().lotteryNumbs.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                setBetData();
            }
        });
    }

    public BetDiceViewModel.Dice getMethod() {
        return BetDiceViewModel.Dice.Normal;
    }

    /**
     * 保留投注数据
     */
    private void setBetData() {
        List<List<String>> lotteryNumbs = binding.getM().lotteryNumbs.get();

        LotteryBetsModel betsModel = getModel();
        //判断是否包含有效数据
        String codes = null;
        if (lotteryNumbs.size() > 1 && !lotteryNumbs.get(1).isEmpty()) {
            codes = String.join("|", Arrays.asList(String.join("&", lotteryNumbs.get(0)), String.join("&", lotteryNumbs.get(1))));
        } else if (lotteryNumbs.size() > 0 && !lotteryNumbs.get(0).isEmpty()) {
            codes = String.join("&", lotteryNumbs.get(0));
        }
        if (!TextUtils.isEmpty(codes) && betsModel != null) {
            LotteryBetRequest.BetOrderData betOrderData = new LotteryBetRequest.BetOrderData();
            betOrderData.setCodes(codes);
            betOrderData.setDesc(betsModel.getTitle());
            betOrderData.setMenuid(betsModel.getMenuMethodLabelData().getMenuid());
            betOrderData.setMethodid(betsModel.getMenuMethodLabelData().getMethodid());
            betOrderData.setType(betsModel.getMenuMethodLabelData().getSelectarea().getType());
            ArrayList<LotteryBetRequest.BetOrderData> orderList = new ArrayList<>();
            orderList.add(betOrderData);

            betCodes.set(lotteryNumbs);

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

        binding.betTip.setVisibility(methoddesc == null || methoddesc.isEmpty() ? GONE : VISIBLE);
        binding.betExample.setVisibility(methodexample == null || methodexample.isEmpty() ? GONE : VISIBLE);
        binding.betHelp.setVisibility(methodhelp == null || methodhelp.isEmpty() ? GONE : VISIBLE);

        if (!TextUtils.isEmpty(methoddesc)) {
            binding.betTip.setText(String.valueOf(Html.fromHtml(methoddesc)));
        }
        binding.betExample.setOnClickListener(v -> showTipDialog(String.valueOf(Html.fromHtml(methodexample))));
        binding.betHelp.setOnClickListener(v -> showTipDialog(String.valueOf(Html.fromHtml(methodhelp))));
    }

    @Override
    public void setModel(LotteryBetsModel model, UserMethodsResponse.DataDTO.PrizeGroupDTO prizeGroup) {
        super.setModel(model,prizeGroup);
        binding.getM().initData(model);
        initTip();

    }

    @Override
    public void clearBet() {
        binding.getM().clear();
    }

}
