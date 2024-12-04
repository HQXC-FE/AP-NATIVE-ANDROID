package com.xtree.lottery.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.xtree.base.global.SPKeyGlobal
import com.xtree.lottery.R
import com.xtree.lottery.data.source.vo.TraceInfoVo
import me.xtree.mvvmhabit.utils.SPUtils


class TraceInfoAdapter(list: ArrayList<TraceInfoVo.Bean>) : BaseQuickAdapter<TraceInfoVo.Bean, BaseViewHolder>(R.layout.item_trace, list) {
    private val username: String = SPUtils.getInstance().getString(SPKeyGlobal.USER_NAME) // 用户名
    override fun convert(holder: BaseViewHolder, item: TraceInfoVo.Bean) {
        holder.setText(R.id.tvw_lottery_name, item.cnname)
        holder.setText(R.id.tvw_bet, item.taskprice)
        holder.setText(R.id.tvw_method_name, item.methodname)
        holder.setText(R.id.tvw_bonus, item.begintime)
        holder.setText(R.id.tvw_username, username)
        val status = when (item.status) {
            "0" -> {
                "进行中"
            }

            "1" -> {
                "已取消"
            }

            "2" -> {
                "已完成"
            }

            else -> {
                ""
            }
        }
        holder.setText(R.id.tvw_status, status)
    }

}