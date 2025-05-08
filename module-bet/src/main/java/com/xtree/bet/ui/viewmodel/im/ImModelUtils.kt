package com.xtree.bet.ui.viewmodel.im

import com.xtree.bet.bean.response.im.MarketLine


class ImModelUtils {

    // 定义各种投注类型分类
    //让球&大小
    private val ahou = setOf(1, 2, 35, 47, 160, 161)
    //波胆
    private val cs = setOf(6, 62, 158, 162)
    //角球
    private val corner = setOf(2, 3, 5, 6, 7, 8)
    //获牌总数
    private val bookings = setOf(0)
    //进球
    private val goals = setOf(7, 60, 61, 18, 16, 17, 21, 13, 14, 15, 22, 23)
    //半场
    private val halves = setOf(2, 3)
    //时段
    private val period = setOf(47, 48, 49, 50, 51, 52, 53, 54)
    //特殊投注
    private val specials = setOf(
        18, 21, 13, 8, 22, 23, 44, 45, 78, 79, 80, 36, 26, 16, 17, 14, 15,
        46, 63, 64, 65, 55, 72, 73, 74, 75, 76, 77, 40, 41, 39, 28, 29, 30,
        33, 34, 47, 58, 59, 35, 19, 20, 26, 27, 24, 25, 159
    )

    fun organizedMarkLinesWith(
        imSport: IMSports,
        imSportEvents: IMSportEvents,
        organizedArray: MutableMap<String, MutableList<MarketLine>>
    ) {
        for (marketLine in imSportEvents.MarketLines) {

            // 让球&大小
            if (marketLine.BetTypeId in ahou) {
                organizedArray.getOrPut("h") { mutableListOf() }.add(marketLine)
            }

            // 波胆
            if (marketLine.BetTypeId in cs) {
                organizedArray.getOrPut("cs") { mutableListOf() }.add(marketLine)
            }

            // 角球
            if ((marketLine.BetTypeId in corner && imSportEvents.EventGroupTypeId == 2)
                || marketLine.BetTypeName.lowercase().contains("corner")
                || marketLine.BetTypeName.contains("角球")
            ) {
                organizedArray.getOrPut("c") { mutableListOf() }.add(marketLine)
            }

            // 获牌总数
            if (imSportEvents.EventGroupTypeId == 3 &&
                (marketLine.BetTypeName.lowercase().contains("booking") || marketLine.BetTypeName.contains("获牌"))
            ) {
                organizedArray.getOrPut("b") { mutableListOf() }.add(marketLine)
            }

            // 进球
            if (marketLine.BetTypeId in goals) {
                organizedArray.getOrPut("s") { mutableListOf() }.add(marketLine)
            }

            // 半场
            if (marketLine.PeriodId in halves) {
                organizedArray.getOrPut("f") { mutableListOf() }.add(marketLine)
            }

            // 时段
            if (marketLine.BetTypeId in period) {
                organizedArray.getOrPut("period") { mutableListOf() }.add(marketLine)
            }

            // 特殊投注
            if (marketLine.BetTypeId in specials) {
                organizedArray.getOrPut("i") { mutableListOf() }.add(marketLine)
            }
        }
    }

}
