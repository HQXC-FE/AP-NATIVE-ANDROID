package com.xtree.lottery.ui.view.viewmodel;

import android.graphics.Typeface;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.drake.brv.BindingAdapter;
import com.xtree.base.mvvm.recyclerview.BaseDatabindingAdapter;
import com.xtree.lottery.R;
import com.xtree.lottery.databinding.ItemLotteryTimesKeyboardBinding;
import com.xtree.lottery.ui.view.TimesKeyBoardView;
import com.xtree.lottery.ui.view.model.TimesKeyBoardModel;

import java.util.ArrayList;

public class TimesKeyBoardViewModel {

    public final ObservableField<ArrayList<TimesKeyBoardModel>> datas = new ObservableField<>(new ArrayList<TimesKeyBoardModel>() {{
        add(new TimesKeyBoardModel("10", TimesKeyBoardModel.KeyTag.TIMES10));
        add(new TimesKeyBoardModel("50", TimesKeyBoardModel.KeyTag.TIMES50));
        add(new TimesKeyBoardModel("100", TimesKeyBoardModel.KeyTag.TIMES100));
        add(new TimesKeyBoardModel("1", TimesKeyBoardModel.KeyTag.NUMBER));
        add(new TimesKeyBoardModel("2", TimesKeyBoardModel.KeyTag.NUMBER));
        add(new TimesKeyBoardModel("3", TimesKeyBoardModel.KeyTag.NUMBER));
        add(new TimesKeyBoardModel("4", TimesKeyBoardModel.KeyTag.NUMBER));
        add(new TimesKeyBoardModel("5", TimesKeyBoardModel.KeyTag.NUMBER));
        add(new TimesKeyBoardModel("6", TimesKeyBoardModel.KeyTag.NUMBER));
        add(new TimesKeyBoardModel("7", TimesKeyBoardModel.KeyTag.NUMBER));
        add(new TimesKeyBoardModel("8", TimesKeyBoardModel.KeyTag.NUMBER));
        add(new TimesKeyBoardModel("9", TimesKeyBoardModel.KeyTag.NUMBER));
        add(new TimesKeyBoardModel(TimesKeyBoardModel.KeyTag.DELETE));
        add(new TimesKeyBoardModel("0", TimesKeyBoardModel.KeyTag.NUMBER));
        add(new TimesKeyBoardModel(TimesKeyBoardModel.KeyTag.DONE));
    }});
    public final ObservableField<ArrayList<Integer>> itemType = new ObservableField<>(new ArrayList<Integer>() {
        {
            add(R.layout.item_lottery_times_keyboard);
        }
    });
    private final TimesKeyBoardView.OnItemClickListener onItemClickListener;
    public BaseDatabindingAdapter.onBindListener onBindListener = new BaseDatabindingAdapter.onBindListener() {

        @Override
        public void onItemClick(int modelPosition, int layoutPosition, int itemViewType) {
            TimesKeyBoardModel timesKeyBoardModel = datas.get().get(modelPosition);
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(timesKeyBoardModel.key, timesKeyBoardModel.keyTag);
            }
        }

        @Override
        public void onBind(@NonNull BindingAdapter.BindingViewHolder bindingViewHolder, @NonNull View view, int itemViewType) {
            ItemLotteryTimesKeyboardBinding binding = (ItemLotteryTimesKeyboardBinding) bindingViewHolder.getViewBinding();
            TimesKeyBoardModel timesKeyBoardModel = bindingViewHolder.getModel();
            if (timesKeyBoardModel.keyTag != TimesKeyBoardModel.KeyTag.NUMBER) {
                binding.tvName.setTypeface(Typeface.DEFAULT_BOLD);
                switch (timesKeyBoardModel.keyTag) {
                    case TIMES10:
                    case TIMES50:
                    case TIMES100:
                        binding.tvName.setText(timesKeyBoardModel.key + "倍");
                        break;
                    case DONE:
                        binding.tvName.setText("完成");
                        break;
                    case DELETE:
                        binding.tvName.setText("清除");
                        break;
                    default:
                        break;
                }
                binding.tvName.setTextColor(binding.getRoot().getResources().getColor(R.color.blue));
            } else {
                binding.tvName.setTypeface(null, Typeface.NORMAL);
                binding.tvName.setText(timesKeyBoardModel.key);
                binding.tvName.setTextColor(binding.getRoot().getResources().getColor(R.color.textColor));
            }
        }
    };

    public TimesKeyBoardViewModel(TimesKeyBoardView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
