package com.xtree.lottery.utils

import com.xtree.lottery.R

object LhcHelper {

    // 六合彩红蓝绿色对照表
    private val colorDic = mapOf(
        "bc_red" to listOf(1, 2, 7, 8, 12, 13, 18, 19, 23, 24, 29, 30, 34, 35, 40, 45, 46),
        "bc_blue" to listOf(3, 4, 9, 10, 14, 15, 20, 25, 26, 31, 36, 37, 41, 42, 47, 48),
        "bc_green" to listOf(5, 6, 11, 16, 17, 21, 22, 27, 28, 32, 33, 38, 39, 43, 44, 49)
    )

    /**
     * 获取号码颜色（红 / 蓝 / 绿 / 默认）
     */
    fun getNumberColor(number: Any): Int {
        val num = number.toString().toIntOrNull() ?: return 0
        return when {
            colorDic["bc_red"]?.contains(num) == true -> R.mipmap.lottery_lhc_ball_red
            colorDic["bc_blue"]?.contains(num) == true -> R.mipmap.lottery_lhc_ball_blue
            colorDic["bc_green"]?.contains(num) == true -> R.mipmap.lottery_lhc_ball_green
            else -> 0
        }
    }

    /**
     * 六合彩开奖号码处理（插入 -- 并按每两位分组）
     */
    fun makeLhcIssue(code: String): List<Map<String, String>> {
        if (code.length < 13) return emptyList()

        val str1 = code.substring(0, 12)  // 前6个号码（每个2位）
        val str2 = code.substring(12)     // 特码
        val merged = "$str1——$str2"   // 插入 "--"

        return merged.chunked(2).map { chunk ->
            mapOf("codes" to chunk)
        }
    }

    /**
     * 六合彩开奖号码处理（插入 -- 并按每两位分组）
     */
    fun makeLhcIssue2(code: String): List<String> {
        if (code.length < 13) return emptyList()

        val str1 = code.substring(0, 12)  // 前6个号码（每个2位）
        val str2 = code.substring(12)     // 特码
        val merged = "$str1——$str2"   // 插入 "--"

        return merged.chunked(2)
    }

}
