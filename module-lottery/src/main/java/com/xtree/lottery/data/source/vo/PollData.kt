package com.xtree.lottery.data.source.vo

data class PollData(
    val channel: String,
    val id: Int,
    val tag: Int,
    val text: String,
    val time: String
)
data class LotteryData(
    val `data`: List<Data>,
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
