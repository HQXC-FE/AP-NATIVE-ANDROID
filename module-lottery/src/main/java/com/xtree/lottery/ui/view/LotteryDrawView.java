package com.xtree.lottery.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.databinding.Observable;

import com.xtree.lottery.R;
import com.xtree.lottery.data.source.response.BonusNumbersResponse;
import com.xtree.lottery.databinding.LayoutLotteryDrawBinding;
import com.xtree.lottery.ui.view.viewmodel.LotteryDrawViewModel;

import java.util.ArrayList;


/**
 * Created by KAKA on 2024/5/1.
 * Describe: 彩票开奖
 */
public class LotteryDrawView extends LinearLayout {

    private LayoutLotteryDrawBinding binding;
    private LotteryDrawViewModel lotteryDrawViewModel;

    /**
     * 刷新上期开奖
     */
    public interface OnLotteryDrawListener{
        void onRefresh(View view);
    }

    public LotteryDrawView(Context context) {
        super(context);
        binding = LayoutLotteryDrawBinding.inflate(LayoutInflater.from(context), this, true);

        initView();
    }

    public LotteryDrawView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        binding = LayoutLotteryDrawBinding.inflate(LayoutInflater.from(context), this, true);

        initView();
    }

    private void initView() {
        lotteryDrawViewModel = new LotteryDrawViewModel();
        binding.setModel(lotteryDrawViewModel);

        binding.getModel().drawCode.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                ArrayList<String> numbs = binding.getModel().drawCode.get();
                if (numbs != null) {

                    binding.lotteryDrawGroup.removeAllViews();

                    for (String numb : numbs) {
                        TextView textView = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.view_lottery_draw_ball, null);
                        textView.setText(numb);
                        binding.lotteryDrawGroup.addView(textView);
                    }
                }
            }
        });
    }

    public void setDrawCode(BonusNumbersResponse.DataDTO bonusNumber) {
        binding.getModel().drawDate.set(bonusNumber.getIssue() + "期：");
        binding.getModel().drawCode.set((ArrayList<String>) bonusNumber.getSplitCode());
    }

    /**
     * 设置开奖监听
     */
    public void setOnLotteryDrawListener(OnLotteryDrawListener listener) {
        binding.getModel().setOnLotteryDrawListener(listener);
    }
}
