package com.xtree.live.data.source.httpnew;

import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.xtree.base.utils.CfLog;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

public class DecryptResponseBodyConverter<T> implements Converter<ResponseBody, T> {

    private Gson gson;
    private TypeAdapter<T> adapter;

    public DecryptResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Nullable
    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = value.string();
        try {
            JSONObject jsonObject = JSON.parseObject(response);
            CfLog.d("===接口返回数据是：\n" + jsonObject.toJSONString());
            // TODO 如果返回的是加密的，请在这里解密
        } catch (Exception e) {
            CfLog.e("===接口返回 Exception：\n" + e.getMessage());
        }
        return adapter.fromJson(response);
    }

}
