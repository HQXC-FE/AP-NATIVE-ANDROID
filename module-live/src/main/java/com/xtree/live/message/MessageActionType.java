package com.xtree.live.message;

import static com.xtree.live.message.MessageActionType.ACTION_ACTIVITY_VOTE;
import static com.xtree.live.message.MessageActionType.ACTION_BAN;
import static com.xtree.live.message.MessageActionType.ACTION_CLEAR_HISTORY;
import static com.xtree.live.message.MessageActionType.ACTION_CLOSE_LIVE;
import static com.xtree.live.message.MessageActionType.ACTION_DEL_MSG;
import static com.xtree.live.message.MessageActionType.ACTION_DEL_TEMP;
import static com.xtree.live.message.MessageActionType.ACTION_GIFT;
import static com.xtree.live.message.MessageActionType.ACTION_KICK_USER;
import static com.xtree.live.message.MessageActionType.ACTION_MSG;
import static com.xtree.live.message.MessageActionType.ACTION_NEW_MSG;
import static com.xtree.live.message.MessageActionType.ACTION_NEW_ROOM;
import static com.xtree.live.message.MessageActionType.ACTION_OPEN;
import static com.xtree.live.message.MessageActionType.ACTION_PIN;
import static com.xtree.live.message.MessageActionType.ACTION_RESERVE;
import static com.xtree.live.message.MessageActionType.ACTION_SEND;
import static com.xtree.live.message.MessageActionType.ACTION_START_LIVE;
import static com.xtree.live.message.MessageActionType.ACTION_SYSTEM;
import static com.xtree.live.message.MessageActionType.ACTION_TOKEN_ERROR;
import static com.xtree.live.message.MessageActionType.ACTION_UNREAD;
import static com.xtree.live.message.MessageActionType.ACTION_USER_ONLINE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import androidx.annotation.StringDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(SOURCE)
@Target({ElementType.TYPE, ElementType.FIELD,ElementType.METHOD,ElementType.PARAMETER})
@StringDef({ACTION_SEND,ACTION_SYSTEM,ACTION_PIN,ACTION_DEL_MSG,ACTION_UNREAD,ACTION_NEW_MSG,ACTION_DEL_TEMP,ACTION_GIFT,ACTION_START_LIVE,ACTION_CLOSE_LIVE, ACTION_RESERVE, ACTION_OPEN,ACTION_NEW_ROOM, ACTION_MSG,ACTION_CLEAR_HISTORY, ACTION_BAN,ACTION_USER_ONLINE,ACTION_KICK_USER, ACTION_TOKEN_ERROR, ACTION_ACTIVITY_VOTE})
public @interface MessageActionType {
    String ACTION_SEND = "send";
    String ACTION_OPEN = "open";
    String ACTION_SYSTEM = "system";
    String ACTION_PIN = "pin";
    String ACTION_DEL_MSG = "delmsg";
    String ACTION_BAN = "ban";
    String ACTION_USER_ONLINE = "useronline";
    String ACTION_UNREAD = "unread";
    String ACTION_NEW_MSG = "newMsg";
    String ACTION_MSG = "msg";
    String ACTION_DEL_TEMP = "delTemp";
    String ACTION_GIFT = "gift";
    String ACTION_START_LIVE = "startLive";
    String ACTION_CLOSE_LIVE = "closeLive";
    String ACTION_RESERVE = "reserveStartLive";
    String ACTION_NEW_ROOM = "newRoom";
    String ACTION_CLEAR_HISTORY = "clearHistory";
    String ACTION_KICK_USER = "kickuser";
    String ACTION_TOKEN_ERROR= "tokenError";
    String ACTION_ACTIVITY_VOTE= "vote";
}
