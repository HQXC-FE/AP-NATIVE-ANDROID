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

import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.lottery.BR;
import com.xtree.lottery.R;
import com.xtree.lottery.data.LotteryDetailManager;
import com.xtree.lottery.databinding.DialogLotteryOrderBinding;
import com.xtree.lottery.ui.lotterybet.viewmodel.LotteryBetConfirmViewModel;
import com.xtree.lottery.ui.lotterybet.viewmodel.LotteryBetsViewModel;
import com.xtree.lottery.ui.lotterybet.viewmodel.LotteryOrderViewModel;
import com.xtree.lottery.ui.viewmodel.factory.AppViewModelFactory;

import java.util.ArrayList;
import java.util.Objects;

import me.xtree.mvvmhabit.base.BaseDialogFragment;
import me.xtree.mvvmhabit.utils.ToastUtils;

/**
 * Created by KAKA on 2024/5/10.
 * Describe: 彩票订单详情
 */
public class LotteryOrderDialogFragment extends BaseDialogFragment<DialogLotteryOrderBinding, LotteryOrderViewModel> {

    private LotteryOrderDialogFragment() {
    }

    /**
     * 启动弹窗
     *
     * @param activity 获取FragmentManager
     */
    public static void show(FragmentActivity activity) {
        LotteryOrderDialogFragment fragment = new LotteryOrderDialogFragment();
        fragment.show(activity.getSupportFragmentManager(), LotteryOrderDialogFragment.class.getName());
    }

    @Override
    public void initView() {
        binding.fvChasingNumber.setOnClickListener(v -> {
            ArrayList<BindModel> list = viewModel.datas.getValue();
            if (list == null || list.isEmpty()) {
                ToastUtils.showLong("请先选号");
                return;
            }
            new ViewModelProvider(requireActivity()).get(LotteryBetConfirmViewModel.class).chasingNumberParams.setValue(null);//清空追号
            if (!LotteryDetailManager.INSTANCE.getMIssues().isEmpty()) {
                LotteryChasingNumberFragment.show(requireActivity(), binding.getModel().betNums.getValue(), binding.getModel().moneyNums.getValue(), binding.getModel().ordersLiveData.getValue());
                viewModel.goChasing();
            }
        });
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.dialog_lottery_order;
    }

    @Override
    public LotteryOrderViewModel initViewModel() {
        LotteryOrderViewModel viewModel = new ViewModelProvider(getActivity()).get(LotteryOrderViewModel.class);
        AppViewModelFactory instance = AppViewModelFactory.getInstance(getActivity().getApplication());
        viewModel.setModel(instance.getmRepository());
        viewModel.betsViewModel = new ViewModelProvider(getActivity()).get(LotteryBetsViewModel.class);
        return viewModel;
    }

    @Override
    public void initData() {
        super.initData();
        binding.setVariable(BR.model, viewModel);
        binding.setConfirmModel(new ViewModelProvider(getActivity()).get(LotteryBetConfirmViewModel.class));
        viewModel.initData(getActivity());
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