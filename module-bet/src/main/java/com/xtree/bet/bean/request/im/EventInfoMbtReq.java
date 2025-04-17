package com.xtree.bet.bean.request.im;

public class EventInfoMbtReq {

        private int sportId;
        private String timeStamp;
        private int market;

        public int getSportId() {
            return sportId;
        }

        public void setSportId(int sportId) {
            this.sportId = sportId;
        }

        public String getTimeStamp() {
            return timeStamp;
        }

        public void setTimeStamp(String timeStamp) {
            this.timeStamp = timeStamp;
        }

        public int getMarket() {
            return market;
        }

        public void setMarket(int market) {
            this.market = market;
        }

        @Override
        public String toString() {
            return "EventInfoMbtReq{" +
                    "sportId=" + sportId +
                    ", timeStamp='" + timeStamp + '\'' +
                    ", market=" + market +
                    '}';
        }

}
