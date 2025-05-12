package com.xtree.mine.data.source.http;



import com.xtree.mine.data.source.HttpDataSource;
import com.xtree.mine.data.source.http.service.HttpApiService;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import com.xtree.base.http.BaseResponse;

/**
 * Created by goldze on 2019/3/26.
 */
public class HttpDataSourceImpl implements HttpDataSource {
    private HttpApiService apiService;
    private volatile static HttpDataSourceImpl INSTANCE = null;

    public static HttpDataSourceImpl getInstance(HttpApiService apiService) {
        if (INSTANCE == null) {
            synchronized (HttpDataSourceImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HttpDataSourceImpl(apiService);
                }
            }
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    private HttpDataSourceImpl(HttpApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public @NonNull Observable<Object> login() {
        return Observable.just(new Object()).delay(3, TimeUnit.SECONDS); //延迟3秒
    }

    @Override
    public Observable<BaseResponse<Object>> demoGet() {
        return apiService.demoGet();
    }

    @Override
    public Observable<BaseResponse<Object>> demoPost(String catalog) {
        return apiService.demoPost(catalog);
    }

    @Override
    public HttpApiService getApiService() {
        return apiService;
    }


}
