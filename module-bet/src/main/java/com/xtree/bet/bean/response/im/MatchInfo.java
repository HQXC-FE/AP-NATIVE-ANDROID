package com.xtree.bet.bean.response.im;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;
import com.xtree.base.vo.BaseBean;

import java.util.ArrayList;
import java.util.List;

public class MatchInfo implements BaseBean {
    /**
     * 现场状态信息
     */
    @SerializedName("LiveStatusInfo")
    public String liveStatusInfo;
    /**
     * 现场状态信息的ID
     */
    @SerializedName("LiveStatusId")
    public String liveStatusId;
    /**
     * 玩法集id
     */
    @SerializedName("OpenParlay")
    public boolean openParlay;
    /**
     * 玩法集id
     */
    @SerializedName("IsLive")
    public boolean isLive;
    /**
     * 玩法集id
     */
    @SerializedName("Market")
    public int market;
    /**
     * 玩法集id
     */
    @SerializedName("HasVisualization")
    public boolean hasVisualization;
    /**
     * 玩法集id
     */
    @SerializedName("HasStatistic")
    public boolean hasStatistic;
    /**
     * 玩法集id
     */
    @SerializedName("BREventId")
    public long brEventId;
    /**
     * 玩法集id
     */
    @SerializedName("SourceId")
    public String sourceId;
    /**
     * 玩法集id
     */
    @SerializedName("TotalMarketLineCount")
    public int totalMarketLineCount;
    /**
     * 玩法集id
     */
    @SerializedName("IsPopular")
    public boolean isPopular;
    /**
     * 玩法集id
     */
    @SerializedName("Season")
    public int season;
    /**
     * 玩法集id
     */
    @SerializedName("MatchDay")
    public int matchDay;
    /**
     * 玩法集id
     */
    @SerializedName("LiveStreaming")
    public int liveStreaming;
    /**
     * 玩法集id
     */
    @SerializedName("IsFavourite")
    public boolean isFavourite;
    /**
     * 玩法集id
     */
    @SerializedName("EventId")
    public long eventId;
    /**
     * 玩法集id
     */
    @SerializedName("EventStatusId")
    public int eventStatusId;
    /**
     * 玩法集id
     */
    @SerializedName("OrderNumber")
    public int orderNumber;

    @SerializedName("EventDate")
    public String eventDate;

    @SerializedName("GroundTypeId")
    public int groundTypeId;

    @SerializedName("EventGroupId")
    public long eventGroupId;

    @SerializedName("EventGroupTypeId")
    public int eventGroupTypeId;

    @SerializedName("HomeTeamId")
    public int homeTeamId;

    @SerializedName("HomeTeam")
    public String homeTeam;

    @SerializedName("AwayTeamId")
    public int awayTeamId;

    @SerializedName("AwayTeam")
    public String awayTeam;

    @SerializedName("RBTime")
    public String rbTime;

    @SerializedName("RBTimeStatus")
    public int rbTimeStatus;

    @SerializedName("HomeScore")
    public String homeScore;

    @SerializedName("AwayScore")
    public String awayScore;

    @SerializedName("HomeRedCard")
    public String homeRedCard;

    @SerializedName("AwayRedCard")
    public String awayRedCard;

    @SerializedName("IsBetTradeOpen")
    public boolean isBetTradeOpen;

    @SerializedName("LiveStreamingUrl")
    public List<LiveStreamingUrl> liveStreamingUrl;

    @SerializedName("RelatedScores")
    public List<RelatedScore> relatedScores;

    @SerializedName("ExtraInfo")
    public String extraInfo;

    @SerializedName("RSportId")
    public int rSportId;

    @SerializedName("EventName")
    public String eventName;

    @SerializedName("EventNamePY")
    public String eventNamePY;

    @SerializedName("EventNameList")
    public String eventNameList;

    @SerializedName("RunnerAddInfo")
    public String runnerAddInfo;

    @SerializedName("IsMaintenance")
    public boolean isMaintenance;

    @SerializedName("Competition")
    public Competition competition;

    @SerializedName("Programme")
    public Programme programme;

    @SerializedName("AvailBetTypes")
    public List<Integer> availBetTypes;

    @SerializedName("MarketLines")
    public List<MarketLine> marketLines;

    public int sportId;

    public String sportName;

