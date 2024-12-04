package com.xtree.lottery.ui.view.viewmodel;

import androidx.databinding.ObservableField;

import com.xtree.lottery.ui.view.LotteryDrawView;

import java.util.ArrayList;

/**
 * Created by KAKA on 2024/5/1.
 * Describe:
 */
public class LotteryDrawViewModel {

    //开奖日期
    public ObservableField<String> drawDate = new ObservableField<>();
    //开奖号码
    public ObservableField<ArrayList<String>> drawCode = new ObservableField<>();

    private LotteryDrawView.OnLotteryDrawListener listener;

    public void setOnLotteryDrawListener(LotteryDrawView.OnLotteryDrawListener listener) {
        this.listener = listener;
    }

    public void refresh() {
        if (listener != null) {
            listener.onRefresh();
        }
    }
}
