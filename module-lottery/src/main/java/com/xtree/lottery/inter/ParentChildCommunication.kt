package com.xtree.lottery.inter

import com.xtree.lottery.data.source.vo.IssueVo

interface ParentChildCommunication {
    // Fragment 调用 Activity 的方法
    fun onFragmentSendData(data: ArrayList<IssueVo>)

    // Activity 调用 Fragment 的方法
    fun onActivitySendData(data: ArrayList<IssueVo>)
}

