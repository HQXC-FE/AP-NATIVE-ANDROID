package com.xtree.lottery.ui.adapter

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.xtree.lottery.R
import com.xtree.lottery.data.source.vo.RecentLotteryVo


class RecentAdapter(list: ArrayList<RecentLotteryVo>) : BaseQuickAdapter<RecentLotteryVo, BaseViewHolder>(R.layout.item_recent_lottery, list) {

    override fun convert(holder: BaseViewHolder, item: RecentLotteryVo) {
        // 设置item数据
        holder.setText(R.id.tv_issue, item.issue)
        val rv = holder.getView<RecyclerView>(R.id.rv_recent_child)
        rv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val adapter = RecentChildAdapter(item.split_code, R.layout.item_recent_child)
        rv.adapter = adapter

    }

}

class RecentChildAdapter(list: ArrayList<String>, layoutResId: Int) : BaseQuickAdapter<String, BaseViewHolder>(layoutResId, list) {
    override fun convert(holder: BaseViewHolder, item: String) {
        holder.setText(R.id.tv_code, item)
    }

}