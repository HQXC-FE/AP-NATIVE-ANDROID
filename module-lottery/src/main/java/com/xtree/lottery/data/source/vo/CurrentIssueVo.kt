package com.xtree.lottery.data.source.vo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal

@Parcelize
data class IssueVo(
    val issue: String,
    val saleend: String,
    val salestart: String,
    val issueid: String,
    val track_limit: Int,

    var multiple: Int = 0,
    var amountBigDecimal: BigDecimal? = BigDecimal.ZERO,
    var isCheck: Boolean = false
) : Parcelable
