package com.xtree.base.vo;

public class PrizeEntry {
    public float prize;
    public float value;

    public PrizeEntry(float prize, float value) {
        this.prize = prize;
        this.value = value;
    }

    public float getPrize() {
        return prize;
    }

    public void setPrize(float prize) {
        this.prize = prize;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
