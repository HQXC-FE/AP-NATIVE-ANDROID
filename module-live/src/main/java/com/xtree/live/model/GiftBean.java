package com.xtree.live.model;

public class GiftBean {

    /**
     * id : 74
     * mark : 0
     * type : 0
     * sid : 0
     * giftname : 星星
     * info : 111
     * needcoin : 1
     * gifticon : http://cunchu.mhuan.shop//ico/gift/gift_74.png
     * list_order : 0
     * swftype : 0
     * swf :
     * swftime : 0.00
     * isplatgift : 0
     * sticker_id : 0
     */

    private String id;
    private int gift_id;
    private int mark;
    private int type;
    private int sid;
    private String giftname;
    private String info;
    private int needcoin;
    private String gifticon;
    private int list_order;
    private int swftype;
    private String swf;
    private String swftime;
    private int isplatgift;
    private int sticker_id;
    private int num;
    private boolean isSelect;
    private String exp_icon;//等级图片
    private String guard_icon;//贵族图标
    private int is_guard;//是否是贵族 0：否 1：是
    private int is_room;//是否是房主 0：否 1：是

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getGift_id() {
        return gift_id;
    }

    public void setGift_id(int gift_id) {
        this.gift_id = gift_id;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getGiftname() {
        return giftname;
    }

    public void setGiftname(String giftname) {
        this.giftname = giftname;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getNeedcoin() {
        return needcoin;
    }

    public void setNeedcoin(int needcoin) {
        this.needcoin = needcoin;
    }

    public String getGifticon() {
        return gifticon;
    }

    public void setGifticon(String gifticon) {
        this.gifticon = gifticon;
    }

    public int getList_order() {
        return list_order;
    }

    public void setList_order(int list_order) {
        this.list_order = list_order;
    }

    public int getSwftype() {
        return swftype;
    }

    public void setSwftype(int swftype) {
        this.swftype = swftype;
    }

    public String getSwf() {
        return swf;
    }

    public void setSwf(String swf) {
        this.swf = swf;
    }

    public String getSwftime() {
        return swftime;
    }

    public void setSwftime(String swftime) {
        this.swftime = swftime;
    }

    public int getIsplatgift() {
        return isplatgift;
    }

    public void setIsplatgift(int isplatgift) {
        this.isplatgift = isplatgift;
    }

    public int getSticker_id() {
        return sticker_id;
    }

    public void setSticker_id(int sticker_id) {
        this.sticker_id = sticker_id;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getExp_icon() {
        return exp_icon;
    }

    public void setExp_icon(String exp_icon) {
        this.exp_icon = exp_icon;
    }

    public String getGuard_icon() {
        return guard_icon;
    }

    public void setGuard_icon(String guard_icon) {
        this.guard_icon = guard_icon;
    }

    public int getIs_guard() {
        return is_guard;
    }

    public void setIs_guard(int is_guard) {
        this.is_guard = is_guard;
    }

    public int getIs_room() {
        return is_room;
    }

    public void setIs_room(int is_room) {
        this.is_room = is_room;
    }
}

