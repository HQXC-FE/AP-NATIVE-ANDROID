package com.xtree.mine.ui.fragment;

import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.gson.Gson;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.xtree.base.adapter.CacheViewHolder;
import com.xtree.base.adapter.CachedAutoRefreshAdapter;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.ClickUtil;
import com.xtree.base.utils.UuidUtil;
import com.xtree.base.vo.ProfileVo;
import com.xtree.base.widget.ListDialog;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.databinding.FragmentBindUsdtAddBinding;
import com.xtree.mine.databinding.FragmentBindUsdtFirstBinding;
import com.xtree.mine.ui.viewmodel.BindUsdtViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.mine.vo.UserFirstBindUSDTVo;
import com.xtree.mine.vo.UserUsdtConfirmVo;
import com.xtree.mine.vo.UserUsdtJumpVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;
import project.tqyb.com.library_res.databinding.ItemTextBinding;

/**
 * 第一次绑定USDT 增加
 */
@Route(path = RouterFragmentPath.Mine.PAGER_BIND_USDT_ADD_FIRST)
public class BindFirstUsdtAddFragment extends BaseFragment<FragmentBindUsdtFirstBinding, BindUsdtViewModel> {

    private String id = "";
    private String type ="ERC20_USDT" ;
    ProfileVo mProfileVo;
    UserUsdtConfirmVo mConfirmVo;
    private BasePopupView loadingView;//显示loadView

    private String check ;//回填资金密码页面返回check
    private UserFirstBindUSDTVo userFirstBindUSDTVo ;

