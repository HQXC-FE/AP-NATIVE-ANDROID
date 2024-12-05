package com.xtree.lottery.ui.view.model;

import androidx.databinding.ObservableField;

import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.lottery.data.source.response.HandicapResponse;

/**
 * Created by KAKA on 2024/5/20.
 * Describe:
 */
public class BetHandicapModel extends BindModel {
    private HandicapResponse.DataDTO.GroupsDTO data;
    public ObservableField<Boolean> clicked = new ObservableField<>(false);

    public BetHandicapModel(HandicapResponse.DataDTO.GroupsDTO data) {
        this.data = data;
    }

    public HandicapResponse.DataDTO.GroupsDTO getData() {
        return data;
    }

    public void setData(HandicapResponse.DataDTO.GroupsDTO data) {
        this.data = data;
    }
}