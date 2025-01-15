package com.xtree.lottery.ui.view.betviews;

import static com.xtree.lottery.utils.LotteryAnalyzer.INPUT_PLAYS_MAP;

import android.content.Context;
import android.text.Html;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.Observable;

import com.xtree.base.utils.filter.LotteryInputFilter;
import com.xtree.lottery.R;
import com.xtree.lottery.data.source.request.LotteryBetRequest;
import com.xtree.lottery.databinding.LayoutBetInputBinding;
import com.xtree.lottery.ui.lotterybet.model.LotteryBetsModel;
import com.xtree.lottery.ui.view.LotterySeatView;
import com.xtree.lottery.ui.view.viewmodel.BetInputViewModel;
import com.xtree.lottery.utils.LotteryAnalyzer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by KAKA on 2024/5/3.
 * Describe: 输入投注视图
 */
public class BetInputView extends BetBaseView {

    private LayoutBetInputBinding binding;

    public BetInputView(@NonNull Context context) {
        super(context);
        binding = LayoutBetInputBinding.inflate(LayoutInflater.from(context), this, true);
        initView();
    }

    public BetInputView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        binding = LayoutBetInputBinding.inflate(LayoutInflater.from(context), this, true);
        initView();
    }

    private void initView() {
        BetInputViewModel model = new BetInputViewModel();
        binding.setModel(model);

        binding.betInputEdit.setFilters(new InputFilter[]{new LotteryInputFilter()});

        binding.getModel().lotteryNumbs.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                setBetData();
            }
        });

        binding.betInputSeatview.setOnSeatListener(new LotterySeatView.onSeatListener() {
            @Override
            public void onSeat(Set<String> seats) {
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

        //格式化输入的字符串
        s = binding.getModel().reFormatNums(s);

        Set<String> validNumbers = LotteryAnalyzer.getValidNumbers(s, Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));

        if (validNumbers.size() == 0) {
            betData.set(null);
            return;
        }

        Set<String> updatedNumbers = new HashSet<>();
        for (String n : validNumbers) {
            updatedNumbers.add(n.replaceAll(" ", ""));
        }
        //替换原集合内容
        validNumbers = updatedNumbers;

        String codes = String.join(",", validNumbers);

        LotteryBetRequest.BetOrderData betOrderData = new LotteryBetRequest.BetOrderData();
        betOrderData.setCodes(codes);
        betOrderData.setDesc(betsModel.getTitle());
        betOrderData.setMenuid(betsModel.getMenuMethodLabelData().getMenuid());
        betOrderData.setMethodid(betsModel.getMenuMethodLabelData().getMethodid());
        betOrderData.setType(betsModel.getMenuMethodLabelData().getSelectarea().getType());
        //设置位数
        if (Boolean.TRUE.equals(binding.getModel().showSeatView.get())) {
            String formatSeats = binding.betInputSeatview.getFormatSeats();
            if (formatSeats != null) {
                betOrderData.setPoschoose(formatSeats);
            } else {
                //如果位数设置不符合规则，设置投注为无效
                betData.set(null);
                return;
            }
        }

        ArrayList<LotteryBetRequest.BetOrderData> orderList = new ArrayList<>();
        orderList.add(betOrderData);

        betCodes.set(codes);
        betData.set(orderList);
    }

    private void initTip() {
        LotteryBetsModel model = getModel();
        String methoddesc = model.getMenuMethodLabelData().getMethoddesc();
        String methodexample = model.getMenuMethodLabelData().getMethodexample();
        String methodhelp = model.getMenuMethodLabelData().getMethodhelp();

        binding.betInputTip.setVisibility(methoddesc == null || methoddesc.isEmpty() ? GONE : VISIBLE);
        binding.betInputExample.setVisibility(methodexample == null || methodexample.isEmpty() ? GONE : VISIBLE);
        binding.betInputHelp.setVisibility(methodhelp == null || methodhelp.isEmpty() ? GONE : VISIBLE);

        if (!TextUtils.isEmpty(methoddesc)) {
            binding.betInputTip.setText(String.valueOf(Html.fromHtml(methoddesc)));
        }
        binding.betInputExample.setOnClickListener(v -> showTipDialog(String.valueOf(Html.fromHtml(methodexample))));
        binding.betInputHelp.setOnClickListener(v -> showTipDialog(String.valueOf(Html.fromHtml(methodhelp))));


        //是否存在此玩法规则
        String rules = INPUT_PLAYS_MAP.get(model.getTitle());
        if (rules == null) {
            return;
        }

        String[] split = rules.split(",");

        if (split.length < 3) {
            return;
        }

        if (Integer.parseInt(split[2]) >= 2) {
            binding.betInputTip2.setText(getResources().getString(R.string.txt_inputview_tip2_mode2));
        } else {
            binding.betInputTip2.setText(getResources().getString(R.string.txt_inputview_tip2_mode1));
        }
    }

    @Override
    public void setModel(LotteryBetsModel model) {
        super.setModel(model);
        binding.getModel().initData(model);

        initTip();

        //设置位数按钮
        if (model.getMenuMethodLabelData().getSelectarea().isSelPosition()) {
            binding.getModel().showSeatView.set(true);

            String defaultposition = model.getMenuMethodLabelData().getDefaultposition();

            if (defaultposition != null) {
                binding.betInputSeatview.setSeatChecked(defaultposition.toCharArray());
            }
        }
    }

    @Override
    public void clearBet() {
        binding.getModel().lotteryNumbs.set("");
    }
}
