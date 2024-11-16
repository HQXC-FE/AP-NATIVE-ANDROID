package com.xtree.live.data.source.response;

import com.xtree.live.ui.main.model.anchorList.ChatRoomModel;

import java.util.ArrayList;

/**
api/chat/getChatRoomList
 返回的Bean类
 */
public class ChatRoomResponse {

    public String vid;//聊天室识别ID(与直播间VID不同)
    public String name;//助手暱稱
    public String room_type;//房间类型 (0 - 直播间列表; 1 - 群组聊天列表; 2 - 私聊列表)
    public String pic;//图片地址
    public String is_favorite;//是否收藏 0:否 1:是
    public String anchor_id;//主播ID
    public String anchor_nickname;//主播暱称
    public String addtime;//添加时间戳
    public String updatetime;// 	更新时间戳
    public String is_pin;//是否置顶 0:否 1:是
    public String pin_time;//置顶时间戳

    public String user_id;//助手帐户ID
    public String is_online;//是否在线 0:否 1:是
    public String is_redbag;//  	是否有正在进行的红包活动 0:否 1:是
    public String online_count;//群组的总人数跟在线人数(限后台可看)
    public String order_key;// 	消息创建时间
    public ArrayList<ChatRoomModel.LastMsg> last_msg;//最后一则讯息

    public class LastMsg{
        public String vid;//聊天室识别ID(与直播间VID不同)
        public String room_type;//房间类型 (0 - 直播间列表; 1 - 群组聊天列表; 2 - 私聊列表)
        public String sender;//消息发送者ID(如果是用户本身则为0)
        public String sender_nickname;// 	消息发送者的暱称(如果是用户本身则为空)
        public String seed;//唯一识别
        public String creation_time;//消息创建时间
        public String send_at_ms;//消息发送时间戳
        public String text;//消息内容
        public String pic;//消息图片路径
        public String pic_bnc;
        public String is_batch;//是否后台批量发送的消息

    }

    @Override
    public String toString() {
        return "ChatRoomResponse{" +
                "vid='" + vid + '\'' +
                ", name='" + name + '\'' +
                ", room_type='" + room_type + '\'' +
                ", pic='" + pic + '\'' +
                ", is_favorite='" + is_favorite + '\'' +
                ", anchor_id='" + anchor_id + '\'' +
                ", anchor_nickname='" + anchor_nickname + '\'' +
                ", addtime='" + addtime + '\'' +
                ", updatetime='" + updatetime + '\'' +
                ", is_pin='" + is_pin + '\'' +
                ", pin_time='" + pin_time + '\'' +
                ", user_id='" + user_id + '\'' +
                ", is_online='" + is_online + '\'' +
                ", is_redbag='" + is_redbag + '\'' +
                ", online_count='" + online_count + '\'' +
                ", order_key='" + order_key + '\'' +
                ", last_msg=" + last_msg +
                '}';
    }
}
