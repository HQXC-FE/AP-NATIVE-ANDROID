package com.xtree.lottery.ui.lotterybet.model;

import android.util.ArrayMap;
import android.util.Log;

import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.base.vo.UserMethodsResponse;
import com.xtree.lottery.data.config.Lottery;
import com.xtree.lottery.data.source.vo.MenuMethodsData;

import java.util.Map;

/**
 * Created by KAKA on 2024/4/29.
 * Describe:
 */
public class LotteryPlayCollectionModel extends BindModel {

    private MenuMethodsData.LabelsDTO.Labels1DTO label;
    private MenuMethodsData.LabelsDTO menulabel;
    private Map<String, UserMethodsResponse.DataDTO> userMethods = new ArrayMap<>();
    private Lottery lottery;

    public Lottery getLottery() {
        return lottery;
    }

    public void setLottery(Lottery lottery) {
        this.lottery = lottery;
    }

    public UserMethodsResponse.DataDTO getUserMethod(String key) {
        return userMethods.get(key);
    }

    public void putUserMethods(String key, UserMethodsResponse.DataDTO userMethod) {
        this.userMethods.put(key, userMethod);
    }

    public MenuMethodsData.LabelsDTO.Labels1DTO getLabel() {
        return label;
    }

    public void setLabel(MenuMethodsData.LabelsDTO.Labels1DTO label) {
        this.label = label;
    }

    public MenuMethodsData.LabelsDTO getMenulabel() {
        return menulabel;
    }

    public void setMenulabel(MenuMethodsData.LabelsDTO menulabel) {
        this.menulabel = menulabel;
    }

    public String labelName() {
        return label.getTitle() + "：";
    }
}
