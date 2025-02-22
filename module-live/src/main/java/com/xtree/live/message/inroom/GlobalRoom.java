package com.xtree.live.message.inroom;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class GlobalRoom extends ChatRoom implements Parcelable {
    public int uid;

    protected GlobalRoom(Parcel in) {
        super(in);
        uid = in.readInt();
    }

    public GlobalRoom(int roomType, String vid, String name) {
        super(roomType, vid, name);
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(uid);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GlobalRoom> CREATOR = new Creator<GlobalRoom>() {
        @Override
        public GlobalRoom createFromParcel(Parcel in) {
            return new GlobalRoom(in);
        }

        @Override
        public GlobalRoom[] newArray(int size) {
            return new GlobalRoom[size];
        }
    };
}
