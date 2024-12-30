package com.xtree.mine.vo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeleteInterMessage {
    @SerializedName(value = "message", alternate = {"msg", "sMsg"})
    @Expose
    public String message; // "页面超时！请重试。",
    public int msg_type; // 1 (异常/失败的时候) 1,2-失败, 3-成功
}
