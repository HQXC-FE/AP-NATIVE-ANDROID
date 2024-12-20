package com.xtree.bet.bean.response.pm;


import java.util.List;

public class MatchLeagueListCacheRsp {
    private int code;
    public Data data;
    private int pages;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public int getPages() {
        return pages;
    }

    public static class Data {
        private int code;
        private  List<MatchInfo> data; // List of integers
        private String msg;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public List<MatchInfo> getData() {
            return data;
        }

    }

}
