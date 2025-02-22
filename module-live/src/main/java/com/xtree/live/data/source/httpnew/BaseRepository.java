package com.xtree.live.data.source.httpnew;

import android.content.Context;

import me.xtree.mvvmhabit.utils.Utils;

public class BaseRepository {
    protected Context mContext;
    public BaseRepository(){
        this.mContext = Utils.getContext();
    }

    protected <T> T obtainApiService(Class<T> t){
        return RepositoryManager.getInstance().obtainApiRetrofitService(t);
    }

    protected <T> T obtainsStrService(Class<T> t){
        return RepositoryManager.getInstance().obtainStrRetrofitService(t);
    }

    protected <T> T obtainJsonService(Class<T> t){
        return RepositoryManager.getInstance().obtainJsonRetrofitService(t);
    }

    protected <T> T obtainDomainCheckService(Class<T> t){
        return RepositoryManager.getInstance().obtainDomainCheckRetrofitService(t);
    }

}
