package com.xtree.base.utils.filter;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * Created by KAKA on 2024/5/8.
 * Describe: 彩票单式投注输入框过滤器
 */
public class LotteryInputFilter implements InputFilter {
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        String regex = "[\\d,,;；\\s]+";
        if (!source.toString().matches(regex)) {

            return "";
        }

        // 获取输入框中已经输入的内容
        String existingText = dest.toString();
        // 获取当前输入的字符
        String currentInput = source.toString();

        // 检查是否连续输入了空格、逗号或分号
        if (existingText.length() > 0) {
            char lastChar = existingText.charAt(existingText.length() - 1);

            // 如果最后一个字符是空格、逗号或分号，且当前输入的是这些字符中的任意一个，则禁止输入
            if (isSpecialCharacter(lastChar) && isSpecialCharacter(currentInput.charAt(0))) {
                return ""; // 禁止连续输入特殊字符
            }
        }
        return null;
    }

    // 判断是否为空格、逗号或分号
    private boolean isSpecialCharacter(char c) {
        return c == ' ' || c == ',' || c == '，' || c == ';' || c == '；';
    }
}
