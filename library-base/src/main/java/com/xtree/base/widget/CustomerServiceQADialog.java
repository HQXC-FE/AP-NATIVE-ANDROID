package com.xtree.base.widget;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.lxj.xpopup.core.BottomPopupView;
import com.xtree.base.R;
import com.xtree.base.databinding.DialogCustomerServiceBinding;

/**
 * 客服 常见问题 展示 Dialog
 */
public class CustomerServiceQADialog extends BottomPopupView {
    interface ICustomerServiceDialog{
        void closeCustomerServiceDialog();
    }
    private static Context mContext ;
    private static String tipString ;
    private static ICustomerServiceDialog callback ;
    private DialogCustomerServiceBinding binding ;
    public CustomerServiceQADialog(@NonNull Context context ){
        super(context);
    }
    public static CustomerServiceQADialog newInstance(@NonNull Context context, String tipStrings) {
        CustomerServiceQADialog dialog = new CustomerServiceQADialog(context);
        mContext = context ;
        tipString  = tipStrings ;
        return dialog;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_customer_service;
    }
    @Override
    protected void onCreate() {
        super.onCreate();
        initView();
    }

    private void initView() {
        binding = DialogCustomerServiceBinding.bind(findViewById(R.id.ll_root));
        binding.ivwBack.setOnClickListener(v -> {
            dismiss();
        });
        binding.tvCustomerServiceTip.setText(this.tipString);
        if (TextUtils.equals(this.tipString , this.mContext.getString(R.string.txt_qa_digital_teaching))){
            binding.tvCustomerServiceDetailed.setText(this.mContext.getString(R.string.txt_qa_digital_teaching_detail));
        } else if (TextUtils.equals(this.tipString , this.mContext.getString(R.string.txt_qa_bind_bank_card))) {
            binding.tvCustomerServiceDetailed.setText(this.mContext.getString(R.string.txt_qa_bind_bank_card_detail));
        }else if (TextUtils.equals(this.tipString , this.mContext.getString(R.string.txt_qa_forget_login_password))) {
            binding.tvCustomerServiceDetailed.setText(this.mContext.getString(R.string.txt_qa_forget_login_password_detail));
            binding.tvCustomerServiceDetailedTip.setVisibility(VISIBLE);
            binding.tvCustomerServiceDetailedTip.setText(this.mContext.getString(R.string.txt_qa_forget_login_password_tip));
        }else if (TextUtils.equals(this.tipString , this.mContext.getString(R.string.txt_qa_down_app))) {
            binding.tvCustomerServiceDetailed.setText(this.mContext.getString(R.string.txt_qa_down_app_detail));
        }else if (TextUtils.equals(this.tipString , this.mContext.getString(R.string.txt_qa_login_password))) {
            binding.tvCustomerServiceDetailed.setText(this.mContext.getString(R.string.txt_qa_login_password_detail));
            binding.tvCustomerServiceDetailedTip.setVisibility(VISIBLE);
            binding.tvCustomerServiceDetailedTip.setText(this.mContext.getString(R.string.txt_qa_login_password_tip));
        }else if (TextUtils.equals(this.tipString , this.mContext.getString(R.string.txt_qa_add_usdt))) {
            binding.tvCustomerServiceDetailed.setText(this.mContext.getString(R.string.txt_qa_add_usdt_detail));
        }
    }

}
