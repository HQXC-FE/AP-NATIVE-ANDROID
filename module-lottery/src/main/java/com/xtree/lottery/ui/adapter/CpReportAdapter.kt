package com.xtree.lottery.ui.adapter

import android.content.Context
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.xtree.lottery.R
import com.xtree.lottery.data.source.vo.LotteryOrderVo


class CpReportAdapter(list: ArrayList<LotteryOrderVo>) : BaseQuickAdapter<LotteryOrderVo, BaseViewHolder>(R.layout.item_lottery_report_cp, list) {

    override fun convert(holder: BaseViewHolder, item: LotteryOrderVo) {
        holder.setText(R.id.tvw_lottery_name, item.lotteryname)
        holder.setText(R.id.tvw_bet, item.totalprice)
        holder.setText(R.id.tvw_method_name, item.methodname)
        holder.setText(R.id.tvw_bonus, item.bonus)
        holder.setText(R.id.tvw_username, item.username)
        holder.setText(R.id.tvw_status, getLotteryStatus(context, item))
    }

}

/**
 * 获取彩票的状态
 */
fun getLotteryStatus(ctx: Context, vo: LotteryOrderVo): String? {
    if (vo.iscancel.equals("1")) {
        return ctx.getString(R.string.txt_cancel_by_self)
    }
    if (vo.iscancel.equals("2")) {
        return ctx.getString(R.string.txt_cancel_by_platform)
    }
    if (vo.iscancel.equals("3")) {
        return ctx.getString(R.string.txt_cancel_by_error)
    }
    if (!vo.iscancel.equals("0")) {
        return ctx.getString(R.string.txt_cancel_no_status)
    }
    if (vo.isgetprize.equals("0")) {
        return ctx.getString(R.string.txt_prizes_not_open)
    }
    if (vo.isgetprize.equals("2")) {
        return ctx.getString(R.string.txt_prizes_not_win)
    }
    return if (vo.isgetprize.equals("1")) {
        if (vo.prizestatus === "0") {
            ctx.getString(R.string.txt_prizes_not_distributed)
        } else ctx.getString(R.string.txt_prizes_distributed)
    } else "---"
}