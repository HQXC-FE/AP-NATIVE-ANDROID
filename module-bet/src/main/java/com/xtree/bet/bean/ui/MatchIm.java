package com.xtree.bet.bean.ui;

import static com.xtree.base.utils.BtDomainUtil.KEY_PLATFORM;

import android.os.Parcel;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.TimeUtils;
import com.xtree.bet.bean.response.im.LeagueInfo;
import com.xtree.bet.bean.response.im.LiveStreamingUrl;
import com.xtree.bet.bean.response.im.MarketLine;
import com.xtree.bet.bean.response.im.MatchInfo;
import com.xtree.bet.constant.IMConstants;
import com.xtree.bet.constant.IMMatchPeriod;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 赛事列表UI显示需要用的比赛信息结构
 */
public class MatchIm implements Match {
    private String className;

    MatchInfo matchInfo;

    LeagueIm mLeague;
    /**
     * 播放器请求头
     */
    private String referUrl;
    private boolean isHead;
    private boolean isExpand;

    private List<MatchInfo> matchInfoList;

    public MatchIm() {
        this.className = getClass().getSimpleName();
    }

    public MatchIm(MatchInfo matchInfo) {
        this.className = getClass().getSimpleName();
        this.matchInfo = matchInfo;
    }

    public MatchIm(List<MatchInfo> matchInfoList) {
        this.className = getClass().getSimpleName();
        this.matchInfoList = matchInfoList;
    }

    @Override
    public boolean isHead() {
        return isHead;
    }

    @Override
    public void setHead(boolean isHead) {
        this.isHead = isHead;
    }

    @Override
    public void setExpand(boolean isExpand) {
        this.isExpand = isExpand;
    }

    @Override
    public boolean isExpand() {
        return isExpand;
    }

    /**
     * 获取比赛ID
     *
     * @return
     */
    public long getId() {
        return Long.valueOf(matchInfo.eventId);
    }

    /**
     * 获取冠军赛赛事名称，用于展示名称
     *
     * @return
     */
    @Override
    public String getChampionMatchName() {
        return matchInfo.competition.getCompetitionName();
    }

    /**
     * 获取主队名称
     *
     * @return
     */
    @Override
    public String getTeamMain() {
        return matchInfo.homeTeam;
    }

    /**
     * 获取客队名称
     *
     * @return
     */
    @Override
    public String getTeamVistor() {
        return matchInfo.awayTeam;
    }

    /**
     * 获取赛事阶段，如 足球上半场，篮球第一节等
     *
     * @return
     */
    @Override
    public String getStage() {
        String state = matchInfo.rbTime;
        if(state != null && !state.isEmpty()){
            String period = state.split(" ")[0];
            String time = "";
            if(state.split(" ").length >1 ){
                time = state.split(" ")[1];
            }
            return IMMatchPeriod.getMatchPeriod(String.valueOf(period)) + " " +time;
        }
        return "";
    }

    /**
     * 是否足球比赛下半场
     *
     * @return
     */
    @Override
    public boolean isFootBallSecondHalf() {
        return TextUtils.equals(matchInfo.rbTime, "7") || TextUtils.equals(matchInfo.rbTime, "31");
    }

    /**
     * 获取走表时间，以秒为单位，如250秒，客户端用秒去转换成时分秒时间
     *
     * @return
     */
    @Override
    public String getTime() {
        try {
            String state = matchInfo.rbTime;
            String time = state.split(" ")[1];

            if (matchInfo != null && time != null) {
                return TimeUtils.sToMs(Integer.valueOf(time));
            } else {
                return "";
            }
        } catch (Exception e) {
            return "";
        }

    }

    /**
     * 获取走表时间秒
     *
     * @return
     */
    @Override
    public int getTimeS() {
        String state = matchInfo.rbTime;
        String time = state.split(" ")[1];
        if (matchInfo != null && time != null) {
            return Integer.valueOf(time);
        } else {
            return 0;
        }
    }

    /**
     * 获取实时比分信息
     *
     * @param type 比分类型，例如角球、黄牌等
     * @return
     */
    @Override
    public List<Integer> getScore(String... type) {
        if(type == null || type.length == 0) return null;
        List<Integer> sc = new ArrayList<>();
        CfLog.d("============= MatchIm getScore type ================="+type[0]);
        if(type[0].equals(IMConstants.SCORE_TYPE_SCORE)){ //获取比分
            if(matchInfo != null && matchInfo.relatedScores != null && matchInfo.relatedScores.size() > 0){
                int homeScore = matchInfo.relatedScores.get(0).homeScore;
                int awayScore = matchInfo.relatedScores.get(0).awayScore;
                sc.add(homeScore);
                sc.add(awayScore);
            }
            return sc;
        }else{ //获取红黄牌
            Gson gson = new Gson();
            Type typeJson = new TypeToken<Map<String, String>>() {}.getType();
            Map<String, String> extraInfoMap = gson.fromJson(matchInfo.extraInfo, typeJson);
            if(extraInfoMap.get(type[0])!= null){
                sc.add(Integer.parseInt(extraInfoMap.get(type[0])));
                sc.add(Integer.parseInt(extraInfoMap.get(type[0])));
            }
            return sc;
        }
    }

