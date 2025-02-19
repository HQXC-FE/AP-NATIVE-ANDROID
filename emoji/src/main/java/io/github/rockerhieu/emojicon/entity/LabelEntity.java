package io.github.rockerhieu.emojicon.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import io.github.rockerhieu.emojicon.R;

public class LabelEntity implements MultiItemEntity {
    public static final int ITEM_TYPE = R.layout.label_item;
    private String label;

    public LabelEntity(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public int getItemType() {
        return ITEM_TYPE;
    }
}
