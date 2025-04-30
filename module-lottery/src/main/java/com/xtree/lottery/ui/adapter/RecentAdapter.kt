package com.xtree.lottery.ui.adapter

import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.text.TextUtils
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.xtree.base.mvvm.includes
import com.xtree.lottery.R
import com.xtree.lottery.data.source.vo.RecentLotteryBackReportVo
import com.xtree.lottery.rule.betting.Matchers
import com.xtree.lottery.utils.DiceCutter
import com.xtree.lottery.utils.LhcHelper
import com.xtree.lottery.utils.PK10Helper
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
        val arrayList = item.displayCode as ArrayList
        val adapter = RecentChildAdapter(arrayList, alias, R.layout.item_recent_child)
        rv.adapter = adapter
    }

}

class RecentChildAdapter(list: ArrayList<Map<String, String>>, val alias: String, layoutResId: Int) :
    BaseQuickAdapter<Map<String, String>, BaseViewHolder>(layoutResId, list) {
    override fun convert(holder: BaseViewHolder, item: Map<String, String>) {
        val tvCode = holder.getView<TextView>(R.id.tv_code)
        if (!TextUtils.isEmpty(alias) && Matchers.k3Alias.includes(alias)) {
            val bitmapDrawable = BitmapDrawable(holder.itemView.resources, DiceCutter.diceResult(holder.itemView.resources, item["codes"]!!.toInt()))
            val params = LinearLayout.LayoutParams(ConvertUtils.dp2px(30f), ConvertUtils.dp2px(30f))
            params.rightMargin = ConvertUtils.dp2px(6f)
            params.topMargin = ConvertUtils.dp2px(6f)
            tvCode.layoutParams = params
            tvCode.background = bitmapDrawable
        } else if (!TextUtils.isEmpty(alias) && (Matchers.pk10Alias.includes(alias) || Matchers.jssmAlias.includes(alias))) {
            val bitmapDrawable1 = PK10Helper.getBallBackground(alias, item["codes"]!!)
            val params = LinearLayout.LayoutParams(ConvertUtils.dp2px(18f), ConvertUtils.dp2px(30f))
            params.rightMargin = ConvertUtils.dp2px(1.5f)
            params.topMargin = ConvertUtils.dp2px(6f)
            tvCode.layoutParams = params
            tvCode.background = bitmapDrawable1
            tvCode.textSize = 14f
            tvCode.setTypeface(null, Typeface.NORMAL)
            tvCode.text = item["codes"]
        } else if (!TextUtils.isEmpty(alias) && Matchers.lhcAlias.includes(alias)) {
            val params = LinearLayout.LayoutParams(ConvertUtils.dp2px(25f), ConvertUtils.dp2px(25f))
            params.rightMargin = ConvertUtils.dp2px(2f)
            params.topMargin = ConvertUtils.dp2px(7f)
            tvCode.layoutParams = params
            tvCode.textSize = 14f
            tvCode.setBackgroundResource(LhcHelper.getNumberColor(item["codes"]!!))
            if (item["codes"].equals("——")) {
                tvCode.setTextColor(ContextCompat.getColor(context, R.color.lt_color_text18))
            } else {
                tvCode.setTypeface(null, Typeface.NORMAL)
                tvCode.setTextColor(ContextCompat.getColor(context, R.color.white))
            }
            tvCode.text = item["codes"]
        } else {
            tvCode.text = item["codes"]
        }
    }

}

