package com.xtree.lottery.ui.view

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.xtree.lottery.R

data class PrizeInfo(val bonus: String?, val issue: String?)

class PrizeNoticeView(private val rootView: ConstraintLayout) {

    private val noticeView: View = LayoutInflater.from(rootView.context)
        .inflate(R.layout.layout_prize_notice, rootView, false)

    private val moneyText: TextView = noticeView.findViewById(R.id.moneyText)
    private val issueText: TextView = noticeView.findViewById(R.id.issueText)
    private val closeBtn: ImageView = noticeView.findViewById(R.id.closeBtn)

    private val handler = Handler(Looper.getMainLooper())
    private val dismissRunnable = Runnable { hide() }

    init {
        rootView.addView(noticeView)
        closeBtn.setOnClickListener {
            hide()
        }
    }

    fun showPrize(prize: PrizeInfo?) {
        if (prize == null) return

        moneyText.text = "${prize.bonus ?: "--"} 元"
        issueText.text = "期号：${prize.issue ?: "--"}"

        noticeView.visibility = View.VISIBLE
        handler.removeCallbacks(dismissRunnable)
        handler.postDelayed(dismissRunnable, 5000)
    }

    fun hide() {
        noticeView.visibility = View.GONE
    }
}
