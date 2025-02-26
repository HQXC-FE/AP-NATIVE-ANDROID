package com.xtree.live.message;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.xtree.live.message.inroom.ChatRoom;

public class GroupRoom extends ChatRoom implements Parcelable {
    public int roomMute;
    public int isOnline;

    public GroupRoom(int roomType, String vid, String name){
        super(roomType, vid, name);
    }
    protected GroupRoom(Parcel in) {
        super(in);
        roomMute = in.readInt();
        isOnline = in.readInt();
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(roomMute);
        dest.writeInt(isOnline);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GroupRoom> CREATOR = new Creator<GroupRoom>() {
        @Override
        public GroupRoom createFromParcel(Parcel in) {
            return new GroupRoom(in);
        }

        @Override
        public GroupRoom[] newArray(int size) {
            return new GroupRoom[size];
        }
    };
}

