package com.xtree.live.socket;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;

public class ChatWebSocketHandler extends Handler {

    private final ChatWebSocketListener mListener;

    public ChatWebSocketHandler(Looper looper, ChatWebSocketListener listener) {
        super(looper);
        this.mListener  = new WeakReference<>(listener).get();
    }

    @Override
    public void handleMessage(Message msg) {
        if (mListener == null) {
            return;
        }

        switch (msg.what) {
            case Constant.WEB_SOCKET_WHAT_RECONNECTION:
                mListener.onReconnection();
                break;
            case Constant.CHAT_WEB_SOCKET_WHAT_OPEN:
                mListener.onOpen(msg);
                break;
            case Constant.CHAT_WEB_SOCKET_WHAT_UPDATE_MSG:
                mListener.onMessage(msg);
                break;
        }
    }
}