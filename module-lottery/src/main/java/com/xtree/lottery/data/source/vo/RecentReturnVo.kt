package com.xtree.lottery.data.source.vo

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecentReturnVo(val title: String, val histories: List<RecentLotteryBackReportVo>) :
    Parcelable