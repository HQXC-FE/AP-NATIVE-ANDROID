package com.xtree.lottery.ui.view.model;

import com.xtree.base.mvvm.recyclerview.BindModel;

import java.util.List;

/**
 * Created by KAKA on 2024/5/6.
 * Describe: 数字选注itemdata
 */
public class BetDigitalModel extends BindModel {

    public Boolean isButton;
    private String tag = "";
    private List<LotteryPickModel> pickDatas;

    public BetDigitalModel(String tag, List<LotteryPickModel> pickDatas, boolean isButton) {
        this.tag = tag;
        this.pickDatas = pickDatas;
        this.isButton = isButton;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public List<LotteryPickModel> getPickDatas() {
        return pickDatas;
    }

    public void setPickDatas(List<LotteryPickModel> pickDatas) {
        this.pickDatas = pickDatas;
    }
}
