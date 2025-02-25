package com.xtree.live.inter

import android.content.Context
import android.content.res.Configuration
import android.view.Window
import com.xtree.live.uitl.DisplayUtil

class DeviceRuntime(val context: Context, val window: Window) {

    var isNavigationBarShow: Boolean = false
    var isPortrait: Boolean = false
    var isPad: Boolean = false
    var isFullScreen: Boolean = false;

    init {
        isPad = (context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE
        isPortrait = DisplayUtil.isPortrait(context)
        isNavigationBarShow = DisplayUtil.isNavigationBarShow(context, window)
        isFullScreen = DisplayUtil.isFullScreen(window)
    }
}