package com.xtree.lottery.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.xtree.base.lottery.data.LotteryPublicData.pushSetting
import com.xtree.base.utils.DomainUtil
import com.xtree.lottery.data.source.vo.Data
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.xtree.mvvmhabit.utils.KLog
import okhttp3.OkHttpClient
import okhttp3.Request
import org.greenrobot.eventbus.EventBus
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit

/**
 * 轮询
 */
object LotteryPolling {
    private var pollingJob: Job? = null
    val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)  // 建议设置长轮询读取时间
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()
    var tag = 1
    fun startPollingWithOkHttp() {
        if (pollingJob?.isActive == true) return // 防止重复启动

        pollingJob = CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                try {
                    val timestamp = System.currentTimeMillis()
                    val date = Date(timestamp)
                    val sdf = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH)
                    sdf.timeZone = TimeZone.getTimeZone("GMT")
                    val formatted = sdf.format(date)

                    val url = DomainUtil.getApiUrl() + "/lp/" + pushSetting!!.pub_channel_id + "/" + pushSetting!!.user_channel_id +
                            "?&token=" + pushSetting!!.pub_channel_token +
                            "&tag=" + tag +
                            "&time=$formatted" +
                            "&eventid=" +
                            "&callback=PushStreamManager_0_onmessage_$timestamp" +
                            "&_=$timestamp"
                    KLog.i("2342342", url)

                    val request = Request.Builder()
                        .url(url)
                        .get()
                        .build()

                    val response = client.newCall(request).execute()

                    if (response.isSuccessful) {
                        val body = response.body?.string() ?: ""
                        KLog.d("PUSH111", body)
                        val jsonArray = extractJsonFromJsonp(body)
                        KLog.d("PUSH122", jsonArray.toString())
                        jsonArray?.let {
                            var tag1 = 0
                            for (i in 0 until it.length()) {
                                val item = it.getJSONObject(i)
                                if (tag1 < item.optInt("tag")) {
                                    tag1 = item.optInt("tag")
                                }
                                val text = item.optString("text", null) ?: continue
                                try {
                                    val json = JSONObject(text)
                                    withContext(Dispatchers.Main) {
                                        //KLog.d("PUSH", "✅ 收到数据：${json.toString(2)}")
                                        val gson = Gson()
                                        KLog.d("PUSH", text)
                                        val module = json.optString("module")
                                        KLog.i(module)
                                        when (module) {
                                            "project_draw_results" -> {
                                                KLog.i("project_draw_results")
                                                val mList: List<Data> =
                                                    gson.fromJson<List<Data>>(json.optString("data"), object : TypeToken<List<Data?>?>() {}.type)
                                                for (i in mList) {
                                                    if (i.got_prize) {
                                                        KLog.i("project_draw_results")
                                                        EventBus.getDefault().post(LotteryEventVo(LotteryEventConstant.EVENT_PRIZI_N0TICE, i))
                                                        break
                                                    }
                                                }
                                            }
                                        }

                                    }
                                } catch (e: Exception) {
                                    KLog.e("PUSH", "❌ 解析失败：$text -> ${e.message}")
                                }
                            }
                            tag = tag1
                        }
                    } else {
                        KLog.e("PUSH", "❌ 请求失败 code=${response.code}")
                    }
                } catch (e: Exception) {
                    KLog.e("PUSH", "❗异常: ${e.toString() + e.message}")
                }


                delay(1000) // 轮询间隔 1 秒
            }
        }
    }

    fun stop() {
        pollingJob?.cancel()
        pollingJob = null
        KLog.i("PUSH", "轮询已停止")
    }


    fun extractJsonFromJsonp(raw: String): JSONArray? {
        val start = raw.indexOf('(')
        val end = raw.lastIndexOf(')')
        return if (start in 1 until end) {
            val jsonText = raw.substring(start + 1, end)
            JSONArray(jsonText)
        } else null
    }
}

