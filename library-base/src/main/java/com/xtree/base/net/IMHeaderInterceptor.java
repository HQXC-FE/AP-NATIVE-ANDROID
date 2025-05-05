package com.xtree.base.net;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.utils.StringUtils;

import java.io.IOException;

import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.Utils;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class IMHeaderInterceptor implements Interceptor {

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request.Builder builder = chain.request()
                .newBuilder();

        String token = SPUtils.getInstance().getString(SPKeyGlobal.USER_TOKEN);
        if (!TextUtils.isEmpty(token)) {
            builder.removeHeader("authorization");
            builder.addHeader("authorization", "bearer " + token);
        }
        builder.addHeader("content-type", "application/vnd.sc-api.v1.json");
        builder.addHeader("App-RNID", "87jumkljo"); //
        builder.addHeader("lang", "zh"); //
        builder.addHeader("requestId", token);
        builder.addHeader("Source", "9");
        builder.addHeader("app-version", StringUtils.getVersionName(Utils.getContext()));
        //请求信息
        return chain.proceed(builder.build());
    }
}
