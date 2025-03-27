package com.xtree.lottery.ui.adapter

import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.text.TextUtils
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.xtree.base.mvvm.includes
import com.xtree.lottery.R
import com.xtree.lottery.data.source.vo.RecentLotteryBackReportVo
import com.xtree.lottery.rule.betting.Matchers
import com.xtree.lottery.utils.DiceCutter
import me.xtree.mvvmhabit.utils.ConvertUtils


class RecentAdapter(list: ArrayList<RecentLotteryBackReportVo>, val alias: String) :
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

        val adapter = RecentChildAdapter(item.displayCode, alias, R.layout.item_recent_child)
        rv.adapter = adapter
    }

}

class RecentChildAdapter(list: ArrayList<Map<String, String>>, val alias: String, layoutResId: Int) :
    BaseQuickAdapter<Map<String, String>, BaseViewHolder>(layoutResId, list) {
    override fun convert(holder: BaseViewHolder, item: Map<String, String>) {
        val tvCode = holder.getView<TextView>(R.id.tv_code)
        if (!TextUtils.isEmpty(alias) && Matchers.k3Alias.includes(alias)) {
            val diceSource = BitmapFactory.decodeResource(holder.itemView.resources, R.raw.lottery_dice)
            val bitmapDrawable = BitmapDrawable(holder.itemView.resources, DiceCutter.cutDiceImage(diceSource, item["codes"]!!.toInt(), 1))
            val params = LinearLayout.LayoutParams(ConvertUtils.dp2px(30f), ConvertUtils.dp2px(30f))
            params.rightMargin = ConvertUtils.dp2px(6f)
            params.topMargin = ConvertUtils.dp2px(6f)
            tvCode.layoutParams = params

            tvCode.background = bitmapDrawable
        } else {
            holder.setText(R.id.tv_code, item["codes"])
        }
    }

}