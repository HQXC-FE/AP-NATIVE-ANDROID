package com.xtree.lottery.ui.view.model;

import androidx.databinding.ObservableField;

import com.xtree.base.mvvm.recyclerview.BindModel;

/**
 * Created by KAKA on 2024/11/30.
 * Describe:
 */
public class BetRacingBetModel extends BindModel {

    public ObservableField<BetRacingNumModel> number_v1 = new ObservableField<>();
    public ObservableField<BetRacingNumModel> number_v2 = new ObservableField<>();

    public BetRacingBetModel() {
    }

    public void setV1(BetRacingNumModel model) {
        number_v1.set(model);
    }

    public void setV2(BetRacingNumModel model) {
        number_v2.set(model);
    }

    public void clearV1() {
        number_v1.set(null);
    }

    public void clearV2() {
        number_v2.set(null);
    }
}