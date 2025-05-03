package com.xtree.base.widget;

import android.content.Context;
import android.widget.CheckBox;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.xtree.base.R;
import com.xtree.base.databinding.ActivityCustomerServiceDialogBinding;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.utils.AppUtil;
import com.xtree.base.utils.ClickUtil;
import com.xtree.base.vo.ProfileVo;

import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;

/**
 *我的客服 Dialog形式
 */
public class CustomerServiceDialogActivity extends BottomPopupView {
    private ProfileVo mProfileVo ;
    private boolean isCheck ;
    private static Context mContext ;;
    private BasePopupView basePopupView = null;
    private ActivityCustomerServiceDialogBinding binding ;

    public CustomerServiceDialogActivity(@NonNull Context context) {
        super(context);
    }

    public static CustomerServiceDialogActivity newInstance(@NonNull Context context) {
        CustomerServiceDialogActivity dialog = new CustomerServiceDialogActivity(context);
        mContext = context ;
        return dialog;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        initView();
    }

    private void  initView(){
        binding = ActivityCustomerServiceDialogBinding.bind(findViewById(R.id.ll_root));
        String json = SPUtils.getInstance().getString(SPKeyGlobal.HOME_PROFILE);
        mProfileVo = new Gson().fromJson(json, ProfileVo.class);

        if (mProfileVo != null){
            binding.tvUserName.setText(mProfileVo.username);
        }else {

        }

        binding.ivwBack.setOnClickListener(v -> {
            this.dismiss();
        });
        binding.customLine1.setOnClickListener(v -> {
            if (!ifAgree()) {
                ToastUtils.showLong(getResources().getString(R.string.me_agree_hint));
                showAgreementDialog(binding.ckbAgreement);
                return;
            }else{
                AppUtil.goCustomerServiceWeb(this.mContext);
            }
        });
        binding.customLine2.setOnClickListener(v -> {
            if (!ifAgree()) {
                ToastUtils.showLong(getResources().getString(R.string.me_agree_hint));
                showAgreementDialog(binding.ckbAgreement);
                return;
            }else{
                AppUtil.goCustomerServiceWeb(this.mContext);
            }
        });
        //教学
        binding.tvQAdigitalTeaching.setOnClickListener(v -> {
            showDialog( binding.tvQAdigitalTeaching.getText().toString().trim());
        });
        binding.ivtvQAdigitalTeaching.setOnClickListener(v -> {
            showDialog( binding.tvQAdigitalTeaching.getText().toString().trim());
        });
        //绑定银行卡
        binding.tvQABindCard.setOnClickListener(v -> {
            showDialog( binding.tvQABindCard.getText().toString().trim());
        });
        binding.ivQABindCard.setOnClickListener(v -> {
            showDialog( binding.tvQABindCard.getText().toString().trim());
        });
        //忘记密码
        binding.tvQAForgetLoginPassword.setOnClickListener(v -> {
            showDialog( binding.tvQAForgetLoginPassword.getText().toString().trim());
        });
        binding.ivQAForgetLoginPassword.setOnClickListener(v -> {
            showDialog( binding.tvQAForgetLoginPassword.getText().toString().trim());
        });
        //下载App
        binding.tvQADownApp.setOnClickListener(v -> {
            showDialog( binding.tvQADownApp.getText().toString().trim());
        });
        binding.ivQADownApp.setOnClickListener(v -> {
            showDialog( binding.tvQADownApp.getText().toString().trim());
        });
        //登录密码不正确
        binding.tvQALoginPassword.setOnClickListener(v -> {
            showDialog( binding.tvQALoginPassword.getText().toString().trim());
        });
        binding.ivQALoginPassword.setOnClickListener(v -> {
            showDialog( binding.tvQALoginPassword.getText().toString().trim());
        });
        //新增USDT
        binding.tvQAAddUSDT.setOnClickListener(v -> {
            showDialog( binding.tvQAAddUSDT.getText().toString().trim());
        });
        binding.ivQAAddUSDT.setOnClickListener(v -> {
            showDialog( binding.tvQAAddUSDT.getText().toString().trim());
        });
    }
    @Override
    protected int getImplLayoutId() {
        return R.layout.activity_customer_service_dialog;
    }

    @Override
    protected int getMaxHeight() {
        return (XPopupUtils.getScreenHeight(getContext()) * 90 / 100);
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
        BasePopupView ppw = new XPopup.Builder(this.mContext).asCustom(new BaseAgreementDialog(this.mContext, checkBox));
        ppw.show();
    }

    public void showDialog(final  String showTip){

        basePopupView = new XPopup.Builder(this.mContext).dismissOnBackPressed(true)
                .dismissOnTouchOutside(false)
                .asCustom(CustomerServiceQADialog.newInstance(this.mContext, showTip));
        basePopupView.show();

    }
}
