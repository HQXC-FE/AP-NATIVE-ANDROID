package com.xtree.live.socket;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.xtree.base.net.live.X9LiveInfo;
import com.xtree.live.BuildConfig;
import com.xtree.live.data.source.http.HttpDataSourceImpl;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ChatClientFactory {
    /**
     * @param clientType 0 socket 1 mqtt
     * @return
     */
    static ChatClient createClient(int clientType, String scheme, String host, String token, ChatWebSocketHandler handler) throws Exception {
        String url = scheme + "://" + host + "/wss/?userToken=" + token;
        if (BuildConfig.DEBUG) Log.d("ChatWebSocketManager", url);
        return new ChatWebSocketClient(URI.create(url), getHeaders(), handler);
//        }
    }


    @NonNull
    public static Map<String, String> getHeaders() {
        String version = formartVersion();
        Map<String, String> headers = new HashMap<>();
        headers.put("x-live-Visitor", X9LiveInfo.INSTANCE.getUid()+"");
        String appToken = X9LiveInfo.INSTANCE.getToken();
        if(!TextUtils.isEmpty(appToken)){
            headers.put("x-live-Token", appToken); // 登录的token
        }
        headers.put("x-live-Oaid", X9LiveInfo.INSTANCE.getOaid());
        headers.put("x-live-Version", version);
        headers.put("x-live-Brand", Build.BRAND); // 手机品牌
        headers.put("x-live-Model", Build.MODEL); // 手机型号
        headers.put("x-live-Channel", X9LiveInfo.INSTANCE.getChannel()); // 渠道号
        headers.put("x-live-Version-OS", "Android_" + Build.VERSION.RELEASE); // 手机版本

        return headers;
    }
    private static String formartVersion() {
        // 获取当前日期
        Date currentDate = new Date();
        // 定义日期格式
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        // 格式化日期
        return "android-" + dateFormat.format(currentDate);
    }

}
