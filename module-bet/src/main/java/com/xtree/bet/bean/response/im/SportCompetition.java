package com.xtree.bet.bean.response.im;

import android.os.Parcel;

import com.xtree.base.vo.BaseBean;

public class SportCompetition implements BaseBean {

    public int competitionID;
    public String competitionName;
    public int count;
    public int earlyFECount;
    public int todayFECount;
    public int rbfeCount;
    public int orCount;
    public boolean isHasLive;

    public SportCompetition() {}

    protected SportCompetition(Parcel in) {
        competitionID = in.readInt();
        competitionName = in.readString();
        count = in.readInt();
        earlyFECount = in.readInt();
        todayFECount = in.readInt();
        rbfeCount = in.readInt();
        orCount = in.readInt();
        isHasLive = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(competitionID);
        dest.writeString(competitionName);
        dest.writeInt(count);
        dest.writeInt(earlyFECount);
        dest.writeInt(todayFECount);
        dest.writeInt(rbfeCount);
        dest.writeInt(orCount);
        dest.writeByte((byte) (isHasLive ? 1 : 0));
    }

    public void readFromParcel(Parcel source) {
        competitionID = source.readInt();
        competitionName = source.readString();
        count = source.readInt();
        earlyFECount = source.readInt();
        todayFECount = source.readInt();
        rbfeCount = source.readInt();
        orCount = source.readInt();
        isHasLive = source.readByte() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SportCompetition> CREATOR = new Creator<SportCompetition>() {
        @Override
        public SportCompetition createFromParcel(Parcel in) {
            return new SportCompetition(in);
        }

        @Override
        public SportCompetition[] newArray(int size) {
            return new SportCompetition[size];
        }
    };
}
