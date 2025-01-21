package com.xtree.lottery.ui.view.model;

import com.xtree.base.mvvm.recyclerview.BindModel;

/**
 * Created by KAKA on 2024/5/6.
 * Describe: 数字选注itemdata
 */
public class BetDiceModel extends BindModel {
    public String number = "?";
    public int index;

    public BetDiceModel(String number) {
        this.number = number;
    }

    public BetDiceModel(String number, int index) {
        this.number = number;
        this.index = index;
    }
}
