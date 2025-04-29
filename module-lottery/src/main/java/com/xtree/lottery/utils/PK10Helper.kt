package com.xtree.lottery.utils

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import me.xtree.mvvmhabit.utils.ConvertUtils

public object PK10Helper {
    fun getBallBackground(alias: String, code: String): GradientDrawable {
        val colors = when (alias) {
            "pk10", "ydl10", "xyft", "jssc", "ynpk10ff", "ynpk10wf", "azxy10" -> {
                when (code) {
                    "01" -> intArrayOf(Color.parseColor("#e4c772"), Color.parseColor("#b68a07"))
                    "02" -> intArrayOf(Color.parseColor("#59c3e4"), Color.parseColor("#028bb4"))
                    "03" -> intArrayOf(Color.parseColor("#7e7e7e"), Color.parseColor("#3a3a3a"))
                    "04" -> intArrayOf(Color.parseColor("#fc794a"), Color.parseColor("#cf4c1d"))
                    "05" -> intArrayOf(Color.parseColor("#13ccbd"), Color.parseColor("#01857a"))
                    "06" -> intArrayOf(Color.parseColor("#7f95ff"), Color.parseColor("#1f44ff"))
                    "07" -> intArrayOf(Color.parseColor("#dee0de"), Color.parseColor("#9c9c9c"))
                    "08" -> intArrayOf(Color.parseColor("#fb4abf"), Color.parseColor("#b72670"))
                    "09" -> intArrayOf(Color.parseColor("#ff6161"), Color.parseColor("#d01919"))
                    "10" -> intArrayOf(Color.parseColor("#62c865"), Color.parseColor("#2b862e"))
                    else -> intArrayOf(Color.LTGRAY, Color.DKGRAY) // 默认
                }
            }

            "jssm" -> { // 急速赛马
                when (code) {
                    "01" -> intArrayOf(Color.parseColor("#e6e6e6"), Color.parseColor("#9c9c9c"))
                    "02" -> intArrayOf(Color.parseColor("#d7d779"), Color.parseColor("#a6a612"))
                    "03" -> intArrayOf(Color.parseColor("#f9a052"), Color.parseColor("#ac4410"))
                    "04" -> intArrayOf(Color.parseColor("#78f55f"), Color.parseColor("#2c9018"))
                    "05" -> intArrayOf(Color.parseColor("#be7cff"), Color.parseColor("#962cff"))
                    "06" -> intArrayOf(Color.parseColor("#78bafc"), Color.parseColor("#1f74c9"))
                    "07" -> intArrayOf(Color.parseColor("#f85d5d"), Color.parseColor("#d42121"))
                    "08" -> intArrayOf(Color.parseColor("#7a7a7a"), Color.parseColor("#484848"))
                    "09" -> intArrayOf(Color.parseColor("#dd5816"), Color.parseColor("#993c0e"))
                    "10" -> intArrayOf(Color.parseColor("#fb62fb"), Color.parseColor("#c627c6"))
                    else -> intArrayOf(Color.LTGRAY, Color.DKGRAY)
                }
            }

            else -> intArrayOf(Color.LTGRAY, Color.DKGRAY)
        }

        return GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors).apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = ConvertUtils.dp2px(6f).toFloat()
        }
    }
}