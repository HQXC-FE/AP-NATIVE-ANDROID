package io.github.rockerhieu.emojicon;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

import io.github.rockerhieu.emojicon.emoji.Emojicon;
import io.github.rockerhieu.emojicon.entity.EmojiEntity;
import io.github.rockerhieu.emojicon.entity.LabelEntity;

public class EmojiAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {
    private final boolean mUseSystemDefault;
    private final OnEmojiTouchListener onEmojiTouchListener;
    public EmojiAdapter(List<MultiItemEntity> data, boolean useSystemDefault, OnEmojiTouchListener onEmojiTouchListener) {
        super(data);
        addItemType(EmojiEntity.ITEM_TYPE, EmojiEntity.ITEM_TYPE);
        addItemType(LabelEntity.ITEM_TYPE, LabelEntity.ITEM_TYPE);
        mUseSystemDefault = useSystemDefault;
        addChildClickViewIds(R.id.emojicon_icon);
        addChildLongClickViewIds(R.id.emojicon_icon);
        this.onEmojiTouchListener =  onEmojiTouchListener;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, MultiItemEntity entity) {
        if (entity.getItemType() == EmojiEntity.ITEM_TYPE) {
            EmojiEntity emojiEntity = (EmojiEntity) entity;
            EmojiconTextView emoji = baseViewHolder.getView(R.id.emojicon_icon);
            emoji.setOnTouchListener((v, event) -> {
                onEmojiTouchListener.onTouch(this, baseViewHolder.getBindingAdapterPosition(), v, event);
                return false;
            });
            emoji.setUseSystemDefault(mUseSystemDefault);
            emoji.setText(emojiEntity.getEmojicon().getEmoji());
        } else if (entity.getItemType() == LabelEntity.ITEM_TYPE) {
            LabelEntity labelEntity = (LabelEntity) entity;
            TextView label = baseViewHolder.getView(R.id.label);
            label.setText(labelEntity.getLabel());
        }
    }
}
