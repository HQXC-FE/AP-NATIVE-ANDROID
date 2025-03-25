package com.xtree.live;

import android.text.TextUtils;

import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.utils.SPUtil;

import me.xtree.mvvmhabit.base.BaseApplication;
import me.xtree.mvvmhabit.utils.SPUtils;

public class LiveConfig {

    public static String getUserId() {

        return SPUtils.getInstance().getInt(SPKey.UID, 0) + "";
    }

    public static boolean isNotificationBeepOn() {
        return SPUtils.getInstance().getBoolean(SPKey.MMKV_KEY_BEEP, true);
    }

    public static void setNotificationBeepOn(boolean isChecked) {
        SPUtils.getInstance().put(SPKey.MMKV_KEY_BEEP, isChecked);
    }

    public static String getLiveToken() {
        return SPUtils.getInstance().getString(SPKey.TOKEN);
    }

    public static void setLiveToken(String token) {
        SPUtils.getInstance().put(SPKey.TOKEN, token);
    }

    public static boolean isLogin() {
        String token = SPUtils.getInstance().getString(SPKeyGlobal.USER_TOKEN, null);
        return !TextUtils.isEmpty(token);
    }

    public static String getChannelCode() {
        return SPUtils.getInstance().getString(SPKey.CHANNEL_CODE, "xc");
    }

    public static void setChannelCode(String token) {
        SPUtils.getInstance().put(SPKey.CHANNEL_CODE, token);
    }

    public static void blockGiftShown(boolean block) {
        SPUtils.getInstance().put(SPKey.BLOCK_LIVE_GIFT_SHOWN, block);
    }

    public static boolean isBlockGiftShown() {
        return SPUtils.getInstance().getBoolean(SPKey.BLOCK_LIVE_GIFT_SHOWN, false);
    }

    public static Boolean isVisitor() {
        return 0 == SPUtils.getInstance().getInt(SPKey.UID, 0);
    }

    public static void setReferer(String url) {
        if (TextUtils.isEmpty(url)) url = "https://zhibo-apps.oxldkm.com/";

        SPUtils.getInstance().put(SPKey.REFERER, url);
    }

    public static String getReferer() {
        return SPUtils.getInstance().getString(SPKey.REFERER, "https://zhibo-apps.oxldkm.com/");
    }

    public static void setVisitorId(String visitorId) {
        SPUtils.getInstance().put(SPKey.VISITOR_ID, visitorId);
    }

    public static void setAppApi(String url) {
        if(TextUtils.isEmpty(url)) url = "https://zhibo-apps.oxldkm.com/";
        if(!url.endsWith("/")) url += "/";
        SPUtils.getInstance().put(SPKey.APP_API, url);
    }
    public static String getAppApi() {
        return SPUtils.getInstance().getString(SPKey.APP_API, "https://zhibo-apps.oxldkm.com/");
    }
}
