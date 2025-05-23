package com.xtree.bet.bean.ui;

import android.os.Parcel;
import android.text.TextUtils;

import com.xtree.base.vo.BaseBean;
import com.xtree.bet.bean.response.im.BtConfirmInfo;
import com.xtree.bet.bean.response.im.WagerSelection;
import com.xtree.bet.bean.response.im.WagerSelectionInfo;

/**
 * 投注确认页选项
 */
public class BetConfirmOptionIm implements BetConfirmOption {
    private Match match;
    private PlayType playType;
    private OptionList optionList;
    private Option mOption;
    private String teamName;
    private double oldOdd;

    private WagerSelectionInfo wagerSelectionInfo;


    public BetConfirmOptionIm(Match match, PlayType playType, OptionList optionList, Option option) {
        this.optionList = optionList;
        this.mOption = option;
        this.match = match;
        this.playType = playType;
        teamName = match.getTeamMain() + " VS " + match.getTeamVistor();
        oldOdd = option.getRealOdd();
    }

    public BetConfirmOptionIm(WagerSelectionInfo wsi, String teamName) {
        this.wagerSelectionInfo = wsi;
        this.teamName = teamName;
    }

    /**
     * 设置比赛及投注信息
     *
     * @return
     */
    public void setData(Match match, PlayType playType, OptionList optionList, Option option) {
        this.optionList = optionList;
        this.mOption = option;
        this.match = match;
        this.playType = playType;
        teamName = match.getTeamMain() + " VS " + match.getTeamVistor();
    }

    /**
     * PM获取投注信息唯一标识
     *
     * @return
     */
    @Override
    public String getMatchId() {
        if (wagerSelectionInfo != null) {
            return String.valueOf(wagerSelectionInfo.getWagerId());
        } else {
            return String.valueOf(getMatch().getId());
        }
    }

    /**
     * 获取投注信息唯一标识
     *
     * @return
     */
    @Override
    public String getCode() {
        if(TextUtils.isEmpty(mOption.getCode())){
            return getMatchId() + mOption.getId();
        }else{
            return mOption.getCode();
        }
    }

    @Override
    public int getPlaceNum() {
        return 0 ;
    }

    @Override
    public String getOptionName() {
        if(!TextUtils.isEmpty(getOption().getSortName())){
            return getOption().getSortName();
        }
        return "";
    }

    @Override
    public String getPlayTypeId() {
        if ( wagerSelectionInfo!= null && !TextUtils.isEmpty(String.valueOf(wagerSelectionInfo.getMarketlineId()))) {
            return String.valueOf(wagerSelectionInfo.getMarketlineId());
        } else if(optionList.getId() > 0){
            return String.valueOf(optionList.getId());
        } else {
            return playType.getMarketId();
        }
    }

    @Override
    public Option getOption() {
        if (wagerSelectionInfo != null && mOption != null) {
            WagerSelection wagerSelection = new WagerSelection();
            wagerSelection.setSelectionId(wagerSelectionInfo.getBetTypeSelectionId());
            wagerSelection.setHandicap(wagerSelectionInfo.getHandicap());
            wagerSelection.setOdds(wagerSelectionInfo.getOdds());
            wagerSelection.setOddsType(wagerSelectionInfo.getOddsType());
//            wagerSelection.set

//            wagerSelection.setSelectionName(wagerSelectionInfo.get);//没有对应字段
            wagerSelection.setSpecifiers(wagerSelectionInfo.getSpecifiers());
            wagerSelection.setWagerSelectionId(wagerSelectionInfo.getWagerSelectionId());


            mOption = new OptionIm(wagerSelection);
            if(oldOdd > 0) {
                mOption.setChange(oldOdd);
            }
        }
        return mOption;
    }

    /**
     * 玩法销售状态是否关闭，0暂停，1开售，-1未开售
     *
     * @return
     */
    @Override
    public boolean isClose() {
        if (wagerSelectionInfo == null) {
            return false;
        }
        return wagerSelectionInfo.getMarketlineStatusId() != 1;
    }

    @Override
    public String getScore() {
        return "";
    }

    @Override
    public String getTeamName() {
        if (TextUtils.isEmpty(match.getTeamMain())) {
            return match.getChampionMatchName();
        }
        return teamName;
    }

    /**
     * 获取投注的比赛信息
     *
     * @return
     */
    @Override
    public Match getMatch() {
        return match;
    }

    /**
     * 获取投注项类型
     *
     * @return
     */
    @Override
    public String getOptionType() {
        if (wagerSelectionInfo != null ) {
            return String.valueOf(wagerSelectionInfo.getOddsType());
        } else {
            return mOption.getOptionType();
        }
    }

    /**
     * 获取投注玩法类型数据
     *
     * @return
     */
    @Override
    public OptionList getOptionList() {
        return optionList;
    }

    /**
     * 获取服务器返回的真实数据
     *
     * @return
     */
    @Override
    public void setRealData(BaseBean data) {
        if(wagerSelectionInfo != null ) {
            oldOdd = wagerSelectionInfo.getOdds();
        }
        this.wagerSelectionInfo = (WagerSelectionInfo) data;
    }

    /**
     * 获取投注玩法数据
     *
     * @return
     */
    @Override
    public PlayType getPlayType() {
        return playType;
    }

    @Override
    public BaseBean getRealData() {
        return wagerSelectionInfo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.wagerSelectionInfo, flags);
        dest.writeParcelable(this.match, flags);
        dest.writeParcelable(this.playType, flags);
        dest.writeParcelable(this.optionList, flags);
        dest.writeParcelable(this.mOption, flags);
        dest.writeString(this.teamName);
    }


    protected BetConfirmOptionIm(Parcel in) {
        this.wagerSelectionInfo = in.readParcelable(WagerSelectionInfo.class.getClassLoader());
        this.match = in.readParcelable(Match.class.getClassLoader());
        this.playType = in.readParcelable(PlayType.class.getClassLoader());
        this.optionList = in.readParcelable(OptionList.class.getClassLoader());
        this.mOption = in.readParcelable(Option.class.getClassLoader());
        this.teamName = in.readString();
    }

    public static final Creator<BetConfirmOptionIm> CREATOR = new Creator<BetConfirmOptionIm>() {
        @Override
        public BetConfirmOptionIm createFromParcel(Parcel source) {
            return new BetConfirmOptionIm(source);
        }

        @Override
        public BetConfirmOptionIm[] newArray(int size) {
            return new BetConfirmOptionIm[size];
        }
    };
}
