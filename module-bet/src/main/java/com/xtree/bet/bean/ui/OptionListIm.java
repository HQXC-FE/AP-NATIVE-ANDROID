package com.xtree.bet.bean.ui;

import android.os.Parcel;


import com.xtree.bet.bean.response.im.OptionDataListInfo;
import com.xtree.bet.bean.response.im.OptionInfo;

import java.util.ArrayList;
import java.util.List;

public class OptionListIm implements OptionList {
    private String className;
    OptionDataListInfo optionDataListInfo;

    public OptionListIm(OptionDataListInfo optionDataListInfo){
        this.optionDataListInfo = optionDataListInfo;
        this.className = getClass().getSimpleName();
    }

    public long getId() {
        return optionDataListInfo.id;
    }

    @Override
    public int getMatchType() {
        return 0;
    }

    /**
     * 玩法销售状态，0暂停，1开售，-1未开售（未开售状态一般是不展示的）
     */
    public boolean isOpen() {
        return optionDataListInfo.ss == 1;
    }

    /**
     * 是否支持串关，0 不可串关，1 可串关
     */
    public boolean isAllowCrossover() {
        return optionDataListInfo.au == 1;
    }

    /**
     * 是否为最优线，带线玩法可用该字段进行排序，从小到大
     * 代表优先级，比如让分玩法有-0.5 -0.25 0几个让球方式，这个属性就代码了它们的优先级
     */
    public int getSort() {
        return optionDataListInfo.mbl;
    }

    /**
     * line值，带线玩法的线，例如大小球2.5线，部分玩法展示可用该字段进行分组展示
     */
    public String getLine() {
        return optionDataListInfo.li;
    }

    /**
     * 玩法选项
     */
    @Override
    public List<Option> getOptionList() {
        List<Option> optionList = new ArrayList<>();
        for (OptionInfo optionInfo : optionDataListInfo.op) {
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
        dest.writeParcelable(this.optionDataListInfo, flags);
    }

    public void readFromParcel(Parcel source) {
        this.className = source.readString();
        this.optionDataListInfo = source.readParcelable(OptionDataListInfo.class.getClassLoader());
    }

    protected OptionListIm(Parcel in) {
        this.className = in.readString();
        this.optionDataListInfo = in.readParcelable(OptionDataListInfo.class.getClassLoader());
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
