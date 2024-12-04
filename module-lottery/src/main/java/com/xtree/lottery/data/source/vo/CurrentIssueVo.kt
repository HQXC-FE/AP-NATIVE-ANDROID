package com.xtree.lottery.data.source.vo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class IssueVo(
    val issue: String,
    val saleend: String,
    val salestart: String,
    val issueid: String,
    val track_limit: Int,


    var multiple: Int = 0,
    var amount: Double = 0.000,
    var isCheck: Boolean = false
) : Parcelable
