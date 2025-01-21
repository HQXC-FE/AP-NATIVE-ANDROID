package com.xtree.lottery.ui.view.betviews;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.Observable;

import com.xtree.lottery.databinding.LayoutBetDiceBinding;
import com.xtree.lottery.ui.lotterybet.model.LotteryBetsModel;
import com.xtree.lottery.ui.view.viewmodel.BetDiceViewModel;

/**
 * Created by KAKA on 2024/5/3.
 * Describe: 快三骰子投注视图
 */
public class BetDiceView extends BetBaseView {

    private LayoutBetDiceBinding binding;

    public BetDiceView(@NonNull Context context) {
        super(context);
        binding = LayoutBetDiceBinding.inflate(LayoutInflater.from(context), this, true);
        initView();
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

    public BetDiceViewModel.Dice getMethod(){
        return BetDiceViewModel.Dice.Normal;
    }

    /**
     * 保留投注数据
     */
    private void setBetData() {
//        String codes = binding.getM().lotteryNumbs.get();
//        LotteryBetsModel betsModel = getModel();
//        //判断是否包含有效数据
//        String numbs = codes.replaceAll("[|,&]", "");
//        if (!TextUtils.isEmpty(numbs) && betsModel != null) {
//            LotteryBetRequest.BetOrderData betOrderData = new LotteryBetRequest.BetOrderData();
//            betOrderData.setCodes(codes.trim());
//            betOrderData.setDesc(betsModel.getTitle());
//            betOrderData.setMenuid(betsModel.getMenuMethodLabelData().getMenuid());
//            betOrderData.setMethodid(betsModel.getMenuMethodLabelData().getMethodid());
//            betOrderData.setType(betsModel.getMenuMethodLabelData().getSelectarea().getType());
//            //设置位数
//            if (Boolean.TRUE.equals(binding.getModel().showSeatView.get())) {
//                String formatSeats = binding.betDigitalSeatview.getFormatSeats();
//                if (formatSeats != null) {
//                    betOrderData.setPoschoose(formatSeats);
//                } else {
//                    //如果位数设置不符合规则，设置投注为无效
//                    betData.set(null);
//                    return;
//                }
//            }
//            ArrayList<LotteryBetRequest.BetOrderData> orderList = new ArrayList<>();
//            orderList.add(betOrderData);
//
//            if (codes.contains("|")) {
//                betCodes.set(binding.getModel().reFormatCode(codes));
//            } else {
//                betCodes.set(binding.getModel().reFormatCode2(codes));
//            }
//
//            betData.set(orderList);
//        } else {
//            betData.set(null);
//        }
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
    public void setModel(LotteryBetsModel model) {
        super.setModel(model);
        binding.getM().initData(model);
        initTip();

    }

    @Override
    public void clearBet() {
        binding.getM().clear();
    }

}
