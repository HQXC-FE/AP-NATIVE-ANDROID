package com.xtree.bet.data;

import com.xtree.bet.bean.request.pm.BtCarCgReq;
import com.xtree.bet.bean.request.pm.BtCarReq;
import com.xtree.bet.bean.request.pm.BtCashOutBetReq;
import com.xtree.bet.bean.request.pm.BtRecordReq;
import com.xtree.bet.bean.request.pm.BtReq;
import com.xtree.bet.bean.request.pm.PMListReq;
import com.xtree.bet.bean.response.im.BetInfo;
import com.xtree.bet.bean.response.im.DeltaEventListRsp;
import com.xtree.bet.bean.response.im.EventListRsp;
import com.xtree.bet.bean.response.im.GetAnnouncementRsp;
import com.xtree.bet.bean.response.im.OutrightEventRsp;
import com.xtree.bet.bean.response.im.PlaceBet;
import com.xtree.bet.bean.response.im.RecommendedSelections;
import com.xtree.bet.bean.response.im.SportCountRsp;
import com.xtree.bet.bean.response.im.WagerListRsp;
import com.xtree.bet.bean.response.pm.BalanceInfo;
import com.xtree.bet.bean.response.pm.BtCashOutPriceInfo;
import com.xtree.bet.bean.response.pm.BtCashOutStatusInfo;
import com.xtree.bet.bean.response.pm.BtRecordRsp;
import com.xtree.bet.bean.response.pm.BtResultInfo;
import com.xtree.bet.bean.response.pm.FrontListInfo;
import com.xtree.bet.bean.response.pm.LeagueAreaInfo;
import com.xtree.bet.bean.response.pm.MatchInfo;
import com.xtree.bet.bean.response.pm.MatchListRsp;
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
     * 获取获取公告
     * 参数使用对比PM的frontListPB接口
     *
     * @return
     */
    @POST("/yewu11/v1/m/getAnnouncement")
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Flowable<BaseResponse<GetAnnouncementRsp>> getAnnouncement();

    /**
     * 获取所有体育赛种数量
     * 参数使用对比PM的initPB接口
     *
     * @return
     */
    @POST("/yewu11/v1/m/getAllSportCount")
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Flowable<BaseResponse<SportCountRsp>> getAllSportCount(@QueryMap Map<String, String> map);

    /**
     * 索取所有竞赛计数
     *
     * @return
     */
    @POST("/yewu11/v1/m/getAllCompetitionCount")
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Flowable<BaseResponse<MatchListRsp>> getAllCompetitionCount(@Body PMListReq pmListReq);

    /**
     * 索取赛事和主要玩法资料
     *
     * @return
     */
    @POST("/yewu11/v1/m/getEventInfoMbt")
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Flowable<BaseResponse<EventListRsp>> getEventInfoMbt(@Body PMListReq pmListReq);

    /**
     * 索取DELTA赛事和主要玩法详情
     *
     * @return
     */
    @GET("/yewu11/pub/v1/m/getDeltaEventInfoMbt")
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Flowable<BaseResponse<DeltaEventListRsp>> getDeltaEventInfoMbt(@QueryMap Map<String, String> map);

    /**
     * 索取DELTA其他玩法详情
     *
     * @return
     */
    @POST("/yewu13/v1/getDeltaMlInfoObt")
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Flowable<BaseResponse<DeltaEventListRsp>> getDeltaMlInfoObt(@Body BtCarCgReq btCarCgReq);

    /**
     * 索取其他玩法资料
     *
     * @return
     */
    @POST("/yewu13/v1/getMlInfoObt")
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Flowable<BaseResponse<EventListRsp>> getMlInfoObt(@Body BtCarReq btCarReq);

    /**
     * 索取赛事选项资料
     *
     * @return
     */
    @GET("/yewu11/v1/w/getSelectedEventInfo")
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Flowable<BaseResponse<EventListRsp>> getSelectedEventInfo(@QueryMap Map<String, String> map);


    /**
     * 获取冠军赛事
     *
     * @return
     */
    @GET("/yewu11/v1/getOutrightEvents")
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Flowable<BaseResponse<WagerListRsp>> getOutrightEvents(@QueryMap Map<String, String> map);

    /**
     * 获取优胜冠军赛事指定比赛,如足球或篮球等
     *
     * @return
     */
    @GET("/yewu11/v1/getDeltaOutrightEventInfo")
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Flowable<BaseResponse<OutrightEventRsp>> getDeltaOutrightEventInfo(@QueryMap Map<String, String> map);

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
     * 索取投注明细
     *
     * @param btCashOutBetReq
     * @return
     */
    @POST("/yewu13/v1/getBetList")
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Flowable<BaseResponse<WagerListRsp>> getBetList(@Body BtCashOutBetReq btCashOutBetReq);

    /**
     * 以页数索取投注明细
     *
     * @return
     */
    @GET("/yewu13/v1/getBetListByPage")
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Flowable<BaseResponse<List<BtCashOutStatusInfo>>> getBetListByPage();

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


    /**
     * 索取投注信息
     */
    @POST("/yewu11/v1/m/getBetInfo")
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Single<BaseResponse<BetInfo>> getBetInfo(@Body Map<String, String> map);


    /**
     * 投注 [ 此 API 常用于展示各投注项目. 当会员在确认投注时将被调用.]
     */
    @POST("/yewu11/v1/m/placeBet")
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Single<BaseResponse<PlaceBet>> placeBet(@Body Map<String, String> map);





}
