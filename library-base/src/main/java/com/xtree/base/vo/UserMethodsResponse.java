package com.xtree.base.vo;

import com.google.gson.annotations.SerializedName;

import java.util.Comparator;
import java.util.List;

/**
 * Created by KAKA on 2024/4/29.
 * Describe:
 */
public class UserMethodsResponse {

    /**
     * status
     */
    @SerializedName("status")
    private int status;
    /**
     * message
     */
    @SerializedName("message")
    private String message;
    /**
     * data
     */
    @SerializedName("data")
    private List<DataDTO> data;
    /**
     * timestamp
     */
    @SerializedName("timestamp")
    private int timestamp;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataDTO> getData() {
        return data;
    }

    public void setData(List<DataDTO> data) {
        this.data = data;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public static class DataDTO {
        /**
         * menuid
         */
        @SerializedName("menuid")
        private String menuid;
        /**
         * methodid
         */
        @SerializedName("methodid")
        private String methodid;
        /**
         * name
         */
        @SerializedName("name")
        private String name;
        /**
         * prizeLevel
         */
        @SerializedName("prize_level")
        private List<String> prizeLevel;
        /**
         * prizeGroup
         */
        @SerializedName("prize_group")
        private List<PrizeGroupDTO> prizeGroup;
        /**
         * title
         */
        @SerializedName("title")
        private String title;
        /**
         * isMultiple
         */
        @SerializedName("is_multiple")
        private int isMultiple;
        /**
         * relationMethods
         */
        @SerializedName("relationMethods")
        private List<Integer> relationMethods;

        public String getMenuid() {
            return menuid;
        }

        public void setMenuid(String menuid) {
            this.menuid = menuid;
        }

        public String getMethodid() {
            return methodid;
        }

        public void setMethodid(String methodid) {
            this.methodid = methodid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<String> getPrizeLevel() {
            return prizeLevel;
        }

        public void setPrizeLevel(List<String> prizeLevel) {
            this.prizeLevel = prizeLevel;
        }

        public List<PrizeGroupDTO> getPrizeGroup() {
            if (prizeGroup != null) {
                prizeGroup.sort(Comparator.comparingInt(PrizeGroupDTO::getValue).reversed());
            }
            return prizeGroup;
        }

        public void setPrizeGroup(List<PrizeGroupDTO> prizeGroup) {
            this.prizeGroup = prizeGroup;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getIsMultiple() {
            return isMultiple;
        }

        public void setIsMultiple(int isMultiple) {
            this.isMultiple = isMultiple;
        }

        public List<Integer> getRelationMethods() {
            return relationMethods;
        }

        public void setRelationMethods(List<Integer> relationMethods) {
            this.relationMethods = relationMethods;
        }

        public static class PrizeGroupDTO {
            /**
             * value
             */
            @SerializedName("value")
            private int value;
            /**
             * label
             */
            @SerializedName("label")
            private String label;

            public int getValue() {
                return value;
            }

            public void setValue(int value) {
                this.value = value;
            }

            public String getLabel() {
                return label;
            }

            public void setLabel(String label) {
                this.label = label;
            }
        }
    }
}
