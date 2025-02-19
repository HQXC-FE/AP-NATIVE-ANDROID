package io.github.rockerhieu.emojicon;

import io.github.rockerhieu.emojicon.emoji.Emojicon;

public interface OnEmojiconClickedListener {
    void onEmojiconClicked(Emojicon emojicon);
    void onEmojiconLongClicked(Emojicon emojicon);
}
