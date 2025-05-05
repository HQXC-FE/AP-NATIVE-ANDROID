package com.xtree.bet.bean.response.im;

import android.os.Parcel;
import android.os.Parcelable;

import com.xtree.base.vo.BaseBean;
import com.xtree.bet.bean.response.fb.LeagueInfo;
import com.xtree.bet.bean.response.fb.MatchTimeInfo;
import com.xtree.bet.bean.response.fb.PlayTypeInfo;
import com.xtree.bet.bean.response.fb.ScoreInfo;
import com.xtree.bet.bean.response.fb.ScoreboardInfo;
import com.xtree.bet.bean.response.fb.TeamInfo;
import com.xtree.bet.bean.response.fb.VideoInfo;

import java.util.List;

public class MatchInfo implements BaseBean {
    public int sportId;
    public String sportName;
    public int orderNumber;
    public List<EventGroupType> eventGroupTypes;
    public List<Object> programmeList; // 注意：Object 类型建议根据实际结构替换
    public boolean openParlay;
    public boolean isHasLive;
    public int earlyFECount;
    public int todayFECount;
    public int orCount;
    public int rbFECount;
    public int count;
    public boolean isCombo;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(sportId);
        dest.writeString(sportName);
        dest.writeInt(orderNumber);
        dest.writeTypedList(eventGroupTypes);
        dest.writeList(programmeList);
        dest.writeByte((byte) (openParlay ? 1 : 0));
        dest.writeByte((byte) (isHasLive ? 1 : 0));
        dest.writeInt(earlyFECount);
        dest.writeInt(todayFECount);
        dest.writeInt(orCount);
        dest.writeInt(rbFECount);
        dest.writeInt(count);
        dest.writeByte((byte) (isCombo ? 1 : 0));
    }

    public void readFromParcel(Parcel in) {
        this.sportId = in.readInt();
        this.sportName = in.readString();
        this.orderNumber = in.readInt();
        this.eventGroupTypes = in.createTypedArrayList(EventGroupType.CREATOR);
        this.programmeList = in.readArrayList(Object.class.getClassLoader());
        this.openParlay = in.readByte() != 0;
        this.isHasLive = in.readByte() != 0;
        this.earlyFECount = in.readInt();
        this.todayFECount = in.readInt();
        this.orCount = in.readInt();
        this.rbFECount = in.readInt();
        this.count = in.readInt();
        this.isCombo = in.readByte() != 0;
    }

    public MatchInfo() {
    }

    protected MatchInfo(Parcel in) {
        sportId = in.readInt();
        sportName = in.readString();
        orderNumber = in.readInt();
        eventGroupTypes = in.createTypedArrayList(EventGroupType.CREATOR);
        programmeList = in.readArrayList(Object.class.getClassLoader()); // 使用真实类型更安全
        openParlay = in.readByte() != 0;
        isHasLive = in.readByte() != 0;
        earlyFECount = in.readInt();
        todayFECount = in.readInt();
        orCount = in.readInt();
        rbFECount = in.readInt();
        count = in.readInt();
        isCombo = in.readByte() != 0;
    }

    public static final Creator<MatchInfo> CREATOR = new Creator<MatchInfo>() {
        @Override
        public MatchInfo createFromParcel(Parcel source) {
            return new MatchInfo(source);
        }

        @Override
        public MatchInfo[] newArray(int size) {
            return new MatchInfo[size];
        }
    };

    public static class EventGroupType implements Parcelable {
        public int eventGroupTypeId;
        public int count;
        public int earlyFECount;
        public int todayFECount;
        public int rbFECount;
        public int orCount;
        public boolean isHasLive;

        public EventGroupType() {}

        protected EventGroupType(Parcel in) {
            eventGroupTypeId = in.readInt();
            count = in.readInt();
            earlyFECount = in.readInt();
            todayFECount = in.readInt();
            rbFECount = in.readInt();
            orCount = in.readInt();
            isHasLive = in.readByte() != 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(eventGroupTypeId);
            dest.writeInt(count);
            dest.writeInt(earlyFECount);
            dest.writeInt(todayFECount);
            dest.writeInt(rbFECount);
            dest.writeInt(orCount);
            dest.writeByte((byte) (isHasLive ? 1 : 0));
        }

        public void readFromParcel(Parcel in) {
            this.eventGroupTypeId = in.readInt();
            this.count = in.readInt();
            this.earlyFECount = in.readInt();
            this.todayFECount = in.readInt();
            this.rbFECount = in.readInt();
            this.orCount = in.readInt();
            this.isHasLive = in.readByte() != 0;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<EventGroupType> CREATOR = new Creator<EventGroupType>() {
            @Override
            public EventGroupType createFromParcel(Parcel in) {
                return new EventGroupType(in);
            }

            @Override
            public EventGroupType[] newArray(int size) {
                return new EventGroupType[size];
            }
        };
    }
}
