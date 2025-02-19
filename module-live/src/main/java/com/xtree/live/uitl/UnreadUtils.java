package com.xtree.live.uitl;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.JsonObject;
import com.hjq.gson.factory.GsonFactory;
import com.xtree.base.utils.SPUtil;
import com.xtree.live.BuildConfig;
import com.xtree.live.SPKey;

import org.jsoup.Connection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import me.xtree.mvvmhabit.base.BaseApplication;

public class UnreadUtils {

    public static void increaseUnreadCount(boolean isVir, String vid) {
        Map<String, Integer> object = getSavedUnreadJson();
        Integer count = object.get(vid);
        if (count == null) count = 0;
        if (isVir && count > 0) {
            count = -count;
        }
        object.put(vid, isVir ? --count : ++count);
        saveUnreadJson(object);
    }


    public static void clearUnread(String vid) {
        Map<String, Integer> json = getSavedUnreadJson();
        json.remove(vid);
        saveUnreadJson(json);
    }

    public static void clearUnread(String... vids) {
        Map<String, Integer> json = getSavedUnreadJson();
        for (String vid : vids) {
            json.remove(vid);
        }
        saveUnreadJson(json);
    }

    public static int getUnreadCount(String vid) {
        Map<String, Integer> unreadMap = getSavedUnreadJson();
        Integer count = unreadMap.get(vid);
        return count == null ? 0 : count;
    }

    public static int calculateTotal() {
        Map<String, Integer> unreadMap = getSavedUnreadJson();
        Set<String> keys = unreadMap.keySet();
        int total = 0;
        for (String key : keys) {
            Integer count = unreadMap.get(key);
            total += Math.max(count == null ? 0 : count, 0);
        }
        return total;
    }

    private static volatile ConcurrentHashMap<String, Integer> sUnreadMap;

    public static synchronized @NonNull Map<String, Integer> getSavedUnreadJson() {
        if (sUnreadMap != null) {
            if (BuildConfig.DEBUG)
                Log.d("UnreadUtils", "read from cache getSavedUnreadJson:" + GsonFactory.getSingletonGson().toJson(sUnreadMap));
            return sUnreadMap;
        }
        sUnreadMap = new ConcurrentHashMap<>();
        String s = SPUtil.get(BaseApplication.getInstance()).get(SPKey.UNREAD,null);
        if (BuildConfig.DEBUG) Log.d("UnreadUtils", "read from file getSavedUnreadJson:" + s);
        JsonObject unreadJson = JsonUtil.fromJson(GsonFactory.getSingletonGson(), s, JsonObject.class);
        if (unreadJson != null) {
            for (String key : unreadJson.keySet()) {
                sUnreadMap.put(key, JsonUtil.getInt(unreadJson, key, 0));
            }
        }
        if (sUnreadMap == null) {
            sUnreadMap = new ConcurrentHashMap<>();
        }
        return sUnreadMap;
    }

    private static void saveUnreadJson(Map<String, Integer> json) {
        saveUnreadJson(GsonFactory.getSingletonGson().toJson(json));
    }

    private static void saveUnreadJson(String json) {
        if (BuildConfig.DEBUG) Log.d("UnreadUtils", "saveUnreadJson:" + json);
        SPUtil.get(BaseApplication.getInstance()).get(SPKey.UNREAD, json);
    }


    public static int showRealUnreadCount(int displayCount) {
        return displayCount > 0 ? 1 : 0;
    }

    public static void refreshUnreadMapFromChatRoomList(List<ChatRoomInfo> list) {
        Map<String, Integer> savedJson = getSavedUnreadJson();
        Map<String, Integer> newMap = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            ChatRoomInfo source = list.get(i);
            Integer count = savedJson.get(source.getVid());
            source.setUnread(count == null ? 0 : count);
            newMap.put(source.getVid(), source.getUnread());
        }

        for (String key : savedJson.keySet()) {
            Integer unreadCount = savedJson.get(key);
            if (unreadCount != null && unreadCount < 0) {
                newMap.put(key, unreadCount);
            }
        }
        savedJson.clear();
        savedJson.putAll(newMap);
        UnreadUtils.saveUnreadJson(savedJson);
    }
}