    /**
     * 获取上半场比分信息
     *
     * @return
     */
    @Override
    public List<Integer> getFirstHalfScore() {
        return getScore(new String[]{String.valueOf(IMConstants.SCORE_TYPE_SCORE_SECOND_HALF)});
    }

    /**
     * 获取比分信息
     *
     * @param type 比分类型，例如角球、黄牌等
     * @return
     */
    @Override
    public List<Score> getScoreList(String... type) {//["S19","S20","S21","S22","S7"]

        List<Score> scoreInfos = new ArrayList<>();
        if (type == null) {
            return scoreInfos;
        }
        //matchInfo.msc 在列表接口和详情接口返回的数据顺序不同  改为统一使用type的顺序
        //列表["S3|23:24","S20|16:12","S22|15:10","S21|8:14","S1088|0.0:0.0","S108|0:0","S10607|0:0","S10904|0:0","S109|0:0","S10606|0:0","S10903|0:0","S106|0:0","S10902|0:0","S107|0:0","S10604|0:0","S10901|0:0","S10603|0:0","S10602|0:0","S10601|0:0","S191|0:0","S190|0:0","S111|0.0:0.0","S19|8:18","S110|0:0","S1111|47:54","S1|47:54","S1235|0.0:0.0","S2|24:30"]
        //详情["S1|55:60","S2|24:30","S3|31:30","S19|8:18","S20|16:12","S21|8:14","S22|23:16","S106|0:0","S107|0:0","S108|0:0","S109|0:0","S110|0:0","S111|0.0:0.0","S190|0:0","S191|0:0","S1088|0.0:0.0","S1111|55:60","S1235|0.0:0.0","S10601|0:0","S10602|0:0","S10603|0:0","S10604|0:0","S10606|0:0","S10607|0:0","S10901|0:0","S10902|0:0","S10903|0:0","S10904|0:0"]
        String state = matchInfo.rbTime;
        if (matchInfo.rbTime != null && !matchInfo.rbTime.isEmpty()) {
            for (int i = 0; i < type.length; i++) {//改为按type的顺序排序
                for (String strScore : matchInfo.rbTime.split(" ")) {

                    List<Integer> sc = new ArrayList<>();
                    if (!TextUtils.isEmpty(strScore) && strScore.contains("|") && strScore.startsWith(type[i] + "|")) {

                        String[] scoreStrs = strScore.split("\\|");
                        String score = scoreStrs[1];
                        score = score.substring(score.indexOf("|") + 1, score.length());
                        if (!TextUtils.isEmpty(score) && score.contains(":")) {
                            sc.add(Integer.valueOf(score.split(":")[0]));
                            sc.add(Integer.valueOf(score.split(":")[1]));
                            scoreInfos.add(new ScorePm(scoreStrs[0], sc));
                        }
                        break;

                    }
                }
            }
        }

        return scoreInfos;
    }

    /**
     * 获取单个赛事玩法总数
     *
     * @return
     */
    @Override
    public int getPlayTypeCount() {
        return matchInfo.totalMarketLineCount;
    }

    /**
     * 获取玩法列表
     *
     *
     * @return
     */
    public List<PlayType> getPlayTypeList() {
        List<PlayType> playTypeList = new ArrayList<>();
        for (MarketLine marketLine : matchInfo.marketLines) {
            if(marketLine.marketLineLevel == 1){
                PlayTypeIm playTypeIm = new PlayTypeIm(marketLine);
                playTypeList.add(playTypeIm);
            }
        }
        CfLog.d("============= MatchIm  playTypeList ================="+playTypeList);
        return playTypeList;
    }

    /**
     * 是否有视频直播
     *
     * @return
     */
    @Override
    public boolean hasVideo() {
        //if(matchInfo.liveStreamingUrl.size() > 0)
        return (matchInfo.liveStreamingUrl.size() > 0);
    }

    @Override
    public boolean isVideoStart() {
        //return matchInfo.mms == 2 || matchInfo.mms == 1;
        return (matchInfo.liveStreamingUrl.size() > 0);
    }

    /**
     * 是否有动画直播
     *
     * @return
     */
    @Override
    public boolean hasAs() {
        //return matchInfo.mvs != -1 && !getAnmiUrls().isEmpty(); 没找到动画对应字段
        return false;
    }

