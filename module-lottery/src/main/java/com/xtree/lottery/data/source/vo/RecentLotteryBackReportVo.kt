package com.xtree.lottery.data.source.vo

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecentLotteryBackReportVo(
    val issue: String,
    val draw_time: String,
    val issueClass: String,
    val displayCode: List<String>,
    val form: List<String>
) : Parcelable
