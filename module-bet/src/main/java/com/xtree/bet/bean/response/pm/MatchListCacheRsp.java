package com.xtree.bet.bean.response.pm;


import java.util.ArrayList;
import java.util.List;

public class MatchListCacheRsp {
    private int code;
    private String msg;
    public List<MatchInfo> data = new ArrayList<>();

    public List<MatchInfo> getData() {
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
