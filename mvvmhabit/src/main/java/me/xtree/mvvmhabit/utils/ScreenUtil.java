package me.xtree.mvvmhabit.utils;

import android.content.res.Resources;
import android.util.DisplayMetrics;

public class ScreenUtil {

    public static void setCustomDensity(Resources resources) {
        // 获取当前屏幕的 DisplayMetrics
        DisplayMetrics metrics = resources.getDisplayMetrics();

        // 设计稿的宽度和高度 (例如 812dp 宽, 375dp 高)
//        float designWidthDp = 812f;
        float designWidthDp = 375f;

        // 计算 density，使用屏幕的宽度
//        float heightRatio = metrics.heightPixels / designHeightDp;
        float widthRatio = metrics.widthPixels / designWidthDp;
        // 取宽高比例中较小的值进行适配
//        float targetDensity = (widthRatio < heightRatio) ? widthRatio : heightRatio;
        float targetDensity = widthRatio;

        // 计算 scaledDensity
        float calculatedScaledDensity = targetDensity * (metrics.scaledDensity / metrics.density);

        // 应用新的 density 和 scaledDensity
        metrics.density = targetDensity;
        metrics.scaledDensity = calculatedScaledDensity;
        metrics.densityDpi = (int) (targetDensity * 160); // 通常使用 160 作为基准 dpi
    }

}
