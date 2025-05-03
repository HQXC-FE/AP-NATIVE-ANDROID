package com.xtree.recharge.ui.fragment;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.xtree.base.utils.AppUtil;
import com.xtree.base.widget.BrowserDialogX5;
import com.xtree.recharge.R;
import com.xtree.recharge.databinding.DialogRcBrowserBinding;

/**
 * 充值浏览器弹窗
 */
public class RechargeBrowserDialog extends BrowserDialogX5 {
    boolean isShowBank = false; // 是否显示跳转到外部浏览器的按钮(充值跳转来的)

    DialogRcBrowserBinding binding;

    public RechargeBrowserDialog(@NonNull Context context, String title, String url) {
        super(context, title, url);
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        binding = DialogRcBrowserBinding.bind(findViewById(R.id.ll_root));

        if (isShowBank) {
            binding.ivwGoWeb.setVisibility(View.VISIBLE);
        }
        binding.ivwGoWeb.setOnClickListener(v -> AppUtil.goBrowser(getContext(), url));
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_rc_browser;
    }

    public BrowserDialogX5 setShowBank(boolean isShowBank) {
        this.isShowBank = isShowBank;
        return this;
    }

}
