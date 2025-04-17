package com.xtree.bet.bean.response.im;

import android.os.Parcel;

import com.xtree.base.vo.BaseBean;

public class StatementRsp implements BaseBean {

    private double AvailableBalance;
    private double OutstandingBalance;
    private String ServerTime;
    private int StatusCode;
    private String StatusDesc;

    public double getAvailableBalance() {
        return AvailableBalance;
    }

    public void setAvailableBalance(double availableBalance) {
        AvailableBalance = availableBalance;
    }

    public double getOutstandingBalance() {
        return OutstandingBalance;
    }

    public void setOutstandingBalance(double outstandingBalance) {
        OutstandingBalance = outstandingBalance;
    }

    public String getServerTime() {
        return ServerTime;
    }

    public void setServerTime(String serverTime) {
        ServerTime = serverTime;
    }

    public int getStatusCode() {
        return StatusCode;
    }

    public void setStatusCode(int statusCode) {
        StatusCode = statusCode;
    }

    public String getStatusDesc() {
        return StatusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        StatusDesc = statusDesc;
    }

    public StatementRsp() {
    }

    protected StatementRsp(Parcel in) {
        AvailableBalance = in.readDouble();
        OutstandingBalance = in.readDouble();
        ServerTime = in.readString();
        StatusCode = in.readInt();
        StatusDesc = in.readString();
    }

    public static final Creator<StatementRsp> CREATOR = new Creator<StatementRsp>() {
        @Override
        public StatementRsp createFromParcel(Parcel in) {
            return new StatementRsp(in);
        }

        @Override
        public StatementRsp[] newArray(int size) {
            return new StatementRsp[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(AvailableBalance);
        dest.writeDouble(OutstandingBalance);
        dest.writeString(ServerTime);
        dest.writeInt(StatusCode);
        dest.writeString(StatusDesc);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
