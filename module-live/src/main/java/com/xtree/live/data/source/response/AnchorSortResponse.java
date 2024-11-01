package com.xtree.live.data.source.response;

import java.util.ArrayList;
/**
 * 主播列表Model
 * /api/anchor/sort 来自于该接口
 */
public class AnchorSortResponse {
    public String total;// 	总数居量
    public String per_page;//每页数量
    public String current_page;//当前页
    public String last_page;//最后一页位置
    public String anchor_id; //主播id
    public ArrayList<AnchorResponse>data ;


    public static class  AnchorResponse implements Comparable<AnchorResponse>  {
        public String id ;
        public String anchor_id;//
        public String platform;//终端类型 1： PC; 2：H5；3：android；4：ios；
        public String side;//投放位置 1：足球；2：篮球；3：电竞；4：首页；5：直播间；6：其它；
        public String description;//说明
        public String heat;//热度
        public String is_live;//是否在直播 1：正在直播 0：不在直播
        public User user;
        public int comParId;
        @Override
        public int compareTo(AnchorResponse o) {
            this.comParId = Integer.valueOf(this.id);
            o.comParId = Integer.valueOf(o.id);
            if (this.comParId > o.comParId){
                return 1;
            } else if (this.comParId < o.comParId) {
                return -1;
            }else{
                return 0;
            }
        }

        public static class User {
            public String id; //主播id
            public String user_nickname; //主播昵称
            public String avatar ; //主播头像
            public String attention;//关注人数


            @Override
            public String toString() {
                return "User{" +
                        "id='" + id + '\'' +
                        ", user_nickname='" + user_nickname + '\'' +
                        ", avatar='" + avatar + '\'' +
                        ", attention='" + attention + '\'' +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "AnchorResponse{" +
                    "id='" + id + '\'' +
                    ", anchor_id='" + anchor_id + '\'' +
                    ", platform='" + platform + '\'' +
                    ", side='" + side + '\'' +
                    ", description='" + description + '\'' +
                    ", heat='" + heat + '\'' +
                    ", is_live='" + is_live + '\'' +
                    ", user=" + user +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "AnchorSortResponse{" +
                "total='" + total + '\'' +
                ", per_page='" + per_page + '\'' +
                ", current_page='" + current_page + '\'' +
                ", last_page='" + last_page + '\'' +
                ", anchor_id='" + anchor_id + '\'' +
                ", data=" + data +
                '}';
    }
}
