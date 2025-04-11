package com.xtree.lottery.ui.view.model;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.lottery.R;

import java.util.ArrayList;

import me.xtree.mvvmhabit.base.BaseApplication;

/**
 * Created by KAKA on 2024/11/30.
 * Describe:
 */
public class BetRacingNumModel extends BindModel {

    //赛车类型
    public static final String TYPE_CAR = "TYPE_CAR";
    public static final ArrayList<String> TYPE_CAR_MENUIDS = new ArrayList<String>(){{
        add("347872");
        add("313172");
    }};
    public static final ArrayList<String> TYPE_CAR_METHODIDS = new ArrayList<String>(){{
        add("6223467");
        add("3110667");
    }};
    //赛马类型
    public static final String TYPE_JSSM = "TYPE_JSSM";
    public static final ArrayList<String> TYPE_JSSM_MENUIDS = new ArrayList<String>(){{
        add("313272");
    }};
    public static final ArrayList<String> TYPE_JSSM_METHODIDS = new ArrayList<String>(){{
        add("3110767");
    }};
    //飞艇类型
    public static final String TYPE_XYFT = "TYPE_XYFT";
    public static final ArrayList<String> TYPE_XYFT_MENUIDS = new ArrayList<String>(){{
        add("399039");
    }};
    public static final ArrayList<String> TYPE_XYFT_METHODIDS = new ArrayList<String>(){{
        add("6363941");
    }};
    public static final String NUM_1 = "01";
    public static final String NUM_2 = "02";
    public static final String NUM_3 = "03";
    public static final String NUM_4 = "04";
    public static final String NUM_5 = "05";
    public static final String NUM_6 = "06";
    public static final String NUM_7 = "07";
    public static final String NUM_8 = "08";
    public static final String NUM_9 = "09";
    public static final String NUM_10 = "10";

    public String number = "?";
    public int icon = R.drawable.car01;
    public ObservableBoolean  enable = new ObservableBoolean(true);
    private int color = R.color.lt_color_racing_default;

    public ObservableField<String> tag = new ObservableField<>("");
    public ObservableField<Integer> tagColor = new ObservableField<>(BaseApplication.getInstance().getResources().getColor(com.xtree.base.R.color.textColor));
    public ObservableField<Boolean> checked = new ObservableField<>(false);

    public BetRacingNumModel(String number,String type) {
        this.number = number;

        switch (type) {
            case TYPE_CAR:
                switch (number) {
                    case NUM_1:
                        icon = R.drawable.car01;
                        break;
                    case NUM_2:
                        icon = R.drawable.car02;
                        break;
                    case NUM_3:
                        icon = R.drawable.car03;
                        break;
                    case NUM_4:
                        icon = R.drawable.car04;
                        break;
                    case NUM_5:
                        icon = R.drawable.car05;
                        break;
                    case NUM_6:
                        icon = R.drawable.car06;
                        break;
                    case NUM_7:
                        icon = R.drawable.car07;
                        break;
                    case NUM_8:
                        icon = R.drawable.car08;
                        break;
                    case NUM_9:
                        icon = R.drawable.car09;
                        break;
                    case NUM_10:
                        icon = R.drawable.car10;
                        break;
                }
                break;
            case TYPE_JSSM:
                switch (number) {
                    case NUM_1:
                        icon = R.drawable.jssm01;
                        break;
                    case NUM_2:
                        icon = R.drawable.jssm02;
                        break;
                    case NUM_3:
                        icon = R.drawable.jssm03;
                        break;
                    case NUM_4:
                        icon = R.drawable.jssm04;
                        break;
                    case NUM_5:
                        icon = R.drawable.jssm05;
                        break;
                    case NUM_6:
                        icon = R.drawable.jssm06;
                        break;
                    case NUM_7:
                        icon = R.drawable.jssm07;
                        break;
                    case NUM_8:
                        icon = R.drawable.jssm08;
                        break;
                    case NUM_9:
                        icon = R.drawable.jssm09;
                        break;
                    case NUM_10:
                        icon = R.drawable.jssm10;
                        break;
                }
                break;
            case TYPE_XYFT:
                switch (number) {
                    case NUM_1:
                        icon = R.drawable.xyft01;
                        break;
                    case NUM_2:
                        icon = R.drawable.xyft02;
                        break;
                    case NUM_3:
                        icon = R.drawable.xyft03;
                        break;
                    case NUM_4:
                        icon = R.drawable.xyft04;
                        break;
                    case NUM_5:
                        icon = R.drawable.xyft05;
                        break;
                    case NUM_6:
                        icon = R.drawable.xyft06;
                        break;
                    case NUM_7:
                        icon = R.drawable.xyft07;
                        break;
                    case NUM_8:
                        icon = R.drawable.xyft08;
                        break;
                    case NUM_9:
                        icon = R.drawable.xyft09;
                        break;
                    case NUM_10:
                        icon = R.drawable.xyft10;
                        break;
                }
                break;
        }

        switch (number) {
            case NUM_1:
                color = R.color.lt_color_racing01;
                break;
            case NUM_2:
                color = R.color.lt_color_racing02;
                break;
            case NUM_3:
                color = R.color.lt_color_racing03;
                break;
            case NUM_4:
                color = R.color.lt_color_racing04;
                break;
            case NUM_5:
                color = R.color.lt_color_racing05;
                break;
            case NUM_6:
                color = R.color.lt_color_racing06;
                break;
            case NUM_7:
                color = R.color.lt_color_racing07;
                break;
            case NUM_8:
                color = R.color.lt_color_racing08;
                break;
            case NUM_9:
                color = R.color.lt_color_racing09;
                break;
            case NUM_10:
                color = R.color.lt_color_racing10;
                break;
        }
    }

    public int getColor() {
        return BaseApplication.getInstance().getResources().getColor(color);
    }
}
