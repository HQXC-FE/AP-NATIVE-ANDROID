package com.xtree.base.utils;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import me.xtree.mvvmhabit.utils.SPUtils;

public class BtDomainUtil {
    public final static String KEY_PLATFORM = "KEY_PLATFORM";
    public final static String KEY_PLATFORM_NAME = "KEY_PLATFORM_NAME";
    public final static String PLATFORM_FBXC = "fbxc";
    public final static String PLATFORM_FB = "fb";
    public final static String PLATFORM_PM = "obg";
    public final static String PLATFORM_PMXC = "obgzy";
    public final static String PLATFORM_IM = "im";
    private static List<String> domainUrl = new ArrayList<>();
    private static List<String> fbDomainUrl = new ArrayList<>();
    private static List<String> fbxcDomainUrl = new ArrayList<>();
    private static String defaultFbDomainUrl;
    private static String defaultFbxcDomainUrl;
    private static String defaultPmDomainUrl;
    private static String defaultPmxcDomainUrl;
    private static String defaultImDomainUrl;

    /**
     * 是否支持多线路
     * @return
     */
    public static boolean isMutiLine(){
        return domainUrl.size() > 1;
    }

    /**
     * 是否存在默认线路
     * @return
     */
    public static boolean hasDefaultLine(String platform){
        if (TextUtils.equals(platform, PLATFORM_FBXC)) {
            return !TextUtils.isEmpty(defaultFbxcDomainUrl);
        } else if (TextUtils.equals(platform, PLATFORM_FB)) {
            return !TextUtils.isEmpty(defaultFbDomainUrl);
        } else if (TextUtils.equals(platform, PLATFORM_PMXC)) {
            return !TextUtils.isEmpty(defaultPmxcDomainUrl);
        } else if (TextUtils.equals(platform, PLATFORM_PM)) {
            return !TextUtils.isEmpty(defaultPmDomainUrl);
        } else {
            return !TextUtils.isEmpty(defaultImDomainUrl);
        }
    }

    public static List<String> getDomainUrl() {
        return domainUrl;
    }

    public static void initDomainUrl() {
        String platform = SPUtils.getInstance().getString(KEY_PLATFORM);
        BtDomainUtil.domainUrl.clear();
        if (TextUtils.equals(platform, PLATFORM_FBXC)) {
            BtDomainUtil.domainUrl.addAll(BtDomainUtil.getFbxcDomainUrl());
        } else if (TextUtils.equals(platform, PLATFORM_FB)) {
            BtDomainUtil.domainUrl.addAll(BtDomainUtil.getFbDomainUrl());
        } else if (TextUtils.equals(platform, PLATFORM_PMXC)) {
            BtDomainUtil.domainUrl.add(defaultPmxcDomainUrl);
        } else if (TextUtils.equals(platform, PLATFORM_PM)) {
            BtDomainUtil.domainUrl.add(defaultPmxcDomainUrl);
        } else {
            BtDomainUtil.domainUrl.add(defaultImDomainUrl);
        }
    }

    public static void addFbDomainUrl(String url){
        BtDomainUtil.fbDomainUrl.clear();
        BtDomainUtil.fbDomainUrl.add(url);
    }

    public static void setFbDomainUrl(List<String> fbDomainUrl) {
        for (String url : fbDomainUrl){
            if(!BtDomainUtil.fbDomainUrl.contains(url)){
                BtDomainUtil.fbDomainUrl.add(url);
            }
        }
    }

    public static List<String> getFbDomainUrl() {
        return fbDomainUrl;
    }

    public static void addFbxcDomainUrl(String url){
        BtDomainUtil.fbxcDomainUrl.clear();
        BtDomainUtil.fbxcDomainUrl.add(url);
    }

    public static void setFbxcDomainUrl(List<String> fbDomainUrl) {
        for (String url : fbDomainUrl){
            if(!BtDomainUtil.fbxcDomainUrl.contains(url)){
                BtDomainUtil.fbxcDomainUrl.add(url);
            }
        }
    }

    public static List<String> getFbxcDomainUrl() {
        return fbxcDomainUrl;
    }

    public static void setDefaultFbDomainUrl(String defaultFbDomainUrl) {
        BtDomainUtil.defaultFbDomainUrl = defaultFbDomainUrl;
    }

    public static void setDefaultFbxcDomainUrl(String defaultFbxcDomainUrl) {
        BtDomainUtil.defaultFbxcDomainUrl = defaultFbxcDomainUrl;
    }

    public static String getDefaultPmDomainUrl() {
        return defaultPmDomainUrl;
    }

    public static void setDefaultPmDomainUrl(String defaultPmDomainUrl) {
        BtDomainUtil.defaultPmDomainUrl = defaultPmDomainUrl;
    }

    public static String getDefaultPmxcDomainUrl() {
        return defaultPmxcDomainUrl;
    }

    public static void setDefaultPmxcDomainUrl(String defaultPmxcDomainUrl) {
        BtDomainUtil.defaultPmxcDomainUrl = defaultPmxcDomainUrl;
    }

    public static String getDefaultImDomainUrl() {
        return defaultImDomainUrl;
    }

    public static void setDefaultgetDefaultImoneDomainUrlDomainUrl(String defaultImDomainUrl) {
        BtDomainUtil.defaultImDomainUrl = defaultImDomainUrl;
    }

}
