package com.xtree.recharge.ui.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.widget.TextViewCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.lxj.xpopup.core.BottomPopupView;
import com.xtree.base.utils.AppUtil;
import com.xtree.base.utils.CfLog;
import com.xtree.recharge.R;
import com.xtree.recharge.databinding.DialogRcOnepayAmountBinding;
import com.xtree.recharge.ui.fragment.AmountAdapter;
import com.xtree.recharge.vo.RechargeVo;

import java.util.Arrays;

public class OnePayNextDialog extends BottomPopupView {

    RechargeVo curRechargeVo;
    double loadMin;
    double loadMax;
    private ICallBack mCallBack;
    private DialogRcOnepayAmountBinding binding;
    private AmountAdapter mAmountAdapter;
    private CharSequence kindTips;

    public OnePayNextDialog(@NonNull Context context) {
        super(context);
    }

    public OnePayNextDialog(@NonNull Context context, AmountAdapter amountAdapter, double loadMin, double loadMax, CharSequence kindTips, RechargeVo curRechargeVo, ICallBack mCallBack) {
        super(context);
        this.mCallBack = mCallBack;
        this.mAmountAdapter = amountAdapter;
        this.loadMin = loadMin;
        this.loadMax = loadMax;
        this.kindTips = kindTips;
        this.curRechargeVo = curRechargeVo;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        initView();
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_rc_onepay_amount;
    }

    private void initView() {
        binding = DialogRcOnepayAmountBinding.bind(findViewById(R.id.ll_root));

        binding.ivwClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss(); // 关闭对话框
            }
        });
        binding.ivwCs.setOnClickListener(v -> AppUtil.goCustomerService(getContext()));
        // 文字大小可随长度而改变,解决长度太长显示不全的问题(HQAP2-3310)
        TextViewCompat.setAutoSizeTextTypeWithDefaults(binding.tvwAmountHint, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(binding.tvwAmountHint,12, 14, 1, TypedValue.COMPLEX_UNIT_SP);
        binding.edtAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                CfLog.d("amount: " + s);
                if (s.toString().indexOf(".") > 0) {
                    binding.tvwRealAmount.setText(s.subSequence(0, s.toString().indexOf(".")));
                } else {
                    binding.tvwRealAmount.setText(s);
                }

                String amount = s.toString();

                if (!amount.equals(mAmountAdapter.getAmount())) {
                    mAmountAdapter.setAmount(amount);
                    mAmountAdapter.notifyDataSetChanged();
                }
                setNextButton();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.btnNext.setOnClickListener(view -> {
            if (mCallBack != null) {
                String txt = binding.tvwRealAmount.getText().toString();
                mCallBack.onClickConfirm(txt);
            }
        });


        AmountAdapter adapter = new AmountAdapter(getContext(), str -> binding.edtAmount.setText(str));
        adapter.addAll(mAmountAdapter.getData());
        mAmountAdapter = adapter;
        binding.rcvAmount.setAdapter(mAmountAdapter);
        binding.rcvAmount.setLayoutManager(new GridLayoutManager(getContext(), 4));
        binding.tvwTipBottom.setText(kindTips);
        setNextButton();

    }

    private void setNextButton() {

        binding.btnNext.setEnabled(false); // 默认禁用

        binding.tvwTipAmount.setText(getContext().getString(R.string.txt_enter_correct_amount, "" + loadMin, "" + loadMax));
        binding.tvwTipAmount.setVisibility(View.GONE);

        String txt = binding.tvwRealAmount.getText().toString().trim();
        double amount = Double.parseDouble(0 + txt);

        if (curRechargeVo.fixedamount_channelshow && curRechargeVo.fixedamount_info.length > 0) {
            binding.edtAmount.setEnabled(false);
            binding.tvwAmountHint.setText(R.string.txt_choose_recharge_amount); // 请选择金额
            // 固额 如果输入框的金额不是固额列表中的其中一个
            if (amount > 0 && !Arrays.asList(curRechargeVo.fixedamount_info).contains(txt)) {
                // "充值金额异常,请选择列表中的固定金额！",
                binding.tvwTipAmount.setText(getContext().getString(R.string.txt_choose_recharge_amount));
                binding.tvwTipAmount.setVisibility(View.VISIBLE);
                return;
            }
        } else {
            binding.edtAmount.setEnabled(true);
            String hint = getContext().getString(R.string.txt_enter_recharge_amount, "" + loadMin, "" + loadMax);
            binding.tvwAmountHint.setText(hint); // 请输入充值金额(最低%1$s元，最高%2$s元)
            if (amount > 0 && loadMax != 0 && (amount < loadMin || amount > loadMax)) {
                binding.tvwTipAmount.setVisibility(View.VISIBLE);
                return;
            }
        }

        binding.tvwAmountHint.setVisibility(amount > 0 ? INVISIBLE : VISIBLE);
        binding.btnNext.setEnabled(amount > 0);
    }

    // 定义回调接口
    public interface ICallBack {
        void onClickConfirm(String realAmount); // 当用户点击确认按钮时触发的回调方法
    }
}
