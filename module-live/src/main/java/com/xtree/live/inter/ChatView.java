package com.xtree.live.inter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.xtree.live.data.AdsBean;
import com.xtree.live.message.ConversationMessage;
import com.xtree.live.message.MessageBan;
import com.xtree.live.message.MessageDelete;
import com.xtree.live.message.MessagePin;
import com.xtree.live.message.MessageRecord;
import com.xtree.live.message.SystemMessageRecord;
import com.xtree.live.message.inroom.InRoomData;

import java.util.List;

public interface ChatView extends BaseViewNew, SendMessage {

    void onGetChatHistory(boolean isRefresh, @Nullable List<MessageRecord> beans);
    void onUpdateChatHistory(@Nullable List<MessageRecord> beans);

    void pinData(MessagePin bean);

    void doDelMsg(MessageDelete bean);

    void doTobUrl(String url);

    void getAdsSuccess(List<AdsBean> beans);

    void onProcessChatHistories(boolean isUpdateFresh, boolean isRefresh, List<ConversationMessage> chatHistories);
    void onProcessReceiveMessageList(List<ConversationMessage> chatHistories);
    void onProcessReceiveMessage(ConversationMessage message);

    void onProcessAdsData(AdsBean bean);

    void onPin(InRoomData bean);
    String lastMsgId();

    String defaultLastMsgId();

    String getRoomVid();
    int uid();
    void setRoomVid(String roomVid);

    @NonNull
    List<ConversationMessage> wholeChatList();

    void onGetLiveInroomLog(List<SystemMessageRecord> data);

    void doBanUser(MessageBan ban);
}
