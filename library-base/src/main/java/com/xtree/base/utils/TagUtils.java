package com.xtree.base.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.xtree.base.global.SPKeyGlobal;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.sentry.SentryLevel;
import io.sentry.protocol.Message;
import me.xtree.mvvmhabit.utils.SPUtils;

import io.sentry.Sentry;
import io.sentry.SentryEvent;

public class TagUtils {

    public static final String TAG_AC = "_ac"; // appCenter
    public static final String TAG_MP = "_mp"; // mixpanel
    public static final String TAG_ST = "_st"; // sentry

    public static final String EVENT_REG = "register";
    public static final String EVENT_LOGIN = "login";
    public static final String EVENT_LOGOUT = "logout";
    public static final String EVENT_RECHARGE = "recharge";
    public static final String EVENT_WITHDRAW = "withdraw";
    public static final String EVENT_TRANSFER = "transfer";
    public static final String EVENT_BIND_YHK = "bind_yhk";
    public static final String EVENT_BIND_USDT = "bind_usdt";
    //反诈拦截
    public static final String EVENT_ANTI_FRAUD = "anti_fraud";
    //测速
    public static final String EVENT_FASTEST = "fastest_api";
    //测速失败
    public static final String EVENT_FASTEST_KEY_FAIL = "fastest_api_fail";
    public static final String KEY_FASTEST_START = "开始测速";
    public static final String KEY_FASTEST_RESTART = "测速浮窗点击重新测速按钮";
    public static final String KEY_FASTEST_ERROR = "测速失败";
    public static final String KEY_FASTEST_GETTHIRDDOMAIN = "获取三方域名列表成功";
    public static final String KEY_FASTEST_GETTHIRDDOMAIN_ERROR = "获取三方域名列表失败";

    private static String CHANNEL_NAME = ""; // 本包的渠道名,关于页显示当前安装包的渠道名用的。
    private static String MEDIA_SOURCE = ""; // 旧包的渠道名,就是升级到最新官方安装包前的渠道名。
    private static boolean IS_TAG = false; // 是否开启打点统计, true:开启, false:关闭
    private static String USER_ID = "";
    private static String USER_NAME = SPUtils.getInstance().getString(SPKeyGlobal.USER_NAME);
    private static String deviceId;

    private static Map<String, Long> mapCache = new HashMap<>(); // 记录打点事件对应的时间,防重复

    public static void init(Context ctx, String[] token, String channel, String userId) {
        init(ctx, token, channel, userId, true);
    }

    public static void init(Context ctx, String[] token, String channel, String userId, boolean isTag) {
        CHANNEL_NAME = channel;
        USER_ID = userId;
        IS_TAG = isTag;
    }

    public static boolean isTag() {
        return IS_TAG;
    }

    public static void logEvent(Context ctx, String event) {
        tagEvent(ctx, event);
    }

    public static void tagEvent(Context ctx, String event) {
        CfLog.i(ctx.getClass().getSimpleName() + ", event: " + event);
        tagSentry(event, event);
    }

    public static void tagEvent(Context ctx, String event, Object value) {
        tagEvent(ctx, event, event, String.valueOf(value));
    }

    public static void tagEvent(Context ctx, String event, String key, String value) {
        CfLog.i(ctx.getClass().getSimpleName() + ", event: " + event + ", key: " + key + ", value: " + value);
        tagSentry(key, value, getMap(key, value));
    }

    /**
     * 日常打点,每天最多只打一次点,统计日活.
     *
     * @param ctx
     */
    public static synchronized void tagDailyEvent(Context ctx) {
        CfLog.d(ctx.getClass().getSimpleName() + ", event: tagDaily");
        if (!IS_TAG) {
            return;
        }
        int date = ctx.getSharedPreferences("myPrefs", Context.MODE_PRIVATE).getInt("lastTagDate", 0);
        int curDate = Integer.parseInt(TimeUtils.getCurDate());
        if (date != curDate) {
            CfLog.d("event: tagDaily, " + curDate);
            ctx.getSharedPreferences("myPrefs", Context.MODE_PRIVATE).edit().putInt("lastTagDate", curDate).commit();
            tagSentry("tagDaily", "" + curDate);
        }
    }


    private static void tagSentry(String event, String value) {
        if (!IS_TAG || isFrequent(event, TAG_ST)) {
            return;
        }
        tagSentry(event, value, null);
    }

    private static void tagSentry(String event, String value, Map<String, String> map) {
        if (!IS_TAG || isFrequent(event, TAG_ST)) {
            return;
        }
        SentryEvent sentryEvent = new SentryEvent();
        sentryEvent.setLevel(SentryLevel.INFO); // 设置为 info，不进入 Issues 列表
        sentryEvent.setTransaction(event);

        // 拼接 message
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append("埋点事件：").append(event);
        if (map != null && !map.isEmpty()) {
            messageBuilder.append(" | 数据：");
            for (Map.Entry<String, String> entry : map.entrySet()) {
                messageBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append(" ");
                sentryEvent.setTag(entry.getKey(), entry.getValue());
            }
        } else {
            messageBuilder.append(" | 数据: Event = ").append(event).append(", Value = ").append(value);
            sentryEvent.setTag(event, value);
        }

        Message message = new Message();
        message.setMessage(messageBuilder.toString().trim());
        sentryEvent.setMessage(message);

        // 上报事件
        Sentry.captureEvent(sentryEvent);
    }

