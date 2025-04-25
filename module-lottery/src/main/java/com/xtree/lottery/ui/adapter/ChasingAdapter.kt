package com.xtree.lottery.ui.adapter

import android.text.TextUtils
import android.widget.CheckBox
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.xtree.lottery.R
import com.xtree.lottery.data.source.vo.IssueVo


class ChasingAdapter(list: MutableList<IssueVo>, public val money: Double, val changeNumber: (Double) -> Unit) : BaseQuickAdapter<IssueVo, BaseViewHolder>(R.layout.item_chasing_number, list) {
    public var checkedPosition = -1
    override fun convert(holder: BaseViewHolder, item: IssueVo) {

        val cbNumber = holder.getView<CheckBox>(R.id.cb_number)
        val tvMultiple = holder.getView<TextView>(R.id.tv_multiple)
        val tvMoney = holder.getView<TextView>(R.id.tv_money)
        cbNumber.text = (holder.layoutPosition + 1).toString()
        holder.setText(R.id.tv_issue, item.issue)

        tvMultiple.text = item.multiple.toString()
        tvMoney.text = "¥".plus(item.amount.toString())


        cbNumber.setOnCheckedChangeListener { buttonView, isChecked ->
            //if (isChecked) {
            //    tvMultiple.text = "1"
            //    tvMoney.text = "¥" + money
            //} else {
            //    tvMultiple.text = "0"
            //    tvMoney.text = "¥0.0000"
            //}
            //changeNumber(item.amount)
            item.isCheck = isChecked
        }
        item.isCheck = holder.layoutPosition <= checkedPosition
        cbNumber.isChecked = item.isCheck
    }

}