package com.xtree.recharge.ui.widget;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.NonNull;

import com.lxj.xpopup.core.CenterPopupView;
import com.xtree.base.widget.ChineseInputFilter;
import com.xtree.recharge.R;
import com.xtree.recharge.databinding.DialogRealNameBinding;

public class RealNameDialog extends CenterPopupView {

    private ICallBack mCallBack;
    private DialogRealNameBinding binding;
    private String mContent;

    public RealNameDialog(@NonNull Context context) {
        super(context);
    }

    public RealNameDialog(@NonNull Context context, String content, ICallBack mCallBack) {
        super(context);
        this.mCallBack = mCallBack;
        this.mContent = content;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        initView();
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_real_name;
    }

    private void initView() {
        binding = DialogRealNameBinding.bind(findViewById(R.id.ll_root));

        //充值教程
        binding.tvwTutorial.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCallBack != null) {
                    mCallBack.showTutorial();
                }
            }
        });

        //联系客服
        binding.tvwCs.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCallBack != null) {
                    mCallBack.showCs();
                }
            }
        });

        //内容输入监听
        binding.edtName.setFilters(new InputFilter[]{new ChineseInputFilter()});
        binding.edtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (!TextUtils.isEmpty(editable.toString())) {
                    binding.ivwClear.setVisibility(VISIBLE);
                } else {
                    binding.ivwClear.setVisibility(GONE);
                }
            }
        });

        //清除输入
        binding.ivwClear.setOnClickListener(view -> binding.edtName.setText(""));

        // 设置确认按钮的点击事件
        binding.tvNext.setOnClickListener(view -> {
            if (mCallBack != null) {
                mCallBack.onClickConfirm(binding.edtName.getText().toString()); // 调用回调接口的方法
            }
        });

        binding.ivwClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss(); // 关闭对话框
            }
        });

    }

    // 定义回调接口
    public interface ICallBack {
        void onClickConfirm(String realName); // 当用户点击确认按钮时触发的回调方法

        void showTutorial();

        void showCs();
    }
}
