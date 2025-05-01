package com.xtree.base.net.fastest

import com.drake.net.NetConfig
import com.drake.net.okhttp.trustSSLCertificate
import com.drake.net.request.UrlRequest
import com.xtree.base.BuildConfig
import me.xtree.mvvmhabit.http.interceptor.logging.Level
import me.xtree.mvvmhabit.http.interceptor.logging.LoggingInterceptor
import okhttp3.ConnectionPool
import okhttp3.Dispatcher
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.internal.platform.Platform
import org.json.JSONObject
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit

/**
 *Created by KAKA on 2024/7/7.
 *Describe: 测速配置项
 */

//测速API
const val FASTEST_API = "/?speedTest=1"
const val FASTEST_API_BMP = "/point.bmp"
const val FASTEST_H5_API = "/#/activity"
const val FASTEST_CONFIG_API = "/xc/app_config.json"
const val FASTEST_MONITOR_API = "/api/sports/speedmonitor"

//测速请求组
const val FASTEST_GOURP_NAME = "fstestaRequest"

const val FASTEST_GOURP_NAME_H5 = "fstestaRequestH5"

//测速接口配置
val FASTEST_BLOCK: (UrlRequest.() -> Unit) = {
    addHeader("App-RNID", "87jumkljo")
//    addHeader("Source", "9")
//    addHeader(
//        "app-version", StringUtils.getVersionName(
//            Utils.getContext()
//        )
//    )
}

//测速URL
fun getFastestAPI(host: String, api: String = FASTEST_API): String {
    return "${host}${api}"
}

/**
 * 配置网络客户端
 */
fun initNet() {
    val okHttpClientBuilder = OkHttpClient.Builder()
    okHttpClientBuilder.connectionPool(ConnectionPool(60, 6, TimeUnit.MINUTES))
    val dispatcher = Dispatcher()
    dispatcher.maxRequests = 1000 // 设置同时执行的最大请求数

    dispatcher.maxRequestsPerHost = 1000 // 设置每个主机同时执行的最大请求数

    okHttpClientBuilder.dispatcher(dispatcher)
    okHttpClientBuilder.trustSSLCertificate()
    okHttpClientBuilder.addInterceptor(
        LoggingInterceptor.Builder()//构建者模式
            .loggable(BuildConfig.DEBUG) //是否开启日志打印
            .setLevel(Level.BASIC) //打印的等级  测速部分信息详细点
            .log(Platform.INFO) // 打印类型
            .ifFastRequest(true)
            .request("ReqFast") // request的Tag  测速部分打自己的tag
            .response("RepFast")// Response的Tag  测速部分打自己的tag
            .build()
    )
//    okHttpClientBuilder.addInterceptor(Interceptor { chain ->
//        val originalRequest = chain.request().newBuilder()
//        val requestWithHeaders = originalRequest.headers()
//        requestWithHeaders.removeAll("Content-Type")
//        requestWithHeaders.add("Content-Type", "application/vnd.sc-api.v1.json")
//
//        chain.proceed(originalRequest.build())
//    })
    NetConfig.initialize("", null, okHttpClientBuilder)
}

fun cjson(vararg body: Pair<String, Any?>): RequestBody {
    return JSONObject(body.toMap()).toString().toCRequestBody()
}

fun String.toCRequestBody(): RequestBody {
    val contentType ="application/vnd.sc-api.v1.json;".toMediaType()
    var charset: Charset = Charsets.UTF_8
    var finalContentType: MediaType? = contentType
    if (contentType != null) {
        val resolvedCharset = contentType.charset()
        if (resolvedCharset == null) {
            charset = Charsets.UTF_8
            finalContentType = "$contentType".toMediaTypeOrNull()
        } else {
            charset = resolvedCharset
        }
    }
    val bytes = toByteArray(charset)
    return bytes.toRequestBody(finalContentType, 0, bytes.size)
}

