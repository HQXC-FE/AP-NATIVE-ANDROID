package com.xtree.base.utils;

public class ClickUtil {
    // 两次点击按钮之间的点击间隔不能少于1500毫秒
    private static final int MIN_CLICK_DELAY_TIME = 1500;
    //上一次点击的时间
    private static long lastClickTime;
    private static long lastClickTime2;
    private static long doNotRepeatRequests;

    /**
     * 限制快速点击
     */
    public static boolean isFastClick() {
        long curClickTime = System.currentTimeMillis();
        if (Math.abs((curClickTime - lastClickTime)) < MIN_CLICK_DELAY_TIME) {
            return true;
        }
        lastClickTime = curClickTime;
        return false;
    }
    /**
     * 限制投注快速点击，4秒内禁止重复点击
     */
    public static boolean isFastClick4000() {
        long curClickTime = System.currentTimeMillis();
        if (Math.abs((curClickTime - lastClickTime2)) < 4000) {
            return true;
        }
        lastClickTime2 = curClickTime;
        return false;
    }

    /**
     * 5秒内禁止重复请求
     */
    public static boolean doNotRepeatRequests() {
        long curClickTime = System.currentTimeMillis();
        if (Math.abs((curClickTime - doNotRepeatRequests)) < 5000) {
            return true;
        }
        doNotRepeatRequests = curClickTime;
        return false;
    }


}
