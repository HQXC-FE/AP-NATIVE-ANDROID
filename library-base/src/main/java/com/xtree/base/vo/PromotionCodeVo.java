package com.xtree.base.vo;

/**
 * 注册新账户获取 第一步 code
 */
public class PromotionCodeVo {
    public String agency_model;
    public String default_skin;
    public String domain;//回传的code
    public String top_id;
    public boolean verifycodeswitch;

    @Override
    public String toString() {
        return "PromotionCodeVo{" +
                "agency_model='" + agency_model + '\'' +
                ", default_skin='" + default_skin + '\'' +
                ", domian='" + domain + '\'' +
                ", top_id='" + top_id + '\'' +
                ", verifycodeswitch=" + verifycodeswitch +
                '}';
    }
}
