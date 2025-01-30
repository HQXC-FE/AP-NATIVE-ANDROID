package com.xtree.base.net.fastest

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.TypeReference
import com.xtree.base.global.SPKeyGlobal
import me.xtree.mvvmhabit.utils.SPUtils

/**
 *Created by KAKA on 2024/11/7.
 *Describe: 缓存最近上传的超时域名
 */
object FastestMonitorCache {

    private val domains = mutableListOf<TopSpeedDomain>()
    var scoreCacheList = mutableListOf<TopSpeedDomain>()
        get() {
            return LineHelpUtil.getUploadList(field)
        }

    const val TIME_OUT = 1000 * 60 * 60

    var MAX_UPLOAD_TIME = TIME_OUT

    var SPEED_CALCULATION = 0F
        set(value) {
            field = if (value >= 100) {
                1F
            } else {
                value / 100
            }
        }

    var app_response_speed_max = 0

    var last_upload_time = 0L

    init {
        val fastest_monitor_timeout =
            SPUtils.getInstance().getInt(SPKeyGlobal.DEBUG_APPLY_FASTEST_MONITOR_TIMEOUT)
        if (fastest_monitor_timeout > 0) {
            MAX_UPLOAD_TIME = fastest_monitor_timeout
        }

        val fastest_score_cache = SPUtils.getInstance().getString(SPKeyGlobal.FASTEST_SCORE_CACHE)
        if (!fastest_score_cache.isNullOrEmpty()) {
            scoreCacheList = JSON.parseObject<List<TopSpeedDomain>>(fastest_score_cache,
                object : TypeReference<List<TopSpeedDomain>?>() {}).toMutableList();
        }

        app_response_speed_max = SPUtils.getInstance().getInt(SPKeyGlobal.APP_Response_Speed_Max)
    }

    fun put(domain: TopSpeedDomain) {
        getDomains().findLast {
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

        getDomains().findLast {
            it.url.equals(domain.url)
        }?.let {
            //如果最后一次上报小于1000，且最新数据》=1000，则重新上报
            if (it.speedSec < 1000 && domain.speedSec >= 1000) {
                return true
            }

            //如果距离最后一次上报超过一小时，重新上报
            return currentTime - it.lastUploadMonitor >= MAX_UPLOAD_TIME
        } ?: run {
            return true
        }
    }

    fun getFastestScore(domain: TopSpeedDomain): Long {

   //     CfLog.i("****** SettingsVo app_response_speed_max " + app_response_speed_max)

        scoreCacheList.findLast {
            it.url.equals(domain.url)
        }?.let {
            //（1）本次测速<上次测速：上次测速扣百分比
            //（2）本次测速>上次测速：本次测速为基础计算数字
            if (domain.speedSec > it.speedScore) {
                domain.speedScore = domain.speedSec
            } else {
                domain.speedScore = it.speedScore - (it.speedScore * SPEED_CALCULATION).toLong()
            }

            if(domain.speedScore > app_response_speed_max && app_response_speed_max > 0){
                domain.speedScore = app_response_speed_max.toLong();
            }

            scoreCacheList.remove(it)

            scoreCacheList.add(domain)

            return domain.speedScore
        } ?: run {
            domain.speedScore = domain.speedSec
            if(domain.speedScore > app_response_speed_max && app_response_speed_max > 0){
                domain.speedScore = app_response_speed_max.toLong();
            }
            scoreCacheList.add(domain)
            return domain.speedScore
        }
    }

    fun saveFastestScore() {
        SPUtils.getInstance()
            .put(SPKeyGlobal.FASTEST_SCORE_CACHE, JSON.toJSONString(scoreCacheList))
    }
}