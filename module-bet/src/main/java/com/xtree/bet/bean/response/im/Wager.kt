package com.xtree.bet.bean.response.im

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.xtree.base.vo.BaseBean
import kotlinx.android.parcel.RawValue
import kotlinx.parcelize.Parcelize

data class WagerEntity(
    val ServerTime: String,
    val StatusCode: Int,
    val StatusDesc: String,
    val WagerList: List<Wager>
)
@Parcelize
data class Wager(
    /**
     * 赌注ID
     */
    @SerializedName("WagerId") val wagerId: String,
    /**
     * 赌注创建的日期和时间
     */
    @SerializedName("WagerCreationDateTime") val wagerCreationDateTime: String,
    /**
     * 用户名
     */
    @SerializedName("MemberCode") val memberCode: String,
    /**
     * 会员提交的投注金额
     */
    @SerializedName("InputtedStakeAmount") val inputtedStakeAmount: Double,
    /**
     * 输赢金额
     */
    @SerializedName("MemberWinLossAmount") val memberWinLossAmount: Double,
    /**
     * 赔率类型
     * @see com.skt.sport.sdk.im.ImOddsType
     */
    @SerializedName("OddsType") val oddsType: Int,
    /**
     * 赌注性质
     * @see com.skt.sport.sdk.im.ImWagerType
     */
    @SerializedName("WagerType") val wagerType: Int,
    /**
     * 投注的设备
     */
    @SerializedName("BettingPlatform") val bettingPlatform: String,
    /**
     * 确认投注状态清单
     * @see com.skt.sport.sdk.im.ImBetConfirmationStatus
     */
    @SerializedName("BetConfirmationStatus") val betConfirmationStatus: Int,
    /**
     * 结算状态
     * @see com.skt.sport.sdk.im.ImBetSettlementStatus
     */
    @SerializedName("BetSettlementStatus") val betSettlementStatus: Int,
    /**
     * 是否有重新结算
     * 0：Not Resettled 没重新结算
     * 1：Resettled 有重新结算
     */
    @SerializedName("BetResettled") val betResettled: Int,
    /**
     * 售出状态
     * 对于已售出的投注，无论是否赛事已经结算，[betSettlementStatus]始终为1
     * @see com.skt.sport.sdk.im.ImBetTradeStatus
     */
    @SerializedName("BetTradeStatus") val betTradeStatus: Int,
    /**
     * 提前兑现的价格ID
     */
    @SerializedName("PricingId") val pricingId: String,
    /**
     * 提前兑现提供的回购价格
     */
    @SerializedName("BuyBackPricing") val buyBackPricing: Double?,
    /**
     * 提前兑现已买赌注的金额. 只有已售给提前兑现的赌注产生数值.
     */
    @SerializedName("BetTradeBuyBackAmount") val betTradeBuyBackAmount: Double,
    /**
     * 排列数量/连串过关类型注单
     */
    @SerializedName("NoOfCombination") val noOfCombination: Int,
    /**
     * 连串过关类型. 单项投注即使不是混
     */
    @SerializedName("ComboSelection") val comboSelection: Int,
    /**
     * 赌注潜在的派彩金额
     */
    @SerializedName("PotentialPayout") val potentialPayout: Double,
    /**
     * 赌注可售给提前兑现
     */
    @SerializedName("CanSell") val canSell: Boolean,
    /**
     * 一个主要赌注至少包含一项赌注. 对于单项投注, 主要 将包含一项赌注. 对于混合过关, 主要赌注将包含多项赌注(因为它是于不同的赛事进行多项选择)
     */
    @SerializedName("WagerItemList") val wagerItemList: List<WagerItem>,
    /**
     * 结算日期
     * TODO：IM API文档未定义
     */
    @SerializedName("SettlementDateTime") val settlementDateTime: String?,
    /**
     * 交易成功日期
     * TODO：IM API文档未定义
     */
    @SerializedName("BetTradeSuccessDateTime") val betTradeSuccessDateTime: String?,
    /**
     * 结算后投注的结果。 (1=赢，2=输，3=平，4=赢一半，5=输一半, 6=输部 分) 对于取消、未结算或兑现的投注，价值将为空
     * */
    @SerializedName("Outcome") val outCome:Int  = -1//结算后投注的结果  (1=赢，2=输，3=平，4=赢一半，5=输一半, 6=输部分)


    ) : BaseBean, Parcelable {
        @Parcelize
        data class WagerItem(
            /**
             * 每项赌注的确认状态
             * @see com.skt.sport.sdk.im.ImBetConfirmationStatus
             */
            @SerializedName("WagerItemConfirmationStatus") val wagerItemConfirmationStatus: Int,
            /**
             * 指出每项投注的确认类型
             * @see com.skt.sport.sdk.im.ImWagerItemConfirmationType
             */
            @SerializedName("WagerItemConfirmationType") val wagerItemConfirmationType: Int,
            /**
             * 每项赌注是否被取消和取消类型
             * @see com.skt.sport.sdk.im.ImWagerItemCancelType
             */
            @SerializedName("WagerItemCancelType") val wagerItemCancelType: Int,
            /**
             * 赌注被取消原因
             * @see com.skt.sport.sdk.im.ImWagerItemCancelReason
             */
            @SerializedName("WagerItemCancelReason") val wagerItemCancelReason: Int,
            /**
             * @see com.skt.sport.sdk.im.ImMarket
             */
            @SerializedName("Market") val market: Int,
            /**
             * 赛事ID
             */
            @SerializedName("EventId") val eventId: Int,
            /**
             * 指出赛事属于定时赛事（普通）或优胜冠军赛事
             * @see com.skt.sport.sdk.im.ImEventTypeId
             */
            @SerializedName("EventTypeId") val eventTypeId: Int,
            /**
             * 赛事日期和时间
             */
            @SerializedName("EventDateTime") val eventDateTime: String,
            /**
             * 体育项目 ID
             */
            @SerializedName("SportId") val sportId: Int,
            /**
             * 竞赛 ID
             */
            @SerializedName("CompetitionId") val competitionId: Int,
            /**
             * 竞赛的名称
             */
            @SerializedName("CompetitionName") val competitionName: String,
            /**
             * 赛事的组别类型. (只适用于定时赛事)
             * @see com.skt.sport.sdk.im.ImEventGroupTypeId
             */
            @SerializedName("EventGroupTypeId") val eventGroupTypeId: Int,
            /**
             * 主队或参赛者的特定 ID (只适用于定时赛事. 如果优胜冠军会是 0)
             */
            @SerializedName("HomeTeamId") val homeTeamId: Int,
            /**
             * 主队名称. (只适用于定时赛事. 如果优胜冠军会是 0)
             */
            @SerializedName("HomeTeamName") val homeTeamName: String,
            /**
             * 客队或参赛者的特定 ID (只适用于定时赛事. 如果优胜冠军会是 0)
             */
            @SerializedName("AwayTeamId") val awayTeamId: Int,
            /**
             * 客队名称. (只适用于定时赛事. 如果优胜冠军会是 0)
             */
            @SerializedName("AwayTeamName") val awayTeamName: String,
            /**
             * 指出最爱队伍. (只适用于定时赛事. 如果优胜冠军会是 0)
             * @see com.skt.sport.sdk.im.ImFavTeam
             */
            @SerializedName("FavTeam") val favTeam: String,
            /**
             * 投注类型 ID
             */
            @SerializedName("BetTypeId") val betTypeId: Int,
            /**
             * 投注名称
             */
            @SerializedName("BetTypeName") val betTypeName: String,
            /**
             * 比赛时段
             * @see com.skt.sport.sdk.im.ImPeriodId
             */
            @SerializedName("PeriodId") val periodId: Int,
            /**
             * /投注选项类型 ID，只适用于定时赛事，如果优胜冠军会是0
             */
            @SerializedName("BetTypeSelectionId") val betTypeSelectionId: Int,
            /**
             * 选项名称
             */
            @SerializedName("SelectionName") val selectionName: String?,
            /**
             * 优胜冠军名称 (只适用于优胜冠军赛事，如果定时赛事会是 0)
             */
            @SerializedName("EventOutrightName") val eventOutrightName: String?,
            /**
             * 优胜冠军类 ID，只适用于优胜冠军，如果定时赛事会是0
             */
            @SerializedName("OutrightTeamId") val outrightTeamId: Int?,
            /**
             * 优胜冠军队伍名称
             */
            @SerializedName("OutrightTeamName") val outrightTeamName: String?,
            /**
             * 赌注选择的赔率值
             */
            @SerializedName("Odds") val odds: Double?,
            /**
             * 该项目仅适用于让球盘和大小盘。
             * 对于让球盘，只针对主队。正值等于主队在让分，负值相反。
             * 对于大小盘，就是大于或小于的得分比较。
             * 如果是空值等于该让分不适用于该盘口或投注类型
             * 让球 大小 先判断是不是0 0.0 0.00如果是  返回0  其他盘返回""
             * 让球    去除小数点后两位  去除 .00    去除末尾0 如果是正数  前面加上 + 如果是整数 加上.0 例如 1 返回 +1.0
             * 大小   去除小数点后两位  去除 .00    去除末尾0  如果是整数  加上.0  例如1  返回 1.0
             * 让球  大小  界面上显示的 如果是0  就显示0
             * 如果是 0.5的倍数 加上+ 或者 - 是什么就显示什么
             * 如果是 其他  就带符号显示  (abs(handicap)/0.5*0.5)/(abs(handicap)/0.5*0.5+0.5)  去除.0
             */
            @SerializedName("Handicap") val handicap: Double?,
            /**
             * 主队于赛事半场得分. 若不存在得分, 将还原为 0
             */
            @SerializedName("HomeTeamHTScore") val homeTeamHTScore: Int?,
            /**
             * 客队于赛事半场得分. 若不存在得分, 将还原为 0
             */
            @SerializedName("AwayTeamHTScore") val awayTeamHTScore: Int?,
            /**
             * 主队于赛事全场得分. 若不存在得分, 将还原为 0
             */
            @SerializedName("HomeTeamFTScore") val homeTeamFTScore: Int?,
            /**
             * 客队于赛事全场得分. 若不存在得分, 将还原为 0
             */
            @SerializedName("AwayTeamFTScore") val awayTeamFTScore: Int?,
            /**
             * 主队于投注成功后得分. 只适用于滚球定时赛事. 若不存在得分, 将还原为 0
             */
            @SerializedName("WagerHomeTeamScore") val wagerHomeTeamScore: Int?,
            /**
             * 客队于投注成功后得分. 只适用于滚球定时赛事. 若不存在得分, 将还原为 0
             */
            @SerializedName("WagerAwayTeamScore") val wagerAwayTeamScore: Int?,
            /**
             * 赛事是否举办于其中一队的主场或中立场 (只适用于定时赛事，如果优胜冠军会是 0)
             * @see com.skt.sport.sdk.im.ImGroundTypeId
             */
            @SerializedName("GroundTypeId") val groundTypeId: Int,
            /**
             * 赛季指标. 仅适用于虚拟足球, 虚拟篮球和虚拟世界杯各项体育
             */
            @SerializedName("Season") val season: Int,
            /**
             * 赛日指标. 仅适用于虚拟足球和虚拟篮球
             */
            @SerializedName("MatchDay") val matchDay: Int,
            /**
             * 特殊字段
             * 当selectionName中有{aaa}时  这里会有aaa=bbb  这时候要把selectionName 中的{aaa}替换成 bbb显示在界面上
             */
            @SerializedName("Specifiers") val specifiers: String?,
            /**
             * 赛事的外部参考ID
             */
            @SerializedName("SourceId") val sourceId: String?,
            /**
             * TODO: IM API文档未定义
             */
            @SerializedName("OverallScore") val overallScore: @RawValue Any?
        ) : Parcelable
    }
