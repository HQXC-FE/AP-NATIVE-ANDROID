package com.xtree.bet.bean.request.im;

public class DeltaOutrightEventsReq {

        private String timeStamp;
        private int sportId;
        private double delta;

        public String getTimeStamp() {
            return timeStamp;
        }

        public void setTimeStamp(String timeStamp) {
            this.timeStamp = timeStamp;
        }

        public int getSportId() {
            return sportId;
        }

        public void setSportId(int sportId) {
            this.sportId = sportId;
        }

        public double getDelta() {
            return delta;
        }

        public void setDelta(double delta) {
            this.delta = delta;
        }

        @Override
        public String toString() {
            return "DeltaOutrightEventsReq{" +
                    "timeStamp='" + timeStamp + '\'' +
                    ", sportId=" + sportId +
                    ", delta=" + delta +
                    '}';
        }

}
