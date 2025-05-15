package com.xtree.bet.bean.response.im;

import android.os.Parcel;
import com.xtree.base.vo.BaseBean;

import java.util.List;

public class ChampionEventsRsp implements BaseBean {
    public List<MatchInfo> Events;

    public double Delta;
    public String ServerTime;
    public int StatusCode;
    public String StatusDesc;

    protected ChampionEventsRsp(Parcel in) {
        Events = in.createTypedArrayList(MatchInfo.CREATOR);
        Delta = in.readDouble();
        ServerTime = in.readString();
        StatusCode = in.readInt();
        StatusDesc = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(Events);
        dest.writeDouble(Delta);
        dest.writeString(ServerTime);
        dest.writeInt(StatusCode);
        dest.writeString(StatusDesc);
    }

    public static final Creator<ChampionEventsRsp> CREATOR = new Creator<ChampionEventsRsp>() {
        @Override
        public ChampionEventsRsp createFromParcel(Parcel in) {
            return new ChampionEventsRsp(in);
        }

        @Override
        public ChampionEventsRsp[] newArray(int size) {
            return new ChampionEventsRsp[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

}

