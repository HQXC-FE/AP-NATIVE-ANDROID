package com.xtree.live.inter;

import androidx.annotation.NonNull;

import com.xtree.live.message.EmojiModel;

public interface OnEmojiGifClickedObserver {
    void onEmojiGifClicked(@NonNull EmojiModel model);
    void onEmojiGifLongClicked(@NonNull EmojiModel model);
}