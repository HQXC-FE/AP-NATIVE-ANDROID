package com.xtree.bet.bean.response.im;

import android.os.Parcel;
import com.xtree.base.vo.BaseBean;
import java.util.List;

public class EventListRsp implements BaseBean {
    private List<Sport> Sports;
    private Long TempDelta;
    private Long Delta;
    private String ServerTime;

    public EventListRsp() {}

    protected EventListRsp(Parcel in) {
        Sports = in.createTypedArrayList(Sport.CREATOR);
        if (in.readByte() == 0) {
            TempDelta = null;
        } else {
            TempDelta = in.readLong();
        }
        Delta = in.readLong();
        ServerTime = in.readString();
    }

    public static final Creator<EventListRsp> CREATOR = new Creator<EventListRsp>() {
        @Override
        public EventListRsp createFromParcel(Parcel in) {
            return new EventListRsp(in);
        }

        @Override
        public EventListRsp[] newArray(int size) {
            return new EventListRsp[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(Sports);
        if (TempDelta == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(TempDelta);
        }
        dest.writeLong(Delta);
        dest.writeString(ServerTime);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Getter and Setter methods
    public List<Sport> getSports() {
        return Sports;
    }

    public void setSports(List<Sport> sports) {
        this.Sports = sports;
    }

    public Long getTempDelta() {
        return TempDelta;
    }

    public void setTempDelta(Long tempDelta) {
        this.TempDelta = tempDelta;
    }

    public Long getDelta() {
        return Delta;
    }

    public void setDelta(Long delta) {
        this.Delta = delta;
    }

    public String getServerTime() {
        return ServerTime;
    }

    public void setServerTime(String serverTime) {
        this.ServerTime = serverTime;
    }

    // toString 方法
    @Override
    public String toString() {
        return "EventListRsp{" +
                "Sports=" + (Sports != null ? Sports.size() + " sports" : "null") +
                ", TempDelta=" + TempDelta +
                ", Delta=" + Delta +
                ", ServerTime='" + ServerTime + '\'' +
                '}';
    }
}

