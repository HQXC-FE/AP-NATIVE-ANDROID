package com.xtree.lottery.ui.view.model;

import com.xtree.base.mvvm.recyclerview.BindModel;

import java.util.List;

/**
 * Created by KAKA on 2024/5/6.
 * Describe:
 */
public class BetDxdsModel extends BindModel {

    private String tag = "";

    private List<LotteryPickModel> pickDatas;

    public BetDxdsModel(String tag, List<LotteryPickModel> pickDatas) {
        this.tag = tag;
        this.pickDatas = pickDatas;
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
