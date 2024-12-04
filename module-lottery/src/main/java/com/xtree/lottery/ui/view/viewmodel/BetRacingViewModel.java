package com.xtree.lottery.ui.view.viewmodel;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.drake.brv.BindingAdapter;
import com.lxj.xpopup.core.BasePopupView;
import com.xtree.base.mvvm.recyclerview.BaseDatabindingAdapter;
import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.lottery.R;
import com.xtree.lottery.ui.lotterybet.model.LotteryBetsModel;
import com.xtree.lottery.ui.view.model.BetRacingBetModel;
import com.xtree.lottery.ui.view.model.BetRacingBetTitleModel;
import com.xtree.lottery.ui.view.model.BetRacingNumModel;

import java.util.ArrayList;


/**
 * Created by KAKA on 2024/11/29.
 * Describe:
 */
public class BetRacingViewModel {
    public final ObservableField<String> lotteryNumbs = new ObservableField<>("");
    private BasePopupView pop;
    private LotteryBetsModel betModel;
    private final ArrayList<BindModel> carModels = new ArrayList<>(
            new ArrayList<BindModel>() {
                {
                    add(new BetRacingNumModel(BetRacingNumModel.NUM_1, BetRacingNumModel.TYPE_CAR));
                    add(new BetRacingNumModel(BetRacingNumModel.NUM_2, BetRacingNumModel.TYPE_CAR));
                    add(new BetRacingNumModel(BetRacingNumModel.NUM_3, BetRacingNumModel.TYPE_CAR));
                    add(new BetRacingNumModel(BetRacingNumModel.NUM_4, BetRacingNumModel.TYPE_CAR));
                    add(new BetRacingNumModel(BetRacingNumModel.NUM_5, BetRacingNumModel.TYPE_CAR));
                    add(new BetRacingNumModel(BetRacingNumModel.NUM_6, BetRacingNumModel.TYPE_CAR));
                    add(new BetRacingNumModel(BetRacingNumModel.NUM_7, BetRacingNumModel.TYPE_CAR));
                    add(new BetRacingNumModel(BetRacingNumModel.NUM_8, BetRacingNumModel.TYPE_CAR));
                    add(new BetRacingNumModel(BetRacingNumModel.NUM_9, BetRacingNumModel.TYPE_CAR));
                    add(new BetRacingNumModel(BetRacingNumModel.NUM_10, BetRacingNumModel.TYPE_CAR));
                }
            }
    );
    private final ArrayList<BindModel> jssmModels = new ArrayList<>(
            new ArrayList<BindModel>() {
                {
                    add(new BetRacingNumModel(BetRacingNumModel.NUM_1, BetRacingNumModel.TYPE_JSSM));
                    add(new BetRacingNumModel(BetRacingNumModel.NUM_2, BetRacingNumModel.TYPE_JSSM));
                    add(new BetRacingNumModel(BetRacingNumModel.NUM_3, BetRacingNumModel.TYPE_JSSM));
                    add(new BetRacingNumModel(BetRacingNumModel.NUM_4, BetRacingNumModel.TYPE_JSSM));
                    add(new BetRacingNumModel(BetRacingNumModel.NUM_5, BetRacingNumModel.TYPE_JSSM));
                    add(new BetRacingNumModel(BetRacingNumModel.NUM_6, BetRacingNumModel.TYPE_JSSM));
                    add(new BetRacingNumModel(BetRacingNumModel.NUM_7, BetRacingNumModel.TYPE_JSSM));
                    add(new BetRacingNumModel(BetRacingNumModel.NUM_8, BetRacingNumModel.TYPE_JSSM));
                    add(new BetRacingNumModel(BetRacingNumModel.NUM_9, BetRacingNumModel.TYPE_JSSM));
                    add(new BetRacingNumModel(BetRacingNumModel.NUM_10, BetRacingNumModel.TYPE_JSSM));
                }
            }
    );
    private final ArrayList<BindModel> xyftModels = new ArrayList<>(
            new ArrayList<BindModel>() {
                {
                    add(new BetRacingNumModel(BetRacingNumModel.NUM_1, BetRacingNumModel.TYPE_XYFT));
                    add(new BetRacingNumModel(BetRacingNumModel.NUM_2, BetRacingNumModel.TYPE_XYFT));
                    add(new BetRacingNumModel(BetRacingNumModel.NUM_3, BetRacingNumModel.TYPE_XYFT));
                    add(new BetRacingNumModel(BetRacingNumModel.NUM_4, BetRacingNumModel.TYPE_XYFT));
                    add(new BetRacingNumModel(BetRacingNumModel.NUM_5, BetRacingNumModel.TYPE_XYFT));
                    add(new BetRacingNumModel(BetRacingNumModel.NUM_6, BetRacingNumModel.TYPE_XYFT));
                    add(new BetRacingNumModel(BetRacingNumModel.NUM_7, BetRacingNumModel.TYPE_XYFT));
                    add(new BetRacingNumModel(BetRacingNumModel.NUM_8, BetRacingNumModel.TYPE_XYFT));
                    add(new BetRacingNumModel(BetRacingNumModel.NUM_9, BetRacingNumModel.TYPE_XYFT));
                    add(new BetRacingNumModel(BetRacingNumModel.NUM_10, BetRacingNumModel.TYPE_XYFT));
                }
            }
    );
    private final ArrayList<BindModel> numBindModels = new ArrayList<>();
    private final ArrayList<BindModel> betBindModels = new ArrayList<>(
            new ArrayList<BindModel>() {
                {
                    add(new BetRacingBetTitleModel());
                    add(new BetRacingBetModel());
                    add(new BetRacingBetModel());
                    add(new BetRacingBetModel());
                    add(new BetRacingBetModel());
                    add(new BetRacingBetModel());
                }
            }
    );
    public final ObservableField<ArrayList<BindModel>> numDatas = new ObservableField<>();
    public final ObservableField<ArrayList<BindModel>> betDatas = new ObservableField<>();
    public final ObservableField<ArrayList<Integer>> numItemType = new ObservableField<>(
            new ArrayList<Integer>() {
                {
                    add(R.layout.item_bet_racing_nums);
                }
            });

