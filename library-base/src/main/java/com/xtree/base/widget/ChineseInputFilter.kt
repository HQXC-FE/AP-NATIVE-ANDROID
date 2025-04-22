package com.xtree.base.widget

import android.text.InputFilter
import android.text.Spanned

// 自定义过滤器
class ChineseInputFilter : InputFilter {
    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        val filtered = StringBuilder()
        for (i in start until end) {
            val char = source[i]
            // 判断字符是否为中文
            if (char.toString().matches(Regex("[\\u4e00-\\u9fa5]"))) {
                filtered.append(char)
            }
        }
        return if (filtered.isEmpty()) "" else filtered
    }
}
