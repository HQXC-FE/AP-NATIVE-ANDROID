package com.xtree.mine.ui.rebateagrt.model;

import androidx.databinding.ObservableField;

import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.base.utils.ClickUtil;

import io.reactivex.functions.Consumer;

/**
 * Created by KAKA on 2024/3/19.
 * Describe:
 */
public class DividendAgrtCheckModel extends BindModel {
    //标题
    public ObservableField<String> numText = new ObservableField<>();
    //编辑模式
    public ObservableField<Boolean> editMode = new ObservableField<>(false);
    public ObservableField<Boolean> deleteMode = new ObservableField<>(false);
    public ObservableField<Boolean> filter2EditMode = new ObservableField<>(true);
    private Consumer<DividendAgrtCheckModel> selectRatioCallBack = null;
    //分红比例
    public ObservableField<String> ratioData = new ObservableField<>("");
    private String ratio = "";
    //累计投注额
    private String profit = "";
    //累计亏损额
    private String netProfit = "0";
    //活跃人数
    private String people = "";
    //连续亏损周期
    private String loseStreak = "0";
    /**
     * 分红比例范围
     */
    private String ratio_range = "";

    public void setSelectRatioCallBack(Consumer<DividendAgrtCheckModel> selectRatioCallBack) {
        this.selectRatioCallBack = selectRatioCallBack;
    }

    public void selectRatio() {
        if (ClickUtil.isFastClick()) {
            return;
        }
        if (selectRatioCallBack != null) {
            try {
                selectRatioCallBack.accept(this);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public String getRatio() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
        this.ratioData.set(ratio + "%");
    }

    public String getRatio_range() {
        return ratio_range;
    }

    public void setRatio_range(String ratio_range) {
        this.ratio_range = ratio_range;
        this.ratioData.set(ratio_range + "%");
    }

    public String getProfit() {
        return profit;
    }

    public void setProfit(String profit) {
        this.profit = profit;
    }

    public String getNetProfit() {
        return netProfit;
    }

    public void setNetProfit(String netProfit) {
        this.netProfit = netProfit;
    }

    public String getPeople() {
        return people;
    }

    public void setPeople(String people) {
        this.people = people;
    }

    public String getLoseStreak() {
        return loseStreak;
    }

    public void setLoseStreak(String loseStreak) {
        this.loseStreak = loseStreak;
    }
}
