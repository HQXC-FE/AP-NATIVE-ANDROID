package com.xtree.mine.vo;

public class BalanceVo {

    public String balance; // "1896.0790",
    public String pre_ag; // "35.00",
    public String now_ag; // "0.00",
    public String dispensing_mul; // null

    @Override
    public String toString() {
        return "BalanceVo { " +
                "balance='" + balance + '\'' +
                ", pre_ag='" + pre_ag + '\'' +
                ", now_ag='" + now_ag + '\'' +
                ", dispensing_mul='" + dispensing_mul + '\'' +
                '}';
    }
}
