package com.xtree.lottery.ui.view;


import android.content.Context;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;

import com.xtree.lottery.R;
import com.xtree.lottery.data.config.Lottery;
import com.xtree.lottery.databinding.LayoutLotteryMoneyBinding;
import com.xtree.lottery.ui.lotterybet.data.LotteryMoneyData;
import com.xtree.lottery.ui.view.model.LotteryMoneyModel;
import com.xtree.lottery.ui.view.viewmodel.LotteryMoneyViewModel;
import com.xtree.lottery.utils.filter.MoneyFilter;

import java.util.List;

import me.xtree.mvvmhabit.utils.ConvertUtils;
import me.xtree.mvvmhabit.utils.SPUtils;

/**
 * Created by KAKA on 2024/5/2.
 * Describe: 彩票投注金额设置
 */
public class LotteryMoneyView extends LinearLayout {

    private LayoutLotteryMoneyBinding binding;
    // 设置 RadioGroup 的布局参数
    private RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(
            RadioGroup.LayoutParams.WRAP_CONTENT,
            RadioGroup.LayoutParams.WRAP_CONTENT
    );
    private List<LotteryMoneyModel> moneyModels;
    private Lottery lottery;

    public LotteryMoneyView(Context context) {
        super(context);
        binding = LayoutLotteryMoneyBinding.inflate(LayoutInflater.from(context), this, true);

        initView();
    }

    public LotteryMoneyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        binding = LayoutLotteryMoneyBinding.inflate(LayoutInflater.from(context), this, true);

        initView();
    }

    private void initView() {

        // 设置子视图之间的间距，这里使用的是单位为像素的间距
        layoutParams.setMargins(0, 0, ConvertUtils.dp2px(5), 0);

        LotteryMoneyViewModel lotteryMoneyViewModel = new LotteryMoneyViewModel();
        lotteryMoneyViewModel.initData();
        binding.setModel(lotteryMoneyViewModel);

        binding.getRoot().setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (binding.lotteryMoneyFactorEdit.isFocused()) {
                        binding.lotteryMoneyFactorEdit.clearFocus();
                    }
                }
                return false;
            }
        });

        binding.lotteryMoneyFactorEdit.setFilters(new InputFilter[]{new MoneyFilter()});

        binding.lotteryMoneyFactorEdit.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String s = binding.lotteryMoneyFactorEdit.getText().toString();
                if (hasFocus) {
                    if (s.contains(LotteryMoneyViewModel.FACTOR_SUFFIX)) {
                        String numString = s.replace(LotteryMoneyViewModel.FACTOR_SUFFIX, "");
                        binding.getModel().factorData.set(numString);
                    }
                } else {
                    binding.getModel().factorData.set(s + LotteryMoneyViewModel.FACTOR_SUFFIX);
                }
            }
        });

        binding.lotteryMoneyUnit.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // 处理选中的 RadioButton 的逻辑
                RadioButton radioButton = findViewById(checkedId);
                if (radioButton != null) {
                    String selectedOption = radioButton.getText().toString();
                    if (moneyModels != null) {
                        for (LotteryMoneyModel moneyModel : moneyModels) {
                            if (moneyModel.getName().equals(selectedOption)) {
                                binding.getModel().unitData.set(moneyModel);
                                //保存选中的金额单元
                                SPUtils.getInstance().put(lottery.getName(), moneyModel.getName());
                                break;
                            }
                        }
                    }

                }
            }
        });
    }

    /**
     * 设置金额格式
     *
     * @param moneyModels 金额格式集
     */
    public void setMoneyUnit(List<LotteryMoneyModel> moneyModels, Lottery lottery) {
        binding.lotteryMoneyUnit.removeAllViews();

        this.lottery = lottery;
        this.moneyModels = moneyModels;
        String defaultName = SPUtils.getInstance().getString(lottery.getName(), moneyModels.get(0).getName());


        for (int i = 0; i < moneyModels.size(); i++) {
            RadioButton radioButton = (RadioButton) LayoutInflater.from(getContext()).inflate(R.layout.label_lottery_money, null);
            radioButton.setText(moneyModels.get(i).getName());
            radioButton.setId(View.generateViewId());
            binding.lotteryMoneyUnit.addView(radioButton, layoutParams);
            if (moneyModels.get(i).getName().equals(defaultName)) {
                binding.lotteryMoneyUnit.check(radioButton.getId());
            }
        }
    }

    /**
     * 获取没注的金额
     *
     * @return 浮点类型
     */
    public float getMoney() {
        return binding.getModel().getMoney();
    }

    public LotteryMoneyData getMoneyData() {
        return binding.getModel().getMoneyData();
    }

    /**
     * 设置金额变化监听
     */
    public void setOnChangeMoneyListener(onChangeMoneyListener listener) {
        binding.getModel().setOnChangeMoneyListener(listener);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // 处理触摸事件，确保 EditText 失去焦点
        if (binding.lotteryMoneyFactorEdit.isFocused()) {
            binding.lotteryMoneyFactorEdit.clearFocus();
        }
        return super.dispatchTouchEvent(ev);
    }

    public interface onChangeMoneyListener {
        void onChange(LotteryMoneyData moneyData);
    }
}
