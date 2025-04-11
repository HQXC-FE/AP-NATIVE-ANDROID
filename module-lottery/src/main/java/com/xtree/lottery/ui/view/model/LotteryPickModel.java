package com.xtree.lottery.ui.view.model;

import androidx.databinding.Observable;
import androidx.databinding.ObservableField;

import com.xtree.base.R;
import com.xtree.base.mvvm.recyclerview.BindModel;

import me.xtree.mvvmhabit.base.BaseApplication;

/**
 * Created by KAKA on 2024/4/22.
 * Describe:
 */
public class LotteryPickModel extends BindModel {

    public static int checkOrderTotal = 0;//选中的总顺序
    public int number = 0;//排列角标号
    public String table = "";//彩票号
    public ObservableField<String> tag = new ObservableField<>("");
    public ObservableField<Integer> tagColor = new ObservableField<>(BaseApplication.getInstance().getResources().getColor(R.color.textColor));
    public ObservableField<Boolean> checked = new ObservableField<>(false);
    public int checkOrderMine = 0;//我选中的顺序

    public LotteryPickModel(int number, String table) {
        this.number = number;
        this.table = table;
        checked.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                Boolean isChecked = checked.get(); // 获取当前值
                // 可以在这里根据变化做逻辑处理
                if (Boolean.TRUE.equals(isChecked)) {
                    // 做一些勾选时的逻辑
                    checkOrderTotal++;
                    checkOrderMine = checkOrderTotal;
                } else {
                    checkOrderMine = 0;
                }
            }
        });
    }

}
