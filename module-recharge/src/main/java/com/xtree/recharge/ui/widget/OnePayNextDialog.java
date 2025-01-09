package com.xtree.recharge.ui.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.NonNull;
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
                if (binding.tvwRealAmount.length() == 0) {
                    binding.tvwAmountHint.setVisibility(View.VISIBLE);
                } else {
                    binding.tvwAmountHint.setVisibility(View.INVISIBLE);
                }


                String amount = s.toString();


                if (!TextUtils.isEmpty(amount) && (Double.parseDouble(amount) < loadMin || Double.parseDouble(amount) > loadMax)) {
                    binding.tvwTipAmount.setVisibility(View.VISIBLE);
                } else {
                    binding.tvwTipAmount.setVisibility(View.GONE);
                }

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

    }

    private void setNextButton() {

        binding.btnNext.setEnabled(false); // 默认禁用


        String txt = binding.tvwRealAmount.getText().toString();
        double amount = Double.parseDouble(0 + txt);
        if (amount < loadMin || amount > loadMax) {
            return;
        }

        if (curRechargeVo.fixedamount_channelshow && curRechargeVo.fixedamount_info.length > 0) {
            // 固额 如果输入框的金额不是固额列表中的其中一个
            if (!txt.isEmpty() && !Arrays.asList(curRechargeVo.fixedamount_info).contains(txt)) {
                // "充值金额异常,请选择列表中的固定金额！",
                return;
            }
        }

        binding.btnNext.setEnabled(true);
    }

    // 定义回调接口
    public interface ICallBack {
        void onClickConfirm(String realAmount); // 当用户点击确认按钮时触发的回调方法
    }
}
