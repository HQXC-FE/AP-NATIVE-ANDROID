package com.xtree.bet.bean.request.im;

import java.util.List;

public class GetBetListReq {

    /**
     * 确认投注状态清单.
     * 1 = Pending 待处理
     * 2 = Confirmed 已确认
     * 3 = Rejected (refers to Danger Cancel) 已拒绝 (危
     * 险球取消)
     * 4 = Cancelled 已取消
     */
    public List<Integer> BetConfirmationStatus;
    public String Token;
    public String MemberCode;
    public String startDate;
    public String EndDate;
    public String TimeStamp;
    public int SourceWallet;
    /**
     * 指出每页记录数.
     * 30, 50, 100
     * 默认 = 30
     */
    public int PageRecords;

    /**
     * 指出返回的页数
     */
    public int Page;


}
