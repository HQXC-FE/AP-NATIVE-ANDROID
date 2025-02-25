package com.xtree.live.inter

interface ContentScrollMeasurer {
    fun getScrollDistance(defaultDistance: Int): Int
    fun getScrollViewId(): Int
}

private typealias GetScrollDistance = (defaultDistance: Int) -> Int
private typealias GetScrollViewId = () -> Int

class ContentScrollMeasurerBuilder : ContentScrollMeasurer {

    private var getScrollDistance: GetScrollDistance? = null
    private var getScrollViewId: GetScrollViewId? = null

    override fun getScrollDistance(defaultDistance: Int): Int = getScrollDistance?.invoke(defaultDistance) ?: 0
    override fun getScrollViewId(): Int = getScrollViewId?.invoke() ?: -1

    fun getScrollDistance(getScrollDistance: GetScrollDistance) {
        this.getScrollDistance = getScrollDistance
    }

    fun getScrollViewId(getScrollViewId: GetScrollViewId) {
        this.getScrollViewId = getScrollViewId
    }
}
