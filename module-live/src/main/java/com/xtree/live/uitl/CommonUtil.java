package com.xtree.live.uitl;

import android.text.TextUtils;

public class CommonUtil {

    /**
     * 内容全是不可显示的字符
     */
    public static boolean isContentBlank(CharSequence content) {
        if (TextUtils.isEmpty(content)) return true;
        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);
            if (c >= 33) {
                return false;
            }
        }
        return true;
    }
}

