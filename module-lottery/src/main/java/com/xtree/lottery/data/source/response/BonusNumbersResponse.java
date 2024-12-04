package com.xtree.lottery.data.source.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by KAKA on 2024/5/7.
 * Describe:
 */
public class BonusNumbersResponse {

    //{"status":10000,"message":"success","data":[{"issue":"20240507-138","draw_time":null,"code":"83298","original_code":"83298","split_code":["8","3","2","9","8"]},{"issue":"20240507-137","draw_time":null,"code":"47295","original_code":"47295","split_code":["4","7","2","9","5"]},{"issue":"20240507-136","draw_time":null,"code":"06862","original_code":"06862","split_code":["0","6","8","6","2"]},{"issue":"20240507-135","draw_time":null,"code":"74517","original_code":"74517","split_code":["7","4","5","1","7"]},{"issue":"20240507-134","draw_time":null,"code":"87479","original_code":"87479","split_code":["8","7","4","7","9"]},{"issue":"20240507-133","draw_time":null,"code":"77882","original_code":"77882","split_code":["7","7","8","8","2"]},{"issue":"20240507-132","draw_time":null,"code":"54331","original_code":"54331","split_code":["5","4","3","3","1"]},{"issue":"20240507-131","draw_time":null,"code":"39026","original_code":"39026","split_code":["3","9","0","2","6"]},{"issue":"20240507-130","draw_time":null,"code":"85525","original_code":"85525","split_code":["8","5","5","2","5"]},{"issue":"20240507-129","draw_time":null,"code":"80646","original_code":"80646","split_code":["8","0","6","4","6"]},{"issue":"20240507-128","draw_time":null,"code":"07094","original_code":"07094","split_code":["0","7","0","9","4"]},{"issue":"20240507-127","draw_time":null,"code":"31681","original_code":"31681","split_code":["3","1","6","8","1"]},{"issue":"20240507-126","draw_time":null,"code":"20080","original_code":"20080","split_code":["2","0","0","8","0"]},{"issue":"20240507-125","draw_time":null,"code":"62932","original_code":"62932","split_code":["6","2","9","3","2"]},{"issue":"20240507-124","draw_time":null,"code":"33023","original_code":"33023","split_code":["3","3","0","2","3"]},{"issue":"20240507-123","draw_time":null,"code":"11707","original_code":"11707","split_code":["1","1","7","0","7"]},{"issue":"20240507-122","draw_time":null,"code":"76939","original_code":"76939","split_code":["7","6","9","3","9"]},{"issue":"20240507-121","draw_time":null,"code":"58858","original_code":"58858","split_code":["5","8","8","5","8"]},{"issue":"20240507-120","draw_time":null,"code":"61065","original_code":"61065","split_code":["6","1","0","6","5"]},{"issue":"20240507-119","draw_time":null,"code":"97026","original_code":"97026","split_code":["9","7","0","2","6"]},{"issue":"20240507-118","draw_time":null,"code":"08240","original_code":"08240","split_code":["0","8","2","4","0"]},{"issue":"20240507-117","draw_time":null,"code":"49405","original_code":"49405","split_code":["4","9","4","0","5"]},{"issue":"20240507-116","draw_time":null,"code":"45416","original_code":"45416","split_code":["4","5","4","1","6"]},{"issue":"20240507-115","draw_time":null,"code":"81088","original_code":"81088","split_code":["8","1","0","8","8"]},{"issue":"20240507-114","draw_time":null,"code":"41532","original_code":"41532","split_code":["4","1","5","3","2"]},{"issue":"20240507-113","draw_time":null,"code":"43752","original_code":"43752","split_code":["4","3","7","5","2"]},{"issue":"20240507-112","draw_time":null,"code":"73085","original_code":"73085","split_code":["7","3","0","8","5"]},{"issue":"20240507-111","draw_time":null,"code":"70278","original_code":"70278","split_code":["7","0","2","7","8"]},{"issue":"20240507-110","draw_time":null,"code":"58521","original_code":"58521","split_code":["5","8","5","2","1"]},{"issue":"20240507-109","draw_time":null,"code":"03245","original_code":"03245","split_code":["0","3","2","4","5"]},{"issue":"20240507-108","draw_time":null,"code":"87571","original_code":"87571","split_code":["8","7","5","7","1"]},{"issue":"20240507-107","draw_time":null,"code":"75599","original_code":"75599","split_code":["7","5","5","9","9"]},{"issue":"20240507-106","draw_time":null,"code":"91663","original_code":"91663","split_code":["9","1","6","6","3"]},{"issue":"20240507-105","draw_time":null,"code":"54546","original_code":"54546","split_code":["5","4","5","4","6"]},{"issue":"20240507-104","draw_time":null,"code":"26708","original_code":"26708","split_code":["2","6","7","0","8"]},{"issue":"20240507-103","draw_time":null,"code":"38328","original_code":"38328","split_code":["3","8","3","2","8"]},{"issue":"20240507-102","draw_time":null,"code":"63569","original_code":"63569","split_code":["6","3","5","6","9"]},{"issue":"20240507-101","draw_time":null,"code":"17839","original_code":"17839","split_code":["1","7","8","3","9"]},{"issue":"20240507-100","draw_time":null,"code":"52595","original_code":"52595","split_code":["5","2","5","9","5"]},{"issue":"20240507-099","draw_time":null,"code":"22654","original_code":"22654","split_code":["2","2","6","5","4"]},{"issue":"20240507-098","draw_time":null,"code":"40022","original_code":"40022","split_code":["4","0","0","2","2"]},{"issue":"20240507-097","draw_time":null,"code":"26305","original_code":"26305","split_code":["2","6","3","0","5"]},{"issue":"20240507-096","draw_time":null,"code":"20783","original_code":"20783","split_code":["2","0","7","8","3"]},{"issue":"20240507-095","draw_time":null,"code":"42047","original_code":"42047","split_code":["4","2","0","4","7"]},{"issue":"20240507-094","draw_time":null,"code":"14618","original_code":"14618","split_code":["1","4","6","1","8"]},{"issue":"20240507-093","draw_time":null,"code":"50101","original_code":"50101","split_code":["5","0","1","0","1"]},{"issue":"20240507-092","draw_time":null,"code":"95373","original_code":"95373","split_code":["9","5","3","7","3"]},{"issue":"20240507-091","draw_time":null,"code":"93384","original_code":"93384","split_code":["9","3","3","8","4"]},{"issue":"20240507-090","draw_time":null,"code":"85667","original_code":"85667","split_code":["8","5","6","6","7"]},{"issue":"20240507-089","draw_time":null,"code":"95930","original_code":"95930","split_code":["9","5","9","3","0"]},{"issue":"20240507-088","draw_time":null,"code":"33054","original_code":"33054","split_code":["3","3","0","5","4"]},{"issue":"20240507-087","draw_time":null,"code":"45063","original_code":"45063","split_code":["4","5","0","6","3"]},{"issue":"20240507-086","draw_time":null,"code":"49414","original_code":"49414","split_code":["4","9","4","1","4"]},{"issue":"20240507-085","draw_time":null,"code":"39361","original_code":"39361","split_code":["3","9","3","6","1"]},{"issue":"20240507-084","draw_time":null,"code":"13771","original_code":"13771","split_code":["1","3","7","7","1"]},{"issue":"20240507-083","draw_time":null,"code":"33762","original_code":"33762","split_code":["3","3","7","6","2"]},{"issue":"20240507-082","draw_time":null,"code":"30863","original_code":"30863","split_code":["3","0","8","6","3"]},{"issue":"20240507-081","draw_time":null,"code":"20780","original_code":"20780","split_code":["2","0","7","8","0"]},{"issue":"20240507-080","draw_time":null,"code":"44138","original_code":"44138","split_code":["4","4","1","3","8"]},{"issue":"20240507-079","draw_time":null,"code":"45601","original_code":"45601","split_code":["4","5","6","0","1"]},{"issue":"20240507-078","draw_time":null,"code":"68567","original_code":"68567","split_code":["6","8","5","6","7"]},{"issue":"20240507-077","draw_time":null,"code":"88444","original_code":"88444","split_code":["8","8","4","4","4"]},{"issue":"20240507-076","draw_time":null,"code":"58908","original_code":"58908","split_code":["5","8","9","0","8"]},{"issue":"20240507-075","draw_time":null,"code":"29765","original_code":"29765","split_code":["2","9","7","6","5"]},{"issue":"20240507-074","draw_time":null,"code":"32170","original_code":"32170","split_code":["3","2","1","7","0"]},{"issue":"20240507-073","draw_time":null,"code":"33388","original_code":"33388","split_code":["3","3","3","8","8"]},{"issue":"20240507-072","draw_time":null,"code":"95499","original_code":"95499","split_code":["9","5","4","9","9"]},{"issue":"20240507-071","draw_time":null,"code":"99607","original_code":"99607","split_code":["9","9","6","0","7"]},{"issue":"20240507-070","draw_time":null,"code":"61890","original_code":"61890","split_code":["6","1","8","9","0"]},{"issue":"20240507-069","draw_time":null,"code":"78324","original_code":"78324","split_code":["7","8","3","2","4"]},{"issue":"20240507-068","draw_time":null,"code":"12243","original_code":"12243","split_code":["1","2","2","4","3"]},{"issue":"20240507-067","draw_time":null,"code":"59425","original_code":"59425","split_code":["5","9","4","2","5"]},{"issue":"20240507-066","draw_time":null,"code":"87416","original_code":"87416","split_code":["8","7","4","1","6"]},{"issue":"20240507-065","draw_time":null,"code":"53006","original_code":"53006","split_code":["5","3","0","0","6"]},{"issue":"20240507-064","draw_time":null,"code":"13741","original_code":"13741","split_code":["1","3","7","4","1"]},{"issue":"20240507-063","draw_time":null,"code":"61168","original_code":"61168","split_code":["6","1","1","6","8"]},{"issue":"20240507-062","draw_time":null,"code":"01726","original_code":"01726","split_code":["0","1","7","2","6"]},{"issue":"20240507-061","draw_time":null,"code":"51859","original_code":"51859","split_code":["5","1","8","5","9"]},{"issue":"20240507-060","draw_time":null,"code":"63759","original_code":"63759","split_code":["6","3","7","5","9"]},{"issue":"20240507-059","draw_time":null,"code":"26280","original_code":"26280","split_code":["2","6","2","8","0"]},{"issue":"20240507-058","draw_time":null,"code":"73406","original_code":"73406","split_code":["7","3","4","0","6"]},{"issue":"20240507-057","draw_time":null,"code":"11504","original_code":"11504","split_code":["1","1","5","0","4"]},{"issue":"20240507-056","draw_time":null,"code":"81487","original_code":"81487","split_code":["8","1","4","8","7"]},{"issue":"20240507-055","draw_time":null,"code":"57990","original_code":"57990","split_code":["5","7","9","9","0"]},{"issue":"20240507-054","draw_time":null,"code":"85909","original_code":"85909","split_code":["8","5","9","0","9"]},{"issue":"20240507-053","draw_time":null,"code":"60492","original_code":"60492","split_code":["6","0","4","9","2"]},{"issue":"20240507-052","draw_time":null,"code":"82191","original_code":"82191","split_code":["8","2","1","9","1"]},{"issue":"20240507-051","draw_time":null,"code":"37597","original_code":"37597","split_code":["3","7","5","9","7"]},{"issue":"20240507-050","draw_time":null,"code":"75486","original_code":"75486","split_code":["7","5","4","8","6"]},{"issue":"20240507-049","draw_time":null,"code":"24943","original_code":"24943","split_code":["2","4","9","4","3"]},{"issue":"20240507-048","draw_time":null,"code":"22342","original_code":"22342","split_code":["2","2","3","4","2"]},{"issue":"20240507-047","draw_time":null,"code":"45293","original_code":"45293","split_code":["4","5","2","9","3"]},{"issue":"20240507-046","draw_time":null,"code":"52320","original_code":"52320","split_code":["5","2","3","2","0"]},{"issue":"20240507-045","draw_time":null,"code":"84414","original_code":"84414","split_code":["8","4","4","1","4"]},{"issue":"20240507-044","draw_time":null,"code":"23384","original_code":"23384","split_code":["2","3","3","8","4"]},{"issue":"20240507-043","draw_time":null,"code":"14450","original_code":"14450","split_code":["1","4","4","5","0"]},{"issue":"20240507-042","draw_time":null,"code":"64504","original_code":"64504","split_code":["6","4","5","0","4"]},{"issue":"20240507-041","draw_time":null,"code":"57445","original_code":"57445","split_code":["5","7","4","4","5"]},{"issue":"20240507-040","draw_time":null,"code":"43428","original_code":"43428","split_code":["4","3","4","2","8"]},{"issue":"20240507-039","draw_time":null,"code":"33335","original_code":"33335","split_code":["3","3","3","3","5"]}],"timestamp":1715052618}

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
         * issue
         */
        @SerializedName("issue")
        private String issue;
        /**
         * drawTime
         */
        @SerializedName("draw_time")
        private String drawTime;
        /**
         * code
         */
        @SerializedName("code")
        private String code;
        /**
         * originalCode
         */
        @SerializedName("original_code")
        private String originalCode;
        /**
         * splitCode
         */
        @SerializedName("split_code")
        private List<String> splitCode;

        public String getIssue() {
            return issue;
        }

        public void setIssue(String issue) {
            this.issue = issue;
        }

        public String getDrawTime() {
            return drawTime;
        }

        public void setDrawTime(String drawTime) {
            this.drawTime = drawTime;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getOriginalCode() {
            return originalCode;
        }

        public void setOriginalCode(String originalCode) {
            this.originalCode = originalCode;
        }

        public List<String> getSplitCode() {
            return splitCode;
        }

        public void setSplitCode(List<String> splitCode) {
            this.splitCode = splitCode;
        }
    }
}
