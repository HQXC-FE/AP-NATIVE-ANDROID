package com.xtree.lottery.ui.view.viewmodel;

import android.view.View;

import androidx.databinding.ObservableField;

import com.xtree.lottery.ui.view.LotteryDrawView;

import java.util.List;

/**
 * Created by KAKA on 2024/5/1.
 * Describe:
 */
public class LotteryDrawViewModel {

    //开奖日期
    public ObservableField<String> drawDate = new ObservableField<>();
    //开奖号码
    public ObservableField<List<String>> drawCode = new ObservableField<>();
    //别名
    public ObservableField<String> alias = new ObservableField<>();

    private LotteryDrawView.OnLotteryDrawListener listener;

    public void setOnLotteryDrawListener(LotteryDrawView.OnLotteryDrawListener listener) {
        this.listener = listener;
    }

    public void refresh(View view) {
        if (listener != null) {
            listener.onRefresh(view);
        }
    }

    /**
     * 模拟开奖
     *
     * @param view
     */
    public void simulate(View view) {
        if (listener != null) {
            listener.onSimulate(view);
        }
    }
}
