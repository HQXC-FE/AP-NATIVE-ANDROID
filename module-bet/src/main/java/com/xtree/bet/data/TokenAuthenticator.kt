package com.xtree.bet.data

import android.text.TextUtils
import com.xtree.base.global.SPKeyGlobal
import com.xtree.base.net.FBRetrofitClient
import com.xtree.base.net.RetrofitClient
import com.xtree.base.utils.BtDomainUtil
import com.xtree.base.vo.FBService
import com.xtree.bet.data.source.HttpDataSource
import com.xtree.bet.data.source.LocalDataSource
import com.xtree.bet.data.source.http.HttpDataSourceImpl
import com.xtree.bet.data.source.local.LocalDataSourceImpl
import com.xtree.base.http.BaseResponse
import com.xtree.base.utils.KLog
import com.xtree.base.utils.RxUtils
import com.xtree.base.utils.SPUtils
import okhttp3.Interceptor
import okhttp3.Response
import org.json.JSONObject
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.locks.ReentrantLock

class TokenAuthenticator : Interceptor {

    private val lock = ReentrantLock()

    private val refreshRequestCount = AtomicInteger(0)  // 使用 AtomicInteger 来记录请求次数

    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()

        val response = chain.proceed(request)

        // 处理特殊的 API 请求
        if (isForwardApiRequest(request.url.toString())) {

            // 获取 Token
            val token = getTokenForPlatform()

            // 如果 token 为空，直接返回原始请求
            if (token.isNullOrEmpty()) {
                return chain.proceed(request)
            }

            // 如果请求次数超过 5 次，则不再进行 token 刷新
            if (refreshRequestCount.get() >= 5) {
                KLog.i("***** Token 刷新次数已达到上限，返回原始响应 *****")
                return response
            }

            val responseBody = response.body?.string()
            if (responseBody != null) {
                try {
                    val jsonObject = JSONObject(responseBody)
                    val codeValue = jsonObject.optInt("code", -1)
                    val successValue = jsonObject.optBoolean("success", false)
                    if (codeValue == 14010 && !successValue) {
                        KLog.i("***** 账号登出错误 14010 重新请求token *****")
                        lock.lock()
                        if (token == getTokenForPlatform()) {
                            val newToken = refreshLiveToken() ?: return response
                            saveToken(newToken)

                            val authenticatedRequest = request.newBuilder()
                                .addHeader("Authorization", newToken)
                                .build()

                            // 使用新 Token 重试请求
                            KLog.i("***** 重新请求token *****")
                            return chain.proceed(authenticatedRequest)
                        }
                    }
                } finally {
                    lock.unlock()
                }
            } else {
                // 如果 responseBody 为空，处理错误
                KLog.i("##### Response body is null #####")
                return response
            }
        }

        return response
    }

    private fun getTokenForPlatform(): String? {
        val mPlatform = SPUtils.getInstance().getString(BtDomainUtil.KEY_PLATFORM)
        return when {
            TextUtils.equals(mPlatform, BtDomainUtil.PLATFORM_FBXC) -> {
                SPUtils.getInstance().getString(SPKeyGlobal.FBXC_TOKEN)
            }
            else -> {
                SPUtils.getInstance().getString(SPKeyGlobal.FB_TOKEN)
            }
        }
    }

    private fun isForwardApiRequest(url: String): Boolean {
        return url.contains("fb/forward?api") || url.contains("fbxc/forward?api")
    }

    private fun refreshLiveToken(): String? {
        refreshRequestCount.incrementAndGet()
        try {
            // 创建 API 服务实例
            val apiService = RetrofitClient.getInstance().create(ApiService::class.java)
            val fbApiService = FBRetrofitClient.getInstance().create(FBApiService::class.java)
            val httpDataSource: HttpDataSource = HttpDataSourceImpl.getInstance(fbApiService, apiService, false)
            val localDataSource: LocalDataSource = LocalDataSourceImpl.getInstance()

            // 根据平台选择调用的 API
            val tokenApi = if (getTokenForPlatform().equals(BtDomainUtil.PLATFORM_FBXC, ignoreCase = true)) {
                BetRepository.getInstance(httpDataSource, localDataSource).baseApiService.fbxcGameTokenApi
            } else {
                BetRepository.getInstance(httpDataSource, localDataSource).baseApiService.fbGameTokenApi
            }

            // 获取 token 响应
            val response = tokenApi.compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .blockingFirst() // 阻塞当前线程，等待刷新结果

            // 使用安全调用，避免空指针异常
            val token = (response as? BaseResponse<FBService>)?.data?.token ?: return null

            KLog.i("##### Response type: ${response::class.java.name} #####")

            return token
        } catch (e: Exception) {
            e.printStackTrace()
            KLog.e("Error refreshing live token: ${e.message}")
            return null // 异常时返回 null
        } finally {
            // 刷新完后可以重置请求次数
            refreshRequestCount.set(0)
        }
    }

    private fun saveToken(newToken: String) {
        val mPlatform = SPUtils.getInstance().getString(BtDomainUtil.KEY_PLATFORM)
        if (TextUtils.equals(mPlatform, BtDomainUtil.PLATFORM_FBXC)) {
            SPUtils.getInstance().put(SPKeyGlobal.FBXC_TOKEN, newToken)
        } else {
            SPUtils.getInstance().put(SPKeyGlobal.FB_TOKEN, newToken)
        }
    }
}
