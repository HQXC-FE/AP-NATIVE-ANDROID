package com.xtree.live.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Build;
import android.util.Log;

public class AudioStateManager {

    private final Context mContext;

    private AudioStateListener mListener;

    private final AudioManager mAudioManager;

    private final AudioFocusChangeListener mAudioFocusChangeListener;

    private final AudioBecomingNoisyReceiver mAudioBecomingNoisyReceiver;

    public interface AudioStateListener {

        void onAudioFocusRequested();

        void onAudioFocusGained();

        void onVolumeLoweringRequested();
    }

    public AudioStateManager(Context context) {
        this.mContext = context;
        mAudioManager = (AudioManager)
                context.getSystemService(Context.AUDIO_SERVICE);
        mAudioFocusChangeListener = new AudioFocusChangeListener();
        mAudioBecomingNoisyReceiver = new AudioBecomingNoisyReceiver();
    }

    public void registerListener(AudioStateListener listener) {
        this.mListener = listener;
    }

    /**
     * Call when about to play
     *
     * @return true if audio focus is granted.
     */
    public boolean requestAudioFocus() {
        int result = mAudioManager.requestAudioFocus(mAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        return result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
    }

    /**
     * Call when no longer in app or no longer need audioFocus
     */
    public void abandonAudioFocus() {
        mAudioManager.abandonAudioFocus(mAudioFocusChangeListener);
    }

    /**
     * Call when playing media
     */
    public void registerNoiseReceiver() {
        IntentFilter filter = new
                IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            mContext.registerReceiver(mAudioBecomingNoisyReceiver, filter,Context.RECEIVER_EXPORTED);
        }else {
            mContext.registerReceiver(mAudioBecomingNoisyReceiver, filter);
        }
    }

    /**
     * Call when media is paused/stopped
     */
    public void unregisterNoiseReceiver() {
        mContext.unregisterReceiver(mAudioBecomingNoisyReceiver);
    }

    private class AudioFocusChangeListener
            implements AudioManager.OnAudioFocusChangeListener {

        @Override
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_LOSS:
                    // another app gained audio focus
                    // STOP PLAYBACK
                    dispatchAudioFocusRequested();
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    // another app want you to pause media
                    // PAUSE
                    dispatchAudioFocusRequested();
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    // LOWER VOLUME (but keep playing)
                    dispatchVolumeLoweringRequested();
                    break;
                case AudioManager.AUDIOFOCUS_GAIN:
                    // START PLAYBACK
                    dispatchAudioFocusGained();
                    break;
            }
        }
    }

    private void dispatchAudioFocusRequested() {
        if (mListener != null)
            mListener.onAudioFocusRequested();
    }

    private void dispatchAudioFocusGained() {
        if (mListener != null)
            mListener.onAudioFocusGained();
    }

    private void dispatchVolumeLoweringRequested() {
        if (mListener != null)
            mListener.onVolumeLoweringRequested();
    }

    /**
     * Called when unplugging your headphones for example
     * Register when start playing music
     */
    private class AudioBecomingNoisyReceiver
            extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //Pause the music
            Log.i("TAG", "Audio became noisy - Pause Music");
        }
    }
}
