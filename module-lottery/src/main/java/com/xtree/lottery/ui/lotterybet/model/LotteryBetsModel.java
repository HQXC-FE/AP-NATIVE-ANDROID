package com.xtree.lottery.ui.lotterybet.model;

import com.xtree.lottery.data.source.response.HandicapResponse;
import com.xtree.lottery.data.source.response.MenuMethodsResponse;
import com.xtree.lottery.data.source.response.UserMethodsResponse;

/**
 * Created by KAKA on 2024/5/4.
 * Describe: 彩种投注数据
 */
public class LotteryBetsModel {

    public static final String LAYOUT_NO_SPLIT = "\\|";

    private String title = "";
    private int position = 0;
    private MenuMethodsResponse.DataDTO.LabelsDTO.Labels1DTO.Labels2DTO menuMethodLabelData;
    private MenuMethodsResponse.DataDTO.LabelsDTO menuMethodLabel;
    private UserMethodsResponse.DataDTO userMethodData;
    private HandicapResponse.DataDTO handicapMethodData;

    public LotteryBetsModel() {
    }

    public LotteryBetsModel(String title, MenuMethodsResponse.DataDTO.LabelsDTO label, MenuMethodsResponse.DataDTO.LabelsDTO.Labels1DTO.Labels2DTO menuMethodLabelData, UserMethodsResponse.DataDTO userMethods) {
        this.title = title;
        this.menuMethodLabel = label;
        this.menuMethodLabelData = menuMethodLabelData;
        this.userMethodData = userMethods;
    }

    public MenuMethodsResponse.DataDTO.LabelsDTO.Labels1DTO.Labels2DTO getMenuMethodLabelData() {
        return menuMethodLabelData;
    }

    public void setMenuMethodLabelData(MenuMethodsResponse.DataDTO.LabelsDTO.Labels1DTO.Labels2DTO menuMethodLabelData) {
        this.menuMethodLabelData = menuMethodLabelData;
    }

    public UserMethodsResponse.DataDTO getUserMethodData() {
        return userMethodData;
    }

    public void setUserMethodData(UserMethodsResponse.DataDTO userMethodData) {
        this.userMethodData = userMethodData;
    }

    public HandicapResponse.DataDTO getHandicapMethodData() {
        return handicapMethodData;
    }

    public void setHandicapMethodData(HandicapResponse.DataDTO handicapMethodData) {
        this.handicapMethodData = handicapMethodData;
    }

    public MenuMethodsResponse.DataDTO.LabelsDTO getMenuMethodLabel() {
        return menuMethodLabel;
    }

    public void setMenuMethodLabel(MenuMethodsResponse.DataDTO.LabelsDTO menuMethodLabel) {
        this.menuMethodLabel = menuMethodLabel;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
