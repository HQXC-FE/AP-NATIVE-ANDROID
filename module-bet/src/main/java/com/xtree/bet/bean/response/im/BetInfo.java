package com.xtree.bet.bean.response.im;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.xtree.base.vo.BaseBean;

import java.util.ArrayList;
import java.util.List;

public class BetInfo implements BaseBean {

    public int WagerType;
    public List<WagerSelectionInfo> WagerSelectionInfos;
    public String Token;
    public String MemberCode;
    public String TimeStamp;
    public int LanguageCode;
    public boolean IsCombo;

    public BetInfo() {}

    protected BetInfo(Parcel in) {
        WagerType = in.readInt();
        WagerSelectionInfos = new ArrayList<>();
        in.readList(WagerSelectionInfos, WagerSelectionInfo.class.getClassLoader());
        Token = in.readString();
        MemberCode = in.readString();
        TimeStamp = in.readString();
        LanguageCode = in.readInt();
        IsCombo = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(WagerType);
        dest.writeList(WagerSelectionInfos);
        dest.writeString(Token);
        dest.writeString(MemberCode);
        dest.writeString(TimeStamp);
        dest.writeInt(LanguageCode);
        dest.writeByte((byte) (IsCombo ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BetInfo> CREATOR = new Creator<BetInfo>() {
        @Override
        public BetInfo createFromParcel(Parcel in) {
            return new BetInfo(in);
        }

        @Override
        public BetInfo[] newArray(int size) {
            return new BetInfo[size];
        }
    };


}

