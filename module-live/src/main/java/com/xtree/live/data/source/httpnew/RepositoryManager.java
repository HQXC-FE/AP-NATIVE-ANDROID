package com.xtree.live.data.source.httpnew;

import android.content.Context;
import android.util.Log;

import androidx.media3.common.C;

import com.xtree.base.BuildConfig;
import com.xtree.base.net.live.LiveHeaderInterceptor;
import com.xtree.base.utils.DomainUtil;
import com.xtree.live.data.source.http.TokenAuthenticator;

import java.util.concurrent.TimeUnit;

import me.xtree.mvvmhabit.http.interceptor.logging.Level;
import me.xtree.mvvmhabit.http.interceptor.logging.LoggingInterceptor;
import me.xtree.mvvmhabit.utils.Utils;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.internal.platform.Platform;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RepositoryManager {
    private static volatile RepositoryManager mInstance;
    private static final long mTimeout = 40;
    private Context mContext;
    private Retrofit mApiRetrofit;
    private Retrofit mStrRetrofit;
    private Retrofit mJsonRetrofit;
    private Retrofit mDomainCheckRetrofit;
    // 是否有统一的请求 入参

    public static RepositoryManager getInstance(){
        if(mInstance == null){
            synchronized (RepositoryManager.class){
                if(mInstance ==null){
                    try{
                        mInstance = new RepositoryManager(Utils.getContext());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
        return mInstance;
    }

    private RepositoryManager(Context context) throws Exception{
        this.mContext = context;
//        String  baseUrl = DomainUtil.getApiUrl();
        String baseUrl = "https://zhibo-apps.oxldkm.com";
        //baseurl  最好是加密
        initRequestManager(baseUrl);
        // 检测 切换URL 的逻辑 可以在这里设置新的 URL
        initDomainCheckUrlManager(baseUrl);
    }

    private void initRequestManager(String baseUrl) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(mTimeout, TimeUnit.SECONDS)
                .writeTimeout(mTimeout, TimeUnit.SECONDS)
                .readTimeout(mTimeout, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(8, mTimeout, TimeUnit.SECONDS));

        builder.addInterceptor(new TokenAuthenticator())
                .addInterceptor(new LiveHeaderInterceptor())
                .addInterceptor(new LoggingInterceptor
                        .Builder()//构建者模式
                        .loggable(BuildConfig.DEBUG) //是否开启日志打印
                        .setLevel(Level.BODY) //打印的等级
                        .log(Platform.INFO) // 打印类型
                        .request("Request") // request的Tag
                        .response("Response")// Response的Tag
                        .build());

        OkHttpClient okHttpClient = builder.build();

        //留作将来 域名加密，解密用
        mApiRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(CipherConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();

        mStrRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();

        mJsonRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(CipherConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();

    }

    private void initDomainCheckUrlManager(String baseUrl) {
        OkHttpClient okHttpClient= new OkHttpClient.Builder()
                .addInterceptor(new LoggingInterceptor
                        .Builder()//构建者模式
                        .loggable(BuildConfig.DEBUG) //是否开启日志打印
                        .setLevel(Level.BODY) //打印的等级
                        .log(Platform.INFO) // 打印类型
                        .request("Request") // request的Tag
                        .response("Response")// Response的Tag
                        .build())
                .connectTimeout(mTimeout, TimeUnit.SECONDS)
                .writeTimeout(mTimeout, TimeUnit.SECONDS)
                .readTimeout(mTimeout, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(8, mTimeout, TimeUnit.SECONDS))
                .build();
        mDomainCheckRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    public synchronized <T> T obtainDomainCheckRetrofitService(Class<T> service){
        return mDomainCheckRetrofit.create(service);
    }

    public synchronized <T> T obtainApiRetrofitService(Class<T> service){
        return mApiRetrofit.create(service);
    }

    public synchronized <T> T obtainStrRetrofitService(Class<T> service){
        return mStrRetrofit.create(service);
    }

    public synchronized <T> T obtainJsonRetrofitService(Class<T> service){
        return mJsonRetrofit.create(service);
    }

}
