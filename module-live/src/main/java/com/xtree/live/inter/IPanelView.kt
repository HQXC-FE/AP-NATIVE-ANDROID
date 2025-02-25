package com.xtree.live.inter

import androidx.annotation.IdRes

interface IPanelView : ViewAssertion {

    @IdRes
    fun getBindingTriggerViewId(): Int

    fun isTriggerViewCanToggle(): Boolean

    fun isShowing():Boolean
}