package com.xtree.recharge.ui.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.xtree.base.utils.AppUtil;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.DomainUtil;
import com.xtree.base.utils.QrcodeUtil;
import com.xtree.base.widget.MsgDialog;
import com.xtree.recharge.R;
import com.xtree.recharge.databinding.DialogRcOrderWebBinding;
import com.xtree.recharge.vo.RechargePayVo;

import me.xtree.mvvmhabit.utils.ToastUtils;

public class RechargeOrderWebDialog extends BottomPopupView {

    RechargePayVo mRechargePayVo;
    DialogRcOrderWebBinding binding;
    BasePopupView ppw;
    BasePopupView ppw2;
    ICallBack mCallBack;

    interface ICallBack {
        void onCallBack();
    }

    public RechargeOrderWebDialog(@NonNull Context context) {
        super(context);
    }

    public RechargeOrderWebDialog(@NonNull Context context, RechargePayVo mRechargePayVo, ICallBack mCallBack) {
        super(context);
        this.mRechargePayVo = mRechargePayVo;
        this.mCallBack = mCallBack;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        initView();
    }

    private void initView() {
        binding = DialogRcOrderWebBinding.bind(findViewById(R.id.ll_root));
        if (mRechargePayVo.paycode.toLowerCase().contains("ebpay")){
            binding.tvw01.setText("需先下载EBPAY钱包才可进行支付，进入充值页面下载。\n"+getResources().getString(R.string.txt_rc_browser_continue));
        }
        binding.ivwClose.setOnClickListener(v -> dismiss());
        binding.tvwCs.setOnClickListener(v -> AppUtil.goCustomerService(getContext()));
        binding.tvwTitle.setText(mRechargePayVo.payname);
        binding.tvwMoney.setText(mRechargePayVo.money);

        binding.tvwCopy.setOnClickListener(v -> copy(mRechargePayVo.qrcodeurl));

        String txt = mRechargePayVo.maxexpiretime + getContext().getString(R.string.txt_minutes); // xx分钟
        txt = "<font color=#EE5A5A> " + txt + " </font>"; // 加彩色
        txt = getContext().getString(R.string.txt_rc_submit_succ_pay_in_minutes_pls, txt).replace("\n", "<br>");
        binding.tvwMaxExpireTime.setText(HtmlCompat.fromHtml(txt, HtmlCompat.FROM_HTML_MODE_LEGACY));

        binding.tvwOk.setOnClickListener(v -> dismiss());
        binding.tvwShowPay.setOnClickListener(v -> showTipDialog());

        binding.tvw01.setText("1. " + binding.tvw01.getText().toString());
        binding.tvw02.setText("2. " + binding.tvw02.getText().toString());
        binding.tvw03.setText("3. " + binding.tvw03.getText().toString());

        if (mRechargePayVo.isqrcode) {
            binding.tvwQrcodeUrl.setText(mRechargePayVo.qrcodeurl);
            binding.llQrcodeUrl.setVisibility(View.VISIBLE);
            binding.ivwQrcode.setVisibility(View.VISIBLE);
            binding.ivwQrcode.setImageBitmap(QrcodeUtil.getQrcode(mRechargePayVo.qrcodeurl));
            binding.tvwOk.setVisibility(View.VISIBLE);
            binding.tvwShowPay.setVisibility(View.GONE); // 隐藏
        }

    }

    private void showTipDialog() {
        String title = getContext().getString(R.string.txt_tip);
        String msg = getContext().getString(R.string.txt_rc_browser_continue);
        ppw = new XPopup.Builder(getContext())
                .moveUpToKeyboard(false)
                .dismissOnTouchOutside(false)
                .dismissOnBackPressed(false)
                .asCustom(new MsgDialog(getContext(), title, msg, null, null, new MsgDialog.ICallBack() {
                    @Override
                    public void onClickLeft() {
                        ppw.dismiss();
                    }

                    @Override
                    public void onClickRight() {
                        goPay();
                        showTipDialog2();
                        ppw.dismiss();
                    }
                }));
        ppw.show();
    }

    private void showTipDialog2() {

        String title = getContext().getString(R.string.txt_tip);
        String msg = getContext().getString(R.string.txt_rc_is_succ);
        String txtLeft = getContext().getString(R.string.txt_rc_question);
        String txtRight = getContext().getString(R.string.txt_rc_succ);

        ppw2 = new XPopup.Builder(getContext())
                .moveUpToKeyboard(false)
                .dismissOnTouchOutside(false)
                .dismissOnBackPressed(false)
                .asCustom(new MsgDialog(getContext(), title, msg, txtLeft, txtRight, new MsgDialog.ICallBack() {
                    @Override
                    public void onClickLeft() {
                        ppw2.dismiss();
                    }

                    @Override
                    public void onClickRight() {
                        ppw2.dismiss();
                        dismissParent(); //ppw.dismiss(); // 上层dialog也要关闭 2024-02-17
                        mCallBack.onCallBack(); // 如果是从其它页跳过来的,要关闭 2024-02-17
                    }
                }));
        ppw2.show();
    }

    private void dismissParent() {
        dismiss();
    }

    private void goPay() {
        String url = mRechargePayVo.redirecturl;
        if (!TextUtils.isEmpty(url)) {
            if (!url.startsWith("http")) {
                if (mRechargePayVo.domainList.size() > 0) {
                    url = mRechargePayVo.domainList.get(0) + url;
                } else {
                    url = DomainUtil.getDomain2() + url;
                }
            }
            CfLog.i(mRechargePayVo.payname + ", jump: " + url);
            //  弹窗
            AppUtil.goBrowser(getContext(), url);
        }
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_rc_order_web;
    }

    @Override
    protected int getMaxHeight() {
        //return super.getMaxHeight();
        return (XPopupUtils.getScreenHeight(getContext()) * 75 / 100);
    }

    private void copy(String txt) {
        CfLog.d(txt);
        ClipboardManager cm = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData cd = ClipData.newPlainText("txt", txt);
        cm.setPrimaryClip(cd);
        ToastUtils.showLong(R.string.txt_copied);
    }

}
