package com.xtree.live.socket;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.NetworkUtils;
import com.google.gson.JsonObject;
import com.xtree.base.net.live.X9LiveInfo;
import com.xtree.live.BuildConfig;
import com.xtree.live.LiveConfig;
import com.xtree.live.chat.InRoomMessage;
import com.xtree.live.chat.LeaveRoomMessage;
import com.xtree.live.message.MessageMsg;
import com.xtree.live.message.RoomType;
import com.xtree.live.uitl.BackoffUtil;
import com.xtree.live.uitl.JsonUtil;

import java.util.Objects;

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
    private volatile String userToken = LiveConfig.getLiveToken();


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
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            mDispatchMessageListener = new DispatchMessageListener();
//        }
        mDispatchMessageListener = new DispatchMessageListener();
        HandlerThread handlerThread = new HandlerThread("WebSocketThread");
        handlerThread.start();
        mHandler = new ChatWebSocketHandler(handlerThread.getLooper(), new ChatWebSocketListener() {
            @Override
            public void onReconnection() {
                Log.d(TAG, "onReconnection");
                getChatTokenDelay();
            }

            @Override
            public void onOpen(Message message) {
                Log.d(TAG, "onOpen");
                mPastAttemptCount = 1;
                mIsConnecting = false;
                try {
                    JsonObject json = (JsonObject) message.obj;
                    mDispatchMessageListener.receiveMessage("open", json);
                } catch (Throwable e) {

                }
            }

            @Override
            public void onMessage(Message message) {
                Log.d("onMessage", "onMessage: 收到的消息：" + message.toString());
                try {
                    JsonObject json = (JsonObject) message.obj;
                    String action = JsonUtil.getString(json, "action");
                    mDispatchMessageListener.receiveMessage(action, json);
                } catch (Throwable e) {

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

    public void pushMessage(com.xtree.live.message.Message message) {
        mDispatchMessageListener.receiveMessage(message);
    }

    private boolean initialize(@NonNull String token) {
        Log.d(TAG, "method:initialize  threadName:" + Thread.currentThread().getName());
        try {
            if (mChatClient != null) mChatClient.closeSession();
        } catch (Throwable e) {
            Log.d(TAG, e.getMessage());
        }

        try {
            String baseUrl = X9LiveInfo.INSTANCE.getAppApi();
            Uri uri = Uri.parse(baseUrl);
            String scheme = "ws";
            if ("https".equals(uri.getScheme())) scheme = "wss";
            mChatClient = ChatClientFactory.createClient(mConnectionType, scheme, uri.getHost(), token, mHandler);
//            String url = scheme + "://" + uri.getHost() + "/wss/?xLiveToken=" + token;
//            newWebsocket(url);
            mChatClient.connectSession();
            return true;
        } catch (Exception e) {

            throw new RuntimeException(e);
        }
    }

/*    OkHttpClient client =new OkHttpClient.Builder()
            .readTimeout(20, TimeUnit.SECONDS)
                .connectTimeout(20,TimeUnit.SECONDS)
                .build();
    private void newWebsocket(String url) {
        Request request = new Request.Builder().url(url).build();
                client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onClosed(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
                super.onClosed(webSocket, code, reason);
            }

            @Override
            public void onClosing(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
                super.onClosing(webSocket, code, reason);
            }

            @Override
            public void onFailure(@NonNull WebSocket webSocket, @NonNull Throwable t, @Nullable Response response) {
                super.onFailure(webSocket, t, response);
            }

            @Override
            public void onMessage(@NonNull WebSocket webSocket, @NonNull String text) {
                super.onMessage(webSocket, text);
                Log.d("WebSocketListener", "onMessage: text "+text);

            }

            @Override
            public void onMessage(@NonNull WebSocket webSocket, @NonNull ByteString bytes) {
                super.onMessage(webSocket, bytes);
            }

            @Override
            public void onOpen(@NonNull WebSocket webSocket, @NonNull Response response) {
                super.onOpen(webSocket, response);
                Log.d("WebSocketListener", "onMessage: text "+response.message());
            }
        });
    }*/

    private int mPastAttemptCount = 1;

    private void getChatTokenDelay() {
        if (!TextUtils.isEmpty(LiveConfig.getLiveToken())) {
            Log.d(TAG, "getChatTokenDelay");
            long backoff = BackoffUtil.exponentialBackoff(mPastAttemptCount++, 30000);
            Log.d(TAG, "backoff :" + backoff);
            //重试
            if (!mHandler.hasMessages(FLAG_GET_TOKEN)) {
                mHandler.sendMessageDelayed(getPostMessage(mHandler, runnable), backoff);
            }
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
        if (!NetworkUtils.isConnected()) {
            getChatTokenDelay();
            return;
        }
        try {
            Log.d(TAG, "refresh token");
            refreshToken();
            String newUserToken = LiveConfig.getLiveToken();

            Log.d(TAG, " oldUserToken:  " + userToken);
            Log.d(TAG, " newUserToken:  " + newUserToken);
            if (initialize(newUserToken)) {
                userToken = newUserToken;
                Log.d(TAG, " initialize success");
                return;
            } else {
                Log.d(TAG, " initialize failure");
            }
        } catch (Throwable t) {
            if (BuildConfig.DEBUG)//noinspection CallToPrintStackTrace
                t.printStackTrace();

        }
        getChatTokenDelay();
    }

    /**
     * @noinspection ResultOfMethodCallIgnored
     */
    @SuppressLint("CheckResult")
    private static void refreshToken() {
//        ApiClient.apiStore().refreshToken(AppConfig.getChannel()).blockingGet();
    }

    public void start() {
        Log.d(TAG, "start");
        //切换用户
        if (!userToken.equals(LiveConfig.getLiveToken())) {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "切换用户");
                Log.d(TAG, "userToken:" + userToken);
                Log.d(TAG, "AppConfig.getToken():" + LiveConfig.getLiveToken());
            }
            userToken = LiveConfig.getLiveToken();
            mIsConnecting = true;
            mHandler.sendMessage(getPostMessage(mHandler, runnable));
            return;
        }
        if (mChatClient != null && mChatClient.isConnected()) {
            Log.d(TAG, "connection is alive, return");
            return;
        }
        if (mIsConnecting) return;
        mIsConnecting = true;
        mHandler.sendMessage(getPostMessage(mHandler, runnable));
    }

    public void stop() {
        Log.d(TAG, "stop");
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

    public void inRoom(String vid) {
        if (TextUtils.isEmpty(vid)) return;
        if (mChatClient == null) return;
        mHandler.post(() -> {
            if (mChatClient != null && mChatClient.isConnected()) {
                try {
                    if (BuildConfig.DEBUG) Log.d(TAG, "inRoom vid:" + vid);
                    mChatClient.inRoom(new InRoomMessage(vid));
                } catch (RuntimeException t) {

                }
            }
        });
    }

    public void leaveRoom(String vid) {
        if (TextUtils.isEmpty(vid)) return;
        if (mChatClient == null) return;
        mHandler.post(() -> {
            if (mChatClient != null && mChatClient.isConnected()) {
                try {
                    if (BuildConfig.DEBUG) Log.d(TAG, "leaveRoom vid:" + vid);
                    mChatClient.leaveRoom(new LeaveRoomMessage(vid));
                } catch (Throwable t) {

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
