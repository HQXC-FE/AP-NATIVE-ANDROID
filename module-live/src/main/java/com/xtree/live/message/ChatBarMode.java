package com.xtree.live.message;

import static com.xtree.live.message.ChatBarMode.CHATBAR_MODE_HIGH;
import static com.xtree.live.message.ChatBarMode.CHATBAR_MODE_LOW;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;

@Retention(SOURCE)
@IntDef({ChatBarMode.CHATBAR_MODE_NONE, CHATBAR_MODE_LOW, CHATBAR_MODE_HIGH})
public @interface ChatBarMode {
    int CHATBAR_MODE_NONE = 0;
    int CHATBAR_MODE_LOW = 1;
    int CHATBAR_MODE_HIGH = 2;
}
