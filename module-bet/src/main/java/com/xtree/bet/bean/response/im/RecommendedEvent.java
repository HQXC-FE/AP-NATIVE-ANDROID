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
    public int awayTeamId;
    public String awayTeam;
    public String rbTime;
    public int rbTimeStatus;
    public int homeScore;
    public int awayScore;
    public int HomeRedCard;
    public int AwayRedCard;
    public boolean IsBetTradeOpen;
    public List<LiveStreamingUrl> LiveStreamingUrl;
    public Object RelatedScores;
    public String ExtraInfo;
    public Competition Competition;
    public Programme Programme;
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



}
