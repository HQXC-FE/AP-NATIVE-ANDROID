package com.xtree.live;

import android.text.TextUtils;

import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.utils.SPUtil;

import me.xtree.mvvmhabit.base.BaseApplication;
import me.xtree.mvvmhabit.utils.SPUtils;

public class LiveConfig {

    public static String getUserId() {

        return SPUtil.get(BaseApplication.getInstance()).getInt(SPKey.UID,0)+"";
    }

    public static boolean isNotificationBeepOn() {
        return SPUtil.get(BaseApplication.getInstance()).get(SPKey.MMKV_KEY_BEEP, true);
    }

    public static void setNotificationBeepOn(boolean isChecked) {
        SPUtil.get(BaseApplication.getInstance()).put(SPKey.MMKV_KEY_BEEP, isChecked);
    }

    public static String getLiveToken() {
        return SPUtil.get(BaseApplication.getInstance()).get(SPKey.TOKEN, null);
    }
    public static void setLiveToken(String token){
        SPUtil.get(BaseApplication.getInstance()).put(SPKey.TOKEN,token);
    }

    public static boolean isLogin(){
        String token = SPUtils.getInstance().getString(SPKeyGlobal.USER_TOKEN,null);
        return !TextUtils.isEmpty(token);
    }

    public static String getChannelCode() {
        return SPUtil.get(BaseApplication.getInstance()).get(SPKey.CHANNEL_CODE, null);
    }
    public static void setChannelCode(String token){
        SPUtil.get(BaseApplication.getInstance()).put(SPKey.CHANNEL_CODE,token);
    }

}
