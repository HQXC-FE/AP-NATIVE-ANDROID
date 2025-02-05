package com.xtree.lottery.ui.adapter

import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.xtree.lottery.R
import com.xtree.lottery.data.source.vo.RecentLotteryBackReportVo


class RecentAdapter(list: ArrayList<RecentLotteryBackReportVo>) :
    BaseQuickAdapter<RecentLotteryBackReportVo, BaseViewHolder>(R.layout.item_recent_lottery, list) {

    override fun convert(holder: BaseViewHolder, item: RecentLotteryBackReportVo) {
        // 设置item数据
        holder.setText(R.id.tv_issue, item.issue)
        if (item.form.isEmpty()) {
            holder.setGone(R.id.tv_status, true)
        } else {
            holder.setGone(R.id.tv_status, false)
            holder.setText(R.id.tv_status, item.form[0]["label"])
            val color = item.form[0]["className"]
            if (color == null || !color.startsWith("#")) {
                holder.setTextColor(R.id.tv_status, Color.parseColor("#000000"))
            } else {
                holder.setTextColor(R.id.tv_status, Color.parseColor(color))
            }
        }

        val rv = holder.getView<RecyclerView>(R.id.rv_recent_child)
        rv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        val adapter = RecentChildAdapter(item.displayCode, R.layout.item_recent_child)
        rv.adapter = adapter
    }

}

class RecentChildAdapter(list: ArrayList<Map<String, String>>, layoutResId: Int) : BaseQuickAdapter<Map<String, String>, BaseViewHolder>(layoutResId, list) {
    override fun convert(holder: BaseViewHolder, item: Map<String, String>) {
        holder.setText(R.id.tv_code, item["codes"])
    }

}