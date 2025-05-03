package com.xtree.bet.bean.response.pm

data class BtErrorBean(
    /**
     * 投注时间(时间戳)
     */
    var betTime: String,

    /**
     * 订单号
     */
    var orderNo: String
)