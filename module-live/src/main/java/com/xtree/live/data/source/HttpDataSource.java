package com.xtree.live.data.source;


import com.xtree.base.vo.FBService;
import com.xtree.live.data.source.request.AnchorSortRequest;
import com.xtree.live.data.source.request.AttentionRequest;
import com.xtree.live.data.source.request.FrontLivesRequest;
import com.xtree.live.data.source.request.LiveTokenRequest;
import com.xtree.live.data.source.request.MatchDetailRequest;
import com.xtree.live.data.source.response.AnchorSortResponse;
import com.xtree.live.data.source.response.FrontLivesResponse;
import com.xtree.live.data.source.response.LiveTokenResponse;
import com.xtree.live.data.source.response.ReviseHotResponse;
import com.xtree.live.data.source.response.fb.MatchInfo;
import com.xtree.live.ui.main.model.anchorList.AttentionListModel;

import java.util.List;

import io.reactivex.Flowable;
import me.xtree.mvvmhabit.http.BaseResponse;

/**
 * Created by goldze on 2019/3/26.
 */
public interface HttpDataSource {

    ApiService getApiService();

    ApiService getLiveService();

    void setLive(LiveTokenResponse liveData);

    //获取直播令牌
    Flowable<BaseResponse<LiveTokenResponse>> getLiveToken(LiveTokenRequest request);

    /**
     * 获取首页直播列表type 1：获取全部，0：足球，1：篮球，2：其他，3：电竞，4：区块链。默认-1
     * page 1
     * limit 6
     *
     * @param request
     * @return
     */
    Flowable<BaseResponse<List<FrontLivesResponse>>> getFrontLives(FrontLivesRequest request);

    Flowable<BaseResponse<AttentionListModel>> getAttention(AttentionRequest request);

    Flowable<BaseResponse<AnchorSortResponse>> getAnchorSort(AnchorSortRequest request);
    //获取直播配置信息
    Flowable<BaseResponse<ReviseHotResponse>> getReviseHot();

    /**
     * 获取 FB体育请求服务地址
     *
     * @return
     */
    Flowable<BaseResponse<FBService>> getFBGameTokenApi();

    /**
     * 获取 FB杏彩体育请求服务地址
     *
     * @return
     */
    Flowable<BaseResponse<FBService>> getFBXCGameTokenApi();

    /**
     * 按运动、分类类型统计可投注的赛事个数
     * 按运动、分类类型获取单个赛事详情及玩法
     *
     * @return
     */
    Flowable<BaseResponse<MatchInfo>> getMatchDetail(MatchDetailRequest request);

}
