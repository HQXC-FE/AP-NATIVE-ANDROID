package com.xtree.lottery.data.source.vo;

import com.google.gson.annotations.SerializedName;

public class SimulatedNumber {

    @SerializedName(value = "number")
    private String number; // null, "2"

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
