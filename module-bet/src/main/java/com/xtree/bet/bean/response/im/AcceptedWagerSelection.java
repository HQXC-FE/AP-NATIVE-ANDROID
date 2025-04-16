package com.xtree.bet.bean.response.im;

import android.os.Parcel;
import android.os.Parcelable;

import com.xtree.base.vo.BaseBean;

public  class AcceptedWagerSelection implements BaseBean {
        public double StakeOdds;
        public double Handicap;
        public int BetTypeSelectionId;
        public long EventId;

        public AcceptedWagerSelection() {}

        protected AcceptedWagerSelection(Parcel in) {
            StakeOdds = in.readDouble();
            Handicap = in.readDouble();
            BetTypeSelectionId = in.readInt();
            EventId = in.readLong();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeDouble(StakeOdds);
            dest.writeDouble(Handicap);
            dest.writeInt(BetTypeSelectionId);
            dest.writeLong(EventId);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<AcceptedWagerSelection> CREATOR = new Creator<AcceptedWagerSelection>() {
            @Override
            public AcceptedWagerSelection createFromParcel(Parcel in) {
                return new AcceptedWagerSelection(in);
            }

            @Override
            public AcceptedWagerSelection[] newArray(int size) {
                return new AcceptedWagerSelection[size];
            }
        };
    }