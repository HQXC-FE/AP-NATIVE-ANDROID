package com.xtree.bet.bean.response.im;

import android.os.Parcel;

import com.xtree.base.vo.BaseBean;

import java.util.List;

/**
 * 比赛信息
 */

public class SportMatch implements BaseBean {

    public int sportId;
    public String sportName;
    public int orderNumber;
    public List<EventGroupType> eventGroupTypes;
    public List<Programme> programmeList;
    public List<SportCompetition> competitionList;
    public List<FilterType> filterTypes;
    public boolean isCombo;
    public boolean isHasLive;
    public int earlyFECount;
    public int todayFECount;
    public int orCount;
    public int rbfeCount;
    public int count;

    public SportMatch() {
    }

    protected SportMatch(Parcel in) {
        sportId = in.readInt();
        sportName = in.readString();
        orderNumber = in.readInt();
        eventGroupTypes = in.createTypedArrayList(EventGroupType.CREATOR);
        programmeList = in.createTypedArrayList(Programme.CREATOR);
        competitionList = in.createTypedArrayList(SportCompetition.CREATOR);
        filterTypes = in.createTypedArrayList(FilterType.CREATOR);
        isCombo = in.readByte() != 0;
        isHasLive = in.readByte() != 0;
        earlyFECount = in.readInt();
        todayFECount = in.readInt();
        orCount = in.readInt();
        rbfeCount = in.readInt();
        count = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(sportId);
        dest.writeString(sportName);
        dest.writeInt(orderNumber);
        dest.writeTypedList(eventGroupTypes);
        dest.writeTypedList(programmeList);
        dest.writeTypedList(competitionList);
        dest.writeTypedList(filterTypes);
        dest.writeByte((byte) (isCombo ? 1 : 0));
        dest.writeByte((byte) (isHasLive ? 1 : 0));
        dest.writeInt(earlyFECount);
        dest.writeInt(todayFECount);
        dest.writeInt(orCount);
        dest.writeInt(rbfeCount);
        dest.writeInt(count);
    }

    public void readFromParcel(Parcel source) {
        sportId = source.readInt();
        sportName = source.readString();
        orderNumber = source.readInt();
        eventGroupTypes = source.createTypedArrayList(EventGroupType.CREATOR);
        programmeList = source.createTypedArrayList(Programme.CREATOR);
        competitionList = source.createTypedArrayList(SportCompetition.CREATOR);
        filterTypes = source.createTypedArrayList(FilterType.CREATOR);
        isCombo = source.readByte() != 0;
        isHasLive = source.readByte() != 0;
        earlyFECount = source.readInt();
        todayFECount = source.readInt();
        orCount = source.readInt();
        rbfeCount = source.readInt();
        count = source.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SportMatch> CREATOR = new Creator<SportMatch>() {
        @Override
        public SportMatch createFromParcel(Parcel in) {
            return new SportMatch(in);
        }

        @Override
        public SportMatch[] newArray(int size) {
            return new SportMatch[size];
        }
    };
}

