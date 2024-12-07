package com.xtree.base.net;

import android.os.Build;
import android.text.TextUtils;

import com.xtree.base.BuildConfig;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.net.fastest.FastestTopDomainUtil;
import com.xtree.base.utils.AppUtil;
import com.xtree.base.utils.HmacSHA256Utils;
import com.xtree.base.utils.StringUtils;
import com.xtree.base.utils.TagUtils;
import com.xtree.base.utils.UuidUtil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import me.xtree.mvvmhabit.base.BaseApplication;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.Utils;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by goldze on 2017/5/10.
 */
public class HeaderInterceptor implements Interceptor {
    public HeaderInterceptor() {

    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request()
                .newBuilder();
        String token = SPUtils.getInstance().getString(SPKeyGlobal.USER_TOKEN);
        if (!TextUtils.isEmpty(token)) {
            builder.addHeader("Authorization", "bearer " + SPUtils.getInstance().getString(SPKeyGlobal.USER_TOKEN));
            builder.addHeader("Cookie", "auth=" + SPUtils.getInstance().getString(SPKeyGlobal.USER_TOKEN) + ";" +
                    SPUtils.getInstance().getString(SPKeyGlobal.USER_SHARE_COOKIE_NAME) + "=" + SPUtils.getInstance().getString(SPKeyGlobal.USER_SHARE_SESSID) + ";");
        }

        builder.addHeader("Content-Type", "application/vnd.sc-api.v1.json");
        builder.addHeader("App-RNID", "87jumkljo"); //
        builder.addHeader("Source", "9");
        builder.addHeader("app-version", StringUtils.getVersionName(Utils.getContext()));
        builder.addHeader("UUID", TagUtils.getDeviceId(Utils.getContext()));
        builder.addHeader("X-Crypto", BuildConfig.DEBUG ? "no" : "yes");
        addUserAgentHeader(chain, builder);

        addSignHeader(chain, builder);

        //请求信息
        return chain.proceed(builder.build());
    }

    /**
     * 设置鉴权请求头
     */
    private void addSignHeader(Chain chain, Request.Builder builder) {
        HttpUrl fullUrl = chain.request().url();

        long sign1TsTime = System.currentTimeMillis() / 1000;

        //根据测速时本地到远端的时间差判断用户本机时间是否有偏差
        if (FastestTopDomainUtil.Companion.getFastestDomain().getValue() != null) {
            long curCTSSec = FastestTopDomainUtil.Companion.getFastestDomain().getValue().curCTSSec;
            //偏差超过20s则同步偏差
            if (Math.abs(curCTSSec) > 20) {
                sign1TsTime += curCTSSec;
            }
        }
        String sign1Ts = sign1TsTime + "," + UuidUtil.getID24();

        String query = fullUrl.encodedQuery();
        String path = fullUrl.encodedPath();
        StringBuilder encodeData = new StringBuilder();
        encodeData.append(sign1Ts).append("\n").append(path);
        if (!TextUtils.isEmpty(query)) {
            encodeData.append("?").append(query);
        }

        //对签名数据反编码
        String decode = encodeData.toString();
        try {
            decode = URLDecoder.decode(encodeData.toString(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String sign1 = HmacSHA256Utils.calculateHmacSHA256(HmacSHA256Utils.HMAC_KEY, decode);

        //加密签名
        builder.addHeader("X-Sign1", sign1);
        builder.addHeader("X-Sign1-Ts", sign1Ts);
    }

    /**
     * 添加useragent请求头
     */
    private void addUserAgentHeader(Chain chain, Request.Builder builder) {
        StringBuilder userAgentString = new StringBuilder();
        userAgentString
                .append("Android");

        if (BaseApplication.getInstance() != null) {
            userAgentString
                    .append(",")
                    .append("Version:")
                    .append(AppUtil.getAppVersion(BaseApplication.getInstance().getApplicationContext()));
        }

        userAgentString
                .append(",")
                .append("Device:")
                .append(Build.BRAND + "," + Build.MODEL + "," + Build.VERSION.SDK_INT);

        builder.removeHeader("User-Agent").addHeader("User-Agent", userAgentString.toString());
    }

}