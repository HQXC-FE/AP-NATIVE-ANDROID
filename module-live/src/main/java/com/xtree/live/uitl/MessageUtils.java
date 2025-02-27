package com.xtree.live.uitl;

import android.text.TextUtils;

import com.xtree.base.utils.DomainUtil;

public class MessageUtils {

    public static String completeImagePath(String path){
        if(!TextUtils.isEmpty(path) && path.startsWith("content://")){
            return path;
        }
        return  completeUrl(path);
    }

    public static String completeUrl(String path){
        if (!TextUtils.isEmpty(path) && !path.startsWith("https") && !path.startsWith("http")) {
            path = DomainUtil.getApiUrl() + path;
        }
        return path;
    }

    public static String replaceDefaultUrl(String path){
        if (!TextUtils.isEmpty(path) && !path.startsWith("http") && !path.startsWith("https")) {
            return path.replace("default/", DomainUtil.getApiUrl());
        }
        return path;
    }

}
