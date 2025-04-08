package com.xtree.lottery.data

import android.annotation.SuppressLint
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import com.xtree.base.vo.UserMethodsResponse
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

    var staticLotteryMethodsData: HashMap<String, MenuMethodsData>? = null
    var dynamicUserMethods: UserMethodsResponse? = null

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
                emitter.onError(e)
            }
        } .subscribeOn(Schedulers.single())
            .observeOn(Schedulers.single())
            .subscribe(
                { data -> staticLotteryMethodsData = data },
                { error -> println("Error: ${error.message}") }
            )
    }
}