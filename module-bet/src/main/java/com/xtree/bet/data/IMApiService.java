package com.xtree.bet.data;

import com.xtree.bet.bean.request.im.AnnouncementReq;
import com.xtree.bet.bean.request.im.BaseIMRequest;
import com.xtree.bet.bean.request.im.CometitionCountReq;
import com.xtree.bet.bean.request.im.DeltaOutrightEventsReq;
import com.xtree.bet.bean.request.im.DeltaEventInfoMbtReq;
import com.xtree.bet.bean.request.im.EventInfoByPageRsq;
import com.xtree.bet.bean.request.im.EventInfoMbtReq;
import com.xtree.bet.bean.request.im.EventInfoResulReq;
import com.xtree.bet.bean.request.im.GetBetInfoReq;
import com.xtree.bet.bean.request.im.GetBetListReq;
import com.xtree.bet.bean.request.im.GetGetDeltaBetTradeReq;
import com.xtree.bet.bean.request.im.GetStatementReq;
import com.xtree.bet.bean.request.im.OutrightEventsReq;
import com.xtree.bet.bean.request.im.AllSportCountReq;
import com.xtree.bet.bean.request.im.PlaceBetReq;
import com.xtree.bet.bean.request.im.SelectedEventInfoReq;
import com.xtree.bet.bean.request.im.StatementReq;
import com.xtree.bet.bean.request.pm.BtRecordReq;
import com.xtree.bet.bean.request.pm.BtReq;
import com.xtree.bet.bean.request.pm.PMListReq;
import com.xtree.bet.bean.response.im.BtConfirmInfo;
import com.xtree.bet.bean.response.im.BetTrade;
import com.xtree.bet.bean.response.im.ChampionEventsRsp;
import com.xtree.bet.bean.response.im.DeltaEventListRsp;
import com.xtree.bet.bean.response.im.EventInfoByPageListRsp;
import com.xtree.bet.bean.response.im.EventListRsp;
import com.xtree.bet.bean.response.im.GetAnnouncementRsp;
import com.xtree.bet.bean.response.im.ImCompletedResultsEntity;
import com.xtree.bet.bean.response.im.OutrightEventRsp;
import com.xtree.bet.bean.response.im.PlaceBet;
import com.xtree.bet.bean.response.im.RecommendedSelections;
import com.xtree.bet.bean.response.im.SportCompetitionCountRsp;
import com.xtree.bet.bean.response.im.SportCountRsp;
import com.xtree.bet.bean.response.im.StatementRsp;
import com.xtree.bet.bean.response.im.Wager;
import com.xtree.bet.bean.response.im.WagerEntity;
import com.xtree.bet.bean.response.pm.BtRecordRsp;
import com.xtree.bet.bean.response.pm.BtResultInfo;
import com.xtree.bet.bean.response.pm.FrontListInfo;
import com.xtree.bet.bean.response.pm.LeagueAreaInfo;
import com.xtree.bet.bean.response.pm.MatchInfo;
import com.xtree.bet.bean.response.pm.PMResultBean;
import com.xtree.bet.bean.response.pm.VideoAnimationInfo;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.Single;
import me.xtree.mvvmhabit.http.BaseResponse;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface IMApiService {

    /**
     * 通用转发请求接口
     */
    String forwardPath = "/api/sports/imsb/forward";
    /**
     * 索取赛事选项资料
     */
    String GetSelectedEventInfo = "GetSelectedEventInfo";
    /**
     * 索取投注信息
     */
    String GetBetInfo = "GetBetInfo";
    /**
     * 投注 [ 此 API 常用于展示各投注项目. 当会员在确认投注时将被调用.]
     */
    String PlaceBet = "PlaceBet";



    /**
     * 获取获取公告
     * 参数使用对比PM的frontListPB接口
     *
     * @return
     */
    @POST(forwardPath)
    @Headers({"content-type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<GetAnnouncementRsp>> getAnnouncement(@Body AnnouncementReq announcementReq);

    /**
     * 获取所有体育赛种数量
     * 参数使用对比PM的initPB接口
     *
     * @return
     */
    @POST(forwardPath)
    @Headers({"content-type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<SportCountRsp>> getAllSportCount(@Body AllSportCountReq sportCountReq);

    /**
     * 投注记录（未结算）
     */
    @POST(forwardPath)
    @Headers({"content-type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<WagerEntity>> getBetList(@Body GetBetListReq getBetListReq);

    /**
     * 投注记录（已结算）
     */
    @POST(forwardPath)
    @Headers({"content-type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<WagerEntity>> getStatement(@Body GetStatementReq getBetListReq);

    /**
     * 提前结算下注
     */
    @POST(forwardPath)
    @Headers({"content-type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<BetTrade>> getDeltaBetTrade(@Body GetGetDeltaBetTradeReq getBetListReq);

    /**
     * 索取所有竞赛计数
     *
     * @return
     */
    @POST("/api/sports/imsb/forward")
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Flowable<BaseResponse<SportCompetitionCountRsp>> getAllCompetitionCount(@Body CometitionCountReq cometitionCountReq);

    /**
     * 索取赛事和主要玩法资料,
     * 这里是第一次获取，是获取全量赛事
     * 获取赛事列表-->对应matchesPagePB接口
     */
    @POST(forwardPath)
    @Headers({"content-type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<EventListRsp>> getEventInfoMbt(@Body EventInfoMbtReq eventInfoMbtReq);

    /**
     * 索取赛事和主要玩法资料,
     * 这里是第一次获取，是获取全量赛事
     * 获取赛事列表--> 进行中
     */
    @POST(forwardPath)
    @Headers({"content-type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<EventInfoByPageListRsp>> getLiveEventInfo(@Body EventInfoByPageRsq eventInfoMbtReq);

    /**
     * 索取赛事和主要玩法资料,
     * 这里是第一次获取，是获取全量赛事
     * 获取赛事列表--> 未开赛
     */
    @POST(forwardPath)
    @Headers({"content-type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<EventInfoByPageListRsp>> getEventInfoByPage(@Body EventInfoByPageRsq eventInfoMbtReq);

    /**
     * 赛果功能
     */
    @POST(forwardPath)
    @Headers({"content-type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<ImCompletedResultsEntity>> GetCompletedResults(@Body EventInfoResulReq req);

    /**
     * 索取投注信息
     */
    @POST(forwardPath)
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Flowable<BaseResponse<BtConfirmInfo>> getBetInfo(@Body BaseIMRequest<GetBetInfoReq> req);


    /**
     * 投注 [ 此 API 常用于展示各投注项目. 当会员在确认投注时将被调用.]
     */
    @POST(forwardPath)
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Single<BaseResponse<PlaceBet>> placeBet(@Body BaseIMRequest<PlaceBetReq> map);

    /**
     * 索取投注账目
     */
    @POST("/yewu11/v1/m/getStatement")
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Single<BaseResponse<List<Wager>>> getStatement(@Body Map<String, String> map);

    /**
     * 索取DELTA赛事和主要玩法详情
     * 这里是刷新获取，获取的是增量赛事
     * @return
     */
    @GET("/api/sports/imsb/forward")
    @Headers({"content-type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<DeltaEventListRsp>> getDeltaEventInfoMbt(@Body DeltaEventInfoMbtReq evenDeltatInfoMbtReq);

    /**
     * 索取其他玩法资料
     *
     * @return
     */
    @POST("/yewu13/v1/getMlInfoObt")
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Flowable<BaseResponse<EventListRsp>> getMlInfoObt(@Body EventInfoMbtReq eventInfoMbtReq);

    /**
     * 索取DELTA其他玩法详情
     *
     * @return
     */
    @POST("/yewu13/v1/getDeltaMlInfoObt")
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Flowable<BaseResponse<DeltaEventListRsp>> getDeltaMlInfoObt(@Body DeltaEventInfoMbtReq evenDeltatInfoMbtReq);

    /**
     * 索取赛事选项资料
     *
     * @return
     */
    @POST(forwardPath)
    @Headers({"content-type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<EventInfoByPageListRsp>> getSelectedEventInfo(@Body BaseIMRequest<SelectedEventInfoReq> selectedEventInfoReq);

    /**
     * 获取冠军赛事
     *
     * @return
     */
    @POST(forwardPath)
    @Headers({"content-type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<ChampionEventsRsp>> getOutrightEvents(@Body OutrightEventsReq outrightEventsReq);

    /**
     * 获取优胜冠军赛事指定比赛,如足球或篮球等
     *
     * @return
     */
    @GET("/yewu11/v1/getDeltaOutrightEventInfo")
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Flowable<BaseResponse<OutrightEventRsp>> getDeltaOutrightEventInfo(@Body DeltaOutrightEventsReq deltaOutrightEventsReq);

    /**
     * 获取优胜冠军赛事指定比赛,如足球或篮球等
     *
     * @return
     */
    @GET("/yewu11/v1/getStatement")
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Flowable<BaseResponse<StatementRsp>> getStatement(@Body StatementReq statementReq);

    /**
     * 索取现场赛果
     *
     * @return
     */
    @POST("/yewu13/v1/getLiveResults")
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Flowable<BaseResponse<BtResultInfo>> getLiveResults(@Body BtReq btReq);

    /**
     * 索取定位
     *
     * @return
     */
    @POST("/yewu13/v1/getLocalizations")
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Flowable<BaseResponse<BtRecordRsp>> getLocalizations(@Body BtRecordReq btRecordReq);

    /**
     * 索取DELTA定位
     *
     * @return
     */
    @GET("/yewu11/v1/getDeltaLocalizations")
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Flowable<BaseResponse<List<LeagueAreaInfo>>> getDeltaLocalizations(@QueryMap Map<String, String> map);

    /**
     * 索取完整赛果
     *
     * @return
     */
    @POST("/yewu11/v1/getCompletedResults")
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Flowable<BaseResponse<VideoAnimationInfo>> getCompletedResults(@Body Map<String, String> map);

    /**
     * 退出
     *
     * @return
     */
    @POST("/yewu11/v1/m/logOut")
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Flowable<BaseResponse<List<MatchInfo>>> logOut(@Body PMListReq pmListReq);


    /**
     * 索取投注账目
     */
    @POST("/yewu11/v2/getStatement")
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Flowable<BaseResponse<FrontListInfo>> getStatement();

    /**
     * 索取余额
     */
    @GET("/yewu11/v2/m/getBalance")
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Flowable<BaseResponse<List<PMResultBean>>> getBalance(@QueryMap Map<String, String> map);

    /**
     * 索取通告
     */
    @POST("/yewu11/v1/m/getAnnouncement")
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Flowable<BaseResponse<List<MatchInfo>>> getAnnouncement(@Body Map<String, String> map);


    /**
     * 索取待处理投注状态
     */
    @POST("/yewu11/v1/m/getPendingWagerStatus")
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Flowable<BaseResponse<List<MatchInfo>>> getPendingWagerStatus(@Body Map<String, String> map);

    /**
     * 索取会员信息
     */
    @POST("/yewu11/v1/m/getMemberByToken")
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Flowable<BaseResponse<List<MatchInfo>>> getMemberByToken(@Body Map<String, String> map);

    /**
     * 索取用户自定义
     */
    @POST("/yewu11/v1/getUserPreferences")
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Flowable<BaseResponse<List<MatchInfo>>> getUserPreferences(@Body Map<String, String> map);

    /**
     * 更新用户自定义
     */
    @POST("/yewu11/v1/m/updateUserPreferences")
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Flowable<BaseResponse<List<MatchInfo>>> updateUserPreferences(@Body Map<String, String> map);

    /**
     * 索取所有收藏赛事
     */
    @POST("/yewu11/v1/m/getPendingWagerStatus")
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Flowable<BaseResponse<List<MatchInfo>>> getFavouriteEvent(@Body Map<String, String> map);


    /**
     * 加收藏赛事
     */
    @POST("/yewu11/v1/m/getPendingWagerStatus")
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Flowable<BaseResponse<List<MatchInfo>>> addFavouriteEvent(@Body Map<String, String> map);


    /**
     * 删除收藏赛事
     */
    @POST("/yewu11/v1/m/removeFavouriteEvent")
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Flowable<BaseResponse<List<MatchInfo>>> removeFavouriteEvent(@Body Map<String, String> map);


    /**
     * 推荐投注选项
     */
    @POST("/yewu11/v1/m/getRecommendedSelections")
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Single<BaseResponse<RecommendedSelections>> getRecommendedSelection(@Body Map<String, String> map);






}