    public final ObservableField<ArrayList<Integer>> betItemType = new ObservableField<>(
            new ArrayList<Integer>() {
                {
                    add(R.layout.item_bet_racing_bets);
                    add(R.layout.item_bet_racing_bet_title);
                }
            });

    public final BaseDatabindingAdapter.onBindListener numOnBindListener = new BaseDatabindingAdapter.onBindListener() {
        @Override
        public void onBind(@NonNull BindingAdapter.BindingViewHolder bindingViewHolder, @NonNull View view, int itemViewType) {
        }

        @Override
        public void onItemClick(int modelPosition, int layoutPosition, int itemViewType) {
            BetRacingNumModel nData = (BetRacingNumModel) numBindModels.get(modelPosition);
            for (int i = 1; i < betBindModels.size(); i++) {
                BetRacingBetModel bData = (BetRacingBetModel) betBindModels.get(i);
                if (bData.number_v1.get() == null) {
                    bData.setV1(nData);
                    break;
                }
                if (bData.number_v2.get() == null) {
                    bData.setV2(nData);
                    break;
                }
            }
        }
    };

    public final BaseDatabindingAdapter.onBindListener betOnBindListener = new BaseDatabindingAdapter.onBindListener() {
        @Override
        public void onBind(@NonNull BindingAdapter.BindingViewHolder bindingViewHolder, @NonNull View view, int itemViewType) {

            if (itemViewType == R.layout.item_bet_racing_bet_title) {
                BetRacingBetTitleModel m = (BetRacingBetTitleModel) betBindModels.get(bindingViewHolder.getModelPosition());
                view.findViewById(R.id.item_racing_bet_title_bt3).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (int i = 1; i < betBindModels.size(); i++) {
                            BetRacingBetModel bData = (BetRacingBetModel) betBindModels.get(i);
                            bData.clearV1();
                            bData.clearV2();
                        }
                    }
                });
            }

            if (itemViewType == R.layout.item_bet_racing_bets) {
                BetRacingBetModel bData = (BetRacingBetModel) betBindModels.get(bindingViewHolder.getModelPosition());
                view.findViewById(R.id.item_racing_bet_bt1).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bData.clearV1();
                    }
                });
                view.findViewById(R.id.item_racing_bet_bt2).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bData.clearV2();
                    }
                });
                view.findViewById(R.id.item_racing_bet_bt3).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bData.clearV1();
                        bData.clearV2();
                    }
                });
            }

        }

        @Override
        public void onItemClick(int modelPosition, int layoutPosition, int itemViewType) {

        }
    };

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

        if (isCarMenu && isCarMETHODIDS) {
            numBindModels.addAll(carModels);
        } else if (isJssmMenu && isJssmMETHODIDS) {
            numBindModels.addAll(jssmModels);
        } else if (isXyftMenu && isXyftMETHODIDS) {
            numBindModels.addAll(xyftModels);
        } else {
            numBindModels.addAll(carModels);
        }

        numDatas.set(numBindModels);
        betDatas.set(betBindModels);
    }

    /**
     * 清空彩票输入框
     */
    public void clear() {
        lotteryNumbs.set("");
    }
}
