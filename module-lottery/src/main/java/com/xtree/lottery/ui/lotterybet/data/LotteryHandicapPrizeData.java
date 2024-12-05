package com.xtree.lottery.ui.lotterybet.data;

/**
 * Created by KAKA on 2024/12/5.
 * Describe:
 */
public class LotteryHandicapPrizeData {
    private String name;
    private int mode;

    public LotteryHandicapPrizeData(String name, int mode) {
        this.name = name;
        this.mode = mode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
}
