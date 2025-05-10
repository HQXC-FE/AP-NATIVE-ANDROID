package com.xtree.bet.constant;

import android.util.ArrayMap;

import java.util.Map;

public class IMMatchPeriod {

    private static Map<String, String> map = new ArrayMap<>();


    public static String getMatchPeriod(String code){
        if(map.isEmpty()){
            map.put("!Live", "未开始");//没有滚球时间或赛事还没开始
            map.put("HT", "休息");
            map.put("FT", "结束");
            map.put("1H", "上半场");
            map.put("2H", "下半场");
            map.put("Q1", "第一节");
            map.put("Q2", "第二节");
            map.put("Q3", "第三节");
            map.put("Q4", "第四节");
            map.put("1H", "上半场");
            map.put("2H", "下半场");
            map.put("OT", "加时");
            map.put("S1", "第一盘");
            map.put("S2", "第二盘");
            map.put("S3", "第三盘");
            map.put("S4", "第四盘");
            map.put("S5", "第五盘");
            map.put("G1", "第一局");
            map.put("G2", "第二局");
            map.put("G3", "第三局");
            map.put("1INNS", "第一局");
            map.put("2INNS", "第二局");
            map.put("3INNS", "第三局");
            map.put("4INNS", "第四局");
            map.put("5INNS", "第五局");
            map.put("6INNS", "第六局");
            map.put("7INNS", "第七局");
            map.put("8INNS", "第八局");
            map.put("9INNS", "第九局");
            map.put("EINNS", "加时");
            map.put("P1", "第一时段");
            map.put("P2", "第二时段");
            map.put("P3", "第三时段");
            map.put("OT", "加时");
            map.put("Pen", "罚时");
            map.put("G1", "第一局");
            map.put("G2", "第二局");
            map.put("G3", "第三局");
            map.put("G4", "第四局");
            map.put("G5", "第五局");
            map.put("G6", "第六局");
            map.put("G7", "第七局");
            map.put("1INNS", "第一局");
            map.put("2INNS", "第二局");
            map.put("SO", "加时");
        }
        return map.get(code);
    }
}
