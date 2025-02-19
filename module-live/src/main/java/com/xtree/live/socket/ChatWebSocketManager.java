package com.xtree.live.socket;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.xtree.live.message.MessageMsg;
import com.xtree.live.message.RoomType;
import com.xtree.live.socket.ChatClient;
import androidx.annotation.NonNull;

import com.google.gson.JsonObject;
import com.shuyu.gsyvideoplayer.utils.NetworkUtils;
import com.xtree.live.chat.InRoomMessage;
import com.xtree.live.chat.LeaveRoomMessage;

import java.util.Objects;

import me.xtree.mvvmhabit.utils.Utils;

public class ChatWebSocketManager {
    private static final int FLAG_GET_TOKEN = Integer.MAX_VALUE - 1;
    static final String TAG = "ChatWebSocketClient";
    private static volatile ChatWebSocketManager mChatWebSocketManager;

    public static ChatWebSocketManager getInstance() {
        if (mChatWebSocketManager == null) {
            synchronized (ChatWebSocketManager.class) {
                if (mChatWebSocketManager == null) {
                    mChatWebSocketManager = new ChatWebSocketManager(1);
                }
            }
        }
        return mChatWebSocketManager;
    }

    public static ChatWebSocketManager getInstance(int connectionType) {
        if (mChatWebSocketManager == null) {
            synchronized (ChatWebSocketManager.class) {
                if (mChatWebSocketManager == null) {
                    mChatWebSocketManager = new ChatWebSocketManager(connectionType);
                }
            }
        }
        return mChatWebSocketManager;
    }

    private ChatClient mChatClient;
    private final DispatchMessageListener mDispatchMessageListener;

    private volatile String mVid = "";
    private volatile @RoomType int mType;
    private volatile String userToken = AppConfig.getAppToken();


    //正在连接
    private volatile boolean mIsConnecting;
    private final ChatWebSocketHandler mHandler;


    /**
     * 1 ws
     * 0 mqtt
     */
    private final int mConnectionType;

    public ChatWebSocketManager(int connectionType) {
        this.mConnectionType = connectionType;
        mDispatchMessageListener = new DispatchMessageListener();
        HandlerThread handlerThread = new HandlerThread("WebSocketThread");
        handlerThread.start();
        mHandler = new ChatWebSocketHandler(handlerThread.getLooper(), new ChatWebSocketListener() {
            @Override
            public void onReconnection() {
                LogUtil.d(TAG, "onReconnection");
                getChatTokenDelay();
            }

            @Override
            public void onOpen(Message message) {
                LogUtil.d(TAG, "onOpen");
                mPastAttemptCount = 1;
                mIsConnecting = false;
                try {
                    JsonObject json = (JsonObject) message.obj;
                    mDispatchMessageListener.receiveMessage("open", json);
                } catch (Throwable e) {
                    CrashReport.postCatchedException(e);
                }
            }

            @Override
            public void onMessage(Message message) {
                Log.d("onMessage", "onMessage: 收到的消息："+message.toString());
                try {
                    JsonObject json = (JsonObject) message.obj;
                    String action = JsonUtil.getString(json, "action");
                    mDispatchMessageListener.receiveMessage(action, json);
                } catch (Throwable e) {
                    CrashReport.postCatchedException(e);
                }
            }
        });
    }

    public void registerMessageListener(MessageListener listener) {
        mDispatchMessageListener.registerMessageListener(listener);
    }

    public void unregisterMessageListener(MessageListener listener) {
        mDispatchMessageListener.unregisterMessageListener(listener);
    }

    public void pushMessage(Message message) {
        mDispatchMessageListener.receiveMessage(message);
    }

    private boolean initialize(@NonNull String token) {
        LogUtil.d(TAG, "method:initialize  threadName:" + Thread.currentThread().getName());
        try {
            if (mChatClient != null) mChatClient.closeSession();
        } catch (Throwable e) {
            LogUtil.d(TAG, e.getMessage());
        }

        try {
            Uri uri = ApiClient.parseDomain();
            String scheme = "ws";
            if ("https".equals(uri.getScheme())) scheme = "wss";
            mChatClient = ChatClientFactory.createClient(mConnectionType, scheme, uri.getHost(), token, mHandler);
            mChatClient.connectSession();
            return true;
        } catch (Exception e) {
            CrashReport.postCatchedException(e);
            throw new RuntimeException(e);
        }
    }

