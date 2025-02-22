package com.xtree.live.data.source.httpnew;

import static com.uber.autodispose.AutoDispose.autoDisposable;

import androidx.lifecycle.LifecycleOwner;

import com.uber.autodispose.AutoDisposeConverter;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

public class RxLifecycleUtils {
    private RxLifecycleUtils(){

    }

    public static <T> AutoDisposeConverter<T> bindLifecycle(LifecycleOwner lifecycleOwner){
        if(null == lifecycleOwner){
            throw new NullPointerException("lifecycle can not be null");
        }
        return autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner));
    }
}
