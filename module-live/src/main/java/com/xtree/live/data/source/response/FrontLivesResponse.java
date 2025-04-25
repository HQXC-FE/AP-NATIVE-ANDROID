package com.xtree.live.data.source.response;

/**
 * Created by KAKA on 2024/10/23.
 * Describe:
 */
public class FrontLivesResponse {

    private int id;
    private String vid;
    private int uid;
    private int type;
    private int isLive;
    private int loadingBar;
    private String pull;
    private int matchId;
    private String title;
    private String thumb;
    private boolean isLoop;
    private String avatar;
    private String userNickname;
    private int heat;

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIsLive() {
        return isLive;
    }

    public void setIsLive(int isLive) {
        this.isLive = isLive;
    }

    public int getLoadingBar() {
        return loadingBar;
    }

    public void setLoadingBar(int loadingBar) {
        this.loadingBar = loadingBar;
    }

    public String getPull() {
        return pull;
    }

    public void setPull(String pull) {
        this.pull = pull;
    }

    public int getMatchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public boolean isLoop() {
        return isLoop;
    }

    public void setLoop(boolean isLoop) {
        this.isLoop = isLoop;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public int getHeat() {
        return heat;
    }

    public void setHeat(int heat) {
        this.heat = heat;
    }

    @Override
    public String toString() {
        return "FrontLivesResponse{" +
                "id=" + id +
                ", vid='" + vid + '\'' +
                ", uid=" + uid +
                ", type=" + type +
                ", isLive=" + isLive +
                ", loadingBar=" + loadingBar +
                ", pull='" + pull + '\'' +
                ", matchId=" + matchId +
                ", title='" + title + '\'' +
                ", thumb='" + thumb + '\'' +
                ", isLoop=" + isLoop +
                ", avatar='" + avatar + '\'' +
                ", userNickname='" + userNickname + '\'' +
                ", heat=" + heat +
                '}';
    }

}
