package com.xtree.live.inter

interface TriggerViewClickInterceptor {
    fun intercept(triggerId: Int): Boolean
}