package com.xtree.mine.vo;

public class UsdtVo {

    public String id; // "3139",
    public String user_name; // "tst033",
    public String usdt_type; // "TRC20_USDT",
    //public String userid; // "5228471",
    public String usdt_card; // "******OIWL",
    public String status; // "1", 1-正常, 3-锁定
    public String atime; // "2023-12-25 20:43:48",
    public String utime; // "2023-12-25 20:43:48",
    //public String user_quota; // "0.0000",
    //public String restrictions_quota; // "16124.0000",
    //public String restrictions_teamquota; // "16124.0000",
    //public String userlimitswitch; // "0",
    //public String teamlinitswitch; // "0",
    //public String uinuout_uptime; // "2023-12-25 20:43:48",
    //public String effective_quota; // "0.0000",
    //public String effective_data; // null,
    //public String cnybank_backblance; // "0.0000",
    //public String card_type; // "1",
    //public String vip_virtual_currency_quota; // null,
    //public String vvcq_updated_at; // null,
    //public String username; // "tst033@as"
    //1、lockbankoprate参数值:yes，前台 管理USDT页面隐藏“重新绑定”按钮；
    //2、lockbankoprate参数值:no，前台 管理USDT页面显示“重新绑定”按钮；

    public boolean lockbankoprate ;

    @Override
    public String toString() {
        return "UsdtVo{" +
                "id='" + id + '\'' +
                ", user_name='" + user_name + '\'' +
                ", usdt_type='" + usdt_type + '\'' +
                ", usdt_card='" + usdt_card + '\'' +
                ", status='" + status + '\'' +
                ", atime='" + atime + '\'' +
                ", utime='" + utime + '\'' +
                ", lockbankoprate='" + lockbankoprate + '\'' +
                '}';
    }
}
