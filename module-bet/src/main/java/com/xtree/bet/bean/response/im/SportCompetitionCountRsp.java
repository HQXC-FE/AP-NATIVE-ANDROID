package com.xtree.bet.bean.response.im;

import android.os.Parcel;
import android.os.Parcelable;

import com.xtree.base.vo.BaseBean;

import java.util.List;

public class SportCompetitionCountRsp implements BaseBean {

    private List<CompetitionCountBean> CompetitionCount;
    private String ServerTime;
    private int StatusCode;
    private String StatusDesc;

    public List<CompetitionCountBean> getCompetitionCount() {
        return CompetitionCount;
    }

    public void setCompetitionCount(List<CompetitionCountBean> competitionCount) {
        CompetitionCount = competitionCount;
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

    protected SportCompetitionCountRsp(Parcel in) {
        CompetitionCount = in.createTypedArrayList(CompetitionCountBean.CREATOR);
        ServerTime = in.readString();
        StatusCode = in.readInt();
        StatusDesc = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(CompetitionCount);
        dest.writeString(ServerTime);
        dest.writeInt(StatusCode);
        dest.writeString(StatusDesc);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SportCompetitionCountRsp> CREATOR = new Creator<SportCompetitionCountRsp>() {
        @Override
        public SportCompetitionCountRsp createFromParcel(Parcel in) {
            return new SportCompetitionCountRsp(in);
        }

        @Override
        public SportCompetitionCountRsp[] newArray(int size) {
            return new SportCompetitionCountRsp[size];
        }
    };

    public static class CompetitionCountBean implements Parcelable {
        public int SportId;
        public String SportName;
        public int OrderNumber;
        public int CompetitionId;
        public String CompetitionName;
        public int PMOrderNumber;
        public int RBOrderNumber;
        public int Market;
        public int ProgrammeId;
        public String ProgrammeName;
        public int ProgrammeOrderNumber;
        public int Count;

        public CompetitionCountBean() {}

        protected CompetitionCountBean(Parcel in) {
            SportId = in.readInt();
            SportName = in.readString();
            OrderNumber = in.readInt();
            CompetitionId = in.readInt();
            CompetitionName = in.readString();
            PMOrderNumber = in.readInt();
            RBOrderNumber = in.readInt();
            Market = in.readInt();
            ProgrammeId = in.readInt();
            ProgrammeName = in.readString();
            ProgrammeOrderNumber = in.readInt();
            Count = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(SportId);
            dest.writeString(SportName);
            dest.writeInt(OrderNumber);
            dest.writeInt(CompetitionId);
            dest.writeString(CompetitionName);
            dest.writeInt(PMOrderNumber);
            dest.writeInt(RBOrderNumber);
            dest.writeInt(Market);
            dest.writeInt(ProgrammeId);
            dest.writeString(ProgrammeName);
            dest.writeInt(ProgrammeOrderNumber);
            dest.writeInt(Count);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<CompetitionCountBean> CREATOR = new Creator<CompetitionCountBean>() {
            @Override
            public CompetitionCountBean createFromParcel(Parcel in) {
                return new CompetitionCountBean(in);
            }

            @Override
            public CompetitionCountBean[] newArray(int size) {
                return new CompetitionCountBean[size];
            }
        };
    }
}
