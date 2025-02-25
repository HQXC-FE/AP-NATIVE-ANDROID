package com.xtree.live.inter;

import androidx.lifecycle.DefaultLifecycleObserver;

import com.xtree.live.message.ConversationMessage;
import com.xtree.live.message.inroom.InRoomData;

public interface Pin extends DefaultLifecycleObserver {
    void setPinData(InRoomData data);
    void setSystemText(ConversationMessage message, int duration);

    void onDetachedFromWindow();
}
