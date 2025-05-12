package com.xtree.recharge.ui.fragment.extransfer;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.utils.TimeUtils;
import com.xtree.recharge.BR;
import com.xtree.recharge.R;
import com.xtree.recharge.databinding.LayoutRcExpBankDialog1KindTipsBinding;
import com.xtree.recharge.ui.fragment.RechargeFragment;
import com.xtree.recharge.ui.viewmodel.ExTransferViewModel;
import com.xtree.recharge.ui.viewmodel.RechargeViewModel;
import com.xtree.recharge.ui.viewmodel.factory.AppViewModelFactory;

import java.util.Map;
import java.util.Objects;
import java.util.Stack;

import com.xtree.base.base.AppManager;
import com.xtree.base.base.BaseDialogFragment;
import com.xtree.base.base.BaseViewModel;
import com.xtree.base.utils.SPUtils;

public class ExTransferKindTipsDialogFragment extends BaseDialogFragment<LayoutRcExpBankDialog1KindTipsBinding, ExTransferViewModel> {

    private ExTransferKindTipsDialogFragment() {
    }

    /**
     * 启动弹窗
     *
     * @param activity 获取FragmentManager
     */
    public static void show(FragmentActivity activity) {
        new Handler(Looper.getMainLooper()).post(() -> {
            ExTransferKindTipsDialogFragment fragment = new ExTransferKindTipsDialogFragment();
            fragment.show(activity.getSupportFragmentManager(), ExTransferKindTipsDialogFragment.class.getName());
            activity.getSupportFragmentManager().executePendingTransactions();
        });
    }

    @Override
    public void initView() {
        binding.tvwRight.setOnClickListener(v -> dismissAllowingStateLoss());
        String key = SPKeyGlobal.RC_NOT_RC_EXP_TIP_TODAY_COUNT;
        binding.cbTipPm.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                SPUtils.getInstance().put(key, TimeUtils.getCurDate());
            } else {
                SPUtils.getInstance().remove(key);
            }
        });
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.layout_rc_exp_bank_dialog_1_kind_tips;
    }

    @Override
    public void onClick(View view) {

    }

    public int initVariableId() {
        return BR.model;
    }

    @Override
    public ExTransferViewModel initViewModel() {
        Stack<Activity> activityStack = AppManager.getActivityStack();
        FragmentActivity fragmentActivity = requireActivity();
        for (Activity activity : activityStack) {
            try {
                FragmentActivity fa = (FragmentActivity) activity;
                for (Fragment fragment : fa.getSupportFragmentManager().getFragments()) {
                    if (fragment.getClass().getCanonicalName().equals(RechargeFragment.class.getCanonicalName())) {
                        fragmentActivity = fa;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ExTransferViewModel viewmodel = new ViewModelProvider(fragmentActivity).get(ExTransferViewModel.class);
        AppViewModelFactory instance = AppViewModelFactory.getInstance(requireActivity().getApplication());
        viewmodel.setModel(instance.getmRepository());
        viewmodel.setRechargeViewModel(new ViewModelProvider(fragmentActivity).get(RechargeViewModel.class));
        return viewmodel;
    }

    @Override
    public void initData() {
        super.initData();
        binding.getModel().setActivity(getActivity());
        binding.getModel().canonicalName = getClass().getCanonicalName();
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.getUC().getStartContainerActivityEvent().removeObservers(this);
        viewModel.getUC().getStartContainerActivityEvent().observe(this, new Observer<Map<String, Object>>() {
            @Override
            public void onChanged(@Nullable Map<String, Object> params) {
                String canonicalName = (String) params.get(BaseViewModel.ParameterField.CANONICAL_NAME);
                Bundle bundle = (Bundle) params.get(BaseViewModel.ParameterField.BUNDLE);
                startContainerFragment(canonicalName, bundle);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.getModel().setActivity(getActivity());
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = Objects.requireNonNull(getDialog()).getWindow();
        WindowManager.LayoutParams params = Objects.requireNonNull(window).getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
        View decorView = window.getDecorView();
        decorView.setBackground(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setCanceledOnTouchOutside(true);
    }
}
