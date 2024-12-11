package com.xtree.live.data.source.http

import com.xtree.base.net.live.X9LiveInfo
import com.xtree.live.data.LiveRepository
import com.xtree.live.data.source.request.LiveTokenRequest
import com.xtree.live.data.source.response.LiveTokenResponse
import me.xtree.mvvmhabit.http.BaseResponse
import me.xtree.mvvmhabit.utils.RxUtils
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.locks.ReentrantLock

class TokenAuthenticator() : Interceptor {

    private val lock = ReentrantLock()

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        // 添加 Authorization Header
        val token = X9LiveInfo.token
        val authenticatedRequest = if (!token.isNullOrEmpty()) {
            request.newBuilder()
                .header("x-live-Token", token)
                .build()
        } else {
            request
        }

        val response = chain.proceed(authenticatedRequest)

        // 处理特定状态码
        when (response.code) {
            401, 423 -> {
                // 确保刷新 Token 的操作是线程安全的
                lock.lock()
                try {
                    val currentToken = X9LiveInfo.token

                    // 检查 Token 是否已被其他线程刷新
                    if (currentToken == token) {
                        // 执行 Token 刷新操作
                        val newToken = refreshLiveToken()
                        if (!newToken.isNullOrEmpty()) {
                            X9LiveInfo.token = newToken

                            // 使用新 Token 重试请求
                            return chain.proceed(
                                request.newBuilder()
                                    .header("x-live-Token", newToken)
                                    .build()
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

        return response
    }

    private fun refreshLiveToken(): String? {
        return try {
            // 使用同步方式刷新 Token
            val response = LiveRepository.getInstance()
                .getLiveToken(LiveTokenRequest())
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .blockingFirst() // 阻塞当前线程，等待刷新结果
            (response as? BaseResponse<LiveTokenResponse>)?.data?.let {
                LiveRepository.getInstance().setLive(it)
                it.xLiveToken
            } ?: run { null }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
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