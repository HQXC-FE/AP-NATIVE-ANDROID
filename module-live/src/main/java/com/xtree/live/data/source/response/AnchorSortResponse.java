package com.xtree.live.data.source.response;

import java.util.ArrayList;
/**
 * 主播列表Model
 * /api/anchor/sort 来自于该接口
 */
public class AnchorSortResponse {

    public ArrayList<User> user ;//主播信息列表
    public String total;// 	总数居量
    public String per_page;//每页数量
    public String current_page;//当前页
    public String last_page;//最后一页位置
    public String anchor_id; //主播id
    public String platform;//终端类型 1： PC; 2：H5；3：android；4：ios；
    public String side;//投放位置 1：足球；2：篮球；3：电竞；4：首页；5：直播间；6：其它；
    public String listRows;//每页数量
    public String type;//数据类型 1：热门， 2：推荐
    public String description;//说明
    public String heat;//热度
    public String is_live;//是否在直播 1：正在直播 0：不在直播

    public ArrayList<User> getUser() {
        return user;
    }

    public void setUser(ArrayList<User> user) {
        this.user = user;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getPer_page() {
        return per_page;
    }

    public void setPer_page(String per_page) {
        this.per_page = per_page;
    }

    public String getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(String current_page) {
        this.current_page = current_page;
    }

    public String getLast_page() {
        return last_page;
    }

    public void setLast_page(String last_page) {
        this.last_page = last_page;
    }

    public String getAnchor_id() {
        return anchor_id;
    }

    public void setAnchor_id(String anchor_id) {
        this.anchor_id = anchor_id;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public String getListRows() {
        return listRows;
    }

    public void setListRows(String listRows) {
        this.listRows = listRows;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHeat() {
        return heat;
    }

    public void setHeat(String heat) {
        this.heat = heat;
    }

    public String getIs_live() {
        return is_live;
    }

    public void setIs_live(String is_live) {
        this.is_live = is_live;
    }

    public class  User{
        //public String id; //主播id
        public String user_nickname; //主播昵称
        public String avatar ; //主播头像
        public String attention;//关注人数
    }
}
