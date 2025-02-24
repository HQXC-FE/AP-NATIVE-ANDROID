package com.xtree.mine.vo;

import java.util.List;

public class UserUsdtTypeVo extends UserBindBaseVo {

    public List<String> usdt_type; // [ "ERC20_USDT", "TRC20_USDT"]

    @Override
    public String toString() {
        return "UserUsdtTypeVo{" +
                "usdt_type=" + usdt_type +
                '}';
    }
}
