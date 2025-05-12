package com.xtree.activity.data.source;


import io.reactivex.Flowable;
import com.xtree.base.http.BaseResponse;

/**
 * Created by goldze on 2019/3/26.
 */
public interface HttpDataSource {

    Flowable<BaseResponse<Object>> login(String username, String password);

    ApiService getApiService();
}
