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

import com.xtree.lottery.BR;
import com.xtree.lottery.R;
import com.xtree.lottery.databinding.DialogLotteryChipsettingBinding;
import com.xtree.lottery.ui.lotterybet.viewmodel.LotteryChipSettingViewModel;
import com.xtree.lottery.ui.lotterybet.viewmodel.LotteryHandicapViewModel;
import com.xtree.lottery.ui.viewmodel.factory.AppViewModelFactory;

import java.util.Objects;

import me.xtree.mvvmhabit.base.BaseDialogFragment;

/**
 * Created by KAKA on 2024/5/18.
 * Describe: 筹码设置弹窗
 */
public class LotteryChipSettingDialogFragment extends BaseDialogFragment<DialogLotteryChipsettingBinding, LotteryChipSettingViewModel> {

    private LotteryChipSettingDialogFragment() {
    }

    /**
     * 启动弹窗
     *
     * @param activity 获取FragmentManager
     */
    public static void show(FragmentActivity activity) {
        LotteryChipSettingDialogFragment fragment = new LotteryChipSettingDialogFragment();
        fragment.show(activity.getSupportFragmentManager(), LotteryChipSettingDialogFragment.class.getName());
    }

    @Override
    public void initView() {

    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.dialog_lottery_chipsetting;
    }

    @Override
    public LotteryChipSettingViewModel initViewModel() {
        LotteryChipSettingViewModel viewmodel = new ViewModelProvider(getActivity()).get(LotteryChipSettingViewModel.class);
        AppViewModelFactory instance = AppViewModelFactory.getInstance(getActivity().getApplication());
        viewmodel.setModel(instance.getmRepository());
        viewmodel.handicapViewModel = new ViewModelProvider(getActivity()).get(LotteryHandicapViewModel.class);
        return viewmodel;
    }

    @Override
    public void initData() {
        super.initData();
        binding.setVariable(BR.model, viewModel);
        viewModel.initData();
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