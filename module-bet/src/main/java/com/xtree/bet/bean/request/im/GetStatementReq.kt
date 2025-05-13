package com.xtree.bet.bean.request.im

class GetStatementReq {
    var api = "getStatement"
    var method = "post"
    var format = "json"
    var LanguageCode = "CHS"//使用会员语言
    var StartDate: String = ""
    var EndDate: String = ""

    //后端统一处理
    var Token: String = ""
    var MemberCode: String = ""
    //lateinit var TimeStamp: String
}
