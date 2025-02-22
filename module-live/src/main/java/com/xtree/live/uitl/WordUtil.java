package com.xtree.live.uitl;

import android.app.Application;
import android.content.Context;

import me.xtree.mvvmhabit.base.BaseApplication;

public class WordUtil {
    public static String getString(Context context, int res) {
        return context.getResources().getString(res);
    }

    public static String getString(int res) {
        return BaseApplication.getInstance().getResources().getString(res);
    }

    public static String getString(int res, Object... formatArgs) {
        return BaseApplication.getInstance().getString(res, formatArgs);
    }

    public static String getString(Context context, int res, Object... formatArgs) {
        return context.getResources().getString(res, formatArgs);
    }
}
