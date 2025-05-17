package com.xtree.bet.bean.ui;

import android.os.Parcel;


import com.xtree.bet.bean.response.im.MarketLine;
import com.xtree.bet.bean.response.im.OddsListItem;
import com.xtree.bet.bean.response.im.OptionDataListInfo;
import com.xtree.bet.bean.response.im.OptionInfo;
import com.xtree.bet.bean.response.im.WagerSelection;

import java.util.ArrayList;
import java.util.List;

public class OptionListIm implements OptionList {
    private String className;
    WagerSelection wagerSelection;

    MarketLine marketLine;

    public OptionListIm(WagerSelection wagerSelection, MarketLine marketLine) {
        this.wagerSelection = wagerSelection;
        this.marketLine = marketLine;
        this.className = getClass().getSimpleName();
    }

    public OptionListIm(MarketLine marketLine) {
        this.marketLine = marketLine;
        this.className = getClass().getSimpleName();
    }

    public long getId() {
        return marketLine.getMarketlineId();
    }

    @Override
    public int getMatchType() {
        return 0;
    }

    /**
     * 指出盘口状态.
     * 1 = 开盘
     * 2 = 关盘
     */
    public boolean isOpen() {
        return !marketLine.isLocked; //未锁定就是开盘
    }

    /**
     * 是否支持串关，0 不可串关，1 可串关
     */
    public boolean isAllowCrossover() {
        return marketLine.isOpenParlay();
    }

    /**
     * 是否为最优线，带线玩法可用该字段进行排序，从小到大
     * 代表优先级，比如让分玩法有-0.5 -0.25 0几个让球方式，这个属性就代码了它们的优先级
     */
    public int getSort() {
        return 0;
    }

    /**
     * line值，带线玩法的线，例如大小球2.5线，部分玩法展示可用该字段进行分组展示
     */
    public String getLine() {
        return null;
    }

    /**
     * 玩法选项
     */
    @Override
    public List<Option> getOptionList() {
        List<Option> optionList = new ArrayList<>();
        for (WagerSelection optionInfo : marketLine.getWagerSelections()) {
            optionList.add(new OptionIm(optionInfo));
        }
        return optionList;
    }


    @Override
    public int getPlaceNum() {
        return 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.className);
        dest.writeParcelable(this.wagerSelection, flags);
        dest.writeParcelable(this.marketLine,flags);
    }

    protected OptionListIm(Parcel in) {
        this.className = in.readString();
        this.wagerSelection = in.readParcelable(WagerSelection.class.getClassLoader());
        this.marketLine = in.readParcelable(MarketLine.class.getClassLoader());
    }

    public static final Creator<OptionListIm> CREATOR = new Creator<OptionListIm>() {
        @Override
        public OptionListIm createFromParcel(Parcel source) {
            return new OptionListIm(source);
        }

        @Override
        public OptionListIm[] newArray(int size) {
            return new OptionListIm[size];
        }
    };
}
