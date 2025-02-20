package com.xtree.live.data.source.response;

import java.io.Serializable;

public class LiveUserBean implements Serializable {

    /**
     * uid : 2
     * user_nickname :
     * avatar :
     * attention : 0
     * heat : 0
     * title : 测试
     * is_attention : 0
     * votestotal : 1
     * votestotal_icon : http://cunchu.mhuan.shop//ico/grade/level_anchor_1.png
     */

    private int id;
    private int uid;
    private String user_nickname;
    private String avatar;
    private int attention;
    private int heat;
    private String title;
    private int is_attention;
    private String votestotal;
    private String votestotal_icon;
    private String signature;
    private String announcement;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUser_nickname() {
        return user_nickname;
    }

    public void setUser_nickname(String user_nickname) {
        this.user_nickname = user_nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getAttention() {
        return attention;
    }

    public void setAttention(int attention) {
        this.attention = attention;
    }

    public int getHeat() {
        return heat;
    }

    public void setHeat(int heat) {
        this.heat = heat;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIs_attention() {
        return is_attention;
    }

    public void setIs_attention(int is_attention) {
        this.is_attention = is_attention;
    }

    public String getVotestotal() {
        return votestotal;
    }

    public void setVotestotal(String votestotal) {
        this.votestotal = votestotal;
    }

    public String getVotestotal_icon() {
        return votestotal_icon;
    }

    public void setVotestotal_icon(String votestotal_icon) {
        this.votestotal_icon = votestotal_icon;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getAnnouncement() {
        return announcement;
    }

    public void setAnnouncement(String announcement) {
        this.announcement = announcement;
    }
}

