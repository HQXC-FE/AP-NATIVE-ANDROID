package com.xtree.live.message;

import com.google.gson.annotations.SerializedName;

public class MessageOnline extends MessageControl {
    @SerializedName("room_type")
    private int roomType;
    @SerializedName("userid")
    private int userid;
    @SerializedName("usertype")
    private int usertype;
    @SerializedName("text")
    private int text;
    @SerializedName("seed")
    private String seed;
    @SerializedName("name")
    private String name;
    @SerializedName("seedsy")
    private String seedsy;
    @SerializedName("weight")
    private String weight;
    @SerializedName("method")
    private String method;


    public int getRoomType() {
        return roomType;
    }

    public void setRoomType(int roomType) {
        this.roomType = roomType;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getUsertype() {
        return usertype;
    }

    public void setUsertype(int usertype) {
        this.usertype = usertype;
    }

    public int getText() {
        return text;
    }

    public void setText(int text) {
        this.text = text;
    }

    public String getSeed() {
        return seed;
    }

    public void setSeed(String seed) {
        this.seed = seed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSeedsy() {
        return seedsy;
    }

    public void setSeedsy(String seedsy) {
        this.seedsy = seedsy;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
