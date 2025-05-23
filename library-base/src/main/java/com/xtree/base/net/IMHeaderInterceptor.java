package com.xtree.base.net;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.utils.StringUtils;

import java.io.IOException;

import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.Utils;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

public class IMHeaderInterceptor implements Interceptor {

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request original = chain.request();
        Request.Builder builder = original.newBuilder();

        String token = SPUtils.getInstance().getString(SPKeyGlobal.USER_TOKEN);
        if (!TextUtils.isEmpty(token)) {
            builder.removeHeader("authorization");
            builder.addHeader("authorization", "bearer " + token);
        }
        builder.addHeader("App-RNID", "87jumkljo"); //
        builder.addHeader("lang", "zh"); //
        builder.addHeader("requestId", token);
        builder.addHeader("Source", "9");
        builder.addHeader("app-version", StringUtils.getVersionName(Utils.getContext()));
        // 如果是 POST 请求并有 body
        if ("POST".equalsIgnoreCase(original.method()) && original.body() != null) {
            MediaType mediaType = MediaType.parse("application/vnd.sc-api.v1.json");

            Buffer buffer = new Buffer();
            original.body().writeTo(buffer);
            byte[] bytes = buffer.readByteArray();

            RequestBody newBody = RequestBody.create(bytes, mediaType);

            builder.method(original.method(), newBody);
        }
        return chain.proceed(builder.build());
    }
}
