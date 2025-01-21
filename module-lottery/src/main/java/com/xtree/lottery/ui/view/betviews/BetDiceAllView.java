package com.xtree.lottery.ui.view.betviews;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.xtree.lottery.ui.view.viewmodel.BetDiceViewModel;

/**
 * Created by KAKA on 2024/5/3.
 * Describe: 骰子投注视图
 */
public class BetDiceAllView extends BetDiceView {


    public BetDiceAllView(@NonNull Context context) {
        super(context);
    }

    public BetDiceAllView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public BetDiceViewModel.Dice getMethod() {
        return BetDiceViewModel.Dice.All;
    }
}
