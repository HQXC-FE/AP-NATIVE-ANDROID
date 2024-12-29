package com.xtree.bet.data

import android.text.TextUtils
import com.google.gson.Gson
import com.xtree.base.global.SPKeyGlobal
import com.xtree.base.net.FBRetrofitClient
import com.xtree.base.net.RetrofitClient
import com.xtree.base.utils.BtDomainUtil
import com.xtree.base.vo.FBService
import com.xtree.bet.data.source.HttpDataSource
import com.xtree.bet.data.source.LocalDataSource
import com.xtree.bet.data.source.http.HttpDataSourceImpl
import com.xtree.bet.data.source.local.LocalDataSourceImpl
import me.xtree.mvvmhabit.http.BaseResponse
import me.xtree.mvvmhabit.utils.KLog
import me.xtree.mvvmhabit.utils.RxUtils
import me.xtree.mvvmhabit.utils.SPUtils
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONObject
import java.util.concurrent.locks.ReentrantLock

class TokenAuthenticator : Interceptor {

    private val lock = ReentrantLock()

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        // 获取 Token
        val token = getTokenForPlatform()

        // 如果 token 为空，直接返回原始请求
        if (token.isNullOrEmpty()) {
            return chain.proceed(request)
        }

        // 添加 Authorization Header
//        val authenticatedRequest = request.newBuilder()
//            .header("Authorization", token)
//            .build()

        val response = chain.proceed(request)

        // 处理特殊的 API 请求
        if (isForwardApiRequest(request.url.toString())) {
            val responseBody = response.body?.string()
            val jsonObject = JSONObject(responseBody)
            val codeValue = jsonObject.optInt("code")
            val successValue = jsonObject.optBoolean("success")

            if (codeValue == 14010 && !successValue) {
                System.out.println("============ Token 14010 Error===============")
                KLog.i("***** 账号登出错误 14010 重新请求token *****" )
                lock.lock()
                try {
                    if (token == getTokenForPlatform()) {
                        val newToken = refreshLiveToken() ?: return response
                        saveToken(newToken)

                        val authenticatedRequest = request.newBuilder()
                            .addHeader("Authorization", newToken)
                            .build()

                        // 使用新 Token 重试请求
                        KLog.i("***** 重新请求token *****" )
                        return chain.proceed(authenticatedRequest)
                    }
                } finally {
                    lock.unlock()
                }
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
        System.out.println("============ Token 14010 refreshLiveToken ===============")
        return try {
            val apiService = RetrofitClient.getInstance().create(ApiService::class.java)
            val fbApiService = FBRetrofitClient.getInstance().create(FBApiService::class.java)
            val httpDataSource: HttpDataSource = HttpDataSourceImpl.getInstance(fbApiService, apiService, false)
            val localDataSource: LocalDataSource = LocalDataSourceImpl.getInstance()
            if(getTokenForPlatform().equals(BtDomainUtil.PLATFORM_FBXC)){
                val response = BetRepository.getInstance(httpDataSource, localDataSource)
                    .baseApiService.fbxcGameZeroTokenApi
                    .compose(RxUtils.schedulersTransformer())
                    .compose(RxUtils.exceptionTransformer())
                    .blockingFirst() // 阻塞当前线程，等待刷新结果
                (response as? BaseResponse<FBService>)?.data?.token
            }else{
                val response = BetRepository.getInstance(httpDataSource, localDataSource)
                    .baseApiService.fbGameZeroTokenApi
                    .compose(RxUtils.schedulersTransformer())
                    .compose(RxUtils.exceptionTransformer())
                    .blockingFirst() // 阻塞当前线程，等待刷新结果
                (response as? BaseResponse<FBService>)?.data?.token
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
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

    private fun buildFormBody(params: Map<String, String>): RequestBody {
        val formBodyBuilder = FormBody.Builder()
        for ((key, value) in params) {
            formBodyBuilder.add(key, value)
        }
        return formBodyBuilder.build()
    }
}
