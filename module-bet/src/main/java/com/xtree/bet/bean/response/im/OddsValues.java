package com.xtree.bet.bean.response.im;

import android.os.Parcel;

import com.xtree.base.vo.BaseBean;

public class OddsValues implements BaseBean {
    public String A;
    public String B;
    public String C;
    public String D;
    public String E;
    public String F;
    public String G;

    protected OddsValues(Parcel in) {
        A = in.readString();
        B = in.readString();
        C = in.readString();
        D = in.readString();
        E = in.readString();
        F = in.readString();
        G = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(A);
        dest.writeString(B);
        dest.writeString(C);
        dest.writeString(D);
        dest.writeString(E);
        dest.writeString(F);
        dest.writeString(G);
    }

    public static final Creator<OddsValues> CREATOR = new Creator<OddsValues>() {
        @Override
        public OddsValues createFromParcel(Parcel in) {
            return new OddsValues(in);
        }

        @Override
        public OddsValues[] newArray(int size) {
            return new OddsValues[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
}