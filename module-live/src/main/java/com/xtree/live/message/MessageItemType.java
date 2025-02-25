package com.xtree.live.message;

import static com.xtree.live.message.MessageItemType.MSG_GLOBAL_BANNER;
import static com.xtree.live.message.MessageItemType.MSG_GLOBAL_GIFT;
import static com.xtree.live.message.MessageItemType.MSG_GLOBAL_IMG;
import static com.xtree.live.message.MessageItemType.MSG_GLOBAL_TEXT;
import static com.xtree.live.message.MessageItemType.MSG_INCOMING_BANNER;
import static com.xtree.live.message.MessageItemType.MSG_INCOMING_IMG;
import static com.xtree.live.message.MessageItemType.MSG_INCOMING_TEXT;
import static com.xtree.live.message.MessageItemType.MSG_OUTGOING_IMG;
import static com.xtree.live.message.MessageItemType.MSG_OUTGOING_TEXT;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import androidx.annotation.IntDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(SOURCE)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@IntDef({MSG_OUTGOING_TEXT, MSG_OUTGOING_IMG, MSG_GLOBAL_IMG, MSG_GLOBAL_BANNER, MSG_INCOMING_BANNER, MSG_INCOMING_TEXT, MSG_INCOMING_IMG, MSG_GLOBAL_TEXT,MSG_GLOBAL_GIFT})
public @interface MessageItemType {
    //自己送的礼物
    int MSG_GLOBAL_GIFT = -4;
    int MSG_OUTGOING_TEXT = 1;
    /**
     * “我”发送的消息
     */
    int MSG_OUTGOING_IMG = 3;
    /**
     * 广场普通消息
     */
    int MSG_GLOBAL_TEXT = 4;

    /**
     * 广场图片消息
     */
    int MSG_GLOBAL_IMG = 10;
    /**
     * 广场banner消息
     */
    int MSG_GLOBAL_BANNER = 6;

    int MSG_INCOMING_BANNER = 7;
    int MSG_INCOMING_TEXT = 8;
    int MSG_INCOMING_IMG = 9;
}
