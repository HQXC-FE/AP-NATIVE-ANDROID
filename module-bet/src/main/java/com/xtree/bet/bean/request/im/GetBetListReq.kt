package com.xtree.bet.bean.request.im

class GetBetListReq {
    var api = "getBetList"
    var method = "post"
    var format = "json"
    var LanguageCode = "CHS"//使用会员语言

    /**
     * 确认投注状态清单.
     * 1 = Pending 待处理
     * 2 = Confirmed 已确认
     * 3 = Rejected (refers to Danger Cancel) 已拒绝 (危
     * 险球取消)
     * 4 = Cancelled 已取消
     */
    lateinit var BetConfirmationStatus: List<Int>

    //var StartDate: String? = null,
    //var EndDate: String? = null,
    //var SourceWallet: Int? = 0,

    var MemberCode: String = ""

    //后端统一处理
    var Token: String = ""
    //lateinit var TimeStamp: String
}
