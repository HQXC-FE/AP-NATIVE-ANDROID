package com.xtree.live.widge;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LifecycleOwner;

import com.blankj.utilcode.util.ConvertUtils;
import com.google.gson.JsonObject;
import com.xtree.live.inter.Pin;
import com.xtree.live.message.BasePin;
import com.xtree.live.message.ConversationMessage;
import com.xtree.live.message.NoPin;
import com.xtree.live.message.Type0Pin;
import com.xtree.live.message.Type1Pin;
import com.xtree.live.message.inroom.InRoomData;

public class PinView extends ConstraintLayout implements Pin {
    private @NonNull BasePin mBasePin;

    public PinView(Context context) {
        super(context);
        mBasePin = new NoPin(this);
    }

    public PinView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        mBasePin = new NoPin(this);
    }

    public PinView(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
        mBasePin = new NoPin(this);
    }


    public BasePin getPin() {
        return mBasePin;
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mBasePin.onDetachedFromWindow();
    }

    @Override
    public void onPause(@NonNull LifecycleOwner owner) {
        mBasePin.onPause(owner);
    }

    @Override
    public void onResume(@NonNull LifecycleOwner owner) {
        mBasePin.onResume(owner);
    }

    public void setPinData(InRoomData bean) {
        if (bean == null) return;

        if (bean.getPinType() == 0 && bean.getPinObj() != null && (bean.getPinObj() instanceof JsonObject && ((JsonObject)bean.getPinObj()).size() > 0) ) {
            if (!(mBasePin instanceof Type0Pin)) {
                removeAllViews();
                mBasePin = new Type0Pin(this);
            }
            mBasePin.setPinData(bean);
            return;
        }
        if (TextUtils.isEmpty(bean.getPinData())) {
            if (!(mBasePin instanceof NoPin)) {
                removeAllViews();
                mBasePin = new NoPin(this);
            }
            mBasePin.setPinData(bean);
            return;
        }
        if (1 == bean.getPinType()) {
            if (!(mBasePin instanceof Type1Pin)) {
                removeAllViews();
                mBasePin = new Type1Pin(this);
            }
            mBasePin.setPinData(bean);
        }
    }

    @Override
    public void setSystemText(ConversationMessage message, int duration) {
        mBasePin.setSystemText(message, duration);
    }


    public int getPinHeight() {
        int height = getHeight();
        if (height <= 0) return ConvertUtils.dp2px(8);
        return height;
    }
}
