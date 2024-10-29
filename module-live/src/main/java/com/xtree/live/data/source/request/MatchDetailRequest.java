package com.xtree.live.data.source.request;

/**
 * Created by KAKA on 2024/10/22.
 * Describe:
 */
public class MatchDetailRequest {
    private String matchId;
    private String languageType="CMN";

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public String getLanguageType() {
        return languageType;
    }

    public void setLanguageType(String languageType) {
        this.languageType = languageType;
    }
}
