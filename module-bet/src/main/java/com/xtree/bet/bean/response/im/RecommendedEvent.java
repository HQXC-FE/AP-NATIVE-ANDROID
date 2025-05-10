package com.xtree.bet.bean.response.im;

import android.os.Parcel;


import com.google.gson.annotations.SerializedName;
import com.xtree.base.vo.BaseBean;

import java.util.List;

public class RecommendedEvent implements BaseBean {

    @SerializedName("OpenParlay")
    public boolean OpenParlay;
    @SerializedName("IsLive")
    public boolean IsLive;
    @SerializedName("Market")
    public int Market;
    @SerializedName("EventId")
    public long EventId;
    @SerializedName("EventName")
    public String EventName;
    @SerializedName("EventStatusId")
    public int EventStatusId;
    @SerializedName("OrderNumber")
    public int OrderNumber;
    @SerializedName("EventDate")
    public String EventDate;
    @SerializedName("HasVisualization")
    public boolean HasVisualization;
    @SerializedName("HasStatistic")
    public boolean HasStatistic;
    @SerializedName("BREventId")
    public int BREventId;
    @SerializedName("SourceId")
    public String SourceId;
    @SerializedName("TotalMarketLineCount")
    public int TotalMarketLineCount;
    @SerializedName("IsPopular")
    public boolean IsPopular;
    @SerializedName("Season")
    public int Season;
    @SerializedName("MatchDay")
    public int MatchDay;
    @SerializedName("LiveStreaming")
    public int LiveStreaming;
    @SerializedName("IsFavourite")
    public boolean IsFavourite;
    @SerializedName("GroundTypeId")
    public int GroundTypeId;
    @SerializedName("EventGroupId")
    public int EventGroupId;
    @SerializedName("EventGroupTypeId")
    public int EventGroupTypeId;
    @SerializedName("HomeTeamId")
    public int HomeTeamId;
    @SerializedName("HomeTeam")
    public String HomeTeam;
    public int AwayTeamId;
    public String AwayTeam;
    public String RBTime;
    public int RBTimeStatus;
    public int HomeScore;
    public int AwayScore;
    public int HomeRedCard;
    public int AwayRedCard;
    public boolean IsBetTradeOpen;
    public List<LiveStreamingUrl> LiveStreamingUrl;
    public Object RelatedScores;
    public String ExtraInfo;
    public Competition Competition;
    public Programme Programme;
    public List<MarketLine> MarketLines;


