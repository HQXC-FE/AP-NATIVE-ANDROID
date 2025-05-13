package com.xtree.bet.bean.response.im;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.xtree.base.vo.BaseBean;

public class Competition implements BaseBean, Parcelable {
    @SerializedName("CompetitionId")
    public int competitionId;
    @SerializedName("CompetitionName")
    public String competitionName;
    @SerializedName("CompetitionTypeId")
    public int competitionTypeId;
    @SerializedName("PMOrderNumber")
    public int pmOrderNumber;
    @SerializedName("RBOrderNumber")
    public int rbOrderNumber;

    public Competition() {
    }

    protected Competition(Parcel in) {
        competitionId = in.readInt();
        competitionName = in.readString();
        competitionTypeId = in.readInt();
        pmOrderNumber = in.readInt();
        rbOrderNumber = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(competitionId);
        dest.writeString(competitionName);
        dest.writeInt(competitionTypeId);
        dest.writeInt(pmOrderNumber);
        dest.writeInt(rbOrderNumber);
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

    // Getters and Setters
    public int getCompetitionId() {
        return competitionId;
    }

    public void setCompetitionId(int competitionId) {
        this.competitionId = competitionId;
    }

    public String getCompetitionName() {
        return competitionName;
    }

    public void setCompetitionName(String competitionName) {
        this.competitionName = competitionName;
    }

    public int getCompetitionTypeId() {
        return competitionTypeId;
    }

    public void setCompetitionTypeId(int competitionTypeId) {
        this.competitionTypeId = competitionTypeId;
    }

    public int getPmOrderNumber() {
        return pmOrderNumber;
    }

    public void setPmOrderNumber(int pmOrderNumber) {
        this.pmOrderNumber = pmOrderNumber;
    }

    public int getRbOrderNumber() {
        return rbOrderNumber;
    }

    public void setRbOrderNumber(int rbOrderNumber) {
        this.rbOrderNumber = rbOrderNumber;
    }
}
