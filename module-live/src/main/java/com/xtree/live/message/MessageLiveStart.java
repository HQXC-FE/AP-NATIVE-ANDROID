package com.xtree.live.message;

public class MessageLiveStart extends MessageControl {
    private String filiterUserType;
    private String fd;
    private String userid;

    public String getFiliterUserType() {
        return filiterUserType;
    }

    public void setFiliterUserType(String filiterUserType) {
        this.filiterUserType = filiterUserType;
    }

    public String getFd() {
        return fd;
    }

    public void setFd(String fd) {
        this.fd = fd;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }


}
