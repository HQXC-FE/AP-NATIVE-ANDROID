package com.xtree.lottery.data.source.vo

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserMethodsVo(
    val is_multiple: Int,
    val menuid: Int,
    val methodid: Int,
    val name: String,
    val prize_group: List<PrizeGroup>,
    //val prize_level: List<String>,
    val relationMethods: List<Int>,
    val title: String
) : Parcelable

@Parcelize
data class PrizeGroup(
    val label: String,
    val value: Int
) : Parcelable