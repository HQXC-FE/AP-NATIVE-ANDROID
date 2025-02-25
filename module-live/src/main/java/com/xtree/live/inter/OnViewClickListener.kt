package com.xtree.live.inter

import android.view.View

interface OnViewClickListener {
    fun onClickBefore(view: View?)
}

private typealias OnClickBefore = (view: View?) -> Unit

class OnViewClickListenerBuilder : OnViewClickListener {

    private var onClickBefore: OnClickBefore? = null

    override fun onClickBefore(view: View?) {
        onClickBefore?.invoke(view)
    }

    fun onClickBefore(onClickBefore: OnClickBefore) {
        this.onClickBefore = onClickBefore
    }
}