package com.xtree.mine.vo.request

data class AdduserRequest(
    val nonce: String,
    val flag: String,
    val usertype: String,
    val username: String,
    val userpass: String,
    val nikename: String,

    val point: String,//point     # 彩票返点
    val livepoint: String,//# 真人返点
    val sportpoint: String,//# 体育返点
    val pokerpoint: String,//# 棋牌返点
    val esportspoint: String, //电竞返点
    val fishingpoint: String,//捕鱼返点

)