package com.xtree.lottery.data

import android.annotation.SuppressLint
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import com.xtree.lottery.data.source.response.UserMethodsResponse
import com.xtree.lottery.data.source.vo.MenuMethodsData
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import me.xtree.mvvmhabit.base.BaseApplication


/**
 *Created by KAKA on 2024/12/24.
 *Describe: 彩票玩法数据管理
 */
@SuppressLint("CheckResult")
object LotteryDataManager {

    var lotteryMethodsData: HashMap<String, MenuMethodsData>? = null
    var userMethods: UserMethodsResponse? = null

    init {
        Single.create { emitter ->
            try {
                val json = BaseApplication.getInstance().assets.open("methods.json").bufferedReader().use { it.readText() }
                val jsonObject = JsonParser.parseString(json).asJsonObject
                val cleanedJson = jsonObject.toString()
                val methods = Gson().fromJson<HashMap<String, MenuMethodsData>>(
                    cleanedJson,
                    object : TypeToken<HashMap<String, MenuMethodsData>>() {}.type
                )
                emitter.onSuccess(methods)
            } catch (e: Exception) {
                emitter.onError(e)  // 发生错误时发出错误信号
            }
        } .subscribeOn(Schedulers.single())  // 在 IO 线程上执行 Single 操作
            .observeOn(Schedulers.single()) // 在单独线程上处理结果（可以选择其他线程）
            .subscribe(
                { data -> lotteryMethodsData = data },
                { error -> println("Error: ${error.message}") }
            )
    }
}