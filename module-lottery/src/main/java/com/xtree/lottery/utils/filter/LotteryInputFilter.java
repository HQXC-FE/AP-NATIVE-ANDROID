package com.xtree.lottery.utils.filter;

import android.text.InputFilter;
import android.text.Spanned;

public class LotteryInputFilter implements InputFilter {
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        StringBuilder filtered = new StringBuilder();

        for (int i = start; i < end; i++) {
            char c = source.charAt(i);

            // 允许的字符：空格、回车、逗号、分号、中文逗号、中文分号、全角空格、数字
            if (Character.isDigit(c) || c == ' ' || c == '\r' || c == ',' || c == ';' || c == '，' || c == '；' || c == '　') {
                // 全角数字转换为半角数字
                if (c >= '０' && c <= '９') {
                    c = (char) (c - '０' + '0');
                }
                filtered.append(c);
            }
        }

        return filtered.toString(); // 只返回符合要求的内容
    }
}
