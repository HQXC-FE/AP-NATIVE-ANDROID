package com.xtree.lottery.ui.view.model;

/**
 * Created by KAKA on 2024/5/3.
 * Describe:
 */
public class LotteryMoneyModel {
    private String name = "";
    private float rate = 0.0f;
    private int modelId = 0;

    public LotteryMoneyModel(String name, float rate, int modelId) {
        this.name = name;
        this.rate = rate;
        this.modelId = modelId;
    }

    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }
}
