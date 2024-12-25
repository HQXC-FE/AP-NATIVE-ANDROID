package com.xtree.recharge.ui.fragment.extransfer;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
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

import com.xtree.recharge.BR;
import com.xtree.recharge.R;
import com.xtree.recharge.databinding.LayoutRcExpBankDialog3Binding;
import com.xtree.recharge.ui.fragment.RechargeFragment;
import com.xtree.recharge.ui.viewmodel.ExTransferViewModel;
import com.xtree.recharge.ui.viewmodel.RechargeViewModel;
import com.xtree.recharge.ui.viewmodel.factory.AppViewModelFactory;

import java.util.Map;
import java.util.Objects;
import java.util.Stack;

import me.xtree.mvvmhabit.base.AppManager;
import me.xtree.mvvmhabit.base.BaseDialogFragment;
import me.xtree.mvvmhabit.base.BaseViewModel;

public class ExTransferVoucherDialogFragment extends BaseDialogFragment<LayoutRcExpBankDialog3Binding, ExTransferViewModel> {

    private ExTransferVoucherDialogFragment() {
    }

    /**
     * 启动弹窗
     *
     * @param activity 获取FragmentManager
     */
    public static void show(FragmentActivity activity) {
        ExTransferVoucherDialogFragment fragment = new ExTransferVoucherDialogFragment();
        fragment.show(activity.getSupportFragmentManager(), ExTransferVoucherDialogFragment.class.getName());
        activity.getSupportFragmentManager().executePendingTransactions();
    }

    @Override
    public void initView() {
        binding.tvwRight.setOnClickListener(v -> dismissAllowingStateLoss());
        binding.ivwClose.setOnClickListener(v -> dismissAllowingStateLoss());

        binding.tvInfoTips.setText(Html.fromHtml("<font>为加速处理请按下面指示上传存款凭证与填写信息，并记得点击下方<font color='#A17DF5'>【确认提交】</font>按钮才算完成！</font>"));
        binding.tvRequestTips.setText(Html.fromHtml("<font>上传转账电子凭证<font color='#A17DF5'>（必传项）</font></font>"));
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.layout_rc_exp_bank_dialog_3;
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
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);
        View decorView = window.getDecorView();
        decorView.setBackground(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setCanceledOnTouchOutside(true);
    }
}
