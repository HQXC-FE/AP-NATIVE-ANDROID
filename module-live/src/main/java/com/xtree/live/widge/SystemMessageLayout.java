package com.xtree.live.widge;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ViewSwitcher;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.xtree.live.R;
import com.xtree.live.message.ConversationMessage;

public class SystemMessageLayout extends LinearLayout {
    public SystemMessageLayout(Context context) {
        super(context);
        init();
    }

    public SystemMessageLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SystemMessageLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public SystemMessageLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private ViewSwitcher switcher;
    private void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.merge_system_message_container, this , true);
        switcher = findViewById(R.id.pin_system_message_switcher);
        switcher.setInAnimation(getContext(), R.anim.anim_system_message_in);
        switcher.setOutAnimation(getContext(), R.anim.anim_system_message_out);
        ImageView applaud = findViewById(R.id.message_applaud);
        Glide.with(applaud).asGif().load(R.raw.applaud).into(applaud);
    }


    public void setSystemMessage(ConversationMessage message) {
        MessageTextView messageTextView = (MessageTextView) switcher.getNextView();
//        messageTextView.setBackgroundColor(messageTextView.getTitleBackgroundColor(message));
        messageTextView.setMessage(message);
        switcher.showNext();
    }

    public void setDuration(int duration){
        if(switcher.getInAnimation() != null) switcher.getInAnimation().setDuration(duration);
        if(switcher.getOutAnimation() != null)switcher.getOutAnimation().setDuration(duration);
    }

}

