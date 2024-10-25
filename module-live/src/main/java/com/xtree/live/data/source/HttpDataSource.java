package com.xtree.live.data.source;


import com.xtree.live.data.source.request.AnchorSortRequest;
import com.xtree.live.data.source.request.AttentionRequest;
import com.xtree.live.data.source.request.FrontLivesRequest;
import com.xtree.live.data.source.request.LiveTokenRequest;
import com.xtree.live.data.source.response.AnchorSortResponse;
import com.xtree.live.data.source.response.FrontLivesResponse;
import com.xtree.live.data.source.response.LiveTokenResponse;
import com.xtree.live.data.source.response.ReviseHotResponse;
import com.xtree.live.ui.main.model.anchorList.AttentionListModel;

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

    Flowable<BaseResponse<FrontLivesResponse>> getFrontLives(FrontLivesRequest request);

    Flowable<BaseResponse<AttentionListModel>> getAttention(AttentionRequest request);

    Flowable<BaseResponse<AnchorSortResponse>> getAnchorSort(AnchorSortRequest request);
    //获取直播配置信息
    Flowable<BaseResponse<ReviseHotResponse>> getReviseHot();
}
