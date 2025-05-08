package com.xtree.lottery.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.xtree.lottery.databinding.LayoutLotteryTimesKeyboardBinding;
import com.xtree.lottery.ui.view.model.TimesKeyBoardModel;
import com.xtree.lottery.ui.view.viewmodel.TimesKeyBoardViewModel;


public class TimesKeyBoardView extends FrameLayout {

    private LayoutLotteryTimesKeyboardBinding binding;
    private TimesKeyBoardViewModel timesKeyBoardViewModel;

    public TimesKeyBoardView(@NonNull Context context) {
        super(context, null);
    }

    public TimesKeyBoardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        binding = LayoutLotteryTimesKeyboardBinding.inflate(LayoutInflater.from(context), this, true);
    }

    public void initData(TimesKeyBoardView.OnItemClickListener onItemClickListener) {
        timesKeyBoardViewModel = new TimesKeyBoardViewModel(onItemClickListener);
        binding.setModel(timesKeyBoardViewModel);
    }


    public interface OnItemClickListener {
        void onItemClick(String key, TimesKeyBoardModel.KeyTag keyTag);
    }

}
