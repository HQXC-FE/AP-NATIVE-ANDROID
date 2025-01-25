package com.xtree.lottery.ui.view.betviews;

import static com.xtree.lottery.utils.LotteryAnalyzer.INPUT_PLAYS_MAP;

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
import com.xtree.lottery.databinding.LayoutBetRacingBinding;
import com.xtree.lottery.ui.lotterybet.model.LotteryBetsModel;
import com.xtree.lottery.ui.view.viewmodel.BetRacingViewModel;
import com.xtree.lottery.utils.LotteryAnalyzer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by KAKA on 2024/11/29.
 * Describe: 竞速投注视图
 */
public class BetRacingView extends BetBaseView {

    private LayoutBetRacingBinding binding;

    public BetRacingView(@NonNull Context context) {
        super(context);
        binding = LayoutBetRacingBinding.inflate(LayoutInflater.from(context), this, true);
        initView();
    }

    public BetRacingView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        binding = LayoutBetRacingBinding.inflate(LayoutInflater.from(context), this, true);
        initView();
    }

    private void initView() {
        BetRacingViewModel model = new BetRacingViewModel();
        binding.setModel(model);

        binding.getModel().lotteryNumbs.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
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
        String s = binding.getModel().lotteryNumbs.get();

        if (TextUtils.isEmpty(s)) {
            return;
        }

        LotteryBetsModel betsModel = getModel();

        //无投注规则数据
        if (betsModel == null) {
            return;
        }

        //是否存在此玩法规则
        String rules = INPUT_PLAYS_MAP.get(betsModel.getTitle());
        if (rules == null) {
            return;
        }

        String[] split = rules.split(",");

        if (split.length < 3) {
            return;
        }

        Set<String> validNumbers = LotteryAnalyzer.getValidNumbers(s, Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));

        if (validNumbers.size() == 0) {
            betData.set(null);
            return;
        }
        Set<String> updatedNumbers = new HashSet<>();
        for (String n : validNumbers) {
            updatedNumbers.add(n.replaceAll(" ", "&"));
        }
        //替换原集合内容
        validNumbers = updatedNumbers;
        //07&08|01&06|03&09|04&05|10&04
        String codes = String.join("|", validNumbers);

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
    }

    private void initTip() {
        LotteryBetsModel model = getModel();
        String methoddesc = model.getMenuMethodLabelData().getMethoddesc();
        String methodexample = model.getMenuMethodLabelData().getMethodexample();
        String methodhelp = model.getMenuMethodLabelData().getMethodhelp();

        binding.betRacingTip.setVisibility(methoddesc == null || methoddesc.isEmpty() ? GONE : VISIBLE);
        binding.betRacingExample.setVisibility(methodexample == null || methodexample.isEmpty() ? GONE : VISIBLE);
        binding.betRacingHelp.setVisibility(methodhelp == null || methodhelp.isEmpty() ? GONE : VISIBLE);

        if (!TextUtils.isEmpty(methoddesc)) {
            binding.betRacingTip.setText(String.valueOf(Html.fromHtml(methoddesc)));
        }
        binding.betRacingExample.setOnClickListener(v -> showTipDialog(String.valueOf(Html.fromHtml(methodexample))));
        binding.betRacingHelp.setOnClickListener(v -> showTipDialog(String.valueOf(Html.fromHtml(methodhelp))));
    }

    @Override
    public void setModel(LotteryBetsModel model, UserMethodsResponse.DataDTO.PrizeGroupDTO prizeGroup) {
        super.setModel(model, prizeGroup);
        binding.getModel().initData(model);

        initTip();
    }

    @Override
    public void clearBet() {
        binding.getModel().lotteryNumbs.set("");
    }
}
