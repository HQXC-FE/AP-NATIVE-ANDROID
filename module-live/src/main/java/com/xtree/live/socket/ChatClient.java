package com.xtree.live.socket;

import com.xtree.live.chat.InRoomMessage;
import com.xtree.live.chat.LeaveRoomMessage;

import org.eclipse.paho.client.mqttv3.MqttException;

public interface ChatClient {
    void connectSession() throws Exception;
    void closeSession() throws InterruptedException, MqttException;

    boolean isConnected();

    void inRoom(InRoomMessage message);
    void leaveRoom(LeaveRoomMessage message);
}
