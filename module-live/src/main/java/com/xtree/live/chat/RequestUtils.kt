package com.xtree.live.chat

import com.alibaba.fastjson.JSONObject
import com.blankj.utilcode.util.EncryptUtils
import com.google.gson.JsonObject
import com.hjq.gson.factory.GsonFactory
import com.xtree.live.LiveConfig
import com.xtree.live.SPKey
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

object RequestUtils {

    @JvmStatic
    fun getQueryMap(map: MutableMap<String, Any?>): Map<String, Any?> {
        map[SPKey.CHANNEL_CODE] = LiveConfig.getChannelCode()
        return map
    }

    @JvmStatic
    fun getRequestBody(jsonObject: JSONObject): RequestBody {
        jsonObject[SPKey.CHANNEL_CODE] = LiveConfig.getChannelCode()
        return jsonObject.toJSONString().toRequestBody("application/json".toMediaType())
    }

    @JvmStatic
    fun getRequestBody(jsonObject: JsonObject): RequestBody {
        jsonObject.addProperty(SPKey.CHANNEL_CODE, LiveConfig.getChannelCode())
        return jsonObject.toString().toRequestBody("application/json".toMediaType())
    }

    @JvmStatic
    fun getRequestBody(map: Map<String, Any>): RequestBody {
        return GsonFactory.getSingletonGson().toJson(map).toRequestBody("application/json".toMediaType())
    }

    @JvmStatic
    fun getApiFrontSign(map: Map<String, Any?>, key:String): String {
        var sign = flatSortQueries(map)
        sign += "&key=$key"

        return EncryptUtils.encryptMD5ToString(sign).lowercase()
    }

    @JvmStatic
    fun flatSortQueries(map: Map<String, Any?>): String {
        return map.entries.sortedBy {
            it.key
        }.joinToString("&") {
            "${it.key}=${if (it.value == null) "" else it.value.toString()}"
        }
    }

}