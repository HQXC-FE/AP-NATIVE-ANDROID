package com.xtree.lottery.utils.filter;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * Created by KAKA on 2024/5/3.
 * Describe: 金额过滤器
 */
public class MoneyFilter implements InputFilter {

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        // 如果输入的是第一个字符且为0，则不允许输入
        if (dstart == 0 && source.length() > 0 && source.charAt(0) == '0') {
            return "";
        }
        // 其他情况允许输入
        return null;
    }
}