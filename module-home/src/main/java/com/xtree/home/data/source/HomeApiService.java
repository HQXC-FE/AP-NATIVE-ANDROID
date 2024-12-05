package com.xtree.home.data.source;

import com.xtree.base.vo.AppUpdateVo;
import com.xtree.base.vo.FBService;
import com.xtree.base.vo.MsgPersonListVo;
import com.xtree.base.vo.PMService;
import com.xtree.base.vo.ProfileVo;
import com.xtree.home.vo.AugVo;
import com.xtree.home.vo.BannersVo;
import com.xtree.home.vo.CookieVo;
import com.xtree.home.vo.DataVo;
import com.xtree.home.vo.EleVo;
import com.xtree.home.vo.GameStatusVo;
import com.xtree.home.vo.LoginResultVo;
import com.xtree.home.vo.NoticeVo;
import com.xtree.home.vo.PaymentDataVo;
import com.xtree.home.vo.PublicDialogVo;
import com.xtree.home.vo.RechargeReportVo;
import com.xtree.home.vo.RedPocketVo;
import com.xtree.home.vo.RewardRedVo;
import com.xtree.home.vo.SettingsVo;
import com.xtree.home.vo.VipInfoVo;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import me.xtree.mvvmhabit.http.BaseResponse;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface HomeApiService {

    /**
     * 获取 banner列表
     * d @return
     */
    @GET("/api/bns/4/banners?limit=20")
    Flowable<BaseResponse<List<BannersVo>>> getBanners();

    /**
     * 获取首页欧洲杯跳转链接
     */
    @GET("/api/bns/11/banners?limit=20")
    Flowable<BaseResponse<List<BannersVo>>> getECLink();

    /**
     * 获取首页公共弹窗
     */
    @GET("/api/bns/13/banners?limit=20")
    Flowable<BaseResponse<List<PublicDialogVo>>> getPublicLink();

    /**
     * 获取 公告列表
     */
    @GET("/api/notice/list?page=1&per_page=10&sort=-istop,-sendtime")
    Flowable<BaseResponse<DataVo<NoticeVo>>> getNotices();

    /**
     * 获取 游戏状态列表（主要是游戏的状态,别名）
     */
    @GET("/api/game/status")
    Flowable<BaseResponse<List<GameStatusVo>>> getGameStatus();

    /**
     * 获取 游戏的链接
     */
    @GET("/api/game/{gameAlias}/playurl")
    Flowable<BaseResponse<Map<String, Object>>> getPlayUrl(@Path("gameAlias") String gameAlias, @QueryMap Map<String, String> map);

    /**
     * 获取 配置信息（主要是公钥,客服链接）
     */
    @GET("/api/settings")
    Flowable<BaseResponse<SettingsVo>> getSettings(@QueryMap(encoded = true) Map<String, String> filters);

    /**
     * 获取 cookie,session
     */
    @GET("/api/auth/sessid?client_id=10000005")
    Flowable<BaseResponse<CookieVo>> getCookie();

    /**
     * 获取 个人信息
     */
    @GET("/api/account/profile")
    Flowable<BaseResponse<ProfileVo>> getProfile();

    /**
     * 获取 个人消息未读数
     */
    @GET("/api/message/list?page=1&per_page=100&is_unread=1")
    Flowable<BaseResponse<MsgPersonListVo>> getMessagePersonList();

    /**
     * 获取 VIP信息
     */
    @GET("/api/account/vipinfo")
    Flowable<BaseResponse<VipInfoVo>> getVipInfo();

    /**
     * 获取 FB体育请求服务地址
     */
    @POST("/api/sports/fb/getToken?cachedToken=1")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<FBService>> getFBGameTokenApi();

    /**
     * 获取 FB杏彩体育请求服务地址
     */
    @POST("/api/sports/fbxc/getToken?cachedToken=1")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<FBService>> getFBXCGameTokenApi();

    /**
     * 获取 PM体育请求服务地址
     */
    @POST("/api/sports/obg/getToken?cachedToken=1")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<PMService>> getPMGameTokenApi();

    /**
     * 获取 PM杏彩体育2请求服务地址
     */
    @POST("/api/sports/obgzy/getToken?cachedToken=1")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<PMService>> getPMXCGameTokenApi();

    /**
     * 获取 AUG LIST
     */
    @GET("/augame/list")
    Flowable<BaseResponse<List<AugVo>>> getAugList();

    /**
     * 获取 电子 LIST
     */
    @GET("/api/game/h5list")
    Flowable<BaseResponse<EleVo>> getEle(@QueryMap Map<String, String> filters);

    /**
     * 充值记录
     * ?starttime=2024-01-10 00:00:00&endtime=2024-01-11 23:59:59&p=1&pn=10&client=m
     */
    @GET("/report/emailreport?")
    Flowable<RechargeReportVo> getRechargeReport(@QueryMap Map<String, String> map);

    /**
     * 获取更新
     */
    @GET("/api/app/version?platform=android")
    Flowable<BaseResponse<AppUpdateVo>> getUpdate();

    /**
     * 获取是否有红包
     */
    @GET("/api/activity/vip/redenvelope")
    Flowable<BaseResponse<RedPocketVo>> getRedPocket();

    /**
     * 获取优惠中心是否有优惠
     */
    @GET("/api/activity/reward/?has_pending_reward=1")
    Flowable<BaseResponse<RewardRedVo>> getReward();

    /**
     * 获取 充值列表(分大小类)
     */
    @GET("/api/deposit/paymentsclassify?getinfo=true")
    Flowable<BaseResponse<PaymentDataVo>> getPaymentsTypeList();

    /**
     * 获取 充值类型详情 (跳转链接用的)
     */
    @GET("/api/deposit/payments?")
    Flowable<BaseResponse<PaymentDataVo.RechargeVo>> getPayment(@Query("bid") String bid);

    /**
     * 异常日志上报
     * @return
     */
    @POST("/api/sports/excaption")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<String>> uploadExcetion(@Body Map<String, String> map);

}
