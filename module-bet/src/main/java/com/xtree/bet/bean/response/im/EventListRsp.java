package com.xtree.bet.bean.response.im;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.xtree.base.vo.BaseBean;

import java.util.List;

/**
 * 推荐赛事列表响应实体
 */
public class EventListRsp implements BaseBean {

    @SerializedName("Sports")
    private List<Sport> sports;

    public EventListRsp() {
    }

    protected EventListRsp(Parcel in) {
        sports = in.createTypedArrayList(Sport.CREATOR);
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

    public List<Sport> getSports() {
        return sports;
    }

    public void setSports(List<Sport> sports) {
        this.sports = sports;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(sports);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "EventListRsp{" +
                "sports=" + (sports != null ? sports.size() + " sports" : "null") +
                '}';
    }

    /**
     * 体育项目实体
     */
    public static class Sport implements BaseBean, Parcelable {

        @SerializedName("SportId")
        public int sportId;

        @SerializedName("SportName")
        public String sportName;

        @SerializedName("Events")
        public List<MatchInfo> events;

        @SerializedName("OrderNumber")
        public int orderNumber;

        public Sport() {
        }

        protected Sport(Parcel in) {
            sportId = in.readInt();
            sportName = in.readString();
            events = in.createTypedArrayList(MatchInfo.CREATOR);
            orderNumber = in.readInt();
        }

        public static final Creator<Sport> CREATOR = new Creator<Sport>() {
            @Override
            public Sport createFromParcel(Parcel in) {
                return new Sport(in);
            }

            @Override
            public Sport[] newArray(int size) {
                return new Sport[size];
            }
        };

        public int getSportId() {
            return sportId;
        }

        public void setSportId(int sportId) {
            this.sportId = sportId;
        }

        public String getSportName() {
            return sportName;
        }

        public void setSportName(String sportName) {
            this.sportName = sportName;
        }

        public List<MatchInfo> getEvents() {
            return events;
        }

        public void setEvents(List<MatchInfo> events) {
            this.events = events;
        }

        public int getOrderNumber() {
            return orderNumber;
        }

        public void setOrderNumber(int orderNumber) {
            this.orderNumber = orderNumber;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(sportId);
            dest.writeString(sportName);
            dest.writeTypedList(events);
            dest.writeInt(orderNumber);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public String toString() {
            return "Sport{" +
                    "sportId=" + sportId +
                    ", sportName='" + sportName + '\'' +
                    ", orderNumber=" + orderNumber +
                    ", events=" + (events != null ? events.size() + " events" : "null") +
                    '}';
        }
    }
}
