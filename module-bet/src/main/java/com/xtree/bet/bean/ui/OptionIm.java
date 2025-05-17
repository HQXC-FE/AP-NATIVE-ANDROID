package com.xtree.bet.bean.ui;

import android.os.Parcel;

import com.xtree.base.utils.CfLog;
import com.xtree.bet.bean.response.im.MarketLine;
import com.xtree.bet.bean.response.im.OptionDataListInfo;
import com.xtree.bet.bean.response.im.OptionInfo;
import com.xtree.bet.bean.response.im.WagerSelection;
import com.xtree.bet.constant.SPKey;

import java.math.BigDecimal;
import java.math.RoundingMode;

import me.xtree.mvvmhabit.utils.SPUtils;

public class OptionIm implements Option {
    private String className;
    private int change;
    private WagerSelection wagerSelection;
    private String code;
    private MarketLine marketLine;


    public OptionIm(WagerSelection wagerSelection) {
        this.wagerSelection = wagerSelection;
        this.className = getClass().getSimpleName();
    }

    public OptionIm(WagerSelection wagerSelection, MarketLine marketLine) {
        this.wagerSelection = wagerSelection;
        this.marketLine = marketLine;
        this.className = getClass().getSimpleName();
    }

//    public OptionIm(WagerSelection optionInfo, OptionDataListInfo optionList, MarketLine marketLine){
//        this.mOptionInfo = optionInfo;
//        this.optionList = optionList;
//        this.marketLine = marketLine;
//        this.className = getClass().getSimpleName();
//    }

    @Override
    public String getId() {
        return "";
    }

    /**
     * 选项全称，投注框一般用全称展示
     */
    public String getName() {
        if (wagerSelection == null) {
            return "";
        }
        return wagerSelection.getSelectionName();
    }

    /**
     * 选项简称(全名or简名，订单相关为全名，否则为简名)， 赔率列表一般都用简称展示
     */
    public String getSortName() {
        if (wagerSelection!=null && wagerSelection.getDisplayHandicap()!=null){
            return wagerSelection.getSelectionName() + " " +wagerSelection.getDisplayHandicap();
        }else {
            return wagerSelection.getSelectionName();
        }
    }

    /**
     * 选项类型，主、客、大、小等，投注时需要提交该字段作为选中的选项参数
     *
     * @return
     */
    public String getOptionType() {
        return String.valueOf(wagerSelection.getOddsType());
    }

    /**
     * 欧盘赔率，目前我们只提供欧洲盘赔率，投注是请提交该字段赔率值作为选项赔率，赔率小于0代表锁盘
     */
    public double getUiShowOdd() {
        if (isHongKongMarket()) {
            BigDecimal bg = BigDecimal.valueOf(wagerSelection.getOdds() - 1);
            return bg.setScale(2, RoundingMode.HALF_UP).doubleValue();
        }
        return wagerSelection.getOdds();
    }

    @Override
    public double getRealOdd() {
        return wagerSelection.getOdds();
    }

    /**
     * 赔率
     */
    public double getBodd() {
        return wagerSelection.getOdds();
    }

    /**
     * 赔率类型
     */
    public int getOddType() {
        return wagerSelection.getOddsType();
    }

    /**
     * 是否香港盘
     *
     * @return
     */
    @Override
    public boolean isHongKongMarket() {
        try {
            return SPUtils.getInstance().getInt(SPKey.BT_MATCH_LIST_ODDTYPE) == 2;
        } catch (Exception e) {
            return false;
        } catch (Throwable e) {
            return false;
        }
    }

    /**
     * 选项结算结果，仅虚拟体育展示
     */
    public int getSettlementResult() {
        return 0;
    }

    @Override
    public boolean setSelected(boolean isSelected) {
        return wagerSelection.isSelected = isSelected;
    }

    /**
     * 是否选中
     *
     * @return
     */
    @Override
    public boolean isSelected() {
        return wagerSelection.isSelected;
    }

    /**
     * line值，带线玩法的线，例如大小球2.5线，部分玩法展示可用该字段进行分组展示
     *
     * @return
     */
    @Override
    public String getLine() {
        return "";
    }

    /**
     * 设置投注选择唯一标识
     *
     * @param code
     */
    @Override
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 获取投注选择唯一标识
     *
     * @return
     */
    @Override
    public String getCode() {
        return code;
    }

    /**
     * 设置投注项赔率的变化
     *
     * @return
     */
    @Override
    public void setChange(double oldOdd) {
        CfLog.d("========== OptionIm setChange oldOdd ==========="+oldOdd);
        CfLog.d("========== OptionIm setChange getRealOdd ==========="+getRealOdd());
        change = oldOdd < getRealOdd() ? 1 : oldOdd > getRealOdd() ? -1 : 0;
        //Log.e("test", "===========" + change);
        wagerSelection.change = change;
        CfLog.d("========== OptionIm setChange mOptionInfo.change ==========="+wagerSelection.change);
    }

    /**
     * 赔率是否上升
     *
     * @return
     */
    @Override
    public boolean isUp() {
        CfLog.d("========== OptionIm isUp mOptionInfo.change==========="+wagerSelection.change);
        return wagerSelection.change == 1;
    }

    /**
     * 赔率是否下降
     *
     * @return
     */
    @Override
    public boolean isDown() {
        CfLog.d("========== OptionIm isDown mOptionInfo.change==========="+wagerSelection.change);
        return wagerSelection.change == -1;
    }

    @Override
    public void reset() {
        wagerSelection.change = 0;
    }

    /**
     * 获取投注选项所属的投注线
     *
     * @return
     */
    @Override
    public OptionList getOptionList() {
        if (wagerSelection == null) {
            return null;
        }
        return new OptionListIm(wagerSelection, marketLine);
    }

    @Override
    public boolean isBtHome() {
        return false;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.className);
        dest.writeInt(this.change);
        dest.writeParcelable(this.wagerSelection, flags);
        dest.writeString(this.code);
        dest.writeParcelable(this.marketLine, flags);
    }


    protected OptionIm(Parcel in) {
        this.className = in.readString();
        this.change = in.readInt();
        this.wagerSelection = in.readParcelable(WagerSelection.class.getClassLoader());
        this.code = in.readString();
        this.marketLine = in.readParcelable(MarketLine.class.getClassLoader());
    }

    public static final Creator<OptionIm> CREATOR = new Creator<OptionIm>() {
        @Override
        public OptionIm createFromParcel(Parcel source) {
            return new OptionIm(source);
        }

        @Override
        public OptionIm[] newArray(int size) {
            return new OptionIm[size];
        }
    };

    @Override
    public String toString() {
        return "OptionIm{" +
                "className='" + className + '\'' +
                ", change=" + change +
                ", mOptionInfo=" + wagerSelection +
                ", code='" + code + '\'' +
                ", marketLine=" + marketLine +
                '}';
    }

}
