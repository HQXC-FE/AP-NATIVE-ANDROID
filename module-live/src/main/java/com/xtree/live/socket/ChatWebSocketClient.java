package com.xtree.live.socket;

import android.os.Message;
import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.hjq.gson.factory.GsonFactory;
import com.xtree.live.BuildConfig;
import com.xtree.live.chat.InRoomMessage;
import com.xtree.live.chat.LeaveRoomMessage;
import com.xtree.live.uitl.JsonUtil;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.Map;

public class ChatWebSocketClient extends WebSocketClient implements ChatClient {

    private static final String TAG = "ChatWebSocketClient";
    private final static int PONG_DELAY_TIME = 5000;
    private final static int PING_CIRCLE_TIME = 5000;
    private ChatWebSocketHandler mHandler;

    public void setHandler(ChatWebSocketHandler handler) {
        this.mHandler = handler;
    }

    private final Runnable restart = new Runnable() {
        @Override
        public void run() {
            mHandler.removeCallbacks(restart);
            stopHeartBeat();
            Message msg = Message.obtain();
            msg.what = Constant.WEB_SOCKET_WHAT_RECONNECTION;
            mHandler.sendMessage(msg);
        }
    };


    private final Runnable heartBeat = () -> {
        if (isOpen()) {
            try {
                sendPing();
            } catch (Exception e) {
//                CrashReport.postCatchedException(e);
                return;
            }
            startHeartBeat();
        }
    };

    public ChatWebSocketClient(URI serverUri, Map<String, String> headers, ChatWebSocketHandler handler) {
        super(serverUri, headers);
        mHandler = handler;
//        if("wss".equals(serverUri.getScheme())) {
//            try {
//                Helper.PairSSL ssl = Helper.getPairSSL();
//                setSocketFactory(ssl.getSslContext().getSocketFactory());
//            } catch (Throwable e) {
////                CrashReport.postCatchedException(e);
//            }
//        }
    }

    @Override
    public void onWebsocketPong(WebSocket conn, Framedata f) {
        super.onWebsocketPong(conn, f);
        log("onWebsocketPong");
        mHandler.removeCallbacks(restart);
    }

    @Override
    public void sendPing() {
        super.sendPing();
        log("sendPing");
        mHandler.postDelayed(restart, PONG_DELAY_TIME);
    }

    @Override
    public void onOpen(ServerHandshake handshake) {
    }

    @Override
    public void onMessage(String message) {
        log("onMessage message:\n" + message);
        try {
            JsonObject json = GsonFactory.getSingletonGson().fromJson(message, JsonObject.class);
            String action = JsonUtil.getString(json, "action");
            Message msg = Message.obtain();
            if (action.equals("open")) {
                msg.what = Constant.CHAT_WEB_SOCKET_WHAT_OPEN;
                msg.obj = json;
                startHeartBeat();
            } else {
                msg.what = Constant.CHAT_WEB_SOCKET_WHAT_UPDATE_MSG;
                msg.obj = json;
            }
            mHandler.sendMessage(msg);
        } catch (JsonSyntaxException e) {
//            CrashReport.postCatchedException(e);
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        log("code=" + code + "   reason=" + reason + "    remote=" + remote);
        stopHeartBeat();
        if (code != 1000) {
            Message msg = Message.obtain();
            msg.what = Constant.WEB_SOCKET_WHAT_RECONNECTION;
            mHandler.sendMessage(msg);
        }
    }

    @Override
    public void onError(Exception ex) {
//        CrashReport.postCatchedException(ex);
    }

    /**
     * connected 之后开启心跳
     */
    private void startHeartBeat() {
        mHandler.postDelayed(heartBeat, PING_CIRCLE_TIME);
    }

    private void stopHeartBeat() {
        mHandler.removeCallbacks(heartBeat);
    }


    @Override
    public void connectSession() throws InterruptedException {
        connectBlocking();
    }

    @Override
    public void closeSession() throws InterruptedException {
        closeBlocking();
    }

    @Override
    public boolean isConnected() {
        return this.isOpen();
    }

    @Override
    public void leaveRoom(LeaveRoomMessage message) {
        send(GsonFactory.getSingletonGson().toJson(message));
    }

    @Override
    public void inRoom(InRoomMessage message) {
        send(GsonFactory.getSingletonGson().toJson(message));
    }

    private void log(String message) {
        if (BuildConfig.DEBUG) Log.d(TAG, message);
    }
}

