package com.xtree.live.message;

import static com.xtree.live.message.RoomType.PAGE_CHAT_GROUP;
import static com.xtree.live.message.RoomType.PAGE_CHAT_PRIVATE;
import static com.xtree.live.message.RoomType.PAGE_CHAT_PRIVATE_ANCHOR;
import static com.xtree.live.message.RoomType.PAGE_CHAT_UNKNOW;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;

@Retention(SOURCE)
@IntDef({RoomType.PAGE_CHAT_GLOBAL, PAGE_CHAT_PRIVATE, PAGE_CHAT_GROUP,PAGE_CHAT_UNKNOW, PAGE_CHAT_PRIVATE_ANCHOR})
public @interface RoomType {
    int PAGE_CHAT_GLOBAL = 0;
    int PAGE_CHAT_GROUP = 1;
    int PAGE_CHAT_PRIVATE = 2;
    int PAGE_CHAT_PRIVATE_ANCHOR = -2;
    int PAGE_CHAT_UNKNOW = -1;
}

