package com.xtree.live.inter;

import androidx.annotation.NonNull;

import java.io.File;

public interface SendMessage {
    //发送文字消息
    public void sendText(String text);

    void onSendText(boolean success, String msg);
    void onSendEmojiGif(boolean success, String msg);
    //发送图片消息
    public void sendPhoto(File pic);
    void onSendPhoto(boolean success, String msg);
    // 发送炫彩弹幕消息
    void sendDanmu(int giftId, int uid, int level, String text);
    void onSendDanmu(boolean success, String msg);

    void sendEmojiGif(String picture);

    @NonNull
    String pmSourceType();

    @NonNull String pmSourceTypeStr();
}