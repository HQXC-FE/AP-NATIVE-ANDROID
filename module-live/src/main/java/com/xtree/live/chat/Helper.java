package com.xtree.live.chat;

import android.annotation.SuppressLint;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.hjq.gson.factory.GsonFactory;
import com.xtree.base.net.live.X9LiveInfo;
import com.xtree.base.utils.MD5Util;
import com.xtree.live.BuildConfig;
import com.xtree.live.LiveConfig;
import com.xtree.live.R;
import com.xtree.live.exception.ApiRequestException;
import com.xtree.live.exception.HttpException;
import com.xtree.live.exception.NetworkException;
import com.xtree.live.uitl.JsonUtil;
import com.xtree.live.uitl.WordUtil;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import me.xtree.mvvmhabit.http.cookie.CookieJarImpl;
import me.xtree.mvvmhabit.http.cookie.store.MemoryCookieStore;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;

public class Helper {
    //XT-LIVE 秘钥 ： RGx1YjzfdOW7pTM
    // 獲取特定客户端对应的key
    private static final String clientSpecialKey = "RGx1YjzfdOW7pTM";
    private static final TypeReference<Map<String, String>> type = new TypeReference<Map<String, String>>() {
    };

    private static volatile Call.Factory internalClient;
    public static Call.Factory factory() {
        if (internalClient == null) {
            synchronized (Helper.class) {
                if (internalClient == null) {
                    internalClient = (Call.Factory) okHttpClientBuilder(60).addInterceptor(getHttpLoggingInterceptor(HttpLoggingInterceptor.Level.NONE)).build();
                }
            }
        }
        return internalClient;
    }

    public static Request.Builder addExtraApiSignHttpHeaders(Request.Builder builder, Request request) throws IOException {
        String method = request.method();
        if("GET".equalsIgnoreCase(method)){
            Map<String, String> queryMap = new HashMap<>();
            for (String queryName : request.url().queryParameterNames()) {
                String value = request.url().queryParameter(queryName);
                queryMap.put(queryName, value == null ? "" :value);
            }
            addApiSign(builder, request, queryMap);
        }else if("POST".equalsIgnoreCase(method)){
            RequestBody requestBody = request.body();
            if(requestBody != null){
                MediaType contentType = requestBody.contentType();
                String contentTypeStr = contentType != null ? contentType.toString() : "";
                Log.d("Helper", "contentType: " + contentTypeStr);
                if(contentTypeStr.contains("application/json")){
                    Buffer buffer = new Buffer();
                    requestBody.writeTo(buffer);
                    String bodyStr = buffer.readString(contentTypeCharSet(contentType));
                    Log.d("Helper", "bodyStr: " + bodyStr);
                    Map<String, String> map = JsonUtil.fromJson(GsonFactory.getSingletonGson(), bodyStr, new TypeToken<Map<String, String>>(){}.getType());
                    if(map == null) map = new HashMap<>();
                    addApiSign(builder, request, map);
                }if(contentTypeStr.contains("application/x-www-form-urlencoded") || contentTypeStr.isEmpty()){
                    Buffer buffer = new Buffer();
                    requestBody.writeTo(buffer);
                    String bodyStr = buffer.readString(contentTypeCharSet(contentType));
                    Map<String, String> map = new HashMap<>();
                    bodyStr = URLDecoder.decode(bodyStr);
                    Log.d("Helper", "bodyStr: " + bodyStr);
                    String[] fieldArray = bodyStr.split("&");
                    for (String s : fieldArray) {
                        String[] keyValue = s.split("=");
                        if (keyValue.length == 2) {
                            String key = keyValue[0] == null ? "" : keyValue[0];
                            String value = keyValue[1] == null ? "" : keyValue[1];
                            map.put(key, value);
                        }
                    }
                    addApiSign(builder, request, map);
                }

            }
        }
        return builder;
    }