    protected MatchInfo(Parcel in) {
        liveStatusInfo = in.readString();
        liveStatusId = in.readString();
        openParlay = in.readByte() != 0;
        isLive = in.readByte() != 0;
        market = in.readInt();
        hasVisualization = in.readByte() != 0;
        hasStatistic = in.readByte() != 0;
        brEventId = in.readLong();
        sourceId = in.readString();
        totalMarketLineCount = in.readInt();
        isPopular = in.readByte() != 0;
        season = in.readInt();
        matchDay = in.readInt();
        liveStreaming = in.readInt();
        isFavourite = in.readByte() != 0;
        eventId = in.readLong();
        eventStatusId = in.readInt();
        orderNumber = in.readInt();
        //long eventDateLong = in.readLong();
        eventDate = in.readString();
        groundTypeId = in.readInt();
        eventGroupId = in.readLong();
        eventGroupTypeId = in.readInt();
        homeTeamId = in.readInt();
        homeTeam = in.readString();
        awayTeamId = in.readInt();
        awayTeam = in.readString();
        rbTime = in.readString();
        rbTimeStatus = in.readInt();
        homeScore = in.readString();
        awayScore = in.readString();
        homeRedCard = in.readString();
        awayRedCard = in.readString();
        isBetTradeOpen = in.readByte() != 0;
        liveStreamingUrl = in.createTypedArrayList(LiveStreamingUrl.CREATOR);
        relatedScores = in.createTypedArrayList(RelatedScore.CREATOR);
        extraInfo = in.readString();
        rSportId = in.readInt();
        eventName = in.readString();
        eventNamePY = in.readString();
        eventNameList = in.readString();
        runnerAddInfo = in.readString();
        isMaintenance = in.readByte() != 0;
        competition = in.readParcelable(Competition.class.getClassLoader());
        programme = in.readParcelable(Programme.class.getClassLoader());
        availBetTypes = new ArrayList<>();
        in.readList(availBetTypes, Integer.class.getClassLoader());
        marketLines = in.createTypedArrayList(MarketLine.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(liveStatusInfo);
        dest.writeString(liveStatusId);
        dest.writeByte((byte) (openParlay ? 1 : 0));
        dest.writeByte((byte) (isLive ? 1 : 0));
        dest.writeInt(market);
        dest.writeByte((byte) (hasVisualization ? 1 : 0));
        dest.writeByte((byte) (hasStatistic ? 1 : 0));
        dest.writeLong(brEventId);
        dest.writeString(sourceId);
        dest.writeInt(totalMarketLineCount);
        dest.writeByte((byte) (isPopular ? 1 : 0));
        dest.writeInt(season);
        dest.writeInt(matchDay);
        dest.writeInt(liveStreaming);
        dest.writeByte((byte) (isFavourite ? 1 : 0));
        dest.writeLong(eventId);
        dest.writeInt(eventStatusId);
        dest.writeInt(orderNumber);
        //dest.writeLong(eventDate != null ? eventDate.getTime() : -1);
        dest.writeString(eventDate);
        dest.writeInt(groundTypeId);
        dest.writeLong(eventGroupId);
        dest.writeInt(eventGroupTypeId);
        dest.writeInt(homeTeamId);
        dest.writeString(homeTeam);
        dest.writeInt(awayTeamId);
        dest.writeString(awayTeam);
        dest.writeString(rbTime);
        dest.writeInt(rbTimeStatus);
        dest.writeString(homeScore);
        dest.writeString(awayScore);
        dest.writeString(homeRedCard);
        dest.writeString(awayRedCard);
        dest.writeByte((byte) (isBetTradeOpen ? 1 : 0));
        dest.writeTypedList(liveStreamingUrl);
        dest.writeTypedList(relatedScores);
        dest.writeString(extraInfo);
        dest.writeInt(rSportId);
        dest.writeString(eventName);
        dest.writeString(eventNamePY);
        dest.writeString(eventNameList);
        dest.writeString(runnerAddInfo);
        dest.writeByte((byte) (isMaintenance ? 1 : 0));
        dest.writeParcelable(competition, flags);
        dest.writeParcelable(programme, flags);
        dest.writeList(availBetTypes);
        dest.writeTypedList(marketLines);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MatchInfo> CREATOR = new Creator<MatchInfo>() {
        @Override
        public MatchInfo createFromParcel(Parcel in) {
            return new MatchInfo(in);
        }

        @Override
        public MatchInfo[] newArray(int size) {
            return new MatchInfo[size];
        }
    };

    public void setSportId(int sportId) {
        this.sportId = sportId;
    }

    public int getSportId() {
        return sportId;
    }

    public void setSportName(String sportName) {
        this.sportName = sportName;
    }

    public String getSportName() {
        return  sportName;
    }
}
