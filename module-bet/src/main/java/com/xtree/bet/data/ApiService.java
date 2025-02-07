package com.xtree.bet.data;

import com.xtree.base.vo.BalanceVo;
import com.xtree.base.vo.FBService;
import com.xtree.base.vo.PMService;
import com.xtree.bet.bean.request.fb.FBListReq;
import com.xtree.bet.bean.request.pm.PMListReq;
import com.xtree.bet.bean.response.HotLeagueInfo;
import com.xtree.bet.bean.response.fb.FbMatchListCacheRsp;
import com.xtree.bet.bean.response.fb.FbStatisticalInfoCacheRsp;
import com.xtree.bet.bean.response.fb.MatchInfo;

import java.util.Map;

import io.reactivex.Flowable;
import me.xtree.mvvmhabit.http.BaseResponse;
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
     * 获取 FB token 参数 cachedToken=1
     * @return
     */
    @POST("/api/sports/fb/getToken?cachedToken=1")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<FBService>> getFBGameTokenApi();

    /**
     * 获取 FB token 参数 cachedToken=0
     * @return
     */
    @POST("/api/sports/fb/getToken?cachedToken=0")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<FBService>> getFBGameZeroTokenApi();

    /**
     * 获取 FBXC token 参数 cachedToken=1
     * @return
     */
    @POST("/api/sports/fbxc/getToken?cachedToken=1")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<FBService>> getFBXCGameTokenApi();

    /**
     * 获取 FBXC token 参数 cachedToken=0
     * @return
     */
    @POST("/api/sports/fbxc/getToken?cachedToken=0")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<FBService>> getFBXCGameZeroTokenApi();

    /**
     * 获取 PM体育请求服务地址
     * @return
     */
    @POST("/api/sports/obg/getToken?cachedToken=1")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<PMService>> getPMGameTokenApi();

    /**
     * 获取 PM杏彩体育2请求服务地址
     * @return
     */
    @POST("/api/sports/obgzy/getToken?cachedToken=1")
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
    @POST("/api/sports/fb/forward?api=/v1/match/getList&method=post")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<FbMatchListCacheRsp>> fbGetFBList(@Body FBListReq FBListReq);

    /**
     * 按运动、分类类型统计可投注的赛事个数
     * @return
     */
    @POST("/api/sports/fb/forward?api=/v1/match/statistical&method=post")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<FbStatisticalInfoCacheRsp>> fbStatistical(@Body Map<String, String> map);

    /**
     * 获取 FB体育请求服务地址
     * @return
     */
    @POST("/api/sports/fbxc/forward?api=/v1/match/getList&method=post")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<FbStatisticalInfoCacheRsp>> fbxcGetFBList(@Body FBListReq FBListReq);

    /**
     * 按运动、分类类型统计可投注的赛事个数
     * @return
     */
    @POST("/api/sports/fbxc/forward?api=/v1/match/statistical&method=post")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<FbStatisticalInfoCacheRsp>> fbxcStatistical(@Body Map<String, String> map);

    /**
     * 获取 PM赛事列表
     * @return
     */
    @POST("/api/sports/obg/forward?api=/yewu11/v1/m/matchesPagePB&method=post")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<com.xtree.bet.bean.response.pm.MatchLeagueListCacheRsp>> pmMatchesPagePB(@Body PMListReq pmListReq);

    /**
     * 获取 PM赛事列表 分页获取非滚球赛事信息
     * @return
     */
    @POST("/api/sports/obg/forward?api=/yewu11/v1/m/noLiveMatchesPagePB&method=post")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<com.xtree.bet.bean.response.pm.MatchLeagueListCacheRsp>> pmNoLiveMatchesPagePB(@Body PMListReq pmListReq);

    /**
     * 获取 PM赛事列表
     * @return
     */
    @POST("/api/sports/obg/forward?api=/yewu11/v1/m/liveMatchesPB&method=post")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<com.xtree.bet.bean.response.pm.MatchListCacheRsp>> pmLiveMatchesPB(@Body PMListReq pmListReq);

    /**
     * 获取 PM赛事列表
     * @return
     */
    @POST("/api/sports/obg/forward?api=/yewu11/v1/m/getMatchBaseInfoByMidsPB")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<com.xtree.bet.bean.response.pm.MatchListCacheRsp>> pmGetMatchBaseInfoByMidsPB(@Body PMListReq pmListReq);

    /**
     * 获取 PM赛事列表
     * @return
     */
    @POST("/api/sports/obgzy/forward?api=/yewu11/v1/m/matchesPagePB&method=post")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<com.xtree.bet.bean.response.pm.MatchLeagueListCacheRsp>> pmxcMatchesPagePB(@Body PMListReq pmListReq);

    /**
     * 获取 PM赛事列表 分页获取非滚球赛事信息
     * @return
     */
    @POST("/api/sports/obgzy/forward?api=/yewu11/v1/m/noLiveMatchesPagePB&method=post")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<com.xtree.bet.bean.response.pm.MatchLeagueListCacheRsp>> pmxcNoLiveMatchesPagePB(@Body PMListReq pmListReq);

    /**
     * 获取 PM赛事列表
     * @return
     */
    @POST("/api/sports/obgzy/forward?api=/yewu11/v1/m/liveMatchesPB&method=post")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<com.xtree.bet.bean.response.pm.MatchListCacheRsp>> pmxcLiveMatchesPB(@Body PMListReq pmListReq);

    /**
     * 获取 PM赛事列表
     * @return
     */
    @POST("/api/sports/obgzy/forward?api=/yewu11/v1/m/getMatchBaseInfoByMidsPB")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<com.xtree.bet.bean.response.pm.MatchListCacheRsp>> pmxcGetMatchBaseInfoByMidsPB(@Body PMListReq pmListReq);

}
