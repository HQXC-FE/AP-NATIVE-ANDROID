package com.xtree.bet.constant;

import android.util.ArrayMap;

import com.google.gson.Gson;
import com.xtree.bet.R;
import com.xtree.bet.bean.response.fb.HotLeague;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IMConstants {
    private static final HashMap<Integer, SportTypeItem> MATCH_GAMES = new HashMap<>();

    public static HashMap<Integer, SportTypeItem> getMatchGames() {
        if (MATCH_GAMES.isEmpty()) {
            MATCH_GAMES.put(0, new SportTypeItem(0, "allleague", "全部", R.drawable.bt_match_item_live_all_selector));
            MATCH_GAMES.put(1111, new SportTypeItem(1111, "hot", "热门", R.drawable.bt_match_item_hot_selector));
            MATCH_GAMES.put(1, new SportTypeItem(1, "soccer", "足球", 1, R.drawable.bt_match_item_zq_selector));
            MATCH_GAMES.put(2, new SportTypeItem(2, "basketball", "篮球", 2, R.drawable.bt_match_item_lq_selector));
            MATCH_GAMES.put(3, new SportTypeItem(3, "tennis", "网球", 3, R.drawable.bt_match_item_wq_selector));
            MATCH_GAMES.put(34, new SportTypeItem(34, "snooker", "斯诺克", 34, R.drawable.bt_match_item_snk_selector));
            MATCH_GAMES.put(8, new SportTypeItem(8, "baseball", "棒球", 8, R.drawable.bt_match_item_bq_selector));
            MATCH_GAMES.put(40, new SportTypeItem(40, "volleyball", "排球", 40, R.drawable.bt_match_item_pq_selector));
            MATCH_GAMES.put(7, new SportTypeItem(7, "badminton", "羽毛球", 7, R.drawable.bt_match_item_ymq_selector));
            MATCH_GAMES.put(19, new SportTypeItem(19, "americanfootball", "美式足球", 19, R.drawable.bt_match_item_mszq_selector));
            MATCH_GAMES.put(36, new SportTypeItem(36, "tabletennis", "乒乓球", 36, R.drawable.bt_match_item_bbq_selector));
            MATCH_GAMES.put(25, new SportTypeItem(25, "icehockey", "冰上曲棍球", 25, R.drawable.bt_match_item_iceq_selector));
            MATCH_GAMES.put(4, new SportTypeItem(4, "rugby", "橄榄球", 4, R.drawable.bt_match_item_glq_selector));
            MATCH_GAMES.put(8, new SportTypeItem(8, "handball", "手球", 12, R.drawable.bt_match_item_sq_selector));
            MATCH_GAMES.put(10, new SportTypeItem(10, "floorball", "地板球", 24, R.drawable.bt_match_item_dbq_selector));
            MATCH_GAMES.put(12, new SportTypeItem(12, "golf", "高尔夫", 17, R.drawable.bt_match_item_golf_selector));
            MATCH_GAMES.put(14, new SportTypeItem(14, "cricket", "板球", 15, R.drawable.bt_match_item_wbq_selector));
            MATCH_GAMES.put(17, new SportTypeItem(17, "futsal", "五人足球", 25, R.drawable.bt_match_item_wrzq_selector));
            MATCH_GAMES.put(18, new SportTypeItem(18, "mixedmartialarts", "综合", 13, R.drawable.bt_match_item_zhgd_selector));
            MATCH_GAMES.put(11, new SportTypeItem(11, "boxing", "拳击", 11, R.drawable.bt_match_item_qj_selector));
            MATCH_GAMES.put(20, new SportTypeItem(15, "darts", "飞镖", 15, R.drawable.bt_match_item_darts_selector));
            MATCH_GAMES.put(21, new SportTypeItem(21, "bowls", "草地滚球", 20, R.drawable.bt_match_item_cdgq_selector));
            MATCH_GAMES.put(24, new SportTypeItem(24, "waterpolo", "水球", 16, R.drawable.bt_match_item_water_ball_selector));
            MATCH_GAMES.put(25, new SportTypeItem(25, "cycling", "自行车", 17, R.drawable.bt_match_item_cycling_selector));
            MATCH_GAMES.put(51, new SportTypeItem(51, "beachvolleyball", "沙滩排球", 18, R.drawable.bt_match_item_stpq_selector));
            MATCH_GAMES.put(92, new SportTypeItem(92, "formula1", "F1赛车", 21, R.drawable.bt_match_item_f1_selector));
            MATCH_GAMES.put(93, new SportTypeItem(93, "specials", "特殊投注", 22, R.drawable.bt_match_item_tstz_selector));
            MATCH_GAMES.put(29, new SportTypeItem(29, "stockcarracing", "赛车", 29, R.drawable.bt_match_item_sc_selector));
            MATCH_GAMES.put(100, new SportTypeItem(100, "olympic", "奥林匹克", 24, R.drawable.bt_match_item_olympic_selector));
            MATCH_GAMES.put(64, new SportTypeItem(64, "dota2", "DOTA2", 64, R.drawable.bt_match_item_data_selector));
            MATCH_GAMES.put(63, new SportTypeItem(63, "lol", "英雄联盟", 27, R.drawable.bt_match_item_lol_selector));
            MATCH_GAMES.put(66, new SportTypeItem(66, "kog", "王者荣耀", 66, R.drawable.bt_match_item_yl_selector));
            MATCH_GAMES.put(177, new SportTypeItem(177, "e-football", "电子足球", 29, R.drawable.bt_match_item_dzzq_selector));
            MATCH_GAMES.put(178, new SportTypeItem(178, "e-basketball", "电子篮球", 30, R.drawable.bt_match_item_dzlq_selector));
        }
        CfLog.d("============ getMatchGames MATCH_GAMES after ============"+MATCH_GAMES.size());
        return MATCH_GAMES;
    }

    private static Map<String, Integer> mapBgMatchDetailTop = new ArrayMap<>();
    /**
     * 玩法ID，与PLAY_METHOD_NAMES一一对应 "今日", "滚球", "早盘", "串关", "冠军"
     */
    public static String[] PLAY_METHOD_TYPES = new String[]{"3", "1", "4", "11", "100"};

    /**
     * 体育分类ID-足球
     */
    public final static String SPORT_ID_FB = "1";
    /**
     * 体育分类ID-篮球
     */
    public static String SPORT_ID_BSB = "2";
    /**
     * 体育分类ID-网球
     */
    public static String SPORT_ID_WQ = "5";
    /**
     * 体育分类ID-斯诺克
     */
    public static String SPORT_ID_SNK = "7";
    /**
     * 体育分类ID-棒球
     */
    public static String SPORT_ID_BQ = "3";
    /**
     * 体育分类ID-排球
     */
    public static String SPORT_ID_PQ = "9";
    /**
     * 体育分类ID-羽毛球
     */
    public static String SPORT_ID_YMQ = "10";
    /**
     * 体育分类ID-美式足球
     */
    public static String SPORT_ID_MSZQ = "6";
    /**
     * 体育分类ID-乒乓球
     */
    public static String SPORT_ID_BBQ = "8";
    /**
     * 体育分类ID-冰球
     */
    public static String SPORT_ID_ICEQ = "4";
    /**
     * 体育分类ID-拳击
     */
    public static String SPORT_ID_QJ = "12";
    /**
     * 体育分类ID-沙滩排球
     */
    public static String SPORT_ID_STPQ = "39";
    /**
     * 体育分类ID-手球
     */
    public static String SPORT_ID_SQ = "11";

    /**
     * 比分类型-得分
     */
    public static String SCORE_TYPE_SCORE = "S1";
    /**
     * 比分类型-上半场比分
     */
    public static String SCORE_TYPE_SCORE_SECOND_HALF = "S2";
    /**
     * 比分类型-角球
     */
    public static String SCORE_TYPE_CORNER = "S5";
    /**
     * 比分类型-黄牌
     */
    public static String SCORE_TYPE_YELLOW_CARD = "S12";
    /**
     * 比分类型-红牌
     */
    public static String SCORE_TYPE_RED_CARD = "S11";

    public static int getBgMatchDetailTop(String sportId) {
        if (mapBgMatchDetailTop.isEmpty()) {
            mapBgMatchDetailTop.put(SPORT_ID_FB, R.mipmap.bt_detail_bg_zq_top);
            mapBgMatchDetailTop.put(SPORT_ID_BSB, R.mipmap.bt_detail_bg_lq_top);
            mapBgMatchDetailTop.put(SPORT_ID_WQ, R.mipmap.bt_detail_bg_wq_top);
            mapBgMatchDetailTop.put(SPORT_ID_SNK, R.mipmap.bt_detail_bg_snk_top);
            mapBgMatchDetailTop.put(SPORT_ID_BQ, R.mipmap.bt_detail_bg_bq_top);
            mapBgMatchDetailTop.put(SPORT_ID_PQ, R.mipmap.bt_detail_bg_pq_top);
            mapBgMatchDetailTop.put(SPORT_ID_YMQ, R.mipmap.bt_detail_bg_ymq_top);
            mapBgMatchDetailTop.put(SPORT_ID_MSZQ, R.mipmap.bt_detail_bg_mszq_top);
            mapBgMatchDetailTop.put(SPORT_ID_ICEQ, R.mipmap.bt_detail_bg_bnq_top);
            mapBgMatchDetailTop.put(SPORT_ID_BBQ, R.mipmap.bt_detail_bg_bbq_top);
            mapBgMatchDetailTop.put(SPORT_ID_QJ, R.mipmap.bt_detail_bg_mszq_default_top);
            mapBgMatchDetailTop.put(SPORT_ID_STPQ, R.mipmap.bt_detail_bg_pq_top);
            mapBgMatchDetailTop.put(SPORT_ID_SQ, R.mipmap.bt_detail_bg_mszq_default_top);
        }
        return mapBgMatchDetailTop.get(sportId);
    }

    private static List<HotLeague> hotFootBallLeagueTopList = new ArrayList<>();
    private static List<HotLeague> hotBasketBallLeagueTopList = new ArrayList<>();
    private static Map<String, Integer> mapHotLeagueIconTop = new ArrayMap<>();

    public static int getHotLeagueIcon(String code) {
        if (mapHotLeagueIconTop.isEmpty()) {
            mapHotLeagueIconTop.put("allleague", R.drawable.bt_hot_league_item_epl_selector);
            mapHotLeagueIconTop.put("EURO", R.drawable.bt_hot_league_item_euro_selector);
            mapHotLeagueIconTop.put("CONMEBOL", R.drawable.bt_hot_league_item_conmebol_selector);
            mapHotLeagueIconTop.put("UCL", R.drawable.bt_hot_league_item_ucl_selector);
            mapHotLeagueIconTop.put("EPL", R.drawable.bt_hot_league_item_epl_selector);
            mapHotLeagueIconTop.put("SEA", R.drawable.bt_hot_league_item_sea_selector);
            mapHotLeagueIconTop.put("LaLiga", R.drawable.bt_hot_league_item_laliga_selector);
            mapHotLeagueIconTop.put("BVB", R.drawable.bt_hot_league_item_bvb_selector);
            mapHotLeagueIconTop.put("Ligue1", R.drawable.bt_hot_league_item_ligue1_selector);
            mapHotLeagueIconTop.put("CSL", R.drawable.bt_hot_league_item_csl_selector);
            mapHotLeagueIconTop.put("NBA", R.drawable.bt_hot_league_item_nba_selector);
            mapHotLeagueIconTop.put("NBL", R.drawable.bt_hot_league_item_nbl_selector);
            mapHotLeagueIconTop.put("CBA", R.drawable.bt_hot_league_item_cba_selector);
            mapHotLeagueIconTop.put("JB1", R.drawable.bt_hot_league_item_jb1_selector);
            mapHotLeagueIconTop.put("KBL", R.drawable.bt_hot_league_item_kbl_selector);
            mapHotLeagueIconTop.put("EL", R.drawable.bt_hot_league_item_el_selector);
        }
        return mapHotLeagueIconTop.get(code);
    }

    public static List<HotLeague> getHotFootBallLeagueTopList() {
        if (hotFootBallLeagueTopList.isEmpty()) {
            Gson gson = new Gson();
            hotFootBallLeagueTopList.add(gson.fromJson("{ leagueid: [], code: \"allleague\", name: \"全部\"}", HotLeague.class));
            //hotFootBallLeagueTopList.add(gson.fromJson("{ leagueid: [33163,1196925222286433163], code: \"EURO\", name: \"欧洲杯\"}", HotLeague.class));
            //hotFootBallLeagueTopList.add(gson.fromJson("{ leagueid: [151,10011000151], code: \"CONMEBOL\", name: \"美洲杯\"}", HotLeague.class));
            hotFootBallLeagueTopList.add(gson.fromJson("{ leagueid: [2,6408,352541568130764801], code: \"UCL\", name: \"欧冠\"}", HotLeague.class));
            hotFootBallLeagueTopList.add(gson.fromJson("{ leagueid: [3,180,29461,1682748461414224369], code: \"EPL\", name: \"英超\"}", HotLeague.class));
            hotFootBallLeagueTopList.add(gson.fromJson("{ leagueid: [4,239], code: \"SEA\", name: \"意甲\"}", HotLeague.class));
            hotFootBallLeagueTopList.add(gson.fromJson("{ leagueid: [5,320,1682748470622372141], code: \"LaLiga\", name: \"西甲\"}", HotLeague.class));
            hotFootBallLeagueTopList.add(gson.fromJson("{ leagueid: [6,32230,276,29569], code: \"BVB\", name: \"德甲\"}", HotLeague.class));
            hotFootBallLeagueTopList.add(gson.fromJson("{ leagueid: [7,79,343264595247255555], code: \"Ligue1\", name: \"法甲\"}", HotLeague.class));
            hotFootBallLeagueTopList.add(gson.fromJson("{ leagueid: [8,6344,1682748478869187623], code: \"CSL\", name: \"中超\"}", HotLeague.class));
        }
        return hotFootBallLeagueTopList;
    }

    public static List<HotLeague> getHotBasketFootBallLeagueTopList() {
        if (hotBasketBallLeagueTopList.isEmpty()) {
            Gson gson = new Gson();
            hotBasketBallLeagueTopList.add(gson.fromJson("{ leagueid: [], code: \"allleague\", name: \"全部\"}", HotLeague.class));
            hotBasketBallLeagueTopList.add(gson.fromJson("{ leagueid: [132], code: \"NBA\", name: \"NBA\"}", HotLeague.class));
            hotBasketBallLeagueTopList.add(gson.fromJson("{ leagueid: [427], code: \"NBL\", name: \"澳洲NBL\"}", HotLeague.class));
            hotBasketBallLeagueTopList.add(gson.fromJson("{ leagueid: [146], code: \"CBA\", name: \"CBA\"}", HotLeague.class));
            hotBasketBallLeagueTopList.add(gson.fromJson("{ leagueid: [48], code: \"JB1\", name: \"日本B1\"}", HotLeague.class));
            hotBasketBallLeagueTopList.add(gson.fromJson("{ leagueid: [208], code: \"KBL\", name: \"韩国KBL\"}", HotLeague.class));
            hotBasketBallLeagueTopList.add(gson.fromJson("{ leagueid: [122], code: \"EL\", name: \"欧洲EL\"}", HotLeague.class));
        }
        return hotBasketBallLeagueTopList;
    }

}
