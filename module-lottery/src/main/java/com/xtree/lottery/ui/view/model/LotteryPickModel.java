package com.xtree.lottery.ui.view.model;

import androidx.databinding.ObservableField;

import com.xtree.base.R;
import com.xtree.base.mvvm.recyclerview.BindModel;

import me.xtree.mvvmhabit.base.BaseApplication;

/**
 * Created by KAKA on 2024/4/22.
 * Describe:
 */
public class LotteryPickModel extends BindModel {

    public int number = 0;//排列角标号
    public String table = "";//彩票号
    public ObservableField<String> tag = new ObservableField<>("");
    public ObservableField<Integer> tagColor = new ObservableField<>(BaseApplication.getInstance().getResources().getColor(R.color.textColor));
    public ObservableField<Boolean> checked = new ObservableField<>(false);

    public LotteryPickModel(int number,String table) {
        this.number = number;
        this.table = table;
    }

}
