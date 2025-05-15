package com.xtree.bet;

import android.content.Context;

import com.google.gson.Gson;
import com.xtree.base.utils.CfLog;
import com.xtree.bet.bean.response.im.ChampionEventsRsp;
import com.xtree.bet.bean.response.im.EventInfoByPageListRsp;
import com.xtree.bet.bean.response.im.MatchEvent;
import com.xtree.bet.bean.response.im.MatchInfo;
import com.xtree.bet.bean.response.im.Sport;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class EventInfoByPageListParser {


    public static EventInfoByPageListRsp getLiveEventInfoListRsp(Context context) {
        String json = loadJSONFromAsset(context, "live_data.json");
        Gson gson = new Gson();
        return gson.fromJson(json, EventInfoByPageListRsp.class);
    }

    public static EventInfoByPageListRsp getEventInfoByPageListRsp(Context context) {
        String json = loadJSONFromAsset(context, "data.json");
        Gson gson = new Gson();
        return gson.fromJson(json, EventInfoByPageListRsp.class);
    }

    public static ChampionEventsRsp getChampionEventsRsp(Context context) {
        String json = loadJSONFromAsset(context, "data.json");
        Gson gson = new Gson();
        return gson.fromJson(json, ChampionEventsRsp.class);
    }

    public static String loadJSONFromAsset(Context context, String fileName) {
        String json;
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

//    public static List<MatchInfo> convertToMatchInfoList(EventInfoByPageListRsp data) {
//        List<MatchInfo> matchList = new ArrayList<>();
//        if (data == null || data.getSports() == null) return matchList;
//        CfLog.d("============== IMListCallBack convertToMatchInfoList ===============");
//        for (Sport sport : data.getSports()) {
//            if (sport.getEvents() == null) continue;
//            for (MatchEvent event : sport.getEvents()) {
////                MatchInfo matchInfo = new MatchInfo();
////                matchInfo.csid = String.valueOf(sport.getSportId());
////                matchInfo.csna = sport.getSportName();
////                matchInfo.mid = String.valueOf(event.eventId);
////                matchInfo.tn = event.competition.getCompetitionName();
////                matchInfo.mhid = String.valueOf(event.homeTeamId);
////                matchInfo.mhn = event.homeTeam;
////                matchInfo.mst = String.valueOf(event.eventDate);
////                matchInfo.man = event.awayTeam;
////                matchInfo.maid = String.valueOf(event.awayTeamId);
////                matchInfo.tid = String.valueOf(event.eventGroupId);
////                matchInfo.mgt = event.rbTime;
////                matchList.add(matchInfo);
////                CfLog.d("============== IMListCallBack matchInfo ==============="+matchInfo);
//            }
//        }
//        CfLog.d("============== IMListCallBack convertToMatchInfoList matchList ==============="+matchList);
//        CfLog.d("============== IMListCallBack convertToMatchInfoList matchList size ==============="+matchList.size());
//        return matchList;
//    }
}
