package com.xtree.live.player;

import java.util.List;

public class LiveVideoModel {
    private int matchId;
    private int type;
    private int gameId;

    private String dimensionRatio = "16:9";

    public int getMatchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    private String streamType;
    private List<String> qualityList;
    private @MediaQuality int qualityType = MediaQuality.SD;
    private int sourceType = StreamSourceType.SOURCE_FLV;
    private String loadingBar = "0";

    public @MediaQuality int getQualityType() {
        return qualityType;
    }

    public List<String> getQualityList() {
        return qualityList;
    }

    public String getLoadingBar() {
        return loadingBar;
    }

    public String getStreamType() {
        return streamType;
    }


    public void setLoadingBar(String loadingBar) {
        this.loadingBar = loadingBar;
    }


    public void setQualityList(List<String> qualityList) {
        this.qualityList = qualityList;
    }

    public void setQualityType(@MediaQuality int qualityType) {
        this.qualityType = qualityType;
    }

    public void setStreamType(String streamType) {
        this.streamType = streamType;
    }

    public @StreamSourceType int getSourceType() {
        return sourceType;
    }

    public void setSourceType(@StreamSourceType int sourceType) {
        this.sourceType = sourceType;
    }

    public String getDimensionRatio() {
        return dimensionRatio;
    }

    public void setDimensionRatio(String dimensionRatio) {
        this.dimensionRatio = dimensionRatio;
    }
}

