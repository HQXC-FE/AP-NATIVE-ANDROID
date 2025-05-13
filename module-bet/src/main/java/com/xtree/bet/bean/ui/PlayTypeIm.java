package com.xtree.bet.bean.ui;

import android.os.Parcel;
import android.text.TextUtils;

import com.xtree.base.utils.TimeUtils;
import com.xtree.bet.bean.response.im.MarketLine;
import com.xtree.bet.bean.response.im.MatchInfo;
import com.xtree.bet.bean.response.im.OptionDataListInfo;
import com.xtree.bet.bean.response.im.OptionInfo;
import com.xtree.bet.bean.response.im.PlayTypeInfo;
import com.xtree.bet.bean.response.im.WagerSelection;
import com.xtree.bet.constant.PMConstants;


import java.util.ArrayList;
import java.util.List;

public class PlayTypeIm implements PlayType {
    private String className;
    private MarketLine marketLine;

    private MatchInfo event;

    public PlayTypeIm(MarketLine marketLine){
        this.marketLine = marketLine;
        this.className = getClass().getSimpleName();
    }

    public PlayTypeIm(MarketLine marketLine, MatchInfo event) {
        this.marketLine = marketLine;
        this.className = getClass().getSimpleName();
        this.event = event;
    }

    @Override
    public String getId() {
        return String.valueOf(marketLine.getMarketlineId());
    }

    @Override
    public String getMarketId() {
        return String.valueOf(marketLine.getMarketlineId());
    }

    /**
     * 获取玩法类型，如 亚盘、大小球等
     *
     * @return
     */
    @Override
    public int getPlayType() {
        return marketLine.getBetTypeId();
    }

    /**
     * 获取玩法名称 - 详情中Group的显示标题
     * <p>
     * Group（ name = playTypeName）
     * Child
     * Child
     *
     * @return
     */
    @Override
    public String getPlayTypeName() {
        return marketLine.getPeriodName() + marketLine.getBetTypeName();
    }

    public MarketLine getPlayTypeInfo(){
        return marketLine;
    }

    @Override
    public String setPlayTypeName(String playTypeName) {
        marketLine.setBetTypeName(playTypeName);
        return marketLine.getBetTypeName();
    }

    @Override
    public List<OptionList> getOptionLists() {
        List<OptionList> optionLists = new ArrayList<>();
        if (marketLine != null && marketLine.getWagerSelections() != null) {
            for (WagerSelection optionDataListInfo : marketLine.getWagerSelections()) {
                optionLists.add(new OptionListIm(optionDataListInfo, marketLine));
            }
        }
        return optionLists;
    }

    /**
     * 获取投注玩法列表
     * @return
     */
    @Override
    public List<Option> getOptionList(String sportId) {
        List<Option> optionList = new ArrayList<>();
        int length = marketLine.getBetTypeName().contains("独赢") && TextUtils.equals(PMConstants.SPORT_ID_FB, sportId) || TextUtils.equals(PMConstants.SPORT_ID_ICEQ, sportId) ? 3 : 2;

        if(marketLine != null && marketLine.getWagerSelections() != null && !marketLine.getWagerSelections().isEmpty()) {
            for (int i = 0; i < length; i++) {
                OptionInfo optionInfo;
                WagerSelection wagerSelection = marketLine.wagerSelections.get(i);;
                try{
                    optionInfo = new OptionInfo();
                    optionInfo.setTy(wagerSelection.selectionId);
                    optionInfo.setBod(wagerSelection.odds);
                    optionInfo.setOdt(wagerSelection.oddsType);
                }catch (Exception e){
                    optionInfo = null;
                }

                if(optionInfo == null){
                    optionList.add(null);
                }else{
                    optionList.add(new OptionIm(wagerSelection, marketLine));
                }
            }
        }else{
            for (int i = 0; i < length; i++) {
                optionList.add(null);
            }
        }
        return optionList;
    }

    /**
     * 获取冠军赛事投注玩法列表
     * @return
     */
    @Override
    public List<Option> getChampionOptionList() {
        List<Option> optionList = new ArrayList<>();
        if(marketLine != null && marketLine.getWagerSelections() != null && !marketLine.getWagerSelections().isEmpty()) {
            for(WagerSelection wagerSelection: marketLine.getWagerSelections()){
                //for (OddsList optionInfo : wagerSelection.oddsList) {
                OptionDataListInfo optionDataListInfo = new OptionDataListInfo();
                optionDataListInfo.hs = marketLine.isLocked ? 1 : 0;
                optionDataListInfo.hmt = marketLine.betTypeId;
                optionList.add(new OptionIm(wagerSelection, marketLine));
            }
        }
        return optionList;
    }

    @Override
    public int getPlayPeriod() {
        return marketLine.getPeriodId();
    }

    /**
     * 获取盘口组标签集合
     *
     * @return
     */
    @Override
    public List<String> getTags() {
        return null;
    }

    /**
     * 获取所属玩法集ID
     *
     * @return
     */
    @Override
    public String getCategoryId() {
        return String.valueOf(marketLine.getMarketlineId());
    }

    @Override
    public String getMatchDeadLine() {
        //return TimeUtils.longFormatString(Long.valueOf(playTypeInfo.hmed), TimeUtils.FORMAT_YY_MM_DD_HH_MM_1);
        return TimeUtils.longFormatString(Long.parseLong(event.getEventDate()), TimeUtils.FORMAT_YY_MM_DD_HH_MM_1);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.className);
        dest.writeParcelable(this.marketLine, flags);
    }

    public void readFromParcel(Parcel source) {
        this.className = source.readString();
        this.marketLine = source.readParcelable(PlayTypeInfo.class.getClassLoader());
    }

    protected PlayTypeIm(Parcel in) {
        this.className = in.readString();
        this.marketLine = in.readParcelable(PlayTypeInfo.class.getClassLoader());
    }

    public static final Creator<PlayTypeIm> CREATOR = new Creator<PlayTypeIm>() {
        @Override
        public PlayTypeIm createFromParcel(Parcel source) {
            return new PlayTypeIm(source);
        }

        @Override
        public PlayTypeIm[] newArray(int size) {
            return new PlayTypeIm[size];
        }
    };
}
