package com.xtree.mine.ui.activity;

import static com.xtree.base.router.RouterActivityPath.Mine.PAGER_CUSTOMER_SERVICE;

import android.os.Build;
import android.os.Bundle;
import android.widget.CheckBox;

import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.gson.Gson;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.utils.AppUtil;
import com.xtree.base.utils.ClickUtil;
import com.xtree.base.vo.ProfileVo;
import com.xtree.base.widget.CustomerServiceQADialog;
import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.databinding.ActivityCustomerServiceBinding;
import com.xtree.mine.ui.fragment.AgreementDialog;
import com.xtree.mine.ui.viewmodel.ChooseWithdrawViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;

import me.xtree.mvvmhabit.base.BaseActivity;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;

/**
 *我的客服 Activity
 */
@Route(path = PAGER_CUSTOMER_SERVICE)
public class CustomerServiceActivity extends BaseActivity<ActivityCustomerServiceBinding, ChooseWithdrawViewModel> {
    private ProfileVo mProfileVo ;
    private boolean isCheck ;

    private BasePopupView basePopupView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ToastUtils.showError("CustomerServiceActivity");
        //修复Android 8.0版本 Activity 设置为透明主题造成的崩溃
        if (Build.VERSION.SDK_INT == 26 && isTranslucentOrFloating()) {
            fixOrientation(this);
        }
        String json = SPUtils.getInstance().getString(SPKeyGlobal.HOME_PROFILE);
        mProfileVo = new Gson().fromJson(json, ProfileVo.class);

        super.onCreate(savedInstanceState);

    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_customer_service;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initView() {
        if (mProfileVo != null){
            binding.tvUserName.setText(mProfileVo.username);
        }else {

        }

        binding.ivwBack.setOnClickListener(v -> {
            this.finish();
        });
        binding.customLine1.setOnClickListener(v -> {
            if (!ifAgree()) {
                ToastUtils.showLong(getResources().getString(R.string.me_agree_hint));
                showAgreementDialog(binding.ckbAgreement);
                return;
            }else{
                AppUtil.goCustomerServiceWeb(this);
            }
        });
        binding.customLine2.setOnClickListener(v -> {
            if (!ifAgree()) {
                ToastUtils.showLong(getResources().getString(R.string.me_agree_hint));
                showAgreementDialog(binding.ckbAgreement);
                return;
            }else{
                AppUtil.goCustomerServiceWeb(this);
            }
        });
        //教学
        binding.llQAdigitalTeaching.setOnClickListener(v -> {
            showDialog( binding.tvQAdigitalTeaching.getText().toString().trim());
        });
        binding.tvQAdigitalTeaching.setOnClickListener(v -> {
            showDialog( binding.tvQAdigitalTeaching.getText().toString().trim());
        });
        binding.ivtvQAdigitalTeaching.setOnClickListener(v -> {
            showDialog( binding.tvQAdigitalTeaching.getText().toString().trim());
        });
        //绑定银行卡
        binding.llQABindCard.setOnClickListener(v -> {
            showDialog( binding.tvQABindCard.getText().toString().trim());
        });
        binding.tvQABindCard.setOnClickListener(v -> {
            showDialog( binding.tvQABindCard.getText().toString().trim());
        });
        binding.ivQABindCard.setOnClickListener(v -> {
            showDialog( binding.tvQABindCard.getText().toString().trim());
        });
        //忘记密码
        binding.llQAForgetLoginPassword.setOnClickListener(v -> {
            showDialog( binding.tvQAForgetLoginPassword.getText().toString().trim());
        });
        binding.tvQAForgetLoginPassword.setOnClickListener(v -> {
            showDialog( binding.tvQAForgetLoginPassword.getText().toString().trim());
        });
        binding.ivQAForgetLoginPassword.setOnClickListener(v -> {
            showDialog( binding.tvQAForgetLoginPassword.getText().toString().trim());
        });
        //下载App
        binding.llQADownApp.setOnClickListener(v -> {
            showDialog( binding.tvQADownApp.getText().toString().trim());
        });
        binding.tvQADownApp.setOnClickListener(v -> {
            showDialog( binding.tvQADownApp.getText().toString().trim());
        });
        binding.ivQADownApp.setOnClickListener(v -> {
            showDialog( binding.tvQADownApp.getText().toString().trim());
        });
        //登录密码不正确
        binding.llQALoginPassword.setOnClickListener(v -> {
            showDialog( binding.tvQALoginPassword.getText().toString().trim());
        });
        binding.tvQALoginPassword.setOnClickListener(v -> {
            showDialog( binding.tvQALoginPassword.getText().toString().trim());
        });
        binding.ivQALoginPassword.setOnClickListener(v -> {
            showDialog( binding.tvQALoginPassword.getText().toString().trim());
        });
        //新增USDT
        binding.llQAAddUSDT.setOnClickListener(v -> {
            showDialog( binding.tvQAAddUSDT.getText().toString().trim());
        });
        binding.tvQAAddUSDT.setOnClickListener(v -> {
            showDialog( binding.tvQAAddUSDT.getText().toString().trim());
        });
        binding.ivQAAddUSDT.setOnClickListener(v -> {
            showDialog( binding.tvQAAddUSDT.getText().toString().trim());
        });
    }

    @Override
    public ChooseWithdrawViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(this.getApplication());
        return new ViewModelProvider(this, factory).get(ChooseWithdrawViewModel.class);
    }

    @Override
    public void initData() {
    }

    @Override
    public void initViewObservable() {
    }
    private boolean ifAgree() {
        return binding.ckbAgreement.isChecked();
    }

    /**
     * 显示使用条款
     */
    private void showAgreementDialog(CheckBox checkBox) {
        if (ClickUtil.isFastClick()) {
            return;
        }
        BasePopupView ppw = new XPopup.Builder(CustomerServiceActivity.this).asCustom(new AgreementDialog(CustomerServiceActivity.this, checkBox));
        ppw.show();
    }

    public void showDialog(final  String showTip){

        basePopupView = new XPopup.Builder(this).dismissOnBackPressed(true)
                .dismissOnTouchOutside(false)
                .asCustom(CustomerServiceQADialog.newInstance(this, showTip));
        basePopupView.show();

    }
}