    @Override
    public boolean isAnimationStart() {
        //return matchInfo.mvs == 2 || matchInfo.mvs == 1;
        return false;
    }

    @Override
    public List<String> getVideoUrls() {
        List<String> urls = new ArrayList<>();
        if (matchInfo != null && matchInfo.liveStreamingUrl != null) {
            for (LiveStreamingUrl liveStreamingUrl : matchInfo.liveStreamingUrl) {
                String videoUrl = liveStreamingUrl.getUrl();
                if (!TextUtils.isEmpty(videoUrl)) {
                    urls.add(videoUrl);
                }
            }
        }
        return urls;
    }

    /**
     * 获取动画播放源信息
     *
     * @return
     */
    @Override
    public List<String> getAnmiUrls() {
        //return matchInfo.liveStreamingUrl.get(0).getUrl();
        //return matchInfo.as;
        List<String> list = new ArrayList<String>();
        return list;
    }

    /**
     * 获取联赛信息
     *
     * @return
     */
    @Override
    public League getLeague() {
        LeagueInfo leagueInfo;
        if (mLeague == null) {
            leagueInfo = new LeagueInfo();
            mLeague = new LeagueIm(leagueInfo);
        } else {
            leagueInfo = mLeague.getLeagueInfo();
        }
        //leagueInfo.picUrlthumb = matchInfo.lurl;
        leagueInfo.nameText = matchInfo.competition.getCompetitionName();
        if (!TextUtils.isEmpty(String.valueOf(matchInfo.competition.getCompetitionId()))) {
            leagueInfo.tournamentId = Long.valueOf(String.valueOf(matchInfo.competition.getCompetitionId()));
        }
        return mLeague;
    }

    /**
     * 获取主队logo
     *
     * @return
     */
    @Override
    public String getIconMain() {
//        if (matchInfo == null || matchInfo.mhlu == null || matchInfo.mhlu.isEmpty() || TextUtils.isEmpty(matchInfo.mhlu.get(0))) {
//            return "";
//        }
//        String logoUrl = matchInfo.mhlu.get(0);
//        String platform = SPUtils.getInstance().getString(KEY_PLATFORM);
//        String domain = SPUtils.getInstance().getString(SPKeyGlobal.PMXC_IMG_SERVICE_URL);
//        if (TextUtils.equals(platform, PLATFORM_PM) || TextUtils.equals(platform, PLATFORM_PMXC)) {
//            domain = SPUtils.getInstance().getString(SPKeyGlobal.PM_IMG_SERVICE_URL);
//        }
//        if (domain.endsWith("/") && logoUrl.startsWith("/")) {
//            return domain.substring(domain.indexOf("/")) + logoUrl;
//        } else if (!domain.endsWith("/") && !logoUrl.startsWith("/")) {
//            return domain + "/" + logoUrl;
//        } else {
//            return domain + logoUrl;
//        }
        return ""; //暂时没有球队图标
    }

    /**
     * 获取客队logo
     *
     * @return
     */
    @Override
    public String getIconVisitor() {
//        if (matchInfo == null || matchInfo.malu == null || matchInfo.malu.isEmpty() || TextUtils.isEmpty(matchInfo.malu.get(0))) {
//            return "";
//        }
//        String logoUrl = matchInfo.malu.get(0);
//        String platform = SPUtils.getInstance().getString(KEY_PLATFORM);
//        String domain = SPUtils.getInstance().getString(SPKeyGlobal.PMXC_IMG_SERVICE_URL);
//        if (TextUtils.equals(platform, PLATFORM_PM) || TextUtils.equals(platform, PLATFORM_PMXC)) {
//            domain = SPUtils.getInstance().getString(SPKeyGlobal.PM_IMG_SERVICE_URL);
//        }
//        if (domain.endsWith("/") && logoUrl.startsWith("/")) {
//            return domain.substring(domain.indexOf("/")) + logoUrl;
//        } else if (!domain.endsWith("/") && !logoUrl.startsWith("/")) {
//            return domain + "/" + logoUrl;
//        } else {
//            return domain + logoUrl;
//        }
        return ""; //暂时没有球队图标
    }

    /**
     * 获取比赛是否进行中状态
     *
     * @return
     */
    @Override
    public boolean isGoingon() {
        if (matchInfo.rbTime == null || matchInfo.rbTime.trim().isEmpty()) {
            return false;
        }

        String[] parts = matchInfo.rbTime.trim().split("\\s+");
        if (parts.length == 0) {
            return false;
        }

        String period = parts[0];
        return !(period.equals("!Live") || period.equals("HT") || period.equals("FT"));
    }

