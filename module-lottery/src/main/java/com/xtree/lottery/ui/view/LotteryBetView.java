package com.xtree.lottery.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.Observable;

import com.xtree.base.mvvm.ExKt;
import com.xtree.base.vo.UserMethodsResponse;
import com.xtree.lottery.data.config.Lottery;
import com.xtree.lottery.data.source.request.LotteryBetRequest;
import com.xtree.lottery.ui.lotterybet.model.LotteryBetsModel;
import com.xtree.lottery.ui.view.betviews.BetBaseView;
import com.xtree.lottery.ui.view.betviews.BetDiceAllView;
import com.xtree.lottery.ui.view.betviews.BetDiceRelationView;
import com.xtree.lottery.ui.view.betviews.BetDiceView;
import com.xtree.lottery.ui.view.betviews.BetDigitalView;
import com.xtree.lottery.ui.view.betviews.BetDxdsTagView;
import com.xtree.lottery.ui.view.betviews.BetDxdsView;
import com.xtree.lottery.ui.view.betviews.BetHandicap1View;
import com.xtree.lottery.ui.view.betviews.BetInputView;
import com.xtree.lottery.ui.view.betviews.BetLhcView;
import com.xtree.lottery.ui.view.betviews.BetRacingView;

import java.util.Arrays;
import java.util.List;

/**
 * Created by KAKA on 2024/5/3.
 * Describe: 彩票投注
 */
public class LotteryBetView extends FrameLayout {

    //竞速标签布局玩法
    private final String[] racingTagTypes = {"竞速", "对决"};
    //使用标签布局的玩法
    String[] boxs = {"趣味型-趣味型-定单双", "趣味-趣味型-和值单双", "趣味-趣味型-和值大小", "趣味-趣味型-奇偶盘", "趣味-趣味型-上下盘", "趣味-趣味型-和值大小单双", "盘面-盘面-上下盘", "盘面-盘面-奇偶盘", "大小单双-和值大小单双-五星和值", "大小单双-大小个数-五星大小个数", "大小单双-大小个数-四星大小个数", "大小单双-大小个数-前三大小个数", "大小单双-大小个数-中三大小个数", "大小单双-大小个数-后三大小个数", "大小单双-单双个数-五星单双个数", "大小单双-单双个数-四星单双个数", "大小单双-单双个数-前三单双个数", "大小单双-单双个数-中三单双个数", "大小单双-单双个数-后三单双个数", "牛牛-牛牛-牛牛"};
    //骰子
    //dice 数组
    String[] dice = {"二不同号-二不同号-标准选号", "二同号复选-二同号复选-二同号复选", "三不同号-三不同号-标准选号", "三同号单选-三同号单选-三同号单选"};
    //dice_all 数组
    String[] diceAll = {"三同号通选-三同号通选-三同号通选", "三连号通选-三连号通选-三连号通选"};
    //dice_relation 数组
    String[] diceRelation = {"二同号单选-二同号单选-标准选号"};
    //骰子

    private OnLotteryBetListener onLotteryBetListener = null;
    private BetBaseView betView;
    private List<String> lotteryNumbsHistory;

    public LotteryBetView(@NonNull Context context) {
        super(context);
    }

    public LotteryBetView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setData(LotteryBetsModel betsModel, @Nullable UserMethodsResponse.DataDTO.PrizeGroupDTO prizeGroup, Lottery lottery) {

        removeAllViews();

        if (betsModel == null) {
            return;
        }
        if ("lhc".equals(lottery.getLinkType())) {//六合彩
            betView = new BetLhcView(getContext());
        } else if (betsModel.getHandicapMethodData() != null) {
            betView = new BetHandicap1View(getContext());
        } else if (betsModel.getMenuMethodLabelData() != null) {

            if (betsModel.getMenuMethodLabelData().getSelectarea() == null || betsModel.getMenuMethodLabelData().getSelectarea().getType() == null) {
                return;
            }
            switch (betsModel.getMenuMethodLabelData().getSelectarea().getType()) {
                case "dxds":
                    if (ExKt.includes(Arrays.asList(boxs), betsModel.getTitle())) {
                        betView = new BetDxdsTagView(getContext());
                    } else {
                        betView = new BetDxdsView(getContext());
                    }
                    break;
                case "dds":
                    betView = new BetDxdsTagView(getContext());
                    break;
                case "input":
                    betView = new BetInputView(getContext());
                    break;
                default:
                    if (Arrays.asList(racingTagTypes).contains(betsModel.getMenuMethodLabelData().getDescription())) {
                        betView = new BetRacingView(getContext());
                    } else if (ExKt.includes(Arrays.asList(dice), betsModel.getTitle())) {
                        betView = new BetDiceView(getContext());
                    } else if (ExKt.includes(Arrays.asList(diceAll), betsModel.getTitle())) {
                        betView = new BetDiceAllView(getContext());
                    } else if (ExKt.includes(Arrays.asList(diceRelation), betsModel.getTitle())) {
                        betView = new BetDiceRelationView(getContext());
                    } else if (ExKt.includes(Arrays.asList(boxs), betsModel.getTitle())) {
                        betView = new BetDxdsTagView(getContext());
                    } else {
                        betView = new BetDigitalView(getContext());
                    }
                    break;
            }
        }

        if (betView == null) {
            return;
        }

        betView.setModel(betsModel, prizeGroup, lottery);

        betView.betData.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (onLotteryBetListener != null) {
                    onLotteryBetListener.onBetChange(betView.betData.get(), betView.betCodes.get());
                }
            }
        });

        //冷热、遗漏
        if (betView instanceof BetDigitalView) {
            BetDigitalView digitalView = (BetDigitalView) betView;
            digitalView.setLotteryNumbsHistory(lotteryNumbsHistory);
        }

        addView(betView);
    }

    public void setPrizeGroup(UserMethodsResponse.DataDTO.PrizeGroupDTO prizeGroup) {
        if (betView == null) {
            return;
        }
        betView.setPrizeGroup(prizeGroup);
    }


    /**
     * 获取投注内容
     */
    public List<LotteryBetRequest.BetOrderData> getBet() {
        return betView.betData.get();
    }

    /**
     * 获取投注号码
     */
    public Object getCodes() {
        return betView.betCodes.get();
    }

    /**
     * 设置开奖历史
     */
    public void setLotteryNumbsHistory(List<String> lotteryNumbsHistory) {
        if (betView != null && betView instanceof BetDigitalView) {
            BetDigitalView digitalView = (BetDigitalView) betView;
            digitalView.setLotteryNumbsHistory(lotteryNumbsHistory);
        } else {
            this.lotteryNumbsHistory = lotteryNumbsHistory;
        }
    }

    /**
     * 设置投注监听
     */
    public void setOnLotteryBetListener(OnLotteryBetListener onLotteryBetListener) {
        this.onLotteryBetListener = onLotteryBetListener;
    }

    /**
     * 清除投注项
     */
    public void clearBet() {
        if (betView != null) {
            betView.clearBet();
        }
    }

    /**
     * 投注号码监听
     */
    public interface OnLotteryBetListener {
        void onBetChange(List<LotteryBetRequest.BetOrderData> betOrderList, Object codes);
    }
}
