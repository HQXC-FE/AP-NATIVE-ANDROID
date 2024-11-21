package com.xtree.bet.data;

import com.xtree.base.vo.BalanceVo;
import com.xtree.base.vo.FBService;
import com.xtree.base.vo.PMService;
import com.xtree.bet.bean.request.fb.BtCarReq;
import com.xtree.bet.bean.request.fb.FBListReq;
import com.xtree.bet.bean.request.pm.PMListReq;
import com.xtree.bet.bean.response.HotLeagueInfo;
import com.xtree.bet.bean.response.fb.BtConfirmInfo;
import com.xtree.bet.bean.response.fb.MatchInfo;
import com.xtree.bet.bean.response.fb.MatchListRsp;
import com.xtree.bet.bean.response.fb.StatisticalInfo;
import com.xtree.bet.bean.response.pm.MenuInfo;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import me.xtree.mvvmhabit.http.BaseResponse;
import me.xtree.mvvmhabit.http.PMBaseResponse;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by goldze on 2017/6/15.
 */

public interface ApiService {
    /**
     * 热门联赛ID列表
     *
     * @return
     */
    @GET("/api/settings")
    Flowable<BaseResponse<HotLeagueInfo>> getSettings(@QueryMap(encoded = true) Map<String, String> filters);

    /**
     * 获取 FB体育请求服务地址
     * @return
     */
    @POST("/api/sports/fb/getToken?cachedToken=0")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<FBService>> getFBGameTokenApi();

    /**
     * 获取 FB杏彩体育请求服务地址
     * @return
     */
    @POST("/api/sports/fbxc/getToken?cachedToken=0")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<FBService>> getFBXCGameTokenApi();

    /**
     * 获取 PM体育请求服务地址
     * @return
     */
    @POST("/api/sports/obg/getToken?cachedToken=0")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<PMService>> getPMGameTokenApi();

    /**
     * 获取 PM杏彩体育2请求服务地址
     * @return
     */
    @POST("/api/sports/obgzy/getToken?cachedToken=0")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<PMService>> getPMXCGameTokenApi();

    /**
     * 异常日志上报
     * @return
     */
    @POST("/api/sports/excaption")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<String>> uploadExcetion(@Body Map<String, String> map);

    /**
     * 获取场馆代理开关
     */
    @POST("/api/sports/gsaswitch")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<Map<String, String>>> getGameSwitch();

    /**
     * 获取 平台中心余额
     */
    @GET("/api/account/balance")
    Flowable<BaseResponse<BalanceVo>> getBalance();

    /**
     * 获取PM/FB游戏的链接
     */
    @GET("/api/game/{gameAlias}/playurl")
    Flowable<BaseResponse<Map<String, Object>>> getPlayUrl(@Path("gameAlias") String gameAlias, @QueryMap Map<String, String> map);

//    /**
//     * 获取PM/FB场馆已缓存的接口信息
//     */
//    @GET("/api/game/sport-cache-api")
//    Flowable<BaseResponse<List<SportCacheApiInfoVo>>> getSportCacheApi();

    /**
     * 获取 FB体育请求服务地址
     * @return
     */
    @POST("/api/sports/fbxc/forward?api=/v1/match/getList&method=post")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<MatchListRsp>> getFBList(@Body FBListReq FBListReq);

    /**
     * 按运动、分类类型统计可投注的赛事个数
     * @return
     */
    @POST("/api/sports/fbxc/forward?api=/v1/match/statistical&method=post")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<StatisticalInfo>> statistical(@Body Map<String, String> map);

    /**
     * 按运动、分类类型统计可投注的赛事个数
     * @return
     */
    @POST("/api/sports/fbxc/forward?api=/v1/order/batchBetMatchMarketOfJumpLine&method=post")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<BtConfirmInfo>> batchBetMatchMarketOfJumpLine(@Body BtCarReq btCarReq);
    /**
     * 按运动、分类类型统计可投注的赛事个数
     * 按运动、分类类型获取单个赛事详情及玩法
     * @return
     */
    @POST("/api/sports/fbxc/forward?api=/v1/match/getMatchDetail&method=post")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<MatchInfo>> getMatchDetail(@Body Map<String, String> map);

    /**
     * 获取 PM赛事列表
     * @return
     */
    @POST("/api/sports/obgzy/forward?api=/yewu11/v1/m/matchesPagePB&method=post")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<PMBaseResponse<com.xtree.bet.bean.response.pm.MatchListRsp>> matchesPagePB(@Body PMListReq pmListReq);
    /**
     * 获取 PM赛事列表 分页获取非滚球赛事信息
     * @return
     */
    @POST("/api/sports/obgzy/forward?api=/yewu11/v1/m/noLiveMatchesPagePB&method=post")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<PMBaseResponse<com.xtree.bet.bean.response.pm.MatchListRsp>> noLiveMatchesPagePB(@Body PMListReq pmListReq);

    /**
     * 获取 PM赛事列表
     * @return
     */
    @POST("/api/sports/obgzy/forward?api=/yewu11/v1/m/liveMatchesPB&method=post")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<PMBaseResponse<List<MatchInfo>>> liveMatchesPB(@Body PMListReq pmListReq);

    /**
     * 获取 PM赛事列表
     * @return
     */
    @POST("/api/sports/obgzy/forward?api=/yewu11/v1/m/getMatchBaseInfoByMidsPB")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<PMBaseResponse<List<com.xtree.bet.bean.response.pm.MatchInfo>>> getMatchBaseInfoByMidsPB(@Body PMListReq pmListReq);

    /**
     * 按运动、分类类型统计可投注的赛事个数
     * @return
     */
    @GET("/api/sports/obgzy/forward?api=/yewu11/pub/v1/m/menu/initPB")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<PMBaseResponse<List<MenuInfo>>> initPB(@QueryMap Map<String, String> map);
}
