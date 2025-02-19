package com.xtree.live.socket;

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

public interface MessageListener {

    void onReceiveMessageDelete(MessageDelete message);

    void onReceiveMessageSystem(MessageSystem message);

    void onReceiveMessagePin(MessagePin message);

    void onReceiveMessageReserve(MessageReserve message);

    void onReceiveMessageUnread(MessageUnread message);

    void onReceiveMessageRead(MessageRead message);

    void onReceiveMessageLiveStart(MessageLiveStart message);

    void onReceiveMessageLiveClose(MessageLiveClose message);

    void onReceiveMessageGift(MessageGift message);

    void onReceiveMessageOpen(MessageOpen message);
    void onReceiveMessageTokenError(MessageTokenError message);

    void onReceiveMessageNewRoom(MessageNewRoom message);
    void onReceiveMessageMsg(MessageMsg message);
    void onReceiveMessageSend(MessageMsg message);
    void onReceiveUnreadMessage(MessageMsg message);
    void onReceiveMessageClearHistory(MessageClearHistory message);
    void onReceiveMessageBan(MessageBan message);
    void onReceiveMessageOnline(MessageOnline message);
    void onReceiveMessageKickUser(MessageKickUser message);
    void onReceiveMessageVote(MessageVote message);
}

