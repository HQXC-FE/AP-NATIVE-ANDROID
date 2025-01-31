package com.xtree.lottery.ui.adapter

import android.graphics.Color
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
        holder.setText(R.id.tv_status, item.form.get(0).get("label"))
        //holder.setTextColor(R.id.tv_status, Color.parseColor(item.form.get(0).get("className")))
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