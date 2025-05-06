package com.xtree.bet.bean.request.im;

import java.util.List;

public class SelectedEventInfoReq {

        private String timeStamp;
        private int sportId;
        private List<Long> eventIds;
        private String includeGroupEvents;
        private int oddsType;
        private String token;
        private String memberCode;

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

        public List<Long> getEventIds() {
            return eventIds;
        }

        public void setEventIds(List<Long> eventIds) {
            this.eventIds = eventIds;
        }

        public String getIncludeGroupEvents() {
            return includeGroupEvents;
        }

        public void setIncludeGroupEvents(String includeGroupEvents) {
            this.includeGroupEvents = includeGroupEvents;
        }

        public int getOddsType() {
            return oddsType;
        }

        public void setOddsType(int oddsType) {
            this.oddsType = oddsType;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getMemberCode() {
            return memberCode;
        }

        public void setMemberCode(String memberCode) {
            this.memberCode = memberCode;
        }

        @Override
        public String toString() {
            return "SelectedEventInfoReq{" +
                    "timeStamp='" + timeStamp + '\'' +
                    ", sportId=" + sportId +
                    ", eventIds=" + eventIds +
                    ", includeGroupEvents='" + includeGroupEvents + '\'' +
                    ", oddsType=" + oddsType +
                    ", token='" + token + '\'' +
                    ", memberCode='" + memberCode + '\'' +
                    '}';
        }

}
