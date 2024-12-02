package com.xtree.mine.vo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CancelGame {
    @SerializedName(value = "message", alternate = {"msg", "sMsg"})
    @Expose
    public String message; // "页面超时！请重试。",
}
