package com.xtree.bet.bean.response.im;

import android.os.Parcel;


import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import com.xtree.base.vo.BaseBean;

import java.util.List;

public class RecommendedEvent implements BaseBean {

    @SerializedName("OpenParlay")
    public boolean openParlay;
    @SerializedName("IsLive")
    public boolean isLive;
    @SerializedName("Market")
    public int market;
    @SerializedName("EventId")
    public long eventId;
    @SerializedName("EventName")
    public String eventName;
    @SerializedName("EventStatusId")
    public int eventStatusId;
    @SerializedName("OrderNumber")
    public int orderNumber;
    @SerializedName("EventDate")
    public String eventDate;
    @SerializedName("HasVisualization")
    public boolean hasVisualization;
    @SerializedName("HasStatistic")
    public boolean hasStatistic;
    @SerializedName("BREventId")
    public int brEventId;
    @SerializedName("SourceId")
    public String sourceId;
    @SerializedName("TotalMarketLineCount")
    public int totalMarketLineCount;
    @SerializedName("IsPopular")
    public boolean isPopular;
    @SerializedName("Season")
    public int season;
    @SerializedName("MatchDay")
    public int matchDay;
    @SerializedName("LiveStreaming")
    public int liveStreaming;
    @SerializedName("IsFavourite")
    public boolean isFavourite;
    @SerializedName("GroundTypeId")
    public int groundTypeId;
    @SerializedName("EventGroupId")
    public int eventGroupId;
    @SerializedName("EventGroupTypeId")
    public int eventGroupTypeId;
    @SerializedName("HomeTeamId")
    public int homeTeamId;
    @SerializedName("HomeTeam")
    public String homeTeam;

    @SerializedName("AwayTeamId")
    public int awayTeamId;

    @SerializedName("awayTeam")
    public String awayTeam;

    @SerializedName("RbTime")
    public String rbTime;

    @SerializedName("RbTimeStatus")
    public int rbTimeStatus;

    @SerializedName("HomeScore")
    public int homeScore;

    @SerializedName("AwayScore")
    public int awayScore;


    @SerializedName("HomeRedCard")
    public int homeRedCard;
    @SerializedName("AwayRedCard")
    public int awayRedCard;

    @SerializedName("IsBetTradeOpen")
    public boolean isBetTradeOpen;

    @SerializedName("liveStreamingUrl")
    public List<LiveStreamingUrl> liveStreamingUrl;

    @SerializedName("RelatedScores")
    public Object relatedScores;

    @SerializedName("extraInfo")
    public String extraInfo;

    @SerializedName("competition")
    public Competition competition;

    @SerializedName("programme")
    public Programme programme;

    @SerializedName("marketLines")
    public List<MarketLine> MarketLines;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {

    }


    public static final Creator<RecommendedEvent> CREATOR = new Creator<RecommendedEvent>() {

        @Override
        public RecommendedEvent createFromParcel(Parcel source) {
            return null;
        }

        @Override
        public RecommendedEvent[] newArray(int size) {
            return new RecommendedEvent[size];
        }
    };


    public boolean isOpenParlay() {
        return openParlay;
    }

    public void setOpenParlay(boolean openParlay) {
        this.openParlay = openParlay;
    }

    public boolean isLive() {
        return isLive;
    }

    public void setLive(boolean live) {
        isLive = live;
    }

    public int getMarket() {
        return market;
    }

