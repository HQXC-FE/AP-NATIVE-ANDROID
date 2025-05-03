package com.xtree.bet.ui.viewmodel;

/**
 * Created by marquis
 */

public interface BtRecordModel {
    /**
     * 查询投注记录
     */
    void betRecord(boolean isSettled);

    /**
     * 批量获取订单提前结算报价
     */
    void cashOutPrice();

    /**
     * 提前结算下注
     *
     * @param orderId                订单ID
     * @param cashOutStake           提前结算本金
     * @param unitCashOutPayoutStake 提前结算报价接口返回的"单位提前结算价格"
     * @param acceptOddsChange       是否接受下注时真实价格低于下注价格(false:不接受价格变低 true:接受价格变低下注)
     * @param parlay                 是否串关
     */
    void cashOutPricebBet(String orderId, double cashOutStake, double unitCashOutPayoutStake, boolean acceptOddsChange, boolean parlay);

    /**
     * 按提前结算订单ID查询提前结算订单金额及状态
     *
     * @param id
     */
    void getCashOutsByIds(String id);

}