    protected RecommendedEvent(Parcel in) {
        // 读取布尔值（用byte存储）
        OpenParlay = in.readByte() != 0;
        IsLive = in.readByte() != 0;
        HasVisualization = in.readByte() != 0;
        HasStatistic = in.readByte() != 0;
        IsPopular = in.readByte() != 0;
        IsFavourite = in.readByte() != 0;
        IsBetTradeOpen = in.readByte() != 0;

        // 读取基本类型
        Market = in.readInt();
        BREventId = in.readInt();
        TotalMarketLineCount = in.readInt();
        Season = in.readInt();
        MatchDay = in.readInt();
        LiveStreaming = in.readInt();
        EventId = in.readInt();
        EventStatusId = in.readInt();
        OrderNumber = in.readInt();
        GroundTypeId = in.readInt();
        EventGroupId = in.readInt();
        EventGroupTypeId = in.readInt();
        HomeTeamId = in.readInt();
        AwayTeamId = in.readInt();
        RBTimeStatus = in.readInt();
        HomeScore = in.readInt();
        AwayScore = in.readInt();
        HomeRedCard = in.readInt();
        AwayRedCard = in.readInt();

        // 读取字符串和对象
        EventDate = in.readString();
        HomeTeam = in.readString();
        AwayTeam = in.readString();
        RBTime = in.readString();
        ExtraInfo = in.readString();

        // 读取可能为null的对象
        SourceId =  in.readString();
        RelatedScores = in.readValue(Object.class.getClassLoader());

        // 读取Parcelable对象
        Competition = in.readParcelable(Competition.class.getClassLoader());
        Programme = in.readParcelable(Programme.class.getClassLoader());

        // 读取集合
        LiveStreamingUrl = in.createTypedArrayList(com.xtree.bet.bean.response.im.LiveStreamingUrl.CREATOR);
        MarketLines = in.createTypedArrayList(MarketLine.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // 写入布尔值（转为byte）
        dest.writeByte((byte) (OpenParlay ? 1 : 0));
        dest.writeByte((byte) (IsLive ? 1 : 0));
        dest.writeByte((byte) (HasVisualization ? 1 : 0));
        dest.writeByte((byte) (HasStatistic ? 1 : 0));
        dest.writeByte((byte) (IsPopular ? 1 : 0));
        dest.writeByte((byte) (IsFavourite ? 1 : 0));
        dest.writeByte((byte) (IsBetTradeOpen ? 1 : 0));

        // 写入基本类型
        dest.writeInt(Market);
        dest.writeInt(BREventId);
        dest.writeInt(TotalMarketLineCount);
        dest.writeInt(Season);
        dest.writeInt(MatchDay);
        dest.writeInt(LiveStreaming);
        dest.writeLong(EventId);
        dest.writeInt(EventStatusId);
        dest.writeInt(OrderNumber);
        dest.writeInt(GroundTypeId);
        dest.writeInt(EventGroupId);
        dest.writeInt(EventGroupTypeId);
        dest.writeInt(HomeTeamId);
        dest.writeInt(AwayTeamId);
        dest.writeInt(RBTimeStatus);
        dest.writeInt(HomeScore);
        dest.writeInt(AwayScore);
        dest.writeInt(HomeRedCard);
        dest.writeInt(AwayRedCard);

        // 写入字符串和对象
        dest.writeString(EventDate);
        dest.writeString(HomeTeam);
        dest.writeString(AwayTeam);
        dest.writeString(RBTime);
        dest.writeString(ExtraInfo);

        // 写入可能为null的对象
        dest.writeValue(SourceId);
        dest.writeValue(RelatedScores);

        // 写入Parcelable对象
        dest.writeParcelable(Competition, flags);
        dest.writeParcelable(Programme, flags);

        // 写入集合
        dest.writeList(LiveStreamingUrl);
        dest.writeList(MarketLines);
    }



    @Override
    public int describeContents() {
        return 0;
    }



    public static final Creator<RecommendedEvent> CREATOR = new Creator<RecommendedEvent>() {
        @Override
        public RecommendedEvent createFromParcel(Parcel in) {
            return new RecommendedEvent(in);
        }

        @Override
        public RecommendedEvent[] newArray(int size) {
            return new RecommendedEvent[size];
        }
    };

    @Override
    public String toString() {
        return "RecommendedEvent{" +
                "OpenParlay=" + OpenParlay +
                ", IsLive=" + IsLive +
                ", Market=" + Market +
                ", HasVisualization=" + HasVisualization +
                ", HasStatistic=" + HasStatistic +
                ", BREventId=" + BREventId +
                ", SourceId='" + SourceId + '\'' +
                ", TotalMarketLineCount=" + TotalMarketLineCount +
                ", IsPopular=" + IsPopular +
                ", Season=" + Season +
                ", MatchDay=" + MatchDay +
                ", LiveStreaming=" + LiveStreaming +
                ", IsFavourite=" + IsFavourite +
                ", EventId=" + EventId +
                ", EventStatusId=" + EventStatusId +
                ", OrderNumber=" + OrderNumber +
                ", EventDate='" + EventDate + '\'' +
                ", GroundTypeId=" + GroundTypeId +
                ", EventGroupId=" + EventGroupId +
                ", EventGroupTypeId=" + EventGroupTypeId +
                ", HomeTeamId=" + HomeTeamId +
                ", HomeTeam='" + HomeTeam + '\'' +
                ", AwayTeamId=" + AwayTeamId +
                ", AwayTeam='" + AwayTeam + '\'' +
                ", RBTime='" + RBTime + '\'' +
                ", RBTimeStatus=" + RBTimeStatus +
                ", HomeScore=" + HomeScore +
                ", AwayScore=" + AwayScore +
                ", HomeRedCard=" + HomeRedCard +
                ", AwayRedCard=" + AwayRedCard +
                ", IsBetTradeOpen=" + IsBetTradeOpen +
                ", LiveStreamingUrl=" + LiveStreamingUrl +
                ", RelatedScores=" + RelatedScores +
                ", ExtraInfo='" + ExtraInfo + '\'' +
                ", Competition=" + Competition +
                ", Programme=" + Programme +
                ", MarketLines=" + MarketLines +
                '}';
    }


}
