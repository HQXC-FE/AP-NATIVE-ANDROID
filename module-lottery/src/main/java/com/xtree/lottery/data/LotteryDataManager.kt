package com.xtree.lottery.data

import android.annotation.SuppressLint
import android.app.Application
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import com.xtree.base.utils.CfLog
import com.xtree.base.vo.UserMethodsResponse
import com.xtree.lottery.data.source.vo.MenuMethodsData
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import me.xtree.mvvmhabit.base.BaseApplication


object LotteryDataManager {

    var staticLotteryMethodsData: HashMap<String, MenuMethodsData>? = null
    var dynamicUserMethods: UserMethodsResponse? = null

    fun init(application: Application) {
        CfLog.e("玩法json加载开始 @ ${System.currentTimeMillis() / 1000}")
        Single.fromCallable {
            val json = application.assets.open("methods.json")
                .bufferedReader().use { it.readText() }

            Gson().fromJson<HashMap<String, MenuMethodsData>>(
                json,
                object : TypeToken<HashMap<String, MenuMethodsData>>() {}.type
            )
        }
            .subscribeOn(Schedulers.io()) // 更适合 IO 操作
            .observeOn(Schedulers.computation()) // 解析完成后再处理到逻辑线程
            .subscribe(
                { data ->
                    staticLotteryMethodsData = data
                    CfLog.e("玩法json加载完成 @ ${System.currentTimeMillis() / 1000}")
                },
                { error -> CfLog.e("玩法json加载失败: ${error.message}") }
            )
    }
}