    private int mPastAttemptCount = 1;
    private void getChatTokenDelay() {
        LogUtil.d(TAG, "getChatTokenDelay");
        long backoff =  BackoffUtil.exponentialBackoff(mPastAttemptCount++, 30000);
        LogUtil.d(TAG, "backoff :" + backoff);
        //重试
        if(!mHandler.hasMessages(FLAG_GET_TOKEN)){
            mHandler.sendMessageDelayed(getPostMessage(mHandler, runnable), backoff);
        }
    }

    private static Message getPostMessage(Handler handler, Runnable r) {
        Message m = Message.obtain(handler, r);
        m.what = FLAG_GET_TOKEN;
        return m;
    }

    private final Runnable runnable = this::getChatToken;

    private void getChatToken() {
        mHandler.removeCallbacksAndMessages(null);
        if (!NetworkUtils.isConnected(Utils.getContext())) {
            getChatTokenDelay();
            return;
        }
        try {
            LogUtil.d(TAG, "refresh token");
            refreshToken();
            String newUserToken = AppConfig.getAppToken();
            LogUtil.d(TAG, " oldUserToken:  " + userToken);
            LogUtil.d(TAG, " newUserToken:  " + newUserToken);
            if(initialize(newUserToken)){
                userToken = newUserToken;
                LogUtil.d(TAG, " initialize success");
                return;
            }else {
                LogUtil.d(TAG, " initialize failure");
            }
        } catch (Throwable t) {
            if(AppManager.debuggable())//noinspection CallToPrintStackTrace
                t.printStackTrace();
            CrashReport.postCatchedException(t);
        }
        getChatTokenDelay();
    }

    /** @noinspection ResultOfMethodCallIgnored*/
    @SuppressLint("CheckResult")
    private static void refreshToken() {
        ApiClient.apiStore().refreshToken(AppConfig.getChannel()).blockingGet();
    }

    public void start() {
        LogUtil.d(TAG, "start");
        //切换用户
        if (!userToken.equals(AppConfig.getAppToken())) {
            if (AppManager.debuggable()) {
                LogUtil.d(TAG, "切换用户");
                LogUtil.d(TAG, "userToken:" + userToken);
                LogUtil.d(TAG, "AppConfig.getToken():" + AppConfig.getAppToken());
            }
            userToken = AppConfig.getAppToken();
            mIsConnecting = true;
            mHandler.sendMessage(getPostMessage(mHandler, runnable));
            return;
        }
        if (mChatClient != null && mChatClient.isConnected()) {
            LogUtil.d(TAG, "connection is alive, return");
            return;
        }
        if (mIsConnecting) return;
        mIsConnecting = true;
        mHandler.sendMessage(getPostMessage(mHandler, runnable));
    }

    public void stop() {
        LogUtil.d(TAG, "stop");
        mIsConnecting = false;
        mHandler.removeCallbacksAndMessages(null);
        mHandler.post(() -> closeSession(mChatClient));
//        mChatClient = null;
    }

    private void closeSession(ChatClient chatClient) {
        if (chatClient != null) {
            try {
                chatClient.closeSession();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public void inRoom(String vid){
        if(TextUtils.isEmpty(vid))return;
        if(mChatClient == null)return;
        mHandler.post(()->{
            if(mChatClient != null && mChatClient.isConnected()){
                try {
                    if(AppManager.debuggable())LogUtil.d(TAG, "inRoom vid:"+ vid);
                    mChatClient.inRoom(new InRoomMessage(vid));
                }catch (RuntimeException t){
                    CrashReport.postCatchedException(t);
                }
            }
        });
    }

    public void leaveRoom(String vid){
        if(TextUtils.isEmpty(vid))return;
        if(mChatClient == null)return;
        mHandler.post(()->{
            if(mChatClient != null && mChatClient.isConnected()){
                try {
                    if(AppManager.debuggable())LogUtil.d(TAG, "leaveRoom vid:"+ vid);
                    mChatClient.leaveRoom(new LeaveRoomMessage(vid));
                }catch (Throwable t){
                    CrashReport.postCatchedException(t);
                }
            }
        });
    }

    boolean isAtCurrentConversation(MessageMsg msg) {
        return Objects.equals(msg.getVid(), mVid);
    }

    public String getVid() {
        return mVid;
    }

    public @RoomType int getType() {
        return mType;
    }

    public void setInRoom(String vid, @RoomType int type) {
        mVid = vid;
        mType = type;
    }

    public boolean isConnecting() {
        return mIsConnecting;
    }
}
