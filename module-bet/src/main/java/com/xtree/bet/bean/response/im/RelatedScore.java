package com.xtree.bet.bean.response.im;


import android.os.Parcel;

import com.google.gson.annotations.SerializedName;
import com.xtree.base.vo.BaseBean;

public class RelatedScore implements BaseBean {
    @SerializedName("EventGroupTypeId")
    public int eventGroupTypeId;

    @SerializedName("HomeScore")
    public int homeScore;

    @SerializedName("AwayScore")
    public int awayScore;

    @SerializedName("HomeRedCard")
    public String homeRedCard;

    @SerializedName("AwayRedCard")
    public String awayRedCard;

    protected RelatedScore(Parcel in) {
        eventGroupTypeId = in.readInt();
        homeScore = in.readInt();
        awayScore = in.readInt();
        homeRedCard = in.readString();
        awayRedCard = in.readString();
    }

    public static final Creator<RelatedScore> CREATOR = new Creator<RelatedScore>() {
        @Override
        public RelatedScore createFromParcel(Parcel in) {
            return new RelatedScore(in);
        }

        @Override
        public RelatedScore[] newArray(int size) {
            return new RelatedScore[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(eventGroupTypeId);
        dest.writeInt(homeScore);
        dest.writeInt(awayScore);
        dest.writeString(homeRedCard);
        dest.writeString(awayRedCard);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}

