package com.xtree.lottery.data.source.vo

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecentLotteryVo(
    val code: String,
    val draw_time: String?,
    val issue: String,
    val original_code: String,
    val split_code: ArrayList<String>
) : Parcelable