    private static void addApiSign(Request.Builder builder, Request request, Map<String, String> queryMap) throws IOException {
        String time = String.valueOf((System.currentTimeMillis() / 1000));
        String version = formartVersion();

        String reqStr ;

        if (formatParams(request).endsWith("&&")){
            reqStr =   formatParams(request).substring( 0 ,formatParams(request).length()-1);
        }else {
            reqStr = formatParams(request);
        }

        String uriPathRevKey = getUriPathRevKey(request);

        String signStr = reqStr + "key=" + clientSpecialKey + "/" + uriPathRevKey + "/"
                + time + "/" + X9LiveInfo.INSTANCE.getVisitor() + "/" + X9LiveInfo.INSTANCE.getOaid() + "/" + version;
        String xLiveSign = MD5Util.generateMd5(signStr);

        builder.removeHeader("Xiao9-Time");
        builder.removeHeader("Xiao9-Sign");
        builder.addHeader("x-live-Time", time);
        builder.addHeader("x-live-Sign", xLiveSign);

    }

    private static Charset contentTypeCharSet(@Nullable MediaType contentType) {
        if(contentType == null)return  StandardCharsets.UTF_8;
        Charset charset = contentType.charset(StandardCharsets.UTF_8);
        if(charset == null)return  StandardCharsets.UTF_8;
        return charset;
    }

