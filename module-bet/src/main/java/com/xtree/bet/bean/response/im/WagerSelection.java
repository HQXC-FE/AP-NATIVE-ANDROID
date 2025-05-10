package com.xtree.bet.bean.response.im;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;
import com.xtree.base.vo.BaseBean;

import java.util.Date;
import java.util.List;

public class WagerSelection implements BaseBean {

    @SerializedName("WagerSelectionId")
    private long wagerSelectionId;

    @SerializedName("SelectionId")
    private int selectionId;

    @SerializedName("SelectionName")
    private String selectionName;

    @SerializedName("Handicap")
    private double handicap;

    @SerializedName("DisplayHandicap")
    private String displayHandicap;

    @SerializedName("Specifiers")
    private String specifiers;

    @SerializedName("OddsType")
    private int oddsType;

    @SerializedName("Odds")
    private double odds;

    @SerializedName("PreBoostOdds")
    private String preBoostOdds;

    @SerializedName("OddsList")
    private List<OddsList> oddsList;

    public WagerSelection() {
    }

    protected WagerSelection(Parcel in) {
        wagerSelectionId = in.readLong();
        selectionId = in.readInt();
        selectionName = in.readString();
        handicap = in.readDouble();
        //long displayTime = in.readLong();
        displayHandicap = in.readString();
        specifiers = in.readString();
        oddsType = in.readInt();
        odds = in.readDouble();
        preBoostOdds = in.readString();
        oddsList = in.createTypedArrayList(OddsList.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(wagerSelectionId);
        dest.writeInt(selectionId);
        dest.writeString(selectionName);
        dest.writeDouble(handicap);
        dest.writeString(displayHandicap);
        dest.writeString(specifiers);
        dest.writeInt(oddsType);
        dest.writeDouble(odds);
        dest.writeString(preBoostOdds);
        dest.writeTypedList(oddsList);
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

    // Getter & Setter methods

    public long getWagerSelectionId() {
        return wagerSelectionId;
    }

    public void setWagerSelectionId(long wagerSelectionId) {
        this.wagerSelectionId = wagerSelectionId;
    }

    public int getSelectionId() {
        return selectionId;
    }

    public void setSelectionId(int selectionId) {
        this.selectionId = selectionId;
    }

    public String getSelectionName() {
        return selectionName;
    }

    public void setSelectionName(String selectionName) {
        this.selectionName = selectionName;
    }

    public double getHandicap() {
        return handicap;
    }

    public void setHandicap(double handicap) {
        this.handicap = handicap;
    }

    public String getDisplayHandicap() {
        return displayHandicap;
    }

    public void setDisplayHandicap(String displayHandicap) {
        this.displayHandicap = displayHandicap;
    }

    public String getSpecifiers() {
        return specifiers;
    }

    public void setSpecifiers(String specifiers) {
        this.specifiers = specifiers;
    }

    public int getOddsType() {
        return oddsType;
    }

    public void setOddsType(int oddsType) {
        this.oddsType = oddsType;
    }

    public double getOdds() {
        return odds;
    }

    public void setOdds(double odds) {
        this.odds = odds;
    }

    public String getPreBoostOdds() {
        return preBoostOdds;
    }

    public void setPreBoostOdds(String preBoostOdds) {
        this.preBoostOdds = preBoostOdds;
    }

    public List<OddsList> getOddsList() {
        return oddsList;
    }

    public void setOddsList(List<OddsList> oddsList) {
        this.oddsList = oddsList;
    }
}


