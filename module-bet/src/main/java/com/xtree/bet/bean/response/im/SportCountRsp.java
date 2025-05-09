package com.xtree.bet.bean.response.im;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.xtree.base.vo.BaseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取所有比赛赛种
 */
public class SportCountRsp implements BaseBean {

    @SerializedName("SportCount")
    private List<CountItem> SportCount;


    protected SportCountRsp(Parcel in) {
        SportCount = in.createTypedArrayList(CountItem.CREATOR);
    }

    public static final Creator<SportCountRsp> CREATOR = new Creator<SportCountRsp>() {
        @Override
        public SportCountRsp createFromParcel(Parcel in) {
            return new SportCountRsp(in);
        }

        @Override
        public SportCountRsp[] newArray(int size) {
            return new SportCountRsp[size];
        }
    };


    public List<CountItem> getSportCount() {
        return SportCount;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(SportCount);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static class CountItem implements Parcelable {
        /**
         * 体育项目ID
         */
        @SerializedName("SportId")
        public int sportId;
        /**
         * 体育项目名称
         */
        @SerializedName("SportName")
        public String sportName;
        /**
         * 体育项目序号
         */
        @SerializedName("OrderNumber")
        public int orderNumber;
        /**
         * 事组别类型清单
         */
        @SerializedName("EventGroupTypes")
        public List<EventGroupType> eventGroupTypes;
        /**
         * 清单
         */
        @SerializedName("ProgrammeList")
        public List<Object> programmeList;
        /**
         * 是否支持连串过关
         */
        @SerializedName("OpenParlay")
        public boolean openParlay;
        /**
         * 目前是否提供滚球赛事
         */
        @SerializedName("IsHasLive")
        public boolean isHasLive;
        /**
         * 早盘盘口的体育定时赛事计数
         */
        @SerializedName("EarlyFECount")
        public int earlyFECount;
        /**
         * 今日盘口的体育定时赛事计数
         */
        @SerializedName("TodayFECount")
        public int todayFECount;
        /**
         * 冠军赛事计数
         */
        @SerializedName("ORCount")
        public int orCount;
        /**
         * 滚球盘口的体育定时赛事计数
         */
        @SerializedName("RBFECount")
        public int rbFECount;
        /**
         * 赛事计数
         */
        @SerializedName("Count")
        public int count;
        /**
         * 是否是串关
         */
        @SerializedName("IsCombo")
        public boolean isCombo;

        public CountItem() {
        }

        protected CountItem(Parcel in) {
            sportId = in.readInt();
            sportName = in.readString();
            orderNumber = in.readInt();
            eventGroupTypes = in.createTypedArrayList(CountItem.EventGroupType.CREATOR);
            programmeList = new ArrayList<>();
            in.readList(programmeList, Object.class.getClassLoader());
            openParlay = in.readByte() != 0;
            isHasLive = in.readByte() != 0;
            earlyFECount = in.readInt();
            todayFECount = in.readInt();
            orCount = in.readInt();
            rbFECount = in.readInt();
            count = in.readInt();
            isCombo = in.readByte() != 0;
        }

        public static final Creator<CountItem> CREATOR = new Creator<CountItem>() {
            @Override
            public CountItem createFromParcel(Parcel in) {
                return new CountItem(in);
            }

            @Override
            public CountItem[] newArray(int size) {
                return new CountItem[size];
            }
        };

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(sportId);
            dest.writeString(sportName);
            dest.writeInt(orderNumber);
            dest.writeTypedList(eventGroupTypes);
            dest.writeList(programmeList);
            dest.writeByte((byte) (openParlay ? 1 : 0));
            dest.writeByte((byte) (isHasLive ? 1 : 0));
            dest.writeInt(earlyFECount);
            dest.writeInt(todayFECount);
            dest.writeInt(orCount);
            dest.writeInt(rbFECount);
            dest.writeInt(count);
            dest.writeByte((byte) (isCombo ? 1 : 0));
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public String toString() {
            return "CountItem{" +
                    "sportId=" + sportId +
                    ", sportName='" + sportName + '\'' +
                    ", orderNumber=" + orderNumber +
                    ", eventGroupTypes=" + eventGroupTypes +
                    ", programmeList=" + programmeList +
                    ", openParlay=" + openParlay +
                    ", isHasLive=" + isHasLive +
                    ", earlyFECount=" + earlyFECount +
                    ", todayFECount=" + todayFECount +
                    ", orCount=" + orCount +
                    ", rbFECount=" + rbFECount +
                    ", count=" + count +
                    ", isCombo=" + isCombo +
                    '}';
        }


        public static class EventGroupType implements Parcelable {
            /**
             * 可选参数用于指出只返回特定赛事组别类型的赛果
             */
            @SerializedName("EventGroupTypeId")
            public int eventGroupTypeId;
            /**
             * 竞赛赛事计数
             */
            @SerializedName("Count")
            public int count;
            /**
             * 早盘盘口的体育定时赛事计数
             */
            @SerializedName("EarlyFECount")
            public int earlyFECount;
            /**
             * 今日盘口的体育定时赛事计数
             */
            @SerializedName("TodayFECount")
            public int todayFECount;
            /**
             * 滚球盘口的体育定时赛事计数
             */
            @SerializedName("RBFECount")
            public int rbFECount;
            /**
             * 冠军赛事计数
             */
            @SerializedName("ORCount")
            public int orCount;
            /**
             * 指出目前是否提供滚球赛事
             */
            @SerializedName("IsHasLive")
            public boolean isHasLive;

            public EventGroupType() {
            }

            protected EventGroupType(Parcel in) {
                eventGroupTypeId = in.readInt();
                count = in.readInt();
                earlyFECount = in.readInt();
                todayFECount = in.readInt();
                rbFECount = in.readInt();
                orCount = in.readInt();
                isHasLive = in.readByte() != 0;
            }

            public static final Creator<CountItem.EventGroupType> CREATOR = new Creator<CountItem.EventGroupType>() {
                @Override
                public CountItem.EventGroupType createFromParcel(Parcel in) {
                    return new CountItem.EventGroupType(in);
                }

                @Override
                public CountItem.EventGroupType[] newArray(int size) {
                    return new CountItem.EventGroupType[size];
                }
            };

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(eventGroupTypeId);
                dest.writeInt(count);
                dest.writeInt(earlyFECount);
                dest.writeInt(todayFECount);
                dest.writeInt(rbFECount);
                dest.writeInt(orCount);
                dest.writeByte((byte) (isHasLive ? 1 : 0));
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public String toString() {
                return "EventGroupType{" +
                        "eventGroupTypeId=" + eventGroupTypeId +
                        ", count=" + count +
                        ", earlyFECount=" + earlyFECount +
                        ", todayFECount=" + todayFECount +
                        ", rbFECount=" + rbFECount +
                        ", orCount=" + orCount +
                        ", isHasLive=" + isHasLive +
                        '}';
            }

        }
    }

    @Override
    public String toString() {
        return "SportCountRsp{" +
                ", sportCount=" + SportCount +
                '}';
    }


}
