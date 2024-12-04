package com.xtree.lottery.ui.adapter

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.xtree.lottery.data.config.Lottery
import com.xtree.lottery.data.config.LotteryParent
import com.xtree.lottery.R

class LotteryAdapter(list: ArrayList<LotteryParent>, val callback: (Lottery) -> Unit) :
    BaseQuickAdapter<LotteryParent, BaseViewHolder>(R.layout.item_lottery, list) {

    override fun convert(holder: BaseViewHolder, item: LotteryParent) {
        // 设置item数据
        holder.setText(R.id.tv_lottery_name, item.name)

        val rvLotteryChild = holder.getView<RecyclerView>(R.id.rv_lottery_child)
        rvLotteryChild.layoutManager = GridLayoutManager(context, 3)
        rvLotteryChild.setHasFixedSize(true)
        val adapter = LotteryChildAdapter(item.list)
        rvLotteryChild.adapter = adapter
        adapter.setOnItemClickListener { _, _, position ->
            callback(item.list[position])
        }
    }

}

class LotteryChildAdapter(list: ArrayList<Lottery>) : BaseQuickAdapter<Lottery, BaseViewHolder>(R.layout.item_lottery_child, list) {
    override fun convert(holder: BaseViewHolder, item: Lottery) {
        holder.setText(R.id.tv_lottery, item.name)
        holder.setImageResource(R.id.iv_lottery, item.link)
    }

}



