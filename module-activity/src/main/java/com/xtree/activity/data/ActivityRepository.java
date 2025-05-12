package com.xtree.activity.data;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

import com.xtree.activity.data.source.ApiService;
import com.xtree.activity.data.source.HttpDataSource;
import com.xtree.activity.data.source.LocalDataSource;

import io.reactivex.Flowable;
import com.xtree.base.base.BaseModel;
import com.xtree.base.http.BaseResponse;

/**
 * MVVM的Model层，统一模块的数据仓库，包含网络数据和本地数据（一个应用可以有多个Repositor）
 */
public class ActivityRepository extends BaseModel implements HttpDataSource, LocalDataSource {
    private volatile static ActivityRepository INSTANCE = null;
    private final HttpDataSource mHttpDataSource;

    private final LocalDataSource mLocalDataSource;

    private ActivityRepository(@NonNull HttpDataSource httpDataSource,
                               @NonNull LocalDataSource localDataSource) {
        this.mHttpDataSource = httpDataSource;
        this.mLocalDataSource = localDataSource;
    }

    public static ActivityRepository getInstance(HttpDataSource httpDataSource,
                                                 LocalDataSource localDataSource) {
        if (INSTANCE == null) {
            synchronized (ActivityRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ActivityRepository(httpDataSource, localDataSource);
                }
            }
        }
        return INSTANCE;
    }

    @VisibleForTesting
    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void saveUserName(String userName) {
        mLocalDataSource.saveUserName(userName);
    }

    @Override
    public void savePassword(String password) {
        mLocalDataSource.savePassword(password);
    }

    @Override
    public String getUserName() {
        return mLocalDataSource.getUserName();
    }

    @Override
    public String getPassword() {
        return mLocalDataSource.getPassword();
    }

    @Override
    public Flowable<BaseResponse<Object>> login(String username, String password) {
        return mHttpDataSource.login(username, password);
    }

    @Override
    public ApiService getApiService() {
        return mHttpDataSource.getApiService();
    }

}
