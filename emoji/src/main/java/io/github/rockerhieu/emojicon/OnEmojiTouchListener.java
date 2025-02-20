package io.github.rockerhieu.emojicon;

import android.view.MotionEvent;
import android.view.View;

interface OnEmojiTouchListener {
    void onTouch(EmojiAdapter adapter, int position, View v, MotionEvent event);
}