    public BindFirstUsdtAddFragment() {
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void initView() {
        binding.llRoot.setOnClickListener(v -> hideKeyBoard());
        binding.ivwBack.setOnClickListener(v -> getActivity().finish());

        binding.tvwERC20.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


                binding.tvwERC20.setBackgroundResource( R.drawable.btn_first_bind_usdt_selector);
                binding.tvwERC20.setTextColor(getContext().getColor(R.color.white));
                binding.tvwTRC20.setBackgroundResource(R.drawable.btn_first_bind_usdt_selector_off);
                binding.tvwTRC20.setTextColor(getContext().getColor(R.color.clr_txt_title));

                binding.tvwArbitrum.setBackgroundResource(R.drawable.btn_first_bind_usdt_selector_off);
                binding.tvwArbitrum.setTextColor(getContext().getColor(R.color.clr_txt_title));
                binding.tvwSolana.setBackgroundResource( R.drawable.btn_first_bind_usdt_selector_off);
                binding.tvwSolana.setTextColor(getContext().getColor(R.color.clr_txt_title));

            }

            binding.tvwTipAddress.setText(R.string.txt_remind_usdt_erc20);
            binding.tvwTipAddressTip.setText(R.string.txt_remind_usdt_erc20);

            type ="ERC20_USDT" ;
        });
        binding.tvwTRC20.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                binding.tvwERC20.setBackgroundResource( R.drawable.btn_first_bind_usdt_selector_off);
                binding.tvwERC20.setTextColor(getContext().getColor(R.color.clr_txt_title));
                binding.tvwTRC20.setBackgroundResource(R.drawable.btn_first_bind_usdt_selector);
                binding.tvwTRC20.setTextColor(getContext().getColor(R.color.white));

                binding.tvwArbitrum.setBackgroundResource(R.drawable.btn_first_bind_usdt_selector_off);
                binding.tvwArbitrum.setTextColor(getContext().getColor(R.color.clr_txt_title));
                binding.tvwSolana.setBackgroundResource( R.drawable.btn_first_bind_usdt_selector_off);
                binding.tvwSolana.setTextColor(getContext().getColor(R.color.clr_txt_title));
            }
            binding.tvwTipAddress.setText(R.string.txt_remind_usdt_trc20);
            binding.tvwTipAddressTip.setText(R.string.txt_remind_usdt_trc20);
            type ="TRC20_USDT" ;
        });
        binding.tvwArbitrum.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                binding.tvwERC20.setBackgroundResource( R.drawable.btn_first_bind_usdt_selector_off);
                binding.tvwERC20.setTextColor(getContext().getColor(R.color.clr_txt_title));
                binding.tvwTRC20.setBackgroundResource( R.drawable.btn_first_bind_usdt_selector_off);
                binding.tvwTRC20.setTextColor(getContext().getColor(R.color.clr_txt_title));

                binding.tvwArbitrum.setBackgroundResource(R.drawable.btn_first_bind_usdt_selector);
                binding.tvwArbitrum.setTextColor(getContext().getColor(R.color.white));
                binding.tvwSolana.setBackgroundResource( R.drawable.btn_first_bind_usdt_selector_off);
                binding.tvwSolana.setTextColor(getContext().getColor(R.color.clr_txt_title));

            }
            binding.tvwTipAddress.setText(R.string.txt_remind_usdt_arb);
            binding.tvwTipAddressTip.setText(R.string.txt_remind_usdt_arb);
            type ="Arbitrum" ;
        });
        binding.tvwSolana.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                binding.tvwERC20.setBackgroundResource( R.drawable.btn_first_bind_usdt_selector_off);
                binding.tvwERC20.setTextColor(getContext().getColor(R.color.clr_txt_title));
                binding.tvwTRC20.setBackgroundResource( R.drawable.btn_first_bind_usdt_selector_off);
                binding.tvwTRC20.setTextColor(getContext().getColor(R.color.clr_txt_title));

                binding.tvwArbitrum.setBackgroundResource(R.drawable.btn_first_bind_usdt_selector_off);
                binding.tvwArbitrum.setTextColor(getContext().getColor(R.color.clr_txt_title));
                binding.tvwSolana.setBackgroundResource( R.drawable.btn_first_bind_usdt_selector);
                binding.tvwSolana.setTextColor(getContext().getColor(R.color.white));
            }
            binding.tvwTipAddress.setText(R.string.txt_remind_usdt_sol);
            binding.tvwTipAddressTip.setText(R.string.txt_remind_usdt_sol);
            type ="Solana" ;
        });

        binding.ivwNext.setOnClickListener(v -> {
            doNext();
        });

        //确定
        binding.tvwSubmit.setOnClickListener(v -> {
            if (ClickUtil.isFastClick()) {
                return;
            }
            doSubmit();
        });
        //返回
        binding.tvwBack.setOnClickListener(v -> {
            if (binding.llAdd.getVisibility() == View.GONE) {
                binding.llAdd.setVisibility(View.VISIBLE);
                binding.llConfirm.setVisibility(View.GONE);
            }
        });


    }

    @Override
    public void initData() {
        super.initData();
        if (getArguments() != null) {
            check =  getArguments().getString("check");
            CfLog.e("initData  check = " +check);
        }
        String json = SPUtils.getInstance().getString(SPKeyGlobal.HOME_PROFILE);
        mProfileVo = new Gson().fromJson(json, ProfileVo.class);
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_bind_usdt_first;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public BindUsdtViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return new ViewModelProvider(this, factory).get(BindUsdtViewModel.class);
    }

    @Override
    public void initViewObservable() {

        //提交返回
        viewModel.firstBindUSDTVoSingleLiveData.observe(this, vo -> {
            CfLog.i("******");
            //getActivity().finish();
            this.userFirstBindUSDTVo = vo ;
            if (TextUtils.equals("1",String.valueOf(this.userFirstBindUSDTVo.msg_type)) && !TextUtils.isEmpty(this.userFirstBindUSDTVo.message)){
                ToastUtils.showError(this.userFirstBindUSDTVo.message);
            }else{

                binding.llAdd.setVisibility(View.GONE);
                binding.llConfirm.setVisibility(View.VISIBLE);
                binding.tvwType.setText(vo.usdt_type);
                binding.tvwAcc.setText(vo.usdt_card);
            }

        });
        viewModel.liveDataProfile.observe(this, vo -> {
            CfLog.i("******");
        });
        viewModel.liveDataVerify.observe(this, isSuccess -> {
            CfLog.i("******");
            if (isSuccess) {
                binding.llAdd.setVisibility(View.VISIBLE);
                binding.llConfirm.setVisibility(View.GONE);
            }
        });

        viewModel.liveDataBindCardResult.observe(this, userUsdtConfirmVo -> {
            CfLog.e("  viewModel.liveDataBindCardResult  viewModel.liveDataBindCardResult  viewModel.liveDataBindCardResult  viewModel.liveDataBindCardResult");
            getActivity().finish();
        });
    }

    private void doNext() {
        String account = binding.edtAcc.getText().toString().trim();
        String account2 = binding.edtAcc2.getText().toString().trim();

        if (account.isEmpty() || account2.isEmpty() ) {
            ToastUtils.showLong(R.string.txt_enter_wallet_addr);
            return;
        }
        if (!account2.equals(account)) {
            ToastUtils.showLong(R.string.txt_address_should_same);
            return;
        }

        HashMap map = new HashMap();
        map.put("flag", "add");
        map.put("oldid", "");
        map.put("entrancetype", "0");

        map.put("usdt_type", id);
        map.put("entrancetype", "ERC20_USDT");
        map.put("usdt_type", type);
        map.put("usdt_card", account);
        map.put("account_again", account2);


        map.put("submit", "submit");
        map.put("bank_id", "");
        map.put("bank_name", "");
        map.put("check", check);
        map.put("mark", "bindusdt");
        map.put("nonce", UuidUtil.getID24());
        LoadingDialog.show(getContext());
        viewModel.doFirstBindCardBySubmit( map);
    }

    private void doSubmit() {
        HashMap map = new HashMap();
        map.put("flag", "confirm");
        map.put("entrancetype", "0");

        map.put("usdt_type", type);
        map.put("usdt_card", this.userFirstBindUSDTVo.usdt_card	);
        map.put("submit", "submit");
        map.put("bank_id", "");
        map.put("bank_name", "");
        map.put("check",   this.userFirstBindUSDTVo.checkcode);
        map.put("mark", "bindusdt");
        map.put("nonce", UuidUtil.getID24());
        LoadingDialog.show(getContext());
        viewModel.doFirstBindUsdtSubmit( map);
    }

}