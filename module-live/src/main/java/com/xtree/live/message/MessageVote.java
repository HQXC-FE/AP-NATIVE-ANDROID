package com.xtree.live.message;

import com.google.gson.annotations.SerializedName;

public class MessageVote extends Message{
    @SerializedName("userid")
    private int userid;
    @SerializedName("seed")
    private String seed;
    @SerializedName("activity_id")
    private int activityId;
    @SerializedName("vote_id")
    private int voteId;
    @SerializedName("vote_option_id")
    private int voteOptionId;

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getSeed() {
        return seed;
    }

    public void setSeed(String seed) {
        this.seed = seed;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public int getVoteId() {
        return voteId;
    }

    public void setVoteId(int voteId) {
        this.voteId = voteId;
    }

    public int getVoteOptionId() {
        return voteOptionId;
    }

    public void setVoteOptionId(int voteOptionId) {
        this.voteOptionId = voteOptionId;
    }
}

