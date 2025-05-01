package com.xtree.lottery.ui.adapter

import android.text.TextUtils
import android.widget.CheckBox
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.xtree.lottery.R
import com.xtree.lottery.data.source.vo.IssueVo
import java.math.BigDecimal


class ChasingAdapter(list: MutableList<IssueVo>, val money: BigDecimal, val changeNumber: () -> Unit) :
    BaseQuickAdapter<IssueVo, BaseViewHolder>(R.layout.item_chasing_number, list) {
    public var checkedPosition = -1
    override fun convert(holder: BaseViewHolder, item: IssueVo) {

        val cbNumber = holder.getView<CheckBox>(R.id.cb_number)
        val tvMultiple = holder.getView<TextView>(R.id.tv_multiple)
        val tvMoney = holder.getView<TextView>(R.id.tv_money)
        cbNumber.text = (holder.layoutPosition + 1).toString()
        holder.setText(R.id.tv_issue, item.issue)

        tvMultiple.text = item.multiple.toString()
        if (item.amountBigDecimal == null) {
            item.amountBigDecimal = BigDecimal.ZERO
        }
        tvMoney.text = "¥".plus(item.amountBigDecimal?.toPlainString())

        item.isCheck = holder.layoutPosition <= checkedPosition
        cbNumber.isChecked = item.isCheck
        cbNumber.setOnCheckedChangeListener { buttonView, isChecked ->
            //倍数等于”0“，且已选中
            if (TextUtils.equals(tvMultiple.text.toString(), "0")) {
                if (isChecked) {
                    tvMultiple.text = "1"
                    tvMoney.text = "¥".plus(money.toPlainString())
                    item.amountBigDecimal = money
                    changeNumber()
                }
            }
            if (!isChecked) {
                tvMultiple.text = "0"
                tvMoney.text = "¥0.0"
                item.amountBigDecimal = BigDecimal.ZERO
                changeNumber()
            }
            item.isCheck = isChecked
        }
    }

}