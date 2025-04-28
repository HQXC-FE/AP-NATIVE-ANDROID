package com.xtree.mine.vo.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ThirdManagementResponse {

    @SerializedName("data")
    private DataDTO data;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private int status;

    @SerializedName("timestamp")
    private int timestamp;

    public void setData(DataDTO data) {
        this.data = data;
    }

    public DataDTO getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public class DataDTO {

        @SerializedName("p")
        private int p;

        @SerializedName("total_myself_price")
        private String totalMyselfPrice;

        @SerializedName("total_num")
        private int totalNum;

        @SerializedName("total_page")
        private int totalPage;

        @SerializedName("total_project_win")
        private String totalProjectWin;

        @SerializedName("list")
        private List<ThirdManagementResponseDTO> list;

        @SerializedName("pn")
        private int pn;

        public void setP(int p) {
            this.p = p;
        }

        public int getP() {
            return p;
        }

        public void setTotalMyselfPrice(String totalMyselfPrice) {
            this.totalMyselfPrice = totalMyselfPrice;
        }

        public String getTotalMyselfPrice() {
            return totalMyselfPrice;
        }

        public void setTotalNum(int totalNum) {
            this.totalNum = totalNum;
        }

        public int getTotalNum() {
            return totalNum;
        }

        public void setTotalPage(int totalPage) {
            this.totalPage = totalPage;
        }

        public int getTotalPage() {
            return totalPage;
        }

        public void setTotalProjectWin(String totalProjectWin) {
            this.totalProjectWin = totalProjectWin;
        }

        public String getTotalProjectWin() {
            return totalProjectWin;
        }

        public void setList(List<ThirdManagementResponseDTO> list) {
            this.list = list;
        }

        public List<ThirdManagementResponseDTO> getList() {
            return list;
        }

        public void setPn(int pn) {
            this.pn = pn;
        }

        public int getPn() {
            return pn;
        }
    }

    public class ThirdManagementResponseDTO {

        @SerializedName("date")
        private String date;

        @SerializedName("myself_price")
        private String myselfPrice;

        @SerializedName("project_win")
        private String projectWin;

        @SerializedName("cid_cn")
        private String cidCn;

        @SerializedName("username")
        private String username;

        @SerializedName("cid")
        private String cid;

        public void setDate(String date) {
            this.date = date;
        }

        public String getDate() {
            return date;
        }

        public void setMyselfPrice(String myselfPrice) {
            this.myselfPrice = myselfPrice;
        }

        public String getMyselfPrice() {
            return myselfPrice;
        }

        public void setProjectWin(String projectWin) {
            this.projectWin = projectWin;
        }

        public String getProjectWin() {
            return projectWin;
        }

        public void setCidCn(String cidCn) {
            this.cidCn = cidCn;
        }

        public String getCidCn() {
            return cidCn;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getUsername() {
            return username;
        }

        public void setCid(String cid) {
            this.cid = cid;
        }

        public String getCid() {
            return cid;
        }
    }
}