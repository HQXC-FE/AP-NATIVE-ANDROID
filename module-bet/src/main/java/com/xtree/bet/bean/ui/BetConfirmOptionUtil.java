package com.xtree.bet.bean.ui;

import static com.xtree.bet.ui.activity.MainActivity.KEY_PLATFORM;
import static com.xtree.bet.ui.activity.MainActivity.PLATFORM_PM;
import static com.xtree.bet.ui.activity.MainActivity.PLATFORM_PMXC;

import android.text.TextUtils;

import me.xtree.mvvmhabit.utils.SPUtils;

public class BetConfirmOptionUtil {
    public static BetConfirmOption getInstance(Match match, PlayType playType, OptionList optionList, Option option){
        String platform = SPUtils.getInstance().getString(KEY_PLATFORM);
        if (!TextUtils.equals(platform, PLATFORM_PM) && !TextUtils.equals(platform, PLATFORM_PMXC)) {
            return new BetConfirmOptionFb(match, playType, optionList, option);
        }else {
            return new BetConfirmOptionPm(match, playType,optionList,option);
        }

    }
}
