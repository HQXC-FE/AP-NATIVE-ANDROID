package com.xtree.lottery.data.source;

/**
 * Created by KAKA on 2024/3/12.
 * Describe: api 静态管理
 */
public class APIManager {

    //用户玩法权限
    public static final String USER_METHODS_URL = "/api/lottery/user-methods";
    //玩法菜单
    public static final String MENU_METHODS_URL = "/api/lottery/%s/method-menus";
    //盘口玩法
    public static final String HANDICAP_METHODS_URL = "/api/lottery/%s/dsp-methods";
    //获取用户余额
    public static final String BALANCE_URL = "/api/account/balance";
    //普通彩票投注
    public static final String BET_URL = "/api/lottery/bet";
    //再来一注
    public static final String COPY_BET_URL = "/api/lottery/copy-bet";
    //秒秒彩模拟开奖
    public static final String SIMULATED_NUMBER = "/api/lottery/%s/simulated-number";
    //秒秒彩投注
    public static final String MMC_BET_URL = "/api/lottery/mmc/bet";

}
