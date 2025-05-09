package com.xtree.lottery.ui.lotterybet;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.xtree.lottery.R;
import com.xtree.lottery.databinding.DialogLotteryTimeKeyboardBinding;
import com.xtree.lottery.ui.lotterybet.viewmodel.LotteryBetsViewModel;
import com.xtree.lottery.ui.view.viewmodel.LotteryMoneyViewModel;

import java.util.Objects;

import me.xtree.mvvmhabit.base.BaseDialogFragment;
import me.xtree.mvvmhabit.base.BaseViewModel;

/**
 * Created by KAKA on 2024/11/5.
 * Describe: 投注确认弹窗
 */
public class LotteryTimesKeyBoardDialogFragment extends BaseDialogFragment<DialogLotteryTimeKeyboardBinding, BaseViewModel> {

    private LotteryMoneyViewModel lotteryMoneyViewModel;

    private LotteryTimesKeyBoardDialogFragment() {
    }

    /**
     * 启动弹窗
     *
     * @param activity 获取FragmentManager
     */
    public static void show(FragmentActivity activity, LotteryMoneyViewModel lotteryMoneyViewModel) {
        LotteryTimesKeyBoardDialogFragment fragment = new LotteryTimesKeyBoardDialogFragment();
        fragment.lotteryMoneyViewModel = lotteryMoneyViewModel;
        fragment.show(activity.getSupportFragmentManager(), LotteryTimesKeyBoardDialogFragment.class.getName());
    }

    @Override
    public void initView() {

    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.dialog_lottery_time_keyboard;
    }

    @Override
    public LotteryBetsViewModel initViewModel() {
        return new ViewModelProvider(this).get(LotteryBetsViewModel.class);
    }


    @Override
    public void initData() {
        super.initData();
        binding.timesKeyBoard.initData((key, keyTag) -> {
            switch (keyTag) {
                case NUMBER:
                    lotteryMoneyViewModel.concat(key);
                    break;
                case DELETE:
                    lotteryMoneyViewModel.fixTimes(1);
                    break;
                case DONE:
                    dismissAllowingStateLoss();
                    break;
                case TIMES10:
                case TIMES50:
                case TIMES100:
                    lotteryMoneyViewModel.fixTimes(Long.parseLong(key));
                    break;
                default:
                    break;
            }
        });
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.getUC().getFinishEvent().removeObservers(this);
        viewModel.getUC().getFinishEvent().observe(this, new androidx.lifecycle.Observer<Void>() {
            @Override
            public void onChanged(@Nullable Void v) {
                dismissAllowingStateLoss();
            }
        });
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = Objects.requireNonNull(getDialog()).getWindow();
        WindowManager.LayoutParams params = Objects.requireNonNull(window).getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);
        View decorView = window.getDecorView();
        decorView.setBackground(new ColorDrawable(Color.TRANSPARENT));
    }
}