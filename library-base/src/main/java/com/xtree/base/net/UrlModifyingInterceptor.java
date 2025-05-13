package com.xtree.base.net;

import androidx.annotation.NonNull;

import com.xtree.base.utils.DomainUtil;

import java.io.IOException;
import java.net.URL;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class UrlModifyingInterceptor implements Interceptor {
    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request originalRequest = chain.request();
        HttpUrl originalUrl = originalRequest.url();

        // 获取目标主机信息
        String baseUrl = DomainUtil.getApiUrl();
        String scheme = baseUrl.startsWith("http:") ? "http" : "https";
        URL url = new URL(baseUrl);
        String newHost = url.getHost();
        int newPort = url.getPort() > 0 ? url.getPort() : ("http".equals(scheme) ? 80 : 443);

        // 如果 host、port、scheme 都一样就不修改
        if (originalUrl.scheme().equals(scheme) && originalUrl.host().equals(newHost) && originalUrl.port() == newPort) {
            return chain.proceed(originalRequest);
        }

        // 构建新的 URL
        HttpUrl modifiedUrl = originalUrl.newBuilder().scheme(scheme).host(newHost).port(newPort).build();

        // 构建新的请求
        Request newRequest = originalRequest.newBuilder().url(modifiedUrl).build();

        return chain.proceed(newRequest);
    }
}

