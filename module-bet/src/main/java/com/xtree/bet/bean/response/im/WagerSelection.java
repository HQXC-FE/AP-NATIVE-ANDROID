package com.xtree.bet.bean.response.im;

import android.os.Parcel;

import com.xtree.base.vo.BaseBean;

import java.util.List;

public class WagerSelection implements BaseBean {

    public long WagerSelectionId;
    public int SelectionId;
    public String SelectionName;
    public String Handicap;
    public String Specifiers;
    public int OddsType;
    public double Odds;
    public List<OddsListItem> OddsList;

    public WagerSelection() {}

    protected WagerSelection(Parcel in) {
        WagerSelectionId = in.readLong();
        SelectionId = in.readInt();
        SelectionName = in.readString();
        Handicap = in.readString();
        Specifiers = in.readString();
        OddsType = in.readInt();
        Odds = in.readDouble();
        OddsList = in.createTypedArrayList(OddsListItem.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(WagerSelectionId);
        dest.writeInt(SelectionId);
        dest.writeString(SelectionName);
        dest.writeString(Handicap);
        dest.writeString(Specifiers);
        dest.writeInt(OddsType);
        dest.writeDouble(Odds);
        dest.writeTypedList(OddsList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WagerSelection> CREATOR = new Creator<WagerSelection>() {
        @Override
        public WagerSelection createFromParcel(Parcel in) {
            return new WagerSelection(in);
        }

        @Override
        public WagerSelection[] newArray(int size) {
            return new WagerSelection[size];
        }
    };
}
