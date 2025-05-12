package com.xtree.base.net;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.DomainUtil;
import com.xtree.base.utils.HttpGsonConverterFactory;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.sentry.Sentry;
import com.xtree.base.http.cookie.CookieJarImpl;
import com.xtree.base.http.cookie.store.PersistentCookieStore;
import com.xtree.base.http.interceptor.CacheInterceptor;
import com.xtree.base.utils.KLog;
import com.xtree.base.utils.Utils;
import okhttp3.Cache;
import okhttp3.ConnectionPool;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Created by goldze on 2017/5/10.
 * RetrofitClient封装单例类, 实现网络请求
 */
public class RetrofitClient {
    //超时时间
    private static final int DEFAULT_TIMEOUT = 40;
    //缓存时间
    private static final int CACHE_TIMEOUT = 10 * 1024 * 1024;
    //服务端根路径
    public static String baseUrl = DomainUtil.getApiUrl();

    private static Context mContext = Utils.getContext();

    private static OkHttpClient okHttpClient;
    private static Retrofit retrofit;

    private Cache cache = null;
    private File httpCacheDirectory;

    private RetrofitClient() {
        this(baseUrl, null);
    }

    private RetrofitClient(String url, Map<String, String> headers) {

        if (TextUtils.isEmpty(url)) {
            url = baseUrl;
        }

        if (httpCacheDirectory == null) {
            httpCacheDirectory = new File(mContext.getCacheDir(), "app_cache");
        }

        try {
            if (cache == null) {
                cache = new Cache(httpCacheDirectory, CACHE_TIMEOUT);
            }
        } catch (Exception e) {
            KLog.e("Could not create http cache", e);
        }


        Interceptor tokenAuthenticator = null;
        try {
            // 获取类的引用
            Class<?> clazz = Class.forName("com.xtree.bet.data.TokenAuthenticator");

            // 创建类的实例（旧方法，Java 9 之后不推荐）
            Object obj = clazz.newInstance();

            // 强制转换为目标类型
            tokenAuthenticator = (Interceptor) obj;
        } catch (Exception e) {
            e.printStackTrace(); // 处理类异常
            Sentry.captureException(e);
            tokenAuthenticator = new Interceptor() {
                @NonNull
                @Override
                public Response intercept(@NonNull Chain chain) throws IOException {
                    // 转发请求
                    return chain.proceed(chain.request());
                }
            };
        }

        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory();
        okHttpClient = new OkHttpClient.Builder()
                .cookieJar(new CookieJarImpl(new PersistentCookieStore(mContext)))
//                .cache(cache)
                //.addInterceptor(new BaseInterceptor(headers))
                .addInterceptor(new HeaderInterceptor())
                .addInterceptor(new DecompressInterceptor())
                .addInterceptor(new CacheInterceptor(mContext))
                .addInterceptor(new UrlModifyingInterceptor())
                .addInterceptor(new ExceptionInterceptor())
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                //.addInterceptor(tokenAuthenticator)
                .addInterceptor(new HttpLoggingInterceptor(message -> KLog.d(message)).setLevel(HttpLoggingInterceptor.Level.BODY))
//                .addInterceptor(new LoggingInterceptor
//                        .Builder()//构建者模式
//                        .loggable(BuildConfig.DEBUG) //是否开启日志打印
//                        .setLevel(Level.BODY) //打印的等级
//                        .log(Platform.INFO) // 打印类型
//                        .request("Request") // request的Tag
//                        .response("Response")// Response的Tag
//                        .build())
                .dns(DnsFactory.getDns())
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(8, DEFAULT_TIMEOUT, TimeUnit.SECONDS))
                // 这里你可以根据自己的机型设置同时连接的个数和时间，我这里8个，和每个保持时间为10s
                .build();
        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(HttpGsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(url)
                .build();

    }

    public static RetrofitClient getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public static void init() {
        baseUrl = DomainUtil.getApiUrl();
        SingletonHolder.INSTANCE = new RetrofitClient();
        CfLog.i("OkHttpClient init");
    }

    public static void setApi(String url) {
        baseUrl = url;
        SingletonHolder.INSTANCE = new RetrofitClient();
        CfLog.e("OkHttpClient setApi");
    }

    /**
     * /**
     * execute your customer API
     * For example:
     * MyApiService service =
     * RetrofitClient.getInstance(MainActivity.this).create(MyApiService.class);
     * <p>
     * RetrofitClient.getInstance(MainActivity.this)
     * .execute(service.lgon("name", "password"), subscriber)
     * * @param subscriber
     */

    public static <T> T execute(Observable<T> observable, Observer<T> subscriber) {
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

        return null;
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    /**
     * create you ApiService
     * Create an implementation of the API endpoints defined by the {@code service} interface.
     */
    public <T> T create(final Class<T> service) {
        if (service == null) {
            throw new RuntimeException("Api service is null!");
        }
        return retrofit.create(service);
    }

    private static class SingletonHolder {
        private static RetrofitClient INSTANCE = new RetrofitClient();
    }
}
