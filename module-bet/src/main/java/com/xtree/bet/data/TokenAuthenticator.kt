package com.xtree.bet.data

import android.text.TextUtils
import com.google.gson.Gson
import com.xtree.base.global.SPKeyGlobal
import com.xtree.base.net.FBRetrofitClient
import com.xtree.base.net.RetrofitClient
import com.xtree.base.utils.BtDomainUtil
import com.xtree.base.vo.FBService
import com.xtree.bet.bean.response.fb.FbMatchListCacheRsp
import com.xtree.bet.bean.response.fb.FbStatisticalInfoCacheRsp
import com.xtree.bet.bean.response.fb.StatisticalInfo
import com.xtree.bet.data.source.HttpDataSource
import com.xtree.bet.data.source.LocalDataSource
import com.xtree.bet.data.source.http.HttpDataSourceImpl
import com.xtree.bet.data.source.local.LocalDataSourceImpl
import me.xtree.mvvmhabit.http.BaseResponse
import me.xtree.mvvmhabit.utils.RxUtils
import me.xtree.mvvmhabit.utils.SPUtils
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.locks.ReentrantLock

class TokenAuthenticator : Interceptor {

    private val lock = ReentrantLock()
    private val mPlatform = ""

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val mPlatform = SPUtils.getInstance().getString(BtDomainUtil.KEY_PLATFORM)
        var token = ""
        if (TextUtils.equals(mPlatform, BtDomainUtil.PLATFORM_FBXC)) {
            token = SPUtils.getInstance().getString(SPKeyGlobal.FBXC_TOKEN)
        } else {
            token = SPUtils.getInstance().getString(SPKeyGlobal.FB_TOKEN)
        }
        // 添加 Authorization Header

        val authenticatedRequest = if (!token.isNullOrEmpty()) {
            //request.newBuilder().header("x-live-Token", token).build()
            request
        } else {
            request
        }

        val response = chain.proceed(authenticatedRequest)

        if(!request.url.toString().contains("fb/forward?api")){
            return response
        }
        System.out.println("================= TokenAuthenticator request.url.toString() ==================="+request.url.toString())
        // 解析响应体为 BaseResponse<StatisticalInfo>
        val responseBody = response.body?.string()  // 获取响应体内容
        System.out.println("================= TokenAuthenticator responseBody ==================="+responseBody)

        System.out.println("=============== TokenAuthenticator code ====================="+responseBody.toString().contains("14010"))
        // 处理特定状态码
        when (response.code) {
            401, 423 -> {
                // 确保刷新 Token 的操作是线程安全的
                lock.lock()
                try {
                    val currentToken = token

                    // 检查 Token 是否已被其他线程刷新
                    if (currentToken == token) {
                        // 执行 Token 刷新操作
                        val newToken = refreshLiveToken()
                        if (!newToken.isNullOrEmpty()) {
                            token = newToken
                            System.out.println("================== token ==================" + token)
                            if (TextUtils.equals(mPlatform, BtDomainUtil.PLATFORM_FBXC)) {
                                SPUtils.getInstance().put(SPKeyGlobal.FBXC_TOKEN, token)
                            } else {
                                SPUtils.getInstance().put(SPKeyGlobal.FB_TOKEN, token)
                            }
                            // 使用新 Token 重试请求
                            return chain.proceed(
                                request.newBuilder().header("x-live-Token", newToken).build()
                            )
                        } else {
                            // 如果刷新失败，直接返回原响应
                            return response
                        }
                    }
                } finally {
                    lock.unlock()
                }
            }

            403 -> handleForbidden() // 处理 403
            500 -> handleServerError() // 处理 500
        }

        if(responseBody.toString().contains("14010")){
            lock.lock()
            try {
                System.out.println("=============== TokenAuthenticator 重新请求Token =====================")
                val currentToken = token

                // 检查 Token 是否已被其他线程刷新
                if (currentToken == token) {
                    // 执行 Token 刷新操作
                    val newToken = refreshLiveToken()
                    if (!newToken.isNullOrEmpty()) {
                        token = newToken
                        System.out.println("================== 新 token ==================" + token)
                        if (TextUtils.equals(mPlatform, BtDomainUtil.PLATFORM_FBXC)) {
                            SPUtils.getInstance().put(SPKeyGlobal.FBXC_TOKEN, token)
                        } else {
                            SPUtils.getInstance().put(SPKeyGlobal.FB_TOKEN, token)
                        }
                        // 使用新 Token 重试请求
                        return chain.proceed(
                            request.newBuilder().header("x-live-Token", newToken).build()
                        )
                    } else {
                        // 如果刷新失败，直接返回原响应
                        return response
                    }
                }
            } finally {
                lock.unlock()
            }
        }

        return response
    }

    private fun refreshLiveToken(): String? {
        return try {
            System.out.println("================== refreshLiveToken  ==================")
            //网络API服务
            val apiService = RetrofitClient.getInstance().create(
                ApiService::class.java
            )
            //网络API服务
            val fbApiService = FBRetrofitClient.getInstance().create(FBApiService::class.java)
            //网络数据源
            val httpDataSource: HttpDataSource =
                HttpDataSourceImpl.getInstance(fbApiService, apiService, false)

            //本地数据源
            val localDataSource: LocalDataSource = LocalDataSourceImpl.getInstance()
            // 使用同步方式刷新 Token
            val response = BetRepository.getInstance(httpDataSource, localDataSource)
                .baseApiService.fbGameZeroTokenApi.compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer()).blockingFirst() // 阻塞当前线程，等待刷新结果
            System.out.println("================== refreshLiveToken  response =================="+response)
            (response as? BaseResponse<FBService>)?.data?.let {
                it.token
            } ?: run { null }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
        return ""
    }



    private fun handleForbidden() {
        // 自定义逻辑，比如跳转到错误页或提示对话框
        println("403 Forbidden: 权限不足")
    }

    private fun handleServerError() {
        // 自定义逻辑，比如提示用户重试
        println("500 Server Error: 服务端异常")
    }
}