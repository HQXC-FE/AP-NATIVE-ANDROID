package com.xtree.service;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.enums.PopupPosition;
import com.xtree.base.vo.MsgPersonInfoVo;
import com.xtree.service.popwindows.ListMsgInfoDialog;
import com.xtree.service.popwindows.PromptPopup;
import com.xtree.service.popwindows.ToastPopup;

public class PopMessageUtils {

    public static void showToastPop(@NonNull Context context, @NonNull String message, @Nullable View.OnClickListener onClickListener) {
        new XPopup.Builder(context)
                .hasShadowBg(false)
                .hasBlurBg(false)
                .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                .isCenterHorizontal(true)
                .offsetY(200)
                .asCustom(new ToastPopup(context, message, onClickListener))
                .show();
    }

    public static void showPrompt(@NonNull Context context, @NonNull String message, @Nullable View.OnClickListener onConfirmListener) {
        new XPopup.Builder(context)
                .hasShadowBg(false)
                .hasBlurBg(false)
                .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                .asCustom(new PromptPopup(context, message, view -> {//取消
                }, view -> {//确认
                    if (onConfirmListener != null) {
                        onConfirmListener.onClick(view);
                    }
                })).show();
    }

    public static void showPromptDetail(@NonNull Context context, MsgPersonInfoVo msgPersonInfoVo) {
        new XPopup.Builder(context).asCustom(new ListMsgInfoDialog(context, msgPersonInfoVo, 80)).show();
    }

}
