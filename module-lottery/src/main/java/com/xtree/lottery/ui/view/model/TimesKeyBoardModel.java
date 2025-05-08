package com.xtree.lottery.ui.view.model;

import com.xtree.base.mvvm.recyclerview.BindModel;

public class TimesKeyBoardModel extends BindModel {


    public String key;

    public KeyTag keyTag;

    public TimesKeyBoardModel(String key, KeyTag keyTag) {
        this.key = key;
        this.keyTag = keyTag;
    }

    public TimesKeyBoardModel(KeyTag keyTag) {
        this.keyTag = keyTag;
    }

    public enum KeyTag {
        NUMBER, TIMES10, TIMES50, TIMES100, DELETE, DONE
    }
}
