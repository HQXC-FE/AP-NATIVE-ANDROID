package com.xtree.live.uitl;

import android.annotation.SuppressLint;
import android.content.Context;

import java.lang.reflect.Field;

import me.xtree.mvvmhabit.base.BaseApplication;

public class DpUtil {

    private static float scale;

    static {
        scale = BaseApplication.getInstance().getResources().getDisplayMetrics().density;
    }

    public static int dp2px(int dpVal) {
        return (int) (scale * dpVal + 0.5f);
    }

    public static float dp2px(float dpValue) {
        return dpValue * scale + 0.5f;
    }

    /**
     * 获取系统状态栏高度
     */
    @SuppressWarnings("ConstantConditions")
    @SuppressLint("PrivateApi")
    public static int getStatusBarHeight(Context context) {
        int height = 0;
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object instance = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = (Integer) field.get(instance);
            height = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return height;
    }
}