    /**
     * 某个事件 是否频繁 (true-频繁,可以return掉,防止某个事件短时间内重复多次)
     *
     * @param eventName 事件名
     * @param tag       打点平台名
     * @return true:是频繁, false:不频繁
     */
    private static boolean isFrequent(String eventName, String tag) {
        if (mapCache.containsKey(eventName + tag) && System.currentTimeMillis() - mapCache.get(eventName + tag) < 60 * 1000) {
            return true;
        }

        mapCache.put(eventName + tag, System.currentTimeMillis());
        return false;
    }

    private static HashMap getMap(String key, String value) {
        HashMap<String, Object> map = new HashMap<>();
        if (!TextUtils.isEmpty(key)) {
            map.put(key, value);
        }
        if (!TextUtils.isEmpty(USER_ID)) {
            map.put("uid", USER_ID);
        }
        if (map.isEmpty()) {
            return null;
        }
        return map;
    }

    private static JSONObject getJson(String key, Object value) {
        JSONObject props = new JSONObject();
        try {
            props.put(key, value);
            if (!TextUtils.isEmpty(USER_ID)) {
                props.put("uid", USER_ID);
            }
        } catch (JSONException e) {
        }
        return props;
    }

    private static JSONObject getJson(HashMap<String, Object> map) {
        JSONObject props = new JSONObject(map);
        if (!TextUtils.isEmpty(USER_ID)) {
            try {
                props.put("uid", USER_ID);
            } catch (JSONException e) {
            }
        }

        return props;
    }

    /**
     * 打开时间，取粗略的时间(用户喜欢在什么时间打开直播间或者APP <br>
     * 参数 open_time:  HHmm ，HH 00-23 , mm 分钟数 取15或30的倍数，00,15,30,45
     */
    public static String getOpenTime() {
        int periodTime = 30;
        String h = new SimpleDateFormat("HH").format(new Date());
        String m = new SimpleDateFormat("mm").format(new Date());
        String m2;

        if (Integer.valueOf(m) < periodTime) {
            m2 = "00";
        } else {
            m2 = String.valueOf(Integer.valueOf(m) / periodTime * periodTime);
        }
        CfLog.i(h + m2 + ",  (" + h + m + ")");
        return h + m2;
    }

    /**
     * 获取间隔的时间，返回 *s, *m
     * 停留时间，秒或分钟，5s,10s,20s,30s,40s,50s, 1m,2m,3m,4m,5m,
     * 10m,15m,20m,30m,40m...... 100m,110m, 130m，超过120分钟全部按130m算。
     */
    public static String getStayTime(long lastTime) {
        long seconds = (System.currentTimeMillis() - lastTime) / 1000;
        CfLog.i("dur: " + seconds + " seconds, (" + seconds / 60 + "m " + seconds % 60 + "s)");
        long minutes;
        String dur;
        if (seconds < 10) {
            dur = "5s";
        } else if (seconds < 60) {
            seconds = seconds / 10 * 10; // seconds 10,20,30,40,50
            dur = seconds + "s";
        } else if (seconds < 7200) {
            minutes = seconds / 60; // minutes
            if (minutes < 5) {
                // 分钟数不变 minutes 1,2,3...
            } else if (minutes <= 20) {
                minutes = minutes / 5 * 5; // minutes 5,10,15,20
            } else if (minutes < 120) {
                minutes = minutes / 10 * 10; // minutes 30,40,50,...100,110
            }
            dur = minutes + "m";
        } else {
            dur = "130m";
        }
        return dur;
    }

    /**
     * 获得设备的AndroidId
     *
     * @param ctx 上下文
     * @return 设备的AndroidId
     */
    public static String getDevId(Context ctx) {

        SharedPreferences sp = ctx.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        String dvcId = sp.getString("dvcId", "");
        if (!dvcId.isEmpty()) {
            return dvcId;
        }

        String str = "";
        try {
            str = getAndroidId(ctx);
            CfLog.i("AndroidId: " + str); // AndroidId: e676****0c4654f3
            str = str.substring(0, 4) + "-" + str.substring(str.length() - 4);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        //dvcId = TimeUtils.getCurDate() + "-" + str;
        dvcId = str;
        sp.edit().putString("dvcId", dvcId).commit();
        return dvcId;
    }

    /**
     * 获得设备的AndroidId
     *
     * @param context 上下文
     * @return 设备的AndroidId
     */
    public static String getAndroidId(Context context) {
        try {
            return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public static String getDeviceId(Context context) {
        if (deviceId != null && !deviceId.isEmpty()) {
            return deviceId;
        }

        try {
            String str = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            str = str.substring(0, 4) + str.substring(str.length() - 4);
            deviceId = str;
            return str;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public static String getDeviceId() {
        return deviceId;
    }

    public static void initDeviceId(Context context) {
        getDeviceId(context);
    }

    /**
     * 获得设备序列号（如：WTK7N16923005607）, 个别设备无法获取
     *
     * @return 设备序列号
     */
    private static String getSERIAL() {
        try {
            return Build.SERIAL;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

}
