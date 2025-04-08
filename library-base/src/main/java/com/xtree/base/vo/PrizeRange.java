package com.xtree.base.vo;

public class PrizeRange {
    public float max;
    public float min;
    public float value;

    public PrizeRange(float max, float min, float value) {
        this.max = max;
        this.min = min;
        this.value = value;
    }

    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        this.max = max;
    }

    public float getMin() {
        return min;
    }

    public void setMin(float min) {
        this.min = min;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}

