package com.xtree.bet.bean.response.im;

import android.os.Parcel;
import android.os.Parcelable;

import com.xtree.base.vo.BaseBean;

public class Competition implements BaseBean{

    public int CompetitionId;
    public String CompetitionName;
    public int PMOrderNumber;
    public int RBOrderNumber;

    public Competition() {}

    protected Competition(Parcel in) {
        CompetitionId = in.readInt();
        CompetitionName = in.readString();
        PMOrderNumber = in.readInt();
        RBOrderNumber = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(CompetitionId);
        dest.writeString(CompetitionName);
        dest.writeInt(PMOrderNumber);
        dest.writeInt(RBOrderNumber);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Competition> CREATOR = new Creator<Competition>() {
        @Override
        public Competition createFromParcel(Parcel in) {
            return new Competition(in);
        }

        @Override
        public Competition[] newArray(int size) {
            return new Competition[size];
        }
    };
}
