package com.xtree.mine.vo.request

data class AdduserRequest(
    val nonce: String,
    val flag: String,
    val usertype: String,
    val username: String,
    val userpass: String,
    val nikename: String,

    val point: String,//point     # 彩票返点
    val esportspoint: String, //电竞返点
    val fishingpoint: String,//新增捕鱼分红

    val livepoint: String,//# 真人返点
    val pokerpoint: String,//# 棋牌返点
    val sportpoint: String,//# 体育返点

  /*  point     # 彩票返点
    esportspoint   # 电竞返点
fishingpoint   # 捕鱼返点
livepoint      # 真人返点
pokerpoint   # 棋牌返点
sportpoint    # 体育返点*/
)