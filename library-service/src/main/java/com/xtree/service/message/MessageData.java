package com.xtree.service.message;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

// 将 MessageData 声明为静态类
public class MessageData implements Parcelable {
    public static final Creator<MessageData> CREATOR = new Creator<MessageData>() {
        @Override
        public MessageData createFromParcel(Parcel in) {
            return new MessageData(in);
        }

        @Override
        public MessageData[] newArray(int size) {
            return new MessageData[size];
        }
    };

    private String subject;

    @SerializedName("messageid")
    private int messageId;

    public MessageData() {
    }

    public MessageData(String subject, int messageId) {
        this.subject = subject;
        this.messageId = messageId;
    }

    protected MessageData(Parcel in) {
        subject = in.readString();
        messageId = in.readInt();
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(subject);
        dest.writeInt(messageId);
    }

    @Override
    public String toString() {
        return "MessageData{" +
                "subject='" + subject + '\'' +
                ", messageId=" + messageId +
                '}';
    }
}