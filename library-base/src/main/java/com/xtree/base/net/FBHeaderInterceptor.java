package com.xtree.base.net;

import static com.xtree.base.utils.BtDomainUtil.PLATFORM_FBXC;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.utils.StringUtils;
import com.xtree.base.utils.TagUtils;

import java.io.IOException;

import com.xtree.base.utils.SPUtils;
import com.xtree.base.utils.Utils;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class FBHeaderInterceptor implements Interceptor {

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request.Builder builder = chain.request()
                .newBuilder();
        String platform = SPUtils.getInstance().getString("KEY_PLATFORM");
        String token;

        if(TextUtils.equals(platform, PLATFORM_FBXC)) {
            token = SPUtils.getInstance().getString(SPKeyGlobal.FBXC_TOKEN);
        } else {
            token = SPUtils.getInstance().getString(SPKeyGlobal.FB_TOKEN);
        }

        builder.addHeader("Content-Type", "application/json; charset=utf-8");
        if (!TextUtils.isEmpty(token)) {
            builder.removeHeader("Authorization");
            builder.addHeader("Authorization", token);
        }
        builder.addHeader("App-RNID", "87jumkljo"); //
        builder.addHeader("Source", "9");
        builder.addHeader("UUID", TagUtils.getDeviceId(Utils.getContext()));
        builder.addHeader("app-version", StringUtils.getVersionName(Utils.getContext()));
        //请求信息
        return chain.proceed(builder.build());
    }
}
