package com.xtree.recharge.ui.fragment.extransfer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.comm100.livechat.VisitorClientInterface;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.AppUtil;
import com.xtree.base.utils.TimeUtils;
import com.xtree.recharge.BR;
import com.xtree.recharge.R;
import com.xtree.recharge.data.source.request.ExCreateOrderRequest;
import com.xtree.recharge.databinding.FragmentExtransferPayeeBinding;
import com.xtree.recharge.ui.fragment.RechargeFragment;
import com.xtree.recharge.ui.viewmodel.ExTransferViewModel;
import com.xtree.recharge.ui.viewmodel.RechargeViewModel;
import com.xtree.recharge.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.recharge.ui.widget.Comm100ChatWindows;

import java.util.Map;
import java.util.Stack;

import me.xtree.mvvmhabit.base.AppManager;
import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.base.ContainerActivity;
import me.xtree.mvvmhabit.bus.RxBus;
import me.xtree.mvvmhabit.utils.SPUtils;

/**
 * Created by KAKA on 2024/5/28.
 * Describe: 极速转账-转账汇款程
 */
@Route(path = RouterFragmentPath.Transfer.PAGER_TRANSFER_EX_PAYEE)
public class ExTransferPayeeFragment extends BaseFragment<FragmentExtransferPayeeBinding, ExTransferViewModel> {

    private Comm100ChatWindows serviceChatFlow;

    @Override
    public void initView() {
        binding.ivwBack.setOnClickListener(v -> viewModel.finish());
        binding.ivwCs.setOnClickListener(v -> AppUtil.goCustomerService(getContext()));
        serviceChatFlow = new Comm100ChatWindows(requireActivity());
        serviceChatFlow.setOnClickListener((view, url, remark) -> {

            String chatUrl = url;
            if (viewModel != null && viewModel.payOrderData.getValue() != null) {
                String merchantOrder = viewModel.payOrderData.getValue().getMerchantOrder();
                if (!TextUtils.isEmpty(merchantOrder)) {
                    chatUrl = serviceChatFlow.getChatUrl(merchantOrder, chatUrl, remark);
                }
            }

            VisitorClientInterface.setChatUrl(chatUrl);

            Intent intent = new Intent(getContext(), ContainerActivity.class);
            intent.putExtra(ContainerActivity.ROUTER_PATH, RouterFragmentPath.Transfer.PAGER_TRANSFER_EX_CHAT);
            requireActivity().startActivity(intent);

            viewModel.close();
        });
        serviceChatFlow.show();

        showExTransferKindTipsDialogFragment();
    }

    private void showExTransferKindTipsDialogFragment() {
        binding.getModel().setActivity(getActivity());
        if (isTipTodayCount()) {
            ExTransferKindTipsDialogFragment.show(getActivity());
        }
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_extransfer_payee;
    }

    @Override
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

        binding.getModel().setActivity(requireActivity());
        binding.getModel().setFlowWindow(serviceChatFlow);
        binding.getModel().canonicalName = getClass().getCanonicalName();

        ExCreateOrderRequest createOrderInfo = RxBus.getDefault().getStickyEvent(ExCreateOrderRequest.class);
        if (createOrderInfo != null) {
            RxBus.getDefault().removeAllStickyEvents();
            binding.getModel().initData(requireActivity(), createOrderInfo);
            binding.getModel().serviceChatTimeKeeping();
        }
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


    /**
     * 是否弹窗(充值次数)
     * 单号: 2684, 2024-03-15
     *
     * @return true:默认弹提示, false:今日不弹提示
     */
    private boolean isTipTodayCount() {
        String cacheDay = SPUtils.getInstance().getString(SPKeyGlobal.RC_NOT_RC_EXP_TIP_TODAY_COUNT, "");
        String today = TimeUtils.getCurDate();
        return !today.equals(cacheDay);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (serviceChatFlow != null) {
            serviceChatFlow.removeView();
            serviceChatFlow = null;
        }
    }

    @Override
    public boolean isBackPressed() {

        if (viewModel != null) {
            viewModel.finish();
        }

        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.getModel().setActivity(getActivity());
    }
}
