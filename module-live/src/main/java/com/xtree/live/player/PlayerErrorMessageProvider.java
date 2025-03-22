package com.xtree.live.player;

import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.media3.common.ErrorMessageProvider;
import androidx.media3.common.PlaybackException;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.mediacodec.MediaCodecRenderer;
import androidx.media3.exoplayer.mediacodec.MediaCodecUtil;

import com.xtree.live.R;
import com.xtree.live.uitl.WordUtil;

class PlayerErrorMessageProvider implements ErrorMessageProvider<PlaybackException> {

    @OptIn(markerClass = UnstableApi.class)
    @Override
    public Pair<Integer, String> getErrorMessage(@NonNull PlaybackException e) {
        String errorString = WordUtil.getString(
                R.string.playback_error_default);
        Throwable cause = e.getCause();
        if (cause instanceof MediaCodecRenderer.DecoderInitializationException) {
            // Special case for decoder initialization failures.
            MediaCodecRenderer.DecoderInitializationException decoderInitializationException =
                    (MediaCodecRenderer.DecoderInitializationException) cause;
            if (decoderInitializationException.codecInfo == null) {
                if (decoderInitializationException.getCause() instanceof MediaCodecUtil.DecoderQueryException) {
                    errorString = WordUtil.getString(R.string.playback_error_unable_to_query_device_decoders);
                } else if (decoderInitializationException.secureDecoderRequired) {
                    errorString =
                            ("直播视频解码错误");
                } else {
                    errorString =
                            ("设备不支持");
                }
            } else {
                errorString = ("设备不支持");
            }
        }
        return Pair.create(0, errorString);
    }
}