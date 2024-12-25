package com.xtree.lottery.data

import com.xtree.lottery.data.source.vo.IssueVo

/**
 * 当前彩票详情数据共享
 */
object LotteryDetailManager {
    var mIndex = 0
    var mIssues = ArrayList<IssueVo>()

    @Synchronized
    fun clearData() {
        mIndex = 0
        mIssues.clear()
    }
}