package com.xtree.lottery.ui.view.betviews;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.Observable;

import com.xtree.base.vo.UserMethodsResponse;
import com.xtree.lottery.data.config.Lottery;
import com.xtree.lottery.data.source.request.LotteryBetRequest;
import com.xtree.lottery.databinding.LayoutBetDigitalBinding;
import com.xtree.lottery.ui.lotterybet.model.LotteryBetsModel;
import com.xtree.lottery.ui.view.LotterySeatView;
import com.xtree.lottery.ui.view.viewmodel.BetDigitalViewModel;
import com.xtree.lottery.utils.LotteryAnalyzer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by KAKA on 2024/5/3.
 * Describe: 数字投注视图
 */
public class BetDigitalView extends BetBaseView {

    private LayoutBetDigitalBinding binding;

    public BetDigitalView(@NonNull Context context) {
        super(context);
        binding = LayoutBetDigitalBinding.inflate(LayoutInflater.from(context), this, true);
        initView();
    }

    public BetDigitalView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        binding = LayoutBetDigitalBinding.inflate(LayoutInflater.from(context), this, true);
        initView();
    }

    private void initView() {

        BetDigitalViewModel model = new BetDigitalViewModel();
        binding.setModel(model);

        binding.betDigitalRadiogroup.check(binding.betDigitalRadiobt1.getId());
        binding.betDigitalRadiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == binding.betDigitalRadiobt1.getId()) {
                    binding.betDigitalOmissionsGroup.clearCheck();
                    binding.betDigitalHotcoldGroup.clearCheck();
                    binding.getModel().closeMissHot();
                    binding.getModel().screenStatus.set(0);
                } else if (checkedId == binding.betDigitalRadiobt2.getId()) {
                    binding.betDigitalHotcoldGroup.clearCheck();
                    binding.betDigitalOmissionsGroup.check(binding.betDigitalOmissionsRadiobt1.getId());
                    binding.getModel().screenStatus.set(1);
                } else if (checkedId == binding.betDigitalRadiobt3.getId()) {
                    binding.betDigitalOmissionsGroup.clearCheck();
                    binding.betDigitalHotcoldGroup.check(binding.betDigitalHotcoldRadiobt1.getId());
                    binding.getModel().screenStatus.set(2);
                }
            }
        });

        binding.betDigitalOmissionsGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == binding.betDigitalOmissionsRadiobt1.getId()) {
                    binding.getModel().showMiss(false);
                } else if (checkedId == binding.betDigitalOmissionsRadiobt2.getId()) {
                    binding.getModel().showMiss(true);
                }
            }
        });

        binding.betDigitalHotcoldGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == binding.betDigitalHotcoldRadiobt1.getId()) {
                    binding.getModel().showHot(30);
                } else if (checkedId == binding.betDigitalHotcoldRadiobt2.getId()) {
                    binding.getModel().showHot(50);
                } else if (checkedId == binding.betDigitalHotcoldRadiobt3.getId()) {
                    binding.getModel().showHot(100);
                }
            }
        });

        binding.betDigitalSeatview.setOnSeatListener(new LotterySeatView.onSeatListener() {
            @Override
            public void onSeat(Set<String> seats) {
                setBetData();
            }
        });

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
            betOrderData.setCodes(codes.trim());
            betOrderData.setDesc(betsModel.getTitle());
            betOrderData.setMenuid(betsModel.getMenuMethodLabelData().getMenuid());
            betOrderData.setMethodid(betsModel.getMenuMethodLabelData().getMethodid());
            betOrderData.setType(betsModel.getMenuMethodLabelData().getSelectarea().getType());
            //设置位数
            if (Boolean.TRUE.equals(binding.getModel().showSeatView.get())) {
                String formatSeats = binding.betDigitalSeatview.getFormatSeats();
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

        binding.betDigitalTip.setVisibility(methoddesc == null || methoddesc.isEmpty() ? GONE : VISIBLE);
        binding.betDigitalExample.setVisibility(methodexample == null || methodexample.isEmpty() ? GONE : VISIBLE);
        binding.betDigitalHelp.setVisibility(methodhelp == null || methodhelp.isEmpty() ? GONE : VISIBLE);

        if (!TextUtils.isEmpty(methoddesc)) {
            binding.betDigitalTip.setText(String.valueOf(Html.fromHtml(methoddesc)));
        }
        binding.betDigitalExample.setOnClickListener(v -> showTipDialog(String.valueOf(Html.fromHtml(methodexample))));
        binding.betDigitalHelp.setOnClickListener(v -> showTipDialog(String.valueOf(Html.fromHtml(methodhelp))));
    }

    @Override
    public void setModel(LotteryBetsModel model, UserMethodsResponse.DataDTO.PrizeGroupDTO prizeGroup, Lottery lottery) {
        super.setModel(model, prizeGroup, lottery);
        binding.getModel().initData(model);
        initTip();

        //复式玩法打开冷热遗漏
//        if (Arrays.asList(useMissPlays).contains(model.getMenuMethodLabelData().getDescription())) {
        if (LotteryAnalyzer.findMissingCodes(lottery.getAlias()) != null) {
            binding.getModel().buttonStatus.set(true);
        }

        //设置位数按钮
        if (model.getMenuMethodLabelData().getSelectarea().isSelPosition()) {
            binding.getModel().showSeatView.set(true);

            String defaultposition = model.getMenuMethodLabelData().getDefaultposition();

            if (defaultposition != null) {
                binding.betDigitalSeatview.setSeatChecked(defaultposition.toCharArray());
            }
        }
    }

    @Override
    public void clearBet() {
        binding.getModel().clearBet();
    }

    public void setLotteryNumbsHistory(List<String> lotteryNumbsHistory) {
        if (binding.getModel() != null) {
            binding.getModel().lotteryHistoryLiveData.setValue(lotteryNumbsHistory);
        }
    }
}
