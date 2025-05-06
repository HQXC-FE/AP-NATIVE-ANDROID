package com.xtree.bet.bean.request.im

import com.google.gson.annotations.SerializedName

/**
 * 体育详情请求实体类
 * @SerializedName + Null => 标识非必传参数
 */
data class SelectedEventInfoReq(
    /**
     * 体育项目
     * 所有内部虚拟体育比赛的比赛名称均以“VS – IM”开头。 内部虚拟运动的队名将以“VS-”开头
     */
    @SerializedName("SportId") val sportId: Int,
    /**
     * 检索赛事ID，最多5个赛事
     */
    @SerializedName("EventIds") val eventIds: List<Long>,
    /**
     * 返回的投注类型 (非必传)
     */
    @SerializedName("BetTypeIds") val betTypeIds: List<Int>? = null,
    /**
     * 筛选时段的ID清单 (非必传)
     * 1 = FT全场
     * 2 = 1H上半场
     * 3 = 2H下半场
     */
    @SerializedName("PeriodIds") val periodIds: List<Int>? = null,
    /**
     * 赔率类型
     */
    @SerializedName("OddsType") val oddsType: Int,
    /**
     * 串关或者非串关
     */
    @SerializedName("IsCombo") val isCombo: Boolean,

    /**
     * 指出属于相同赛事组别ID的其他赛事是否同
     * 时被返回.
     */
    @SerializedName("IncludeGroupEvents") val includeGroupEvents: Boolean,
    /**
     * 会员令牌 - 非必传
     */
    @SerializedName("Token") val token: String? = null,
    /**
     * 用户名 - 非必传
     */
    @SerializedName("MemberCode") val memberCode: String? = null,
)
