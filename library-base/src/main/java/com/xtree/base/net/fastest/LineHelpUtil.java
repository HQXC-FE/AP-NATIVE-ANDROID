package com.xtree.base.net.fastest;

import static com.xtree.base.net.fastest.FastestConfigKt.FASTEST_API;
import static com.xtree.base.net.fastest.FastestConfigKt.FASTEST_API_BMP;

import com.google.gson.Gson;
import com.xtree.base.utils.CfLog;

import java.util.ArrayList;
import java.util.List;

public class LineHelpUtil {


    public static List<String> makeUpApiListWithBMP(List<String> apiList){
        CfLog.e("line make apiList" + new Gson().toJson(apiList));

        if(apiList == null || apiList.size() == 0){
            return apiList;
        }

        List<String> result = new ArrayList<>();

        for (String s: apiList){
            result.add(s + FASTEST_API); //接口测速
            result.add(s + FASTEST_API_BMP + "?a=" + System.currentTimeMillis());//资源测速
        }

        CfLog.e("line make result" + new Gson().toJson(result));

        return result;
    }

    public static String removePointBmpAndAfter(String input) {
        int index = input.indexOf(FASTEST_API_BMP);
        if (index != -1) {
            return input.substring(0, index);
        }
        return input;
    }


    public static List<TopSpeedDomain> getUploadList(List<TopSpeedDomain> topSpeedDomains){
        CfLog.e("line make in " + new Gson().toJson(topSpeedDomains));

        List<TopSpeedDomain> gateWayCheckList = new ArrayList<>();
        List<TopSpeedDomain> sourceCheckList = new ArrayList<>();

        for (TopSpeedDomain topSpeedDomain : topSpeedDomains){
            if(topSpeedDomain.type == 0){
                gateWayCheckList.add(topSpeedDomain);
            }else if(topSpeedDomain.type == 1){
                sourceCheckList.add(topSpeedDomain);
            }
        }
        CfLog.e("line make gate " + new Gson().toJson(gateWayCheckList));
        CfLog.e("line make source " + new Gson().toJson(sourceCheckList));

        for (TopSpeedDomain t : gateWayCheckList){
            for (TopSpeedDomain b : sourceCheckList){
                if(t.url.equals(b.url)){
                    t.speedSecBmp = b.speedSec;
                }
            }
        }
        CfLog.e("line make gateWayCheckList " + new Gson().toJson(gateWayCheckList));
        return gateWayCheckList;

    }



}
