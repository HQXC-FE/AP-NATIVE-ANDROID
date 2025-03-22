package com.xtree.live.inter;

public interface VideoControllerListener {
    void isControllerVisible(Boolean isVisible);
    void onFullscreenButtonClick();

    void onIsPlayingChanged(boolean isPlaying);
}