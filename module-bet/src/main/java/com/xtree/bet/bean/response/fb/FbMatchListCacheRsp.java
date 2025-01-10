package com.xtree.bet.bean.response.fb;

public class FbMatchListCacheRsp {
    private int code;
    private String msg;
    public MatchListRsp data = new MatchListRsp();

    public MatchListRsp getData() {
        return data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }


}