    public void setMarket(int market) {
        this.market = market;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public int getEventStatusId() {
        return eventStatusId;
    }

    public void setEventStatusId(int eventStatusId) {
        this.eventStatusId = eventStatusId;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public boolean isHasVisualization() {
        return hasVisualization;
    }

    public void setHasVisualization(boolean hasVisualization) {
        this.hasVisualization = hasVisualization;
    }

    public boolean isHasStatistic() {
        return hasStatistic;
    }

    public void setHasStatistic(boolean hasStatistic) {
        this.hasStatistic = hasStatistic;
    }

    public int getBrEventId() {
        return brEventId;
    }

    public void setBrEventId(int brEventId) {
        this.brEventId = brEventId;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public int getTotalMarketLineCount() {
        return totalMarketLineCount;
    }

    public void setTotalMarketLineCount(int totalMarketLineCount) {
        this.totalMarketLineCount = totalMarketLineCount;
    }

    public boolean isPopular() {
        return isPopular;
    }

    public void setPopular(boolean popular) {
        isPopular = popular;
    }

    public int getSeason() {
        return season;
    }

    public void setSeason(int season) {
        this.season = season;
    }

    public int getMatchDay() {
        return matchDay;
    }

    public void setMatchDay(int matchDay) {
        this.matchDay = matchDay;
    }

    public int getLiveStreaming() {
        return liveStreaming;
    }

    public void setLiveStreaming(int liveStreaming) {
        this.liveStreaming = liveStreaming;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    public int getGroundTypeId() {
        return groundTypeId;
    }

    public void setGroundTypeId(int groundTypeId) {
        this.groundTypeId = groundTypeId;
    }

    public int getEventGroupId() {
        return eventGroupId;
    }

    public void setEventGroupId(int eventGroupId) {
        this.eventGroupId = eventGroupId;
    }

    public int getEventGroupTypeId() {
        return eventGroupTypeId;
    }

    public void setEventGroupTypeId(int eventGroupTypeId) {
        this.eventGroupTypeId = eventGroupTypeId;
    }

    public int getHomeTeamId() {
        return homeTeamId;
    }

    public void setHomeTeamId(int homeTeamId) {
        this.homeTeamId = homeTeamId;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public int getAwayTeamId() {
        return awayTeamId;
    }

    public void setAwayTeamId(int awayTeamId) {
        this.awayTeamId = awayTeamId;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(String awayTeam) {
        this.awayTeam = awayTeam;
    }

    public String getRbTime() {
        return rbTime;
    }

    public void setRbTime(String rbTime) {
        this.rbTime = rbTime;
    }

    public int getRbTimeStatus() {
        return rbTimeStatus;
    }

    public void setRbTimeStatus(int rbTimeStatus) {
        this.rbTimeStatus = rbTimeStatus;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(int homeScore) {
        this.homeScore = homeScore;
    }

    public int getAwayScore() {
        return awayScore;
    }

    public void setAwayScore(int awayScore) {
        this.awayScore = awayScore;
    }

    public int getHomeRedCard() {
        return homeRedCard;
    }

    public void setHomeRedCard(int homeRedCard) {
        this.homeRedCard = homeRedCard;
    }

    public int getAwayRedCard() {
        return awayRedCard;
    }

    public void setAwayRedCard(int awayRedCard) {
        this.awayRedCard = awayRedCard;
    }

    public boolean isBetTradeOpen() {
        return isBetTradeOpen;
    }

    public void setBetTradeOpen(boolean betTradeOpen) {
        isBetTradeOpen = betTradeOpen;
    }

    public List<LiveStreamingUrl> getLiveStreamingUrl() {
        return liveStreamingUrl;
    }

    public void setLiveStreamingUrl(List<LiveStreamingUrl> liveStreamingUrl) {
        this.liveStreamingUrl = liveStreamingUrl;
    }

    public Object getRelatedScores() {
        return relatedScores;
    }

    public void setRelatedScores(Object relatedScores) {
        this.relatedScores = relatedScores;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    public Competition getCompetition() {
        return competition;
    }

    public void setCompetition(Competition competition) {
        this.competition = competition;
    }

    public Programme getProgramme() {
        return programme;
    }

    public void setProgramme(Programme programme) {
        this.programme = programme;
    }

    public List<MarketLine> getMarketLines() {
        return MarketLines;
    }

    public void setMarketLines(List<MarketLine> marketLines) {
        MarketLines = marketLines;
    }
}
