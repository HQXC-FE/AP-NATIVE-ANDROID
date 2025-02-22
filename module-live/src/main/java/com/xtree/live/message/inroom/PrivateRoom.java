package com.xtree.live.message.inroom;

import android.os.Parcel;
import android.os.Parcelable;

public class PrivateRoom extends ChatRoom implements Parcelable {
    public int isOnline;
    public int assistant;
    protected PrivateRoom(Parcel in) {
        super(in);
        isOnline = in.readInt();
        assistant = in.readInt();
    }

    public PrivateRoom(int roomType, String vid, String name, int assistant) {
        super(roomType, vid, name);
        this.assistant = assistant;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(isOnline);
        dest.writeInt(assistant);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PrivateRoom> CREATOR = new Creator<PrivateRoom>() {
        @Override
        public PrivateRoom createFromParcel(Parcel in) {
            return new PrivateRoom(in);
        }

        @Override
        public PrivateRoom[] newArray(int size) {
            return new PrivateRoom[size];
        }
    };
}

