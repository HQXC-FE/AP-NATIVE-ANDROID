package com.xtree.live.data.source.response.fb;

import android.os.Parcel;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.xtree.base.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 赛事列表UI显示需要用的比赛信息结构
 */
public class MatchFb implements Match{
    private String className;
    private static final String[] CHINESE_DIGITS = {"0", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
    MatchInfo matchInfo;

    private boolean isHead; //
    private boolean isExpand;

    public MatchFb(){
        this.className = getClass().getSimpleName();
    }

    public MatchFb(MatchInfo matchInfo){
        this.className = getClass().getSimpleName();
        this.matchInfo = matchInfo;
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
     * @return
     */
    public long getId(){
        if(matchInfo == null){
            return 0;
        }
        return matchInfo.id;
    }

    /**
     * 获取冠军赛赛事名称，用于展示名称
     * @return
     */
    @Override
    public String getChampionMatchName() {
        return matchInfo.nm;
    }

    /**
     * 获取主队名称
     * @return
     */
    @Override
    public String getTeamMain() {
        if(matchInfo.ts == null || matchInfo.ts.isEmpty()){
            return "";
        }
        return matchInfo.ts.get(0).na;
    }

    /**
     * 获取客队名称
     * @return
     */
    @Override
    public String getTeamVistor() {
        if(matchInfo.ts == null || matchInfo.ts.isEmpty()){
            return "";
        }
        return matchInfo.ts.get(1).na;
    }

    /**
     * 获取赛事阶段，如 足球上半场，篮球第一节等
     * @return
     */
    @Override
    public String getStage() {
        if(matchInfo.mc != null) {
            return FBMatchPeriod.getMatchPeriod(String.valueOf(matchInfo.mc.pe));
        }else {
            return "";
        }
    }

    /**
     * 是否足球比赛下半场
     * @return
     */
    @Override
    public boolean isFootBallSecondHalf() {
        if (matchInfo == null || matchInfo.mc == null) {
            return false;
        }
        return matchInfo.mc.pe == 1003 || matchInfo.mc.pe == 1004;
    }

    /**
     * 获取走表时间，以秒为单位，如250秒，客户端用秒去转换成时分秒时间
     * @return
     */
    @Override
    public String getTime() {
        if(matchInfo != null && matchInfo.mc != null && matchInfo.mc.s >= 0) {
            return TimeUtils.sToMs(matchInfo.mc.s);
        }else {
            return "";
        }
    }

    /**
     * 获取走表时间秒
     * @return
     */
    @Override
    public int getTimeS() {
        if(matchInfo != null && matchInfo.mc != null && matchInfo.mc.s >= 0) {
            return matchInfo.mc.s;
        }else {
            return 0;
        }
    }

    /**
     * 获取实时比分信息
     * @param type 比分类型，例如角球、黄牌等
     * @return
     */
    @Override
    public List<Integer> getScore(String... type) {
        if(matchInfo.nsg != null && !matchInfo.nsg.isEmpty()) {
            for (ScoreInfo scoreInfo : matchInfo.nsg) {
                if (TextUtils.equals(String.valueOf(scoreInfo.tyg), type[0])) {
                    return scoreInfo.sc;
                }
            }
        }
        List<Integer> sc = new ArrayList<>();
        sc.add(0);
        sc.add(0);
        return sc;
    }

    /**
     * 获取上半场比分信息
     * @return
     */
    @Override
    public List<Integer> getFirstHalfScore() {
        if(matchInfo.nsg != null && !matchInfo.nsg.isEmpty()) {
            for (ScoreInfo scoreInfo : matchInfo.nsg) {
                if (TextUtils.equals(String.valueOf(scoreInfo.tyg), String.valueOf(FBConstants.SCORE_TYPE_SCORE)) &&
                        TextUtils.equals(String.valueOf(scoreInfo.pe), "1002")) {
                    return scoreInfo.sc;
                }
            }
        }
        List<Integer> sc = new ArrayList<>();
        sc.add(0);
        sc.add(0);
        return sc;
    }

    /**
     * 获取比分信息
     * @param type 比分类型，例如角球、黄牌等
     * @return
     */
    @Override
    public List<Score> getScoreList(String... type) {
        List<Score> scoreInfos = new ArrayList<>();
        if(matchInfo.nsg != null && !matchInfo.nsg.isEmpty()) {
            for (ScoreInfo scoreInfo : matchInfo.nsg) {
                if (TextUtils.equals(String.valueOf(scoreInfo.tyg), type[0])) {
                    scoreInfos.add(new ScoreFb(scoreInfo));
                }
            }
        }

        return scoreInfos;
    }

    /**
     * 获取单个赛事玩法总数
     * @return
     */
    @Override
    public int getPlayTypeCount() {
        return matchInfo.tms;
    }

    /**
     * 获取玩法列表
     * @return
     */
    public List<PlayType> getPlayTypeList() {
        List<PlayType> playTypeList = new ArrayList<>();
        for (PlayTypeInfo playTypeInfo: matchInfo.mg) {
            playTypeInfo.hmed = matchInfo.bt;
            PlayTypeFb playTypeFbAdapter = new PlayTypeFb(playTypeInfo);
            playTypeList.add(playTypeFbAdapter);
        }
        return playTypeList;
    }

    /**
     * 是否有视频直播
     * @return
     */
    @Override
    public boolean hasVideo() {
        if(matchInfo == null || matchInfo.vs == null){
            return false;
        }
        return matchInfo.vs.have;
    }

    @Override
    public boolean isVideoStart() {
        return isGoingon();
    }

    /**
     * 是否有动画直播
     * @return
     */
    @Override
    public boolean hasAs() {
        return matchInfo.as != null && !matchInfo.as.isEmpty();
    }

    @Override
    public boolean isAnimationStart() {
        return isGoingon();
    }

    @Override
    public List<String> getVideoUrls() {
        List<String> urls = new ArrayList<>();
        if (!TextUtils.isEmpty(matchInfo.vs.m3u8SD)) {
            urls.add(matchInfo.vs.m3u8SD);
        } else if (!TextUtils.isEmpty(matchInfo.vs.m3u8HD)) {
            urls.add(matchInfo.vs.m3u8HD);
        } else if (!TextUtils.isEmpty(matchInfo.vs.flvSD)) {
            urls.add(matchInfo.vs.flvSD);
        } else if (!TextUtils.isEmpty(matchInfo.vs.flvHD)) {
            urls.add(matchInfo.vs.flvHD);
        } else if (!TextUtils.isEmpty(matchInfo.vs.web)) {
            urls.add(matchInfo.vs.web);
        }
        return urls;
    }

    @Override
    public List<String> getAnmiUrls() {
        return matchInfo.as;
    }

    /**
     * 获取联赛信息
     * @return
     */
    @Override
    public League getLeague() {
        return new LeagueFb(matchInfo.lg);
    }

    /**
     * 获取主队logo
     * @return
     */
    @Override
    public String getIconMain() {
        return matchInfo.ts.get(0).lurl;
    }

    /**
     * 获取客队logo
     * @return
     */
    @Override
    public String getIconVisitor() {
        return matchInfo.ts.get(1).lurl;
    }

    /**
     * 获取比赛是否开始状态
     * @return
     */
    @Override
    public boolean isGoingon() {
        return matchInfo.ms != 4;
    }

    @Override
    public long getMatchTime() {
        return this.matchInfo.bt;
    }
    /**
     * 是否冠军赛事
     * @return
     */
    @Override
    public boolean isChampion() {
        return this.matchInfo.ty == 1;
    }
    /**
     * 获取赛种ID，如足球，篮球
     */
    @Override
    public String getSportId() {
        if (this.matchInfo == null) {
            return "";
        }
        return String.valueOf(this.matchInfo.sid);
    }
    /**
     * 获取赛种名称，如足球，篮球
     */
    @Override
    public String getSportName() {
        return FBSportName.getSportName(getSportId());
    }

    @Override
    public String getReferUrl() {
        return null;
    }

    /**
     * PM设置播放器请求头信息
     * @param referUrl
     */
    @Override
    public void setReferUrl(String referUrl) {

    }

    /**
     * 是否已经产生有角球
     * @return
     */
    @Override
    public boolean hasCornor() {
        List<Integer> cornor = getScore(String.valueOf(FBConstants.SCORE_TYPE_CORNER));
        return cornor.get(0) > 0 || cornor.get(1) > 0;
    }

    /**
     * 是否中立场
     * @return
     */
    @Override
    public boolean isNeutrality() {
        return matchInfo.ne == 1;
    }

    /**
     * 是否篮球上下半场赛节配置
     * @return
     */
    @Override
    public boolean isBasketBallDouble() {
        if(matchInfo == null || matchInfo.mc == null){
            return false;
        }
        return matchInfo.mc.pe == 3002 || matchInfo.mc.pe == 3003 || matchInfo.mc.pe == 3004;
    }

    /**
     * 是否主队发球
     * @return
     */
    @Override
    public String getFormat() {
        return CHINESE_DIGITS[matchInfo.fid] + (matchInfo.sid == 5 ? "盘" : "局") + CHINESE_DIGITS[(matchInfo.fid / 2 + 1)] + "胜";
    }

    /**
     * 是否主队发球
     * @return
     */
    @Override
    public boolean isHomeSide() {
        return matchInfo.ssi == 1;
    }

    /**
     * 是否需要显示发球方图标
     * @return
     */
    @Override
    public boolean needCheckHomeSide() {
        return matchInfo.sid == Integer.valueOf(FBConstants.SPORT_ID_WQ)
                || matchInfo.sid == Integer.valueOf(FBConstants.SPORT_ID_PQ)
                || matchInfo.sid == Integer.valueOf(FBConstants.SPORT_ID_STPQ)
                || matchInfo.sid == Integer.valueOf(FBConstants.SPORT_ID_YMQ)
                || matchInfo.sid == Integer.valueOf(FBConstants.SPORT_ID_BBQ)
                || matchInfo.sid == Integer.valueOf(FBConstants.SPORT_ID_SNK)
                || matchInfo.sid == Integer.valueOf(FBConstants.SPORT_ID_BQ)
                || matchInfo.sid == Integer.valueOf(FBConstants.SPORT_ID_MSZQ);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(this == obj){
            return true;
        }
        if(obj == null || obj.getClass() != OptionFb.class){
            return false;
        }
        MatchFb optionFb = (MatchFb) obj;
        return getId() == optionFb.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(String.valueOf(getId()));
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.className);
        dest.writeParcelable(this.matchInfo, flags);
        dest.writeByte(this.isHead ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isExpand ? (byte) 1 : (byte) 0);
    }

    public void readFromParcel(Parcel source) {
        this.className = source.readString();
        this.matchInfo = source.readParcelable(MatchInfo.class.getClassLoader());
        this.isHead = source.readByte() != 0;
        this.isExpand = source.readByte() != 0;
    }

    protected MatchFb(Parcel in) {
        this.className = in.readString();
        this.matchInfo = in.readParcelable(MatchInfo.class.getClassLoader());
        this.isHead = in.readByte() != 0;
        this.isExpand = in.readByte() != 0;
    }

    public static final Creator<MatchFb> CREATOR = new Creator<MatchFb>() {
        @Override
        public MatchFb createFromParcel(Parcel source) {
            return new MatchFb(source);
        }

        @Override
        public MatchFb[] newArray(int size) {
            return new MatchFb[size];
        }
    };
}
