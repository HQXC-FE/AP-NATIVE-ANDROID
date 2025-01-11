package com.xtree.live.ui.main.service;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xtree.base.utils.CfLog;
import com.xtree.live.messenger.ILiveInputMessenger;
import com.xtree.live.ui.main.service.message.LiveMessageCenterThread;
import com.xtree.live.ui.main.service.response.LiveRoomChatResponse;
import com.xtree.service.IWebSocket;
import com.xtree.service.WebSocketManager;
import com.xtree.service.message.MessageType;

import java.lang.reflect.Type;
import java.util.HashMap;

import io.sentry.Sentry;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class LivePushClient implements IWebSocket {
    private LiveMessageCenterThread messageCenter;
    HashMap<String, Object> map;
    Gson gson = new Gson();

    //应用内消息传递
    private ILiveInputMessenger inputMessenger;

    public LivePushClient(ILiveInputMessenger inputMessenger) {
        this.inputMessenger = inputMessenger;
    }

    @Override
    public void connectSocket(String url, long checkInterval) {
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
                try {
                    //text="{\"type\":\"message\",\"fd\":9197,\"data\":{\"subject\":\"\\u5145\\u503c\\u7533\\u8bf7\",\"messageid\":41450873},\"timestamp\":1730360802}";
                    if (text == null || text.isEmpty()) {
                        CfLog.i("服务器返回空消息");
                        return;
                    }
                    CfLog.i("socket message:" + text);
                    gson = new Gson();
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
                            LiveRoomChatResponse liveRoomChatVo = gson.fromJson(text, LiveRoomChatResponse.class);
                            if (inputMessenger != null && liveRoomChatVo != null) {
                                inputMessenger.sendMessage(MessageType.Output.REMOTE_MSG, liveRoomChatVo);
                            }
                            break;
                        case "send":
                            CfLog.i("直播间消息");
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
}
