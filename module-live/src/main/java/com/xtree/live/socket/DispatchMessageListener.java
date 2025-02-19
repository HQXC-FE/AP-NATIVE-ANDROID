package com.xtree.live.socket;

import android.os.Build;
import android.util.ArraySet;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.blankj.utilcode.util.ThreadUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.hjq.gson.factory.GsonFactory;
import com.xtree.live.LiveConfig;
import com.xtree.live.R;
import com.xtree.live.chat.Counter;
import com.xtree.live.message.Message;
import com.xtree.live.message.MessageActionType;
import com.xtree.live.message.MessageBan;
import com.xtree.live.message.MessageClearHistory;
import com.xtree.live.message.MessageDelete;
import com.xtree.live.message.MessageGift;
import com.xtree.live.message.MessageKickUser;
import com.xtree.live.message.MessageLiveClose;
import com.xtree.live.message.MessageLiveStart;
import com.xtree.live.message.MessageMsg;
import com.xtree.live.message.MessageNewRoom;
import com.xtree.live.message.MessageOnline;
import com.xtree.live.message.MessageOpen;
import com.xtree.live.message.MessagePin;
import com.xtree.live.message.MessageRead;
import com.xtree.live.message.MessageReserve;
import com.xtree.live.message.MessageSystem;
import com.xtree.live.message.MessageTokenError;
import com.xtree.live.message.MessageUnread;
import com.xtree.live.message.MessageVote;
import com.xtree.live.message.RoomType;

import java.util.Objects;
import java.util.Set;

public class DispatchMessageListener {
    public static final String TAG = "DispatchMessageListener";
    private final Set<MessageListener> messageListeners;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public DispatchMessageListener() {
        messageListeners = new ArraySet<>();
    }

    public void receiveMessage(String action, JsonObject data) throws JsonSyntaxException {
        Class<? extends Message> clazz = messageClass(action);
        if (clazz == null) return;
        Message message = GsonFactory.getSingletonGson().fromJson(data, clazz);
        if (message == null) return;
        beforeReceiveMessage(message);
        receiveMessage(message);
    }

    private void beforeReceiveMessage(Message message) {
        if (MessageActionType.ACTION_MSG.equals(message.getAction()) || MessageActionType.ACTION_NEW_MSG.equals(message.getAction())) {
            executeMsg((MessageMsg) message);
        }
    }


    private Class<? extends Message> messageClass(String action) {
        switch (action) {
            case MessageActionType.ACTION_MSG:
            case MessageActionType.ACTION_SEND:
            case MessageActionType.ACTION_NEW_MSG:
                return MessageMsg.class;
            case MessageActionType.ACTION_OPEN:
                return MessageOpen.class;
            case MessageActionType.ACTION_PIN:
                return MessagePin.class;
            case MessageActionType.ACTION_DEL_MSG:
                return MessageDelete.class;
            case MessageActionType.ACTION_UNREAD:
                return MessageUnread.class;
            case MessageActionType.ACTION_GIFT:
                return MessageGift.class;
            case MessageActionType.ACTION_START_LIVE:
                return MessageLiveStart.class;
            case MessageActionType.ACTION_CLOSE_LIVE:
                return MessageLiveClose.class;
            case MessageActionType.ACTION_RESERVE:
                return MessageReserve.class;
            case MessageActionType.ACTION_NEW_ROOM:
                return MessageNewRoom.class;
            case MessageActionType.ACTION_CLEAR_HISTORY:
                return MessageClearHistory.class;
            case MessageActionType.ACTION_BAN:
                return MessageBan.class;
            case MessageActionType.ACTION_USER_ONLINE:
                return MessageOnline.class;
            case MessageActionType.ACTION_KICK_USER:
                return MessageKickUser.class;
            case MessageActionType.ACTION_TOKEN_ERROR:
                return MessageTokenError.class;
            case MessageActionType.ACTION_ACTIVITY_VOTE:
                return MessageVote.class;
            default:
                return null;
        }
    }

