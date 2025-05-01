package com.xtree.home.vo;

import com.xtree.base.vo.RechargeOrderVo;

import java.util.List;


public class RechargeReportVo {
    public List<RechargeOrderVo> result; // 充值记录
    public int nowPage; // 1, 当前页
    public String count; // "7" 库总条数
    public int showRows; // 10 每页条数
}