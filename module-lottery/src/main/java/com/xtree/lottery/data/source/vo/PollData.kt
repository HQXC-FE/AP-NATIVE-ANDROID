package com.xtree.lottery.data.source.vo

class PollData(
    val channel: String,
    val id: Int,
    val tag: Int,
    val text: LotteryData,
    val time: String
)
data class LotteryData(
    val data: String,
    val module: String,
    val tokenId: String
)

data class Data(
    val bonus: String,
    val got_prize: Boolean,
    val id: String,
    val issue: String,
    val lotteryid: Int,
    val lotteryname: String
)
