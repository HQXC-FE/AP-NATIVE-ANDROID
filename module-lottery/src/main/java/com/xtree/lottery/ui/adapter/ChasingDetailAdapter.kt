package com.xtree.lottery.ui.adapter

import android.text.TextUtils
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lxj.xpopup.XPopup
import com.xtree.base.utils.ClickUtil
import com.xtree.lottery.R
import com.xtree.lottery.data.source.vo.ATaskdetail
import com.xtree.lottery.ui.view.BtCpDetailDialog
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone


class ChasingDetailAdapter(list: MutableList<ATaskdetail>, val owner: LifecycleOwner) :
    BaseQuickAdapter<ATaskdetail, BaseViewHolder>(R.layout.item_chase_detail, list) {
    override fun convert(holder: BaseViewHolder, item: ATaskdetail) {
        // 复选框
        val deadlineMillis = try {
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            sdf.timeZone = TimeZone.getTimeZone("GMT+8")
            sdf.parse(item.canneldeadline)?.time ?: 0L
        } catch (e: Exception) {
            0L
        }
        val cbIssue = holder.getView<CheckBox>(R.id.cb_issue)

        val showCheckbox = item.status.toInt() < 2 && deadlineMillis > System.currentTimeMillis()

        if (!showCheckbox) {
            // 隐藏复选框图标
            cbIssue.buttonDrawable = null
        }
        // 禁用点击
        cbIssue.isClickable = showCheckbox
        cbIssue.isFocusable = showCheckbox
        item.showCheckbox = showCheckbox
        cbIssue.isChecked = item.isChecked
        cbIssue.setOnCheckedChangeListener { _, _ ->
            item.isChecked = cbIssue.isChecked
        }

        cbIssue.text = item.issue
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
                if (ClickUtil.isFastClick()) {
                    return@setOnClickListener
                }
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
