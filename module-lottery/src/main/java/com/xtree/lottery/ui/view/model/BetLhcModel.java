package com.xtree.lottery.ui.view.model;

import com.xtree.base.mvvm.recyclerview.BindModel;

/**
 * Created by KAKA on 2024/5/6.
 * Describe: 数字选注itemdata
 */
public class BetLhcModel extends BindModel {
    public String number = "?";
    public int index;

    public BetLhcModel(String number) {
        this.number = number;
    }

    public BetLhcModel(String number, int index) {
        this.number = number;
        this.index = index;
    }
}
