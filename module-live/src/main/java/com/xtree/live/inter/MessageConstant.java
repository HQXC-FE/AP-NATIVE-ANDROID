package com.xtree.live.inter;

import android.graphics.Color;

import androidx.annotation.ColorInt;
import androidx.core.content.ContextCompat;

import com.xtree.live.R;
import com.xtree.live.uitl.WordUtil;

public interface MessageConstant {
    String CHAT_ROOM_THANKS = WordUtil.getString(R.string.chat_room_thanks);
    String CHAT_ROOM_GIFTED = WordUtil.getString(R.string.chat_room_gifted);
    String ENTER_ROOM = WordUtil.getString(R.string.enter_room);
    String CHAT_ROOM_WELCOME = WordUtil.getString(R.string.chat_room_welcome);
    String LIVE_ANCHOR = WordUtil.getString(R.string.live_anchor);

    @ColorInt
    int TEXT_COLOR_PRIMARY = WordUtil.getColor(R.color.textColor);
    @ColorInt int TEXT_COLOR_SECONDARY = Color.parseColor("#666666");
    @ColorInt int TEXT_COLOR_CHAT_LINK = Color.parseColor("#127BD1");
    @ColorInt int TEXT_COLOR_TERTIARY = WordUtil.getColor(R.color.message_text_color);
    @ColorInt int TEXT_COLOR_PRIMARY_ONDARK = WordUtil.getColor(R.color.white);

    @ColorInt int CHAT_USER_COLOR = Color.parseColor("#025BE8");
    @ColorInt int COLOR_ACCENT = Color.parseColor("#D02D54");
}
