package io.github.rockerhieu.emojicon.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import io.github.rockerhieu.emojicon.R;
import io.github.rockerhieu.emojicon.emoji.Emojicon;

public class EmojiEntity implements MultiItemEntity {
    public static final int ITEM_TYPE = R.layout.emojicon_item;
    private Emojicon emojicon;

    public EmojiEntity(Emojicon emojicon) {
        this.emojicon = emojicon;
    }

    public Emojicon getEmojicon() {
        return emojicon;
    }

    public void setEmojicon(Emojicon emojicon) {
        this.emojicon = emojicon;
    }

    @Override
    public int getItemType() {
        return ITEM_TYPE;
    }


}
