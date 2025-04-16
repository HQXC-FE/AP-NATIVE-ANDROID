package com.xtree.lottery.ui.adapter

import android.text.TextUtils
import android.view.View
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lxj.xpopup.XPopup
import com.xtree.lottery.R
import com.xtree.lottery.data.source.vo.ATaskdetail
import com.xtree.lottery.ui.view.BtCpDetailDialog


class ChasingDetailAdapter(list: MutableList<ATaskdetail>, val owner: LifecycleOwner) :
    BaseQuickAdapter<ATaskdetail, BaseViewHolder>(R.layout.item_chase_detail, list) {
    override fun convert(holder: BaseViewHolder, item: ATaskdetail) {
        holder.setText(R.id.cb_issue, item.issue)
        holder.setText(R.id.tv_chase_number, item.multiple.plus("倍"))
        val status: String = if (TextUtils.equals("0", item.status)) {
            "进行中"
        } else if (TextUtils.equals("2", item.status)) {
            "已取消"
        } else if (TextUtils.equals("1", item.status)) {
            "已完成"
        } else {
            "未知"
        }
        holder.setText(R.id.tv_chase_status, status)

        val tvOrderDetail = holder.getView<TextView>(R.id.tv_order_detail)
        // 查看详情
        if (item.projectid.isNullOrEmpty()) {
            tvOrderDetail.visibility = View.INVISIBLE
            tvOrderDetail.isClickable = false
        } else {
            tvOrderDetail.isClickable = true
            tvOrderDetail.visibility = View.VISIBLE
            tvOrderDetail.setOnClickListener {
                val dialog: BtCpDetailDialog =
                    BtCpDetailDialog.newInstance(
                        context,
                        owner,
                        item.projectid
                    )
                XPopup.Builder(context).asCustom(dialog).show()
            }
        }
    }
}
