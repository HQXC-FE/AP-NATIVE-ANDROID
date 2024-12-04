package com.xtree.lottery.ui.view.model;

public class RoadMapDLItemModel {

    private int showColorType; // 蓝1  红2 绿3  灰4  透明5

    public RoadMapDLItemModel(int showColorType) {
        this.showColorType = showColorType;
    }

    public int getShowColorType() {
        return showColorType;
    }

    public void setShowColorType(int showColorType) {
        this.showColorType = showColorType;
    }
}
