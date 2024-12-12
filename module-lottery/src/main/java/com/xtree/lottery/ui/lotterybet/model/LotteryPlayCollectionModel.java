package com.xtree.lottery.ui.lotterybet.model;

import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.lottery.data.source.response.MenuMethodsResponse;
import com.xtree.lottery.data.source.response.UserMethodsResponse;

/**
 * Created by KAKA on 2024/4/29.
 * Describe:
 */
public class LotteryPlayCollectionModel extends BindModel {

    private MenuMethodsResponse.DataDTO.LabelsDTO.Labels1DTO label;
    private MenuMethodsResponse.DataDTO.LabelsDTO menulabel;
    private UserMethodsResponse.DataDTO userMethods;

    public UserMethodsResponse.DataDTO getUserMethods() {
        return userMethods;
    }

    public void setUserMethods(UserMethodsResponse.DataDTO userMethods) {
        this.userMethods = userMethods;
    }

    public MenuMethodsResponse.DataDTO.LabelsDTO.Labels1DTO getLabel() {
        return label;
    }

    public void setLabel(MenuMethodsResponse.DataDTO.LabelsDTO.Labels1DTO label) {
        this.label = label;
    }

    public MenuMethodsResponse.DataDTO.LabelsDTO getMenulabel() {
        return menulabel;
    }

    public void setMenulabel(MenuMethodsResponse.DataDTO.LabelsDTO menulabel) {
        this.menulabel = menulabel;
    }

    public String labelName() {
        return label.getTitle() + "：";
    }
}
