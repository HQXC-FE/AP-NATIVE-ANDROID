package com.xtree.live.data.source.httpnew;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.xtree.base.utils.CfLog;

import java.io.IOException;

import retrofit2.Converter;

public class EncryptResponseBodyConverter<T> implements Converter<T, String> {
    private Gson gson;

    public EncryptResponseBodyConverter(Gson gson) {
        this.gson = gson;
    }

    @Nullable
    @Override
    public String convert(T value) throws IOException {
        String bodyStr = value==null?"":value.toString();
        //TODO 如果需要请求接口前加密，可以在这里加密
        CfLog.d("===接口请求数据是：\n"+bodyStr);
        return bodyStr;
    }
}
