package com.xtree.live.data.source.httpnew;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class CipherConverterFactory extends Converter.Factory {
    private final Gson gson;

    public static CipherConverterFactory create() {
        return create(new GsonBuilder().create());
    }

    public static CipherConverterFactory create(Gson gson) {
        return new CipherConverterFactory(gson);
    }

    private CipherConverterFactory(Gson gson) {
        if (gson == null) throw new NullPointerException("gson == null");
        this.gson = gson;
    }

    @Nullable
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new DecryptResponseBodyConverter<>(gson, adapter);
    }

    @Nullable
    @Override
    public Converter<?, String> stringConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new EncryptResponseBodyConverter<>(gson);
    }
}
