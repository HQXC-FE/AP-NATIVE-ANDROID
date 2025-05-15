package com.xtree.bet.bean.request.im

class EventInfoResulReq {
    private val api = "GetCompletedResults"
    private val method = "post"
    private val format = "json"
    var LanguageCode = "CHS"

    var SportId = 0
    var EventTypeId = 0
    var StartDate: String = ""
    var EndDate: String = ""
    //var CompetitionId = 0

    //后端统一处理
    var Token: String = ""
    var MemberCode: String = ""
}
