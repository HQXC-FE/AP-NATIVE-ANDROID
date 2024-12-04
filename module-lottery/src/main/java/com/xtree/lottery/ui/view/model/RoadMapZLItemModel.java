package com.xtree.lottery.ui.view.model;

public class RoadMapZLItemModel {

    private String text = "";

    private int textBgDrawableId = 0;

    private boolean isBJL = false;

    public RoadMapZLItemModel() {
    }


    public RoadMapZLItemModel(String text, int textBgDrawableId) {
        this.text = text;
        this.textBgDrawableId = textBgDrawableId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getTextBgDrawableId() {
        return textBgDrawableId;
    }

    public void setTextBgDrawableId(int textBgDrawableId) {
        this.textBgDrawableId = textBgDrawableId;
    }

    public boolean isBJL() {
        return isBJL;
    }

    public void setBJL(boolean BJL) {
        isBJL = BJL;
    }
}