    /**
     * 获取比赛开始时间
     *
     * @return
     */
    @Override
    public long getMatchTime() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            // 去掉多余的小数位（.0000000 -> .000）
            String input = matchInfo.eventDate.replace(".0000000", ".000");
            OffsetDateTime offsetDateTime = OffsetDateTime.parse(input);
            Instant instant = offsetDateTime.toInstant(); // UTC 时间点
            return instant.toEpochMilli();
        }
        return 0L;
    }

    /**
     * 是否冠军赛事
     *
     * @return
     */
    @Override
    public boolean isChampion() {
        if(matchInfo.eventGroupTypeId != 1){
             return true;
        }else{
            return false;
        }
    }

    /**
     * 获取赛种ID，如足球，篮球
     */
    @Override
    public String getSportId() {
        return String.valueOf(matchInfo.sportId);
    }

    /**
     * 获取赛种名称，如足球，篮球
     */
    @Override
    public String getSportName() {
        return matchInfo.getSportName();
    }

    @Override
    public String getReferUrl() {
        return referUrl;
    }

    /**
     * PM设置播放器请求头信息
     *
     * @param referUrl
     */
    @Override
    public void setReferUrl(String referUrl) {
        this.referUrl = referUrl;
    }

    /**
     * 是否已经产生有角球
     *
     * @return
     */
    @Override
    public boolean hasCornor() {
        List<Integer> cornor = getScore(IMConstants.SCORE_TYPE_CORNER);
        if(cornor.size() > 1){
            return cornor.get(0) > 0 || cornor.get(1) > 0;
        }else{
            return false;
        }

    }

    /**
     * 是否中立场
     *
     * @return
     */
    @Override
    public boolean isNeutrality() {
        //return TextUtils.equals(matchInfo.mng, "1"); //未找到对应字段
        return false;
    }

    /**
     * 获取赛制
     *
     * @return
     */
    @Override
    public String getFormat() {
        //return matchInfo.mfo;  未找到对应字段
        return "";
    }

    @Override
    public boolean isHomeSide() {
        //return TextUtils.equals(matchInfo.mat, "home"); 未找到对应字段
        return false;

    }

    /**
     * 是否需要显示发球方图标
     *
     * @return
     */
    @Override
    public boolean needCheckHomeSide() {
        return TextUtils.equals(String.valueOf(matchInfo.rSportId), IMConstants.SPORT_ID_WQ)
                || TextUtils.equals(String.valueOf(matchInfo.rSportId), IMConstants.SPORT_ID_PQ)
                || TextUtils.equals(String.valueOf(matchInfo.rSportId), IMConstants.SPORT_ID_STPQ)
                || TextUtils.equals(String.valueOf(matchInfo.rSportId), IMConstants.SPORT_ID_YMQ)
                || TextUtils.equals(String.valueOf(matchInfo.rSportId), IMConstants.SPORT_ID_BBQ)
                || TextUtils.equals(String.valueOf(matchInfo.rSportId), IMConstants.SPORT_ID_SNK)
                || TextUtils.equals(String.valueOf(matchInfo.rSportId), IMConstants.SPORT_ID_BQ)
                || TextUtils.equals(String.valueOf(matchInfo.rSportId), IMConstants.SPORT_ID_MSZQ);
    }

    /**
     * 是否篮球上下半场赛节配置
     *
     * @return
     */
    @Override
    public boolean isBasketBallDouble() {
        if (matchInfo == null) {
            return false;
        }
        //return matchInfo.mle == 17; //未找到对应字段
        return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.className);
        dest.writeParcelable(this.matchInfo, flags);
        dest.writeParcelable(this.mLeague, flags);
        dest.writeString(this.referUrl);
        dest.writeByte(this.isHead ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isExpand ? (byte) 1 : (byte) 0);
    }

    public void readFromParcel(Parcel source) {
        this.className = source.readString();
        this.matchInfo = source.readParcelable(MatchInfo.class.getClassLoader());
        this.mLeague = source.readParcelable(LeaguePm.class.getClassLoader());
        this.referUrl = source.readString();
        this.isHead = source.readByte() != 0;
        this.isExpand = source.readByte() != 0;
    }

    protected MatchIm(Parcel in) {
        this.className = in.readString();
        this.matchInfo = in.readParcelable(MatchInfo.class.getClassLoader());
        this.mLeague = in.readParcelable(LeaguePm.class.getClassLoader());
        this.referUrl = in.readString();
        this.isHead = in.readByte() != 0;
        this.isExpand = in.readByte() != 0;
    }

    public static final Creator<MatchIm> CREATOR = new Creator<MatchIm>() {
        @Override
        public MatchIm createFromParcel(Parcel source) {
            return new MatchIm(source);
        }

        @Override
        public MatchIm[] newArray(int size) {
            return new MatchIm[size];
        }
    };
}