    public static Request.Builder addExtraHttpHeaders(Request.Builder builder){
        Map<String, String> map = getHeaders();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            builder.addHeader(entry.getKey(), entry.getValue());
        }
        return builder;
    }

    public static URLConnection addExtraHttpHeaders(URLConnection connection){
        Map<String, String> map = getHeaders();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            connection.setRequestProperty(entry.getKey(), entry.getValue());
        }
        return connection;
    }

    public static OkHttpClient.Builder okHttpClientBuilder(int timeout){
        PairSSL pairSSL = null;
        try {
            pairSSL = getPairSSL();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {

        }
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .followRedirects(true)
                .followSslRedirects(true)
                .retryOnConnectionFailure(true)
                .callTimeout(timeout, TimeUnit.SECONDS)
                .cookieJar(new CookieJarImpl(new MemoryCookieStore()));
        if(pairSSL != null){
            builder.sslSocketFactory(pairSSL.sslContext.getSocketFactory(), pairSSL.trustManager);
        }
        return builder;
    }

    @NonNull
    static HttpLoggingInterceptor getHttpLoggingInterceptor(HttpLoggingInterceptor.Level level) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(Helper::formatMsg);
        interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        if (BuildConfig.DEBUG) {
            interceptor.setLevel(level);
        }
        return interceptor;
    }

    @NonNull
    public static PairSSL getPairSSL() throws NoSuchAlgorithmException, KeyManagementException {
        @SuppressLint("CustomX509TrustManager")
        X509TrustManager trustManager =  getX509TrustManager();
        TrustManager[] tm = {trustManager};
        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, tm, null);
        return new PairSSL(trustManager, sslContext);
    }

    @NonNull
    private static X509TrustManager getX509TrustManager() {
        @SuppressLint("CustomX509TrustManager")
        X509TrustManager trustManager = new X509TrustManager() {
            @SuppressLint("TrustAllX509TrustManager")
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {

            }

            @SuppressLint("TrustAllX509TrustManager")
            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {

            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
        return trustManager;
    }


    private static void formatMsg(String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }

        if (msg.contains("-->") && msg.contains("=")) {

            return;
        }
        if (msg.contains("<--") && msg.contains("https://")) {

            return;
        }
        if (isJsonThenPrint(msg)) {
            return;
        }

        Log.d("Server Result ", msg);
    }

    private static boolean isJsonThenPrint(String json) {
        TypeAdapter<JsonElement> strictAdapter = GsonFactory.getSingletonGson().getAdapter(JsonElement.class);
        try {
            JsonElement jsonElement =  strictAdapter.fromJson(json);
            if(jsonElement instanceof JsonObject){
                ((JsonObject)jsonElement).remove("z_api_doc");
                ((JsonObject)jsonElement).remove("z_debug");

            }
        } catch (JsonSyntaxException | IOException e) {
            return false;
        }
        return true;
    }

    public static class PairSSL{
        private final X509TrustManager trustManager;
        private final SSLContext sslContext;

        public PairSSL(X509TrustManager trustManager, SSLContext sslContext) {
            this.trustManager = trustManager;
            this.sslContext = sslContext;
        }

        public X509TrustManager getTrustManager() {
            return trustManager;
        }

        public SSLContext getSslContext() {
            return sslContext;
        }
    }

    public static @NonNull Pair<Integer, String> errorMessage(Throwable e) {
        if (BuildConfig.DEBUG)e.printStackTrace();
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            String msg = httpException.message();
            int code = httpException.code();
            try {
                JsonObject json = GsonFactory.getSingletonGson().fromJson(msg, JsonObject.class);
                if(json != null)msg = JsonUtil.getString(json, "msg");
            } catch (JsonSyntaxException ex) {
                if(!TextUtils.isEmpty(msg)){
                    if(msg.startsWith("<html>") || msg.endsWith("<html>"))msg = "";
                }
            }

            if (TextUtils.isEmpty(msg)) {
                msg = "HTTP " + httpException.code();
            }

            if (TextUtils.isEmpty(msg)) {

                if (code == 500) {
                    return new Pair<>(code, "内部服务器错误，请稍后再试");
                }
                if (code == 504) {
                    return new Pair<>(504, "网关超时，请稍后再试");
                }
                if (code == 404) {
                    return new Pair<>(404, "无响应服务，请稍后再试");
                }
                if (code == 502) {
                    return new Pair<>(502, "无效网关，请稍后再试");
                }
            }
            return new Pair<>(code, msg);
        } else if (e instanceof ApiRequestException) {
            if (e.getCause() instanceof SocketTimeoutException) {
                return new Pair<>(1, "请求超时");
            } else {
                return new Pair<>(1, "网络请求失败");
            }
        } else if (e instanceof JsonSyntaxException) {
            return new Pair<>(1, "请求超时");
        }else if (e instanceof NetworkException) {
            return new Pair<>(1, "网络连接已断开");
        } else {
            return new Pair<>(1, "出错了");
        }
    }

    @NonNull
    public static Map<String, String> getHeaders() {
        String version = formartVersion();
        Map<String, String> headers = new HashMap<>();
        headers.put("x-live-Visitor", X9LiveInfo.INSTANCE.getUid()+"");
        String appToken = LiveConfig.getLiveToken();
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

    private static String formatParams(Request request) throws IOException {

        String reqStr = "";
        String method = request.method();

        if (method.equals("GET")) {
            // URL 中的查询参数
            String query = request.url().query();
            if (query != null) {
                // 将查询字符串按 '&' 分割
                String[] pairs = query.split("&");

                // 对键值对进行排序
                Arrays.sort(pairs);

                // 使用 StringBuilder 构建排序后的结果字符串
                StringBuilder sortedQueryString = new StringBuilder();
                for (int i = 0; i < pairs.length; i++) {
                    if (i > 0) {
                        sortedQueryString.append("&");
                    }
                    sortedQueryString.append(pairs[i]);
                }

                reqStr = sortedQueryString.toString();
            }
        }
        // 处理 POST 请求参数
        else if (method.equals("POST")) {
            RequestBody requestBody = request.body();
            if (requestBody instanceof FormBody) {
                FormBody formBody = (FormBody) requestBody;
                StringBuilder params = new StringBuilder();
                for (int i = 0; i < formBody.size(); i++) {
                    params.append(formBody.name(i)).append("=").append(formBody.value(i)).append("&");
                }
                reqStr = params.toString();
            } else if (requestBody != null) {
                Buffer buffer = new Buffer();
                requestBody.writeTo(buffer);
                String json = buffer.readUtf8();
                Map<String, String> params = JSON.parseObject(json, type);
                if (params == null || params.isEmpty()) {
                    return "";
                }

                // 獲取key并按字母由小到大排序
                List<String> sortKeys = new ArrayList<>(params.keySet());
                Collections.sort(sortKeys);

                // API所需GET或POST所有参数由小到大排序，并以key=value方式"&"符号连接
                StringBuilder reqStrBuilder = new StringBuilder();
                for (String key : sortKeys) {
                    reqStrBuilder.append(key).append("=").append(params.get(key)).append("&");
                }
                reqStr = reqStrBuilder.toString();
            }
        }
        if (!reqStr.isEmpty()) {
            return reqStr + "&";
        }
        return reqStr;
    }

    private static String getUriPathRevKey(Request request) {
        String[] segments = request.url().encodedPath().split("/");
        StringBuilder uriPathRevKey = new StringBuilder();
        for (String segment : segments) {
            if (!segment.isEmpty()) {
                uriPathRevKey.append(segment.charAt(segment.length() - 1));
            }
        }
        uriPathRevKey.reverse();
        return uriPathRevKey.toString();
    }

    @NonNull
    public static String oaid() {
        return X9LiveInfo.INSTANCE.getOaid();
    }

}
