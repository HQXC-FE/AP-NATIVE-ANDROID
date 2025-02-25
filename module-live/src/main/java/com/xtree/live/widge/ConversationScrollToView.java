package com.xtree.live.widge;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.xtree.live.R;

public class ConversationScrollToView extends FrameLayout {

    private final View scrollToBottom, scrollToNewMessage, scrollToNewMessageBadge;
    private final Animation inAnimation;
    private final Animation outAnimation;

    public ConversationScrollToView(@NonNull Context context) {
        this(context, null);
    }

    public ConversationScrollToView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ConversationScrollToView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        inflate(context, R.layout.conversation_scroll_to, this);

        scrollToBottom = findViewById(R.id.conversation_scroll_to_bottom);
        scrollToNewMessage = findViewById(R.id.conversation_scroll_to_new_message);
        scrollToNewMessageBadge = findViewById(R.id.conversation_scroll_to_new_message_badge);

        inAnimation  = AnimationUtils.loadAnimation(context, R.anim.fade_scale_in);
        outAnimation = AnimationUtils.loadAnimation(context, R.anim.fade_scale_out);

        inAnimation.setDuration(100);
        outAnimation.setDuration(50);
    }

    public void setShown(boolean isShown) {
        if (isShown) {
            setVisibility(VISIBLE);
//      ViewUtil.animateIn(this, inAnimation);
        } else {
            setVisibility(INVISIBLE);
//      ViewUtil.animateOut(this, outAnimation, View.INVISIBLE);
        }
    }


    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        this.scrollToBottom.setOnClickListener(l);
        this.scrollToNewMessage.setOnClickListener(l);
    }

    public void setUnreadCount(int unreadCount) {
        this.scrollToBottom.setVisibility(unreadCount > 0 ? GONE : VISIBLE);
        this.scrollToNewMessage.setVisibility(unreadCount > 0 ? VISIBLE : GONE);
        this.scrollToNewMessageBadge.setVisibility(unreadCount > 0 ? VISIBLE : GONE);
    }

    private @NonNull CharSequence formatUnreadCount(int unreadCount) {
        return unreadCount > 99 ? "99+" : String.valueOf(unreadCount);
    }
}
