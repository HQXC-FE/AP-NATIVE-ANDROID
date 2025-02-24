package com.xtree.live.chat;

import com.xtree.live.socket.ChatWebSocketManager;

public class InOutRoomHelper {
    public static void inRoom(String vid) {
        ChatWebSocketManager.getInstance().inRoom(vid);
    }

    public static void leaveRoom(String vid) {
        ChatWebSocketManager.getInstance().leaveRoom(vid);
    }
}
