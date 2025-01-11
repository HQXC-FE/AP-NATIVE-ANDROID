package com.xtree.live.ui.main.service.response;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class LiveRoomChatResponse implements Parcelable {
    public String action;   // send
    public String msg_id;   // 1731984524076617 //讯息id
    public String vid;      // AAABBBCCC //直播间 vid
    public String text;     // 6 //讯息内容
    public String type;     // 0
    public int msg_type; // 1   //0:弹幕 1:文字, 2:图片, 3:模版(图+文)
    public int sender;   // 19  //消息发送者ID
    public int sender_user_type;  // 1 //消息发送者的类型(1:后台管理者,2:会员)
    public String sender_nickname;   // 测试1号  //消息发送者的暱称
    public String time;     // 2024-11-19 10:48:44  //消息时间
    public String time_ms;  // 1731984524102  //消息时间戳
    public String avatar;   // http:\/\/test.com\/\/upload\/default\/20240827\/e69912167837a46c64e1628808ae5f43.450x450.png"  //頭像
    public String channel_id;  //后台专门发渠道信息用的
    public String seed; //唯一识别
    public String originalText; //原始内容
    public String has_tag; //是否有标签(1:是,0:否)
    public String designation;  //头衔
    public String designation_color;  //头衔颜色
    public String block_channel;  //需要屏蔽的渠道ID
    public String readed_count; //已读未读标记
    public String qos;
    public String is_batch;
    public String anchor_id;  //主播ID
    public String anchor_nickname;  //主播暱称
    public String sender_total_exp;   //总经验
    public String sender_current_exp;
    public String sender_exp;
    public String sender_difference;  //消息发送者现在等级经验到下个等级所需要的经验量
    public String sender_exp_icon;  //消息发送者的经验图标
    public String prefix;  //平台识别
    public String method;  //chat swoole 要用到的 method

    protected LiveRoomChatResponse(Parcel in) {
        action = in.readString();
        msg_id = in.readString();
        vid = in.readString();
        text = in.readString();
        type = in.readString();
        msg_type = in.readInt();
        sender = in.readInt();
        sender_user_type = in.readInt();
        sender_nickname = in.readString();
        time = in.readString();
        time_ms = in.readString();
        avatar = in.readString();
        channel_id = in.readString();
        seed = in.readString();
        originalText = in.readString();
        has_tag = in.readString();
        designation = in.readString();
        designation_color = in.readString();
        block_channel = in.readString();
        readed_count = in.readString();
        qos = in.readString();
        is_batch = in.readString();
        anchor_id = in.readString();
        anchor_nickname = in.readString();
        sender_total_exp = in.readString();
        sender_current_exp = in.readString();
        sender_exp = in.readString();
        sender_difference = in.readString();
        sender_exp_icon = in.readString();
        prefix = in.readString();
        method = in.readString();
    }

    public static final Creator<LiveRoomChatResponse> CREATOR = new Creator<LiveRoomChatResponse>() {
        @Override
        public LiveRoomChatResponse createFromParcel(Parcel in) {
            return new LiveRoomChatResponse(in);
        }

        @Override
        public LiveRoomChatResponse[] newArray(int size) {
            return new LiveRoomChatResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(action);
        dest.writeString(msg_id);
        dest.writeString(vid);
        dest.writeString(text);
        dest.writeString(type);
        dest.writeInt(msg_type);
        dest.writeInt(sender);
        dest.writeInt(sender_user_type);
        dest.writeString(sender_nickname);
        dest.writeString(time);
        dest.writeString(time_ms);
        dest.writeString(avatar);
        dest.writeString(channel_id);
        dest.writeString(seed);
        dest.writeString(originalText);
        dest.writeString(has_tag);
        dest.writeString(designation);
        dest.writeString(designation_color);
        dest.writeString(block_channel);
        dest.writeString(readed_count);
        dest.writeString(qos);
        dest.writeString(is_batch);
        dest.writeString(anchor_id);
        dest.writeString(anchor_nickname);
        dest.writeString(sender_total_exp);
        dest.writeString(sender_current_exp);
        dest.writeString(sender_exp);
        dest.writeString(sender_difference);
        dest.writeString(sender_exp_icon);
        dest.writeString(prefix);
        dest.writeString(method);
    }
}
