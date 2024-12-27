package com.xtree.base.widget;

import android.content.Context;
import android.widget.CheckBox;

import androidx.annotation.NonNull;

import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.xtree.base.R;
import com.xtree.base.databinding.DialogAgreementBaseBinding;

/**
 * 客服使用条例
 */
public class BaseAgreementDialog extends BottomPopupView {
    private DialogAgreementBaseBinding binding ;
    public BaseAgreementDialog(@NonNull Context context , CheckBox checkBox) {
        super(context);
        binding = DialogAgreementBaseBinding.bind(findViewById(R.id.cl_root));
        binding.tvAgree.setOnClickListener(v -> {
            checkBox.setChecked(true);
            dismiss();
        });
        binding.tvNoAgree.setOnClickListener(v -> {
            checkBox.setChecked(false);
            dismiss();
        });
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_agreement_base;
    }

    @Override
    protected int getMaxHeight() {
        return  XPopupUtils.getScreenHeight(getContext()) * 90 / 100 ;
    }

}
