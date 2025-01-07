package com.xtree.live.ui.main.service;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xtree.base.utils.CfLog;
import com.xtree.live.ui.main.service.message.LiveMessageCenterThread;
import com.xtree.service.IWebSocket;
import com.xtree.service.WebSocketManager;
import com.xtree.service.messenger.IInputMessenger;

import java.lang.reflect.Type;
import java.util.HashMap;

import io.sentry.Sentry;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class LivePushClient implements IWebSocket {
    private LiveMessageCenterThread messageCenter;
    HashMap<String, Object> map;
    //private boolean isManualClose = false; // 标记是否为主动关闭
    //String url = "";
    //long checkInterval;
    //private ScheduledExecutorService watchdogExecutor; // Watchdog 定时任务
    //private int retryCount = 0; // 重连次数
    //private static final int MAX_RETRIES = 3; // 最大重连次数

    //应用内消息传递
    private IInputMessenger inputMessenger;

    public LivePushClient(IInputMessenger inputMessenger) {
        this.inputMessenger = inputMessenger;
        //watchdogExecutor = Executors.newSingleThreadScheduledExecutor(); // Watchdog 定时任务线程池
    }

    @Override
    public void connectSocket(String url, long checkInterval) {
        //this.url = url;
        //this.checkInterval = checkInterval;
        //if (retryCount > MAX_RETRIES) {
        //    CfLog.e("超过最大重连次数，停止重连并通知用户！");
        //    stopWatchdog();
        //    return;
        //}
        if (messageCenter != null) {
            stopSocket();
        }
        messageCenter = new LiveMessageCenterThread();
        CfLog.i(String.format("长链接开始 \t%s,\t%d", url, checkInterval));
        WebSocketManager.getInstance().newWebSocket(url, new WebSocketListener() {
            @Override
            public void onOpen(@NonNull WebSocket webSocket, @NonNull Response response) {
                messageCenter.startThread(webSocket, checkInterval);
                new Handler(Looper.getMainLooper()).post(() -> {
                    if (messageCenter != null) {
                        messageCenter.sendHeart();
                        CfLog.d("发送心跳");
                    }
                });
            }

            @Override
            public void onMessage(@NonNull WebSocket webSocket, @NonNull String text) {
                //isManualClose = false; // 重置主动关闭标志
                //retryCount = 0; // 重置重连计数
                //startWatchdog(); // 开始监测
                try {
                    //text="{\"type\":\"message\",\"fd\":9197,\"data\":{\"subject\":\"\\u5145\\u503c\\u7533\\u8bf7\",\"messageid\":41450873},\"timestamp\":1730360802}";
                    if (text == null || text.isEmpty()) {
                        CfLog.i("服务器返回空消息");
                        return;
                    }
                    CfLog.i("socket message:" + text);
                    Gson gson = new Gson();
                    Type type = new TypeToken<HashMap<String, Object>>() {
                    }.getType();
                    try {
                        map = gson.fromJson(text, type);
                        CfLog.i("解析成功: " + map);
                    } catch (Exception e) {
                        e.printStackTrace();
                        CfLog.e("JSON 解析失败: " + e.getMessage());
                        return;
                    }
                    String action = (String) map.get("action");
                    switch (action) {
                        case "open":
                            CfLog.i("长链接消息确认成功");
                            break;
                        case "response":
                            CfLog.i("回應");
                            break;
                        case "msg":
                            CfLog.i("消息");
                            //if (inputMessenger != null && remoteMessage.getData() != null && remoteMessage.getData().size() > 0) {
                            //    inputMessenger.sendMessage(MessageType.Output.REMOTE_MSG, remoteMessage.getData().get(0));
                            //}
                            break;
                        case "send":
                            CfLog.i("消息");
                            break;
                        case "close"://服务端返回失败，主动断开
                            if (messageCenter != null) {
                                messageCenter.stopThread(false);
                                messageCenter = null;
                            }
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Sentry.captureException(e);
                }
            }

            @Override
            public void onClosed(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
                messageCenter.stopThread(true);
                messageCenter = null;
                CfLog.i(String.format("服务器关闭%s", reason));
            }

            @Override
            public void onFailure(@NonNull WebSocket webSocket, @NonNull Throwable t, @Nullable Response response) {
                messageCenter.stopThread(false);
                // 原先的无法使用，最后在此去除掉messageCenter的Reference后，运行正常
                messageCenter = null;
                CfLog.i(String.format("服务失败%s,%s", t, response));
            }
        });
    }


    @Override
    public void stopSocket() {
        if (messageCenter != null) {
            messageCenter.stopThread();
            messageCenter = null;
            //isManualClose = true;
        }
    }

    @Override
    public void sendMessage(String message) {
        if (messageCenter != null) {
            messageCenter.sendMessage(message);
        }
    }

    @Override
    public boolean isRunning() {
        if (messageCenter != null) {
            return messageCenter.isRunning();
        }
        return false;
    }

    //private void handleDisconnection() {
    //    if (!isManualClose && retryCount < MAX_RETRIES) {
    //        retryCount++;
    //        CfLog.e("非主动断线，开始第 " + retryCount + " 次重连...");
    //        connectSocket(url, checkInterval);
    //    } else if (retryCount >= MAX_RETRIES) {
    //        CfLog.e("达到最大重连次数，停止所有操作！");
    //        stopSocket();
    //    }
    //}

    //// 启动 Watchdog 监测连接状态
    //private void startWatchdog() {
    //    stopWatchdog(); // 确保没有重复的任务
    //    watchdogExecutor.scheduleWithFixedDelay(() -> {
    //        CfLog.e("检测中...");
    //        if (messageCenter == null) {
    //            CfLog.e("连接异常，尝试重连...");
    //            handleDisconnection();
    //        }
    //    }, 5, 5, TimeUnit.SECONDS); // 每 5 秒检查一次连接状态
    //}

    //// 停止 Watchdog
    //public void stopWatchdog() {
    //    if (watchdogExecutor != null && !watchdogExecutor.isShutdown()) {
    //        watchdogExecutor.shutdownNow();
    //        watchdogExecutor = Executors.newSingleThreadScheduledExecutor(); // 重置线程池
    //    }
    //}
}
