package com.xtree.mine.vo.WithdrawVo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * 提款提交结果
 */
public class WithdrawalSubmitVo {
    @SerializedName(value = "message", alternate = {"msg", "sMsg"})
    @Expose
    public String message; // "页面超时！请重试。",

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
