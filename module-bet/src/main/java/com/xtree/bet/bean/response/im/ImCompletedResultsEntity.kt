package com.xtree.bet.bean.response.im

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * 完整赛果，API(api/mobile/GetCompletedResults)的返回对象
 */
@Parcelize
data class ImCompletedResultsEntity(
    @SerializedName("ServerTime") val serverTime: String,
    /**
     * @see com.skt.sport.sdk.im.ImConst.statusCodes
     */
    @SerializedName("StatusCode") val statusCode: Int,
    @SerializedName("StatusDesc") val statusDesc: String,
    /**
     * 竞赛清单
     */
    @SerializedName("Competitions") val competitions: List<Competition2>
) : Parcelable

/**
 * 竞赛清单
 */
@Parcelize
data class Competition2(
    /**
     * 每项竞赛的特定 ID
     */
    @SerializedName("CompetitionId") val competitionId: Int,
    /**
     * 体育项目ID
     * @see com.skt.sport.sdk.im.ImSportsId
     */
    @SerializedName("SportId") val sportId: Int,
    /**
     * 竞赛的名称.
     */
    @SerializedName("CompetitionName") val competitionName: String,
    /**
     * IMSB 中使用的序号用于排序竞赛
     */
    @SerializedName("CompetitionSeq") val competitionSeq: Int,
    /**
     * 赛事清单
     */
    @SerializedName("Events") val events: List<Event>
) : Parcelable {
    @Parcelize
    data class Event(
        /**
         * 赛事ID
         */
        @SerializedName("EventId") val eventId: Long,
        /**
         * 可视化投注预测赛事ID.
         */
        @SerializedName("BREventId") val brEventId: Long?,
        /**
         * 赛事的外部参考ID.
         */
        @SerializedName("SourceId") val sourceId: String?,
        /**
         * 赛季指标. 仅适用于虚拟足球, 虚拟篮球和虚拟世界杯 各项体育.
         */
        @SerializedName("SeasonId") val season: String?,
        /**
         * 赛日指标. 仅适用于虚拟足球和虚拟篮球
         */
        @SerializedName("MatchDay") val matchDay: Int?,
        /**
         * 指出赛事属于定时赛事(普通)或优胜冠军赛事
         * @see com.skt.sport.sdk.im.ImEventTypeId
         */
        @SerializedName("EventTypeId") val eventTypeId: Int,
        /**
         * 优胜冠军的名称.
         * 这将会被清空如果赛事不是优胜冠军赛事.
         */
        @SerializedName("EventOutrightName") val eventOutrightName: String?,
        /**
         * IMSB 中使用的序号用于排序竞赛中赛事的先后显示.
         */
        @SerializedName("EventSeq") val eventSeq: Int,
        /**
         * 如果赛事是属于赛事组别，将显示赛事组别 ID
         */
        @SerializedName("EventGroupId") val eventGroupID: Long,
        /**
         * 指出赛事的组别类型.
         * @see com.skt.sport.sdk.im.ImEventGroupTypeId
         */
        @SerializedName("EventGroupTypeId") val eventGroupTypeId: Int,
        /**
         * 主队或参赛者的特定 ID.
         */
        @SerializedName("HomeTeamId") val homeTeamId: Int,
        /**
         * 主队名称.
         */
        @SerializedName("HomeTeam") val homeTeamName: String,
        /**
         * 客队或参赛者的特定 ID.
         */
        @SerializedName("AwayTeamId") val awayTeamId: Int,
        /**
         * 客队名称.
         */
        @SerializedName("AwayTeam") val awayTeamName: String,
        /**
         * 指出赛事是否在主队场地或中立场地进行(只适用于 定时赛事，如果优胜冠军会是0)
         * 0 = 中立场地
         * 1 = 主队场地
         */
        @SerializedName("GroundTypeId") val groundTypeId: Int,
        /**
         * 赛事日期
         * 示例：2020-06-12T00:00:00-04:00
         */
        @SerializedName("EventDate") val eventDate: String,
        /**
         * 赛事详情清单
         */
        @SerializedName("ResultList") val resultList: List<Result>,
        /**
         * 赛事详情关联比分清单（目前板球用）
         */
        @SerializedName("RelatedScores") val relatedScores: List<RelatedScore>
    ) : Parcelable {


        @Parcelize
        data class Result(
            /**
             * 比赛时段 ID
             * @see com.skt.sport.sdk.im.ImPeriodId
             */
            @SerializedName("PeriodId") val periodId: Int,
            /**
             * 比赛时段名称
             */
            @SerializedName("PeriodName") val periodName: String,
            /**
             * 主队得分
             */
            @SerializedName("HomeScore") val homeScore: Int?,
            /**
             * 客队得分
             */
            @SerializedName("AwayScore") val awayScore: Int?,
            /**
             * 优胜冠军队伍 ID. 这会被清空如果不是优胜冠军
             */
            @SerializedName("OutrightWinnerTeamID") val outrightWinnerTeamID: Long?,
            /**
             * 队名. 这会被清空如果不是优胜冠军.
             */
            @SerializedName("OutrightWinnerTeamName") val outrightWinnerTeamName: String?,
            /**
             * 指某时间段的结果被取消
             */
            @SerializedName("IsCancelled") val isCancelled: Boolean,
            /**
             * 取消原因
             * 0 = No Reason（无原因）
             * 1 = Abandoned（賽事終止）
             */
            @SerializedName("CancelReason") val cancelReason: Int
        ) : Parcelable
    }
}
