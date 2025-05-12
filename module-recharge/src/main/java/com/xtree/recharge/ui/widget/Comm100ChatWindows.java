package com.xtree.recharge.ui.widget;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.Gson;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.mvvm.ExKt;
import com.xtree.base.utils.AppUtil;
import com.xtree.base.utils.DomainUtil;
import com.xtree.base.utils.TagUtils;
import com.xtree.base.widget.FloatingWindows;
import com.xtree.recharge.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import io.sentry.Sentry;
import com.xtree.base.base.BaseApplication;
import com.xtree.base.utils.SPUtils;
import com.xtree.base.utils.Utils;

/**
 * Created by KAKA on 2024/6/12.
 * Describe: comm100 聊天
 */
public class Comm100ChatWindows extends FloatingWindows {

    private final int siteId = 65000194;
    private final String planId = "1e906220-bcfb-4f17-a5eb-bf7e9ab74be9";
    private OnClickListener onClickListener = null;

    /**
     * onepay 客服配置
     * siteid
     * 65000194
     * <p>
     * campaign id
     * 1e906220-bcfb-4f17-a5eb-bf7e9ab74be9
     */
    public Comm100ChatWindows(Context context) {
        super(context);
        onCreate(R.layout.layout_commchat_flowview);
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public void initData() {
        setIcon(R.mipmap.icon_kefu_float);

        View cancleView = secondaryLayout.findViewById(R.id.commchat_flowview_cancle);

        if (cancleView != null) {
            cancleView.setOnClickListener(v -> hideTip());
        }


        if (floatView != null) {
            floatView.setOnClickListener(v -> {
                //旧客服处理
                String oldChatUrl = SPUtils.getInstance().getString(SPKeyGlobal.ONEPAY_CUSTOMER_SERVICE_LINK, "");
                if (!TextUtils.isEmpty(oldChatUrl)) {
                    if (!oldChatUrl.contains("?")) {
                        oldChatUrl += "?CUSTOM!orderid=";
                    } else {
                        oldChatUrl += "&CUSTOM!orderid=";
                    }
                    oldChatUrl = ExKt.plusDomainOrNot(oldChatUrl, DomainUtil.getApiUrl());
                }
                //新客服处理
                String newChatUrl = oldChatUrl;
                Map<String, String> remark = null;
                Set newChatUrlSet = SPUtils.getInstance().getStringSet(SPKeyGlobal.OP_HICHAT_URL_SUFFIX, Set.of());

                if (!newChatUrlSet.isEmpty()) {
                    newChatUrl = (String) newChatUrlSet.iterator().next();
                    //拼接域名
                    newChatUrl = ExKt.plusDomainOrNot(newChatUrl, DomainUtil.getApiUrl());
                    //静态参数
                    remark = new HashMap<>();
                    remark.put("deviceId", "android-app-" + TagUtils.getDeviceId(Utils.getContext()));
                    remark.put("deviceInfo", "Android" + "," + "Version:" + AppUtil.getAppVersion(BaseApplication.getInstance().getApplicationContext()) + "," + "Device:" + Build.BRAND + "," + Build.MODEL + "," + Build.VERSION.SDK_INT);
                    remark.put("userId", SPUtils.getInstance().getString(SPKeyGlobal.USER_ID));
//                        put("userIp", "userIp");
//                        put("orderId", "orderId");

                }
                if (onClickListener != null) {
                    onClickListener.onClick(v, newChatUrl, remark);
                }
            });
        }
    }

    /**
     * 显示提示
     */
    public void showTip() {
        setBottomLocation();
    }

    @Override
    public void show() {
        if (TextUtils.isEmpty(SPUtils.getInstance().getString(SPKeyGlobal.ONEPAY_CUSTOMER_SERVICE_LINK, "")) && SPUtils.getInstance().getStringSet(SPKeyGlobal.OP_HICHAT_URL_SUFFIX, Set.of()).isEmpty()) {
        } else {
            super.show();
        }

    }

    /**
     * 隐藏提示
     */
    public void hideTip() {
        removeSecond();
    }

    /**
     * 封装URL链接
     */
    public String getChatUrl(String merchantOrder, String chatUrl, Map<String, String> remark) {

        //用老链接
        if (remark == null) {
            return chatUrl += merchantOrder;
        } else {
            remark.put("orderId", merchantOrder);
            // 2. 将 Map 转换为 JSON 字符串
            Gson gson = new Gson();
            String remarkJsonString = gson.toJson(remark);
            // 3. 对 JSON 字符串进行 URL 编码
            String encodedJson = remarkJsonString;
            try {
                encodedJson = URLEncoder.encode(remarkJsonString, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                Sentry.captureException(e);
                e.printStackTrace();
            }
//            String sid = SPUtils.getInstance().getString(SPKeyGlobal.USER_NAME, "");
            return chatUrl += ("&sid=" + merchantOrder + "&remark=" + encodedJson);
        }
    }

    public interface OnClickListener {
        void onClick(View view, String url, Map<String, String> remark);
    }
}
