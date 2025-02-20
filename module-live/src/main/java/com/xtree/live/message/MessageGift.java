package com.xtree.live.message;

import com.google.gson.annotations.SerializedName;

public class MessageGift extends MessageControl implements MessageVid{

    @SerializedName("vid")
    private String vid;
    @SerializedName("seed")
    private String seed;
    @SerializedName("seedsy")
    private String seedsy;
    @SerializedName("gift_id")
    private String giftId;
    @SerializedName("sender_nickname")
    private String senderNickname;

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getSeed() {
        return seed;
    }

    public void setSeed(String seed) {
        this.seed = seed;
    }

    public String getSeedsy() {
        return seedsy;
    }

    public void setSeedsy(String seedsy) {
        this.seedsy = seedsy;
    }

    public String getGiftId() {
        return giftId;
    }

    public void setGiftId(String giftId) {
        this.giftId = giftId;
    }

    public String getSenderNickname() {
        return senderNickname;
    }

    public void setSenderNickname(String senderNickname) {
        this.senderNickname = senderNickname;
    }
}
