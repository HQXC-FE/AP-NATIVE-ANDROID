package com.xtree.live.message;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xtree.live.R;
import com.xtree.live.message.inroom.InRoomData;
import com.xtree.live.widge.PinView;

public class NoPin extends BasePin{

    public NoPin(PinView root) {
        super(root);
    }

    public void setPinData(InRoomData data) {
        if(mPinView.getPin() != this){
            mPinView.removeAllViews();
            mPinView.addView(mInsertView);
        }
    }

    @Override
    View inflate(ViewGroup root) {
        return LayoutInflater.from(root.getContext()).inflate(R.layout.view_no_pin, root, true);
    }
}