package com.xtree.live;

import android.text.TextUtils;

import com.xtree.base.utils.SPUtil;

import me.xtree.mvvmhabit.base.BaseApplication;

public class LiveConfig {

    public static String getUserId() {

        return SPUtil.get(BaseApplication.getInstance()).get(SPKey.UID,null);
    }

    public static boolean isNotificationBeepOn() {
        return SPUtil.get(BaseApplication.getInstance()).get(SPKey.MMKV_KEY_BEEP, true);
    }

    public static void setNotificationBeepOn(boolean isChecked) {
        SPUtil.get(BaseApplication.getInstance()).put(SPKey.MMKV_KEY_BEEP, isChecked);
    }

}
