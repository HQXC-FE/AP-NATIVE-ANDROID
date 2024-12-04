package com.xtree.lottery.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.Observable;

import com.xtree.lottery.data.source.request.LotteryBetRequest;
import com.xtree.lottery.ui.lotterybet.model.LotteryBetsModel;
import com.xtree.lottery.ui.view.betviews.BetBaseView;
import com.xtree.lottery.ui.view.betviews.BetDxdsTagView;
import com.xtree.lottery.ui.view.betviews.BetDigitalView;
import com.xtree.lottery.ui.view.betviews.BetDxdsView;
import com.xtree.lottery.ui.view.betviews.BetHandicap1View;
import com.xtree.lottery.ui.view.betviews.BetInputView;
import com.xtree.lottery.ui.view.betviews.BetRacingView;

import java.util.Arrays;
import java.util.List;

/**
 * Created by KAKA on 2024/5/3.
 * Describe: 彩票投注
 */
public class LotteryBetView extends FrameLayout {

    /**
     * 投注号码监听
     */
    public interface OnLotteryBetListener{
        void onBetChange(List<LotteryBetRequest.BetOrderData> betOrderList);
    }

    private OnLotteryBetListener onLotteryBetListener = null;

    private BetBaseView betView;
    //使用标签布局的玩法
    private final String[] dxdsTagTypes = {"牛牛", "五星和值", "五星大小个数", "四星大小个数", "前三大小个数", "中三大小个数", "后三大小个数", "五星单双个数", "四星单双个数", "前三单双个数", "中三单双个数", "后三单双个数"};
    //竞速标签布局玩法
    private final String[] racingTagTypes = {"竞速", "对决"};

    public LotteryBetView(@NonNull Context context) {
        super(context);
    }

    public LotteryBetView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setData(LotteryBetsModel betsModel) {

        removeAllViews();

        if (betsModel == null) {
            return;
        }

        if (betsModel.getHandicapMethodData() != null) {

            betView = new BetHandicap1View(getContext());

        } else if (betsModel.getMenuMethodLabelData() != null) {

            if (betsModel.getMenuMethodLabelData().getSelectarea() == null
                    || betsModel.getMenuMethodLabelData().getSelectarea().getType() == null) {
                return;
            }

            switch (betsModel.getMenuMethodLabelData().getSelectarea().getType()) {
                case "dxds":
                    if (Arrays.asList(dxdsTagTypes).contains(
                            betsModel.getMenuMethodLabelData().getDescription())
                    ) {
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
                    if (Arrays.asList(racingTagTypes).contains(
                            betsModel.getMenuMethodLabelData().getDescription())
                    ) {
                        betView = new BetRacingView(getContext());
                    } else {
                        betView = new BetDigitalView(getContext());
                    }
                    break;
            }
        }

        if (betView == null) {
            return;
        }

        betView.setModel(betsModel);

        betView.betData.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (onLotteryBetListener != null) {
                    onLotteryBetListener.onBetChange(betView.betData.get());
                }
            }
        });

        addView(betView);
    }

    /**
     * 获取投注内容
     */
    public List<LotteryBetRequest.BetOrderData> getBet() {
        return betView.betData.get();
    }

    /**
     * 设置开奖历史
     */
    public void setLotteryNumbsHistory(List<String> lotteryNumbsHistory) {
        if (betView != null && betView instanceof BetDigitalView) {
            BetDigitalView digitalView = (BetDigitalView) betView;
            digitalView.setLotteryNumbsHistory(lotteryNumbsHistory);
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
}
