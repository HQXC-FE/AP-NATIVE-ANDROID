package com.xtree.bet.data.source.http;


import com.xtree.bet.data.ApiService;
import com.xtree.bet.data.FBApiService;
import com.xtree.bet.data.IMApiService;
import com.xtree.bet.data.PMApiService;
import com.xtree.bet.data.source.HttpDataSource;

/**
 * Created by goldze on 2019/3/26.
 */
public class IMHttpDataSourceImpl implements HttpDataSource {
    private ApiService baseApiService;
    private IMApiService imApiService;
    private volatile static IMHttpDataSourceImpl INSTANCE = null;

    public static IMHttpDataSourceImpl getInstance(IMApiService apiService, ApiService baseApiService, boolean reNew) {
        if(reNew){
            synchronized (IMHttpDataSourceImpl.class) {
                INSTANCE = new IMHttpDataSourceImpl(apiService, baseApiService);
            }
        }else {
            if (INSTANCE == null) {
                synchronized (IMHttpDataSourceImpl.class) {
                    if (INSTANCE == null) {
                        INSTANCE = new IMHttpDataSourceImpl(apiService, baseApiService);
                    }
                }
            }
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    private IMHttpDataSourceImpl(IMApiService apiService, ApiService baseApiService) {
        this.imApiService = apiService;
        this.baseApiService = baseApiService;
    }

    @Override
    public FBApiService getApiService() {
        return null;
    }

    @Override
    public PMApiService getPMApiService() {
        return null;
    }

    @Override
    public IMApiService getIMApiService() {
        return imApiService;
    }

    @Override
    public ApiService getBaseApiService() {
        return baseApiService;
    }
}
