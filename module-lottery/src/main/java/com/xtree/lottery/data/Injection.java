package com.xtree.lottery.data;


import com.xtree.lottery.data.source.HttpDataSource;
import com.xtree.lottery.data.source.LocalDataSource;
import com.xtree.lottery.data.source.LotteryApiService;
import com.xtree.lottery.data.source.http.HttpDataSourceImpl;
import com.xtree.lottery.data.source.local.LocalDataSourceImpl;
import com.xtree.base.net.RetrofitClient;

/**
 * 注入全局的数据仓库
 */
public class Injection {
    public static LotteryRepository provideMainRepository(boolean reNew) {

        //网络API服务
        LotteryApiService apiService = RetrofitClient.getInstance().create(LotteryApiService.class);
        //网络数据源
        HttpDataSource httpDataSource = HttpDataSourceImpl.getInstance(apiService);
        //本地数据源
        LocalDataSource localDataSource = LocalDataSourceImpl.getInstance();
        //两条分支组成一个数据仓库
        return LotteryRepository.getInstance(httpDataSource, localDataSource);
    }
}
