package com.xtree.lottery.ui.view.viewmodel;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.drake.brv.BindingAdapter;
import com.xtree.base.mvvm.recyclerview.BaseDatabindingAdapter;
import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.lottery.R;
import com.xtree.lottery.ui.lotterybet.model.LotteryBetsModel;
import com.xtree.lottery.ui.view.model.BetRacingNumModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by KAKA on 2024/5/4.
 * Describe:
 */
public class BetLhcViewModel extends BindModel {

    public final ObservableField<List<List<String>>> lotteryNumbs = new ObservableField<>(Arrays.asList(Collections.emptyList(), Collections.emptyList()));
    public final ObservableField<ArrayList<Integer>> itemTypes = new ObservableField<>(new ArrayList<Integer>() {
        {
            add(R.layout.item_bet_lhc);
        }
    });
    public final ObservableField<List<BindModel>> datas = new ObservableField<>(new ArrayList<>());
    public final BaseDatabindingAdapter.onBindListener onBindListener = new BaseDatabindingAdapter.onBindListener() {
        @Override
        public void onBind(@NonNull BindingAdapter.BindingViewHolder bindingViewHolder, @NonNull View view, int itemViewType) {

        }

        @Override
        public void onItemClick(int modelPosition, int layoutPosition, int itemViewType) {
        }
    };
    private LotteryBetsModel betModel;

    public BetLhcViewModel() {
    }

    public void initData(LotteryBetsModel model) {
        this.betModel = model;
        String menuid = betModel.getMenuMethodLabelData().getMenuid();
        String methodid = betModel.getMenuMethodLabelData().getMethodid();
        boolean isCarMenu = BetRacingNumModel.TYPE_CAR_MENUIDS.contains(menuid);
        boolean isJssmMenu = BetRacingNumModel.TYPE_JSSM_MENUIDS.contains(menuid);
        boolean isXyftMenu = BetRacingNumModel.TYPE_XYFT_MENUIDS.contains(menuid);
        boolean isCarMETHODIDS = BetRacingNumModel.TYPE_CAR_METHODIDS.contains(methodid);
        boolean isJssmMETHODIDS = BetRacingNumModel.TYPE_JSSM_METHODIDS.contains(methodid);
        boolean isXyftMETHODIDS = BetRacingNumModel.TYPE_XYFT_METHODIDS.contains(methodid);


    }


    /**
     * 清空彩票输入框
     */
    public void clear() {
        lotteryNumbs.set(Arrays.asList(Collections.emptyList(), Collections.emptyList()));
        notifyChange();
    }

}
