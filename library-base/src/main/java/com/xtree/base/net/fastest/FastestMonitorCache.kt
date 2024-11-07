package com.xtree.base.net.fastest

import com.xtree.base.global.SPKeyGlobal
import com.xtree.base.vo.TopSpeedDomain
import me.xtree.mvvmhabit.utils.SPUtils

/**
 *Created by KAKA on 2024/11/7.
 *Describe: 缓存最近上传的超时域名
 */
object FastestMonitorCache {

    private val domains = mutableListOf<TopSpeedDomain>()

    const val TIME_OUT = 1000 * 60 * 60

    var MAX_UPLOAD_TIME = TIME_OUT

    init {
        val fastest_monitor_timeout =
            SPUtils.getInstance().getInt(SPKeyGlobal.DEBUG_APPLY_FASTEST_MONITOR_TIMEOUT)
        if (fastest_monitor_timeout > 0) {
            MAX_UPLOAD_TIME = fastest_monitor_timeout
        }
    }

    fun put(domain: TopSpeedDomain) {
        domains.findLast {
            it.url.equals(domain.url)
        }?.let {
            domains.remove(it)
            domains.add(domain)
        } ?: run {
            domains.add(domain)
        }
    }

    fun getDomains(): MutableList<TopSpeedDomain> {
        val currentTime = System.currentTimeMillis()
        domains.retainAll { currentTime - it.lastUploadMonitor <= MAX_UPLOAD_TIME }
        return domains
    }

    /**
     * 检查是否当前域名最后一次上传时间已超过1小时
     */
    fun check(domain: TopSpeedDomain): Boolean {
        val currentTime = System.currentTimeMillis()
        val con = getDomains().any {
            it.url == domain.url
        }

        if (!con) {
            return true
        }

        return getDomains().any { currentTime - it.lastUploadMonitor >= MAX_UPLOAD_TIME && it.url == domain.url }
    }
}