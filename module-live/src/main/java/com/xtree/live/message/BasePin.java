package com.xtree.live.message;

import android.animation.AnimatorSet;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.blankj.utilcode.util.ConvertUtils;
import com.xtree.live.R;
import com.xtree.live.inter.Pin;
import com.xtree.live.uitl.IntentUtils;
import com.xtree.live.widge.PinView;
import com.xtree.live.widge.SystemMessageLayout;

public abstract class BasePin implements Pin {
    protected final int CORNER_SIZE  = ConvertUtils.dp2px(4);

    AnimatorSet mAnimatorSet;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    protected final SystemMessageLayout systemMessageLayout;


    protected PinView mPinView;
    protected View mInsertView;



    public BasePin(PinView root) {
        this.mPinView = root;
        this.mInsertView = inflate(root);
        this.systemMessageLayout = findViewById(R.id.pin_system_message_container);
    }

    public final <T extends View> T findViewById(@IdRes int id) {
        return mInsertView.findViewById(id);
    }

    abstract View inflate(ViewGroup root);

    public void setSystemText(ConversationMessage message, int duration) {
        mHandler.removeCallbacks(removeRunnable);
        systemMessageLayout.setVisibility(View.VISIBLE);
        systemMessageLayout.setDuration(duration / 2);
        systemMessageLayout.setSystemMessage(message);
        mHandler.postDelayed(removeRunnable, 5000);
    }

    @Override
    public void onPause(@NonNull LifecycleOwner owner) {
        if (mAnimatorSet != null &&mAnimatorSet.isRunning()) mAnimatorSet.pause();
    }

    @Override
    public void onResume(@NonNull LifecycleOwner owner) {
        if (mAnimatorSet != null && mAnimatorSet.isStarted()) mAnimatorSet.resume();
    }

    @Override
    public void onDetachedFromWindow() {
        if (mAnimatorSet != null) {
            mAnimatorSet.cancel();
            mAnimatorSet.removeAllListeners();
        }
        mHandler.removeCallbacksAndMessages(null);
    }

    private final Runnable removeRunnable = new Runnable() {
        @Override
        public void run() {
            systemMessageLayout.setVisibility(View.GONE);
        }
    };

    protected Context getContext() {
        return mPinView.getContext();
    }

    protected void jumpToBrowser(Context context, Object url) {
        if (url == null) return;
        IntentUtils.jumpToBrowser(context, url.toString().trim());
    }

    protected String foldSpace(CharSequence content) {
        if (TextUtils.isEmpty(content)) return "";
        return content.toString().replace("\r\n", " ").replace("\r", " ").replace("\r", " ");
    }
}

