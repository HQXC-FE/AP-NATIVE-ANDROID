package com.xtree.bet.bean.response.im;

import android.os.Parcel;

import com.xtree.base.vo.BaseBean;
import android.os.Parcelable;

public class Programme implements BaseBean, Parcelable {

    private int programmeId;
    private String programmeName;
    private int programmeOrderNumber;

    public Programme() {
    }

    protected Programme(Parcel in) {
        programmeId = in.readInt();
        programmeName = in.readString();
        programmeOrderNumber = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(programmeId);
        dest.writeString(programmeName);
        dest.writeInt(programmeOrderNumber);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Programme> CREATOR = new Creator<Programme>() {
        @Override
        public Programme createFromParcel(Parcel in) {
            return new Programme(in);
        }

        @Override
        public Programme[] newArray(int size) {
            return new Programme[size];
        }
    };

    // Getter & Setter
    public int getProgrammeId() {
        return programmeId;
    }

    public void setProgrammeId(int programmeId) {
        this.programmeId = programmeId;
    }

    public String getProgrammeName() {
        return programmeName;
    }

    public void setProgrammeName(String programmeName) {
        this.programmeName = programmeName;
    }

    public int getProgrammeOrderNumber() {
        return programmeOrderNumber;
    }

    public void setProgrammeOrderNumber(int programmeOrderNumber) {
        this.programmeOrderNumber = programmeOrderNumber;
    }
}

