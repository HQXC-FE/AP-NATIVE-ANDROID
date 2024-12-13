package com.xtree.lottery.utils;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

public class AnimUtils {

    public static void rotateView(View view) {
        if(view == null) return;

        // 创建旋转动画
        RotateAnimation rotateAnimation = new RotateAnimation(
                0f,                      // 起始角度
                360f,                    // 结束角度
                Animation.RELATIVE_TO_SELF, 0.5f, // X轴旋转中心
                Animation.RELATIVE_TO_SELF, 0.5f  // Y轴旋转中心
        );
        rotateAnimation.setDuration(500);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        //rotateAnimation.setRepeatCount(Animation.ZORDER_NORMAL);
        rotateAnimation.setRepeatMode(Animation.RESTART);
        // 开始动画
        view.startAnimation(rotateAnimation);
    }

}
