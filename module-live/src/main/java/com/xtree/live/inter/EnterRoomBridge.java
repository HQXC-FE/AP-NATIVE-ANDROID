package com.xtree.live.inter;

import com.xtree.live.message.inroom.ChatRoom;

public interface EnterRoomBridge {
    void intoChatRoom(int roomType, String vid, int uid, ChatRoom chatRoom);

    void intoChatList();
}

