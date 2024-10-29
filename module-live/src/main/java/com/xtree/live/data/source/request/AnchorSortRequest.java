package com.xtree.live.data.source.request;

/**
 * 获取主播排序列表请求体
 *  /api/anchor/sort?type=1&platform=1&side=1&listRows=6&page=1
 */
public class AnchorSortRequest {
    public AnchorSortRequest(){
        super();
        this.type ="1";
        this.platform = "1";
        this.side = "1" ;
        this.listRows = "6";
        this.page = "1";
    }
    private String type ;//1：热门， 2：推荐
    private String platform ;//终端类型 	1： PC; 2：H5；3：android；4：ios；
    private String side;//投放位置 	1：足球；2：篮球；3：电竞；4：首页；5：直播间；6：其它；
    private String  listRows ;//每页数量,默认6
    private String page;//页码默认1


    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
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

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }
}
