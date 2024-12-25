package com.xtree.recharge.ui.widget;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.lxj.xpopup.core.CenterPopupView;
import com.xtree.recharge.R;
import com.xtree.recharge.databinding.DialogOnepayNextTipsBinding;

public class RcExpPositiveConfirmDialog extends CenterPopupView {

    private ICallBack mCallBack;
    private DialogOnepayNextTipsBinding binding;

    public RcExpPositiveConfirmDialog(@NonNull Context context) {
        super(context);
    }

    public RcExpPositiveConfirmDialog(@NonNull Context context, ICallBack mCallBack) {
        super(context);
        this.mCallBack = mCallBack;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        initView();
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.layout_rc_exp_positive_bank_confirm_dialog;
    }

    private void initView() {
        binding = DialogOnepayNextTipsBinding.bind(findViewById(R.id.ll_root));

        // 设置确认按钮的点击事件
        binding.tvwRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCallBack != null) {
                    mCallBack.onClickConfirm(); // 调用回调接口的方法
                }
                dismiss(); // 关闭对话框
            }
        });
        binding.ivwClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss(); // 关闭对话框
            }
        });

//        binding.tvwLeft.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dismiss(); // 关闭对话框
//            }
//        });

    }

    // 定义回调接口
    public interface ICallBack {
        void onClickConfirm(); // 当用户点击确认按钮时触发的回调方法
    }
}
