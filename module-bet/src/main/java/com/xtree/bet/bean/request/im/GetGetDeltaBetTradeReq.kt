package com.xtree.bet.bean.request.im

class GetGetDeltaBetTradeReq {
    var api = "GetDeltaBetTrade"
    var method = "post"
    var format = "json"

    var WagerIds= arrayListOf<String>()

    //后端统一处理
    var Token: String = ""
    var MemberCode: String = ""
    //lateinit var TimeStamp: String
}
