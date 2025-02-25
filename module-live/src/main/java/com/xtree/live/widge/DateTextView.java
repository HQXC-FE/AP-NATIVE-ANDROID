package com.xtree.live.widge;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.xtree.live.R;
import com.xtree.live.uitl.ViewUtil;

public class DateTextView extends androidx.appcompat.widget.AppCompatTextView {
    private final Animation animateIn;
    private final Animation animateOut;

    private boolean pendingHide = false;

    public DateTextView(@NonNull Context context) {
        this(context, null);
    }

    public DateTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,android.R.attr.textViewStyle);
    }

    public DateTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.animateIn = AnimationUtils.loadAnimation(getContext(), R.anim.slide_from_top);
        this.animateOut = AnimationUtils.loadAnimation(getContext(), R.anim.slide_to_top);

        this.animateIn.setDuration(100);
        this.animateOut.setDuration(100);
    }

    public void init() {

    }

    public void show() {
        if (TextUtils.isEmpty(getText())) {
            return;
        }

        if (pendingHide) {
            pendingHide = false;
        } else {
            ViewUtil.animateIn(this, animateIn);
        }
    }

    public void hide() {
        pendingHide = true;
        postDelayed(() -> {
            if (pendingHide) {
                pendingHide = false;
                ViewUtil.animateOut(this, animateOut, View.GONE);
            }
        }, 400);
    }
}

