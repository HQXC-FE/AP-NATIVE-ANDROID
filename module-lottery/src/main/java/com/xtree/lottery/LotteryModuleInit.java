package com.xtree.lottery;

import android.app.Application;

import com.xtree.base.base.IModuleInit;
import com.xtree.lottery.data.LotteryDataManager;

import me.xtree.mvvmhabit.utils.KLog;

/**
 * Created by goldze on 2018/6/21 0021.
 */

public class LotteryModuleInit implements IModuleInit {
    @Override
    public boolean onInitAhead(Application application) {
        KLog.e("彩票模块初始化 -- onInitAhead");
        LotteryDataManager.INSTANCE.init(application);
        return false;
    }

    @Override
    public boolean onInitLow(Application application) {
        KLog.e("彩票模块初始化 -- onInitLow");
        return false;
    }
}
