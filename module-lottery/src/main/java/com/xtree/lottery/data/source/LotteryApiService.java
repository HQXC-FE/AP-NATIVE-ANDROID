package com.xtree.lottery.data.source;

import com.xtree.lottery.data.source.vo.CancelOrderVo;
import com.xtree.lottery.data.source.vo.IssueVo;
import com.xtree.lottery.data.source.vo.LotteryChaseDetailVo;
import com.xtree.lottery.data.source.vo.LotteryOrderVo;
import com.xtree.lottery.data.source.vo.LotteryReportVo;
import com.xtree.lottery.data.source.vo.MethodMenus;
import com.xtree.lottery.data.source.vo.PollData;
import com.xtree.lottery.data.source.vo.RecentLotteryVo;
import com.xtree.lottery.data.source.vo.TraceInfoVo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import me.xtree.mvvmhabit.http.BaseResponse;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface LotteryApiService {
    /**
     * 近期开奖
     */
    @GET("/api/lottery/{id}/bonus-numbers?limit=100")
    Flowable<BaseResponse<List<RecentLotteryVo>>> getRecentLottery(@Path("id") int id);

    /**
     * 彩种数据
     */
    @GET("/api/lottery/{alias}/method-menus")
    Flowable<BaseResponse<MethodMenus>> getMethodMenus(@Path("alias") String alias);

    /**
     * 获取当前期号
     */
    @GET("/api/lottery/{id}/current-issue")
    Flowable<BaseResponse<IssueVo>> getCurrentIssue(@Path("id") int id);

    /**
     * 获取未来的300期
     */
    @GET("/api/lottery/{id}/tracking-issues?limit=300")
    Flowable<BaseResponse<ArrayList<IssueVo>>> getTrackingIssue(@Path("id") int id);

    /**
     * 投注记录-列表(彩票)
     * ?controller=gameinfo&action=newgamelist&starttime=2023-09-13 00:00:00&endtime=2024-02-13 23:59:59
     * &lotteryid=0&methodid=0&p=1&pn=20&ischild=0&client=m
     */
    @GET("/gameinfo")
    Flowable<LotteryReportVo> getCpReport(@QueryMap Map<String, String> map);

    /**
     * 投注记录-详情(彩票)
     * platform=FBXC,project_id=10950255273****7510,nonce=***
     */
    @GET("/gameinfo/newgamedetail/{id}?client=m")
    Flowable<LotteryOrderVo> getBtCpOrderDetail(@Path("id") String id);

    /**
     * 取消订单
     * platform=FBXC,project_id=10950255273****7510,nonce=***
     */
    @GET("/gameinfo/cancelgame/{id}?client=m")
    Flowable<CancelOrderVo> cancelOrder(@Path("id") String id);

    /**
     * 追号记录-列表(彩票)
     * ?controller=gameinfo&action=newgamelist&starttime=2023-09-13 00:00:00&endtime=2024-02-13 23:59:59
     * &lotteryid=0&methodid=0&p=1&pn=20&ischild=0&client=m
     */
    @GET("/report/traceinfo")
    Flowable<TraceInfoVo> getTraceinfo(@QueryMap Map<String, String> map);

    /**
     * 追号记录-详情(彩票)
     * platform=FBXC,project_id=10950255273****7510,nonce=***
     */
    @GET("/report/taskdetail/{id}?client=m")
    Flowable<LotteryChaseDetailVo> getBtChaseDetailDetail(@Path("id") String id);

    /**
     * 追号终止
     */
    @POST("/gameinfo/canceltask?client=m")
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Flowable<CancelOrderVo> cancelTask(@Body Map<String, Object> map);

    /**
     * 轮询接口
     */
    @GET("/lp/{id}")
    Flowable<BaseResponse<PollData>> getPoll(@Path("id") int id, @QueryMap Map<String, String> map);

    /**
     * GET
     *
     * @param url 接口名称
     * @return 返回体
     */
    @GET("{url}")
    Flowable<ResponseBody> get(@Path(value = "url", encoded = true) String url);

    /**
     * GET
     *
     * @param url 接口名称
     * @param map 拼接参数
     * @return 返回体
     */
    @GET("{url}")
    Flowable<ResponseBody> get(@Path(value = "url", encoded = true) String url, @QueryMap(encoded = true) Map<String, Object> map);

    /**
     * POST
     *
     * @param url 接口名称
     * @param map body
     * @return 返回体
     */
    @POST("{url}")
    Flowable<ResponseBody> post(@Path(value = "url", encoded = true) String url, @Body Map<String, Object> map);

    /**
     * POST
     *
     * @param url  接口名称
     * @param qmap 拼接参数
     * @param map  body
     * @return 返回体
     */
    @POST("{url}")
    Flowable<ResponseBody> post(@Path(value = "url", encoded = true) String url, @QueryMap(encoded = true) Map<String, Object> qmap, @Body Map<String, Object> map);

    /**
     * POST
     *
     * @param url 接口名称
     * @param map body
     * @return 返回体
     */
    @POST("{url}")
    Flowable<ResponseBody> post(@Path(value = "url", encoded = true) String url, @Body Map<String, Object> map, @Header("Content-Type") String contentType);


    /**
     * POST
     *
     * @param url  接口名称
     * @param qmap 拼接参数
     * @param map  body
     * @return 返回体
     */
    @POST("{url}")
    Flowable<ResponseBody> post(@Path(value = "url", encoded = true) String url, @QueryMap(encoded = true) Map<String, Object> qmap, @Body Map<String, Object> map, @Header("Content-Type") String contentType);
}
