package com.xtree.base.net;

import android.content.Context;
import android.os.Build;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;

public class HttpCallBackCacheManager {

    private static volatile HttpCallBackCacheManager instance;
    private final File cacheDir;

    // 私有构造函数
    private HttpCallBackCacheManager(Context context) {
        this.cacheDir = new File(context.getCacheDir(), "flowable_cache");
        if (!cacheDir.exists()) {
            cacheDir.mkdirs(); // 确保缓存目录存在
        }
    }

    /**
     * 初始化单例（建议在 Application 中调用一次）
     */
    public static void init(Context context) {
        if (instance == null) {
            synchronized (HttpCallBackCacheManager.class) {
                if (instance == null) {
                    instance = new HttpCallBackCacheManager(context.getApplicationContext());
                }
            }
        }
    }

    /**
     * 获取单例实例
     */
    public static HttpCallBackCacheManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("FileCacheManager is not initialized. Call init(context) first.");
        }
        return instance;
    }

    /**
     * 读取缓存数据
     */
    public <T> Flowable<T> getCache(String key, Type type) {
        return Flowable.create(emitter -> {
            File cacheFile = new File(cacheDir, key);
            if (cacheFile.exists()) {
                try {
                    String data;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        data = new String(java.nio.file.Files.readAllBytes(cacheFile.toPath()));
                    } else {
                        StringBuilder builder = new StringBuilder();
                        try (FileInputStream fis = new FileInputStream(cacheFile); InputStreamReader isr = new InputStreamReader(fis); BufferedReader br = new BufferedReader(isr)) {
                            String line;
                            while ((line = br.readLine()) != null) {
                                builder.append(line);
                            }
                        }
                        data = builder.toString();
                    }
                    T parsedData = new Gson().fromJson(data, type);
                    emitter.onNext(parsedData);
                } catch (Exception e) {
                    e.printStackTrace();
                    emitter.onError(e);
                }
            }
            emitter.onComplete();
        }, BackpressureStrategy.BUFFER);
    }

    public <T> void saveCache(String key, T data) {
        File cacheFile = new File(cacheDir, key);
        try {
            String jsonData = new Gson().toJson(data);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                java.nio.file.Files.write(cacheFile.toPath(), jsonData.getBytes());
            } else {
                try (FileOutputStream fos = new FileOutputStream(cacheFile)) {
                    fos.write(jsonData.getBytes());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

