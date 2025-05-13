package com.xtree.bet.bean.response.im

data class BetTrade(
    var Value: List<String>,//提前兑现版本改变清单
    var WagerId: String,//下注成功的注单号.
    var BetTradeStatus: Int,//0 = 未售出 1 = 已售出 2 = 进行中 3 = 已取消
    var BetTradeBuyBackAmount: String?,//提前兑现已买投注的金额. 只有已售给提前兑现的投注产生数值.
    var BuyBackPricing: String?,//提前兑现提供的回购价格.
    var PricingId: String?,//提前兑现提供的价格ID.
    var CanSell: Boolean//指出投注是否可售给提前兑现.
)