package com.xtree.live.socket;

import android.os.Message;

public interface ChatWebSocketListener {
    /**
     * socket重连
     */
    void onReconnection();
    /**
     * 信息更新
     */
    void onMessage(Message message);
    /**
     * socket开启
     */
    void onOpen(Message message);
}