    private final Counter mCounter = new Counter("work counter", 1000);

    public void receiveMessage(Message message) {
        mCounter.count(message);
        ThreadUtils.runOnUiThread(() -> {
            for (MessageListener listener : messageListeners) {
                if (message instanceof MessageMsg) {
                    MessageMsg messageMsg = (MessageMsg) message;
                    if (MessageActionType.ACTION_SEND.equals(message.getAction())) {
                        listener.onReceiveMessageSend(messageMsg);
                    } else {
                        if (MessageActionType.ACTION_MSG.equals(message.getAction())) {
                            listener.onReceiveMessageMsg(messageMsg);
                        }
                        if (!ChatWebSocketManager.getInstance().isAtCurrentConversation(messageMsg)) {
                            listener.onReceiveUnreadMessage(messageMsg);
                        }
                    }
                } else if (message instanceof MessageOpen) {
                    listener.onReceiveMessageOpen((MessageOpen) message);
                } else if (message instanceof MessageDelete) {
                    listener.onReceiveMessageDelete((MessageDelete) message);
                } else if (message instanceof MessageSystem) {
                    listener.onReceiveMessageSystem((MessageSystem) message);
                } else if (message instanceof MessagePin) {
                    listener.onReceiveMessagePin((MessagePin) message);
                } else if (message instanceof MessageReserve) {
                    listener.onReceiveMessageReserve((MessageReserve) message);
                } else if (message instanceof MessageUnread) {
                    listener.onReceiveMessageUnread((MessageUnread) message);
                } else if (message instanceof MessageRead) {
                    listener.onReceiveMessageRead((MessageRead) message);
                } else if (message instanceof MessageLiveClose) {
                    listener.onReceiveMessageLiveClose((MessageLiveClose) message);
                } else if (message instanceof MessageGift) {
                    listener.onReceiveMessageGift((MessageGift) message);
                } else if (message instanceof MessageNewRoom) {
                    listener.onReceiveMessageNewRoom((MessageNewRoom) message);
                } else if (message instanceof MessageClearHistory) {
                    listener.onReceiveMessageClearHistory((MessageClearHistory) message);
                } else if (message instanceof MessageBan) {
                    listener.onReceiveMessageBan((MessageBan) message);
                } else if (message instanceof MessageOnline) {
                    listener.onReceiveMessageOnline((MessageOnline) message);
                } else if (message instanceof MessageKickUser) {
                    listener.onReceiveMessageKickUser((MessageKickUser) message);
                } else if (message instanceof MessageVote) {
                    listener.onReceiveMessageVote((MessageVote) message);
                } else if (message instanceof MessageTokenError) {
                    //是否消费tokenError事件
                    listener.onReceiveMessageTokenError((MessageTokenError) message);
                }
            }
        });
    }


    private void executeMsg(MessageMsg message) {
        if (!ChatWebSocketManager.getInstance().isAtCurrentConversation(message)) {
            //room_type 2,非群聊未读消息播报语音 虚拟房间的消息 不响铃
            if (message.getIsVir() == 0 && RoomType.PAGE_CHAT_PRIVATE == message.getRoomType() && LiveConfig.isNotificationBeepOn() && !Objects.equals(LiveConfig.getUserId(), message.getSender())) {
                Log.d(TAG, "-------play new msg----");
//                MediaUtil.playNotificationBeep(AppManager.getContext(), R.raw.strong_notification);
            }
            if(!Objects.equals(LiveConfig.getUserId(), message.getSender())){
                UnreadUtils.increaseUnreadCount(message.getIsVir() == 1, message.getVid());
            }
        }
    }


    public void unregisterMessageListener(MessageListener listener) {
        messageListeners.remove(listener);
    }

    public void registerMessageListener(MessageListener listener) {
        messageListeners.add(listener);
    }
}
