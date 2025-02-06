package com.xtree.lottery.ui.view.model;

import androidx.databinding.ObservableField;

import com.xtree.base.mvvm.recyclerview.BindModel;

/**
 * Created by KAKA on 2024/5/6.
 * Describe:
 */
public class BetDxdsTagModel extends BindModel {
    public ObservableField<Boolean> clicked = new ObservableField<>(false);
    private String tag;
    private String code;

    public BetDxdsTagModel(String tag, String code) {
        this.tag = tag;
        this.code = code;
    }

    public BetDxdsTagModel(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getCode() {
        return code;
    }
}
