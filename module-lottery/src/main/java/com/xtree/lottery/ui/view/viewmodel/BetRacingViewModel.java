package com.xtree.lottery.ui.view.viewmodel;

import android.text.TextUtils;
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
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * Created by KAKA on 2024/11/29.
 * Describe:
 */
public class BetRacingViewModel {
    public final ObservableField<String> lotteryNumbs = new ObservableField<>("");
    public final ObservableField<ArrayList<BindModel>> numDatas = new ObservableField<>();
    public final ObservableField<ArrayList<BindModel>> betDatas = new ObservableField<>();
    public final ObservableField<ArrayList<Integer>> numItemType = new ObservableField<>(new ArrayList<Integer>() {
        {
            add(R.layout.item_bet_racing_nums);
        }
    });
    public final ObservableField<ArrayList<Integer>> betItemType = new ObservableField<>(new ArrayList<Integer>() {
        {
            add(R.layout.item_bet_racing_bets);
            add(R.layout.item_bet_racing_bet_title);
        }
    });
    private final ArrayList<BindModel> carModels = new ArrayList<>(new ArrayList<BindModel>() {
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
    });
    private final ArrayList<BindModel> jssmModels = new ArrayList<>(new ArrayList<BindModel>() {
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
    });
    private final ArrayList<BindModel> xyftModels = new ArrayList<>(new ArrayList<BindModel>() {
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
    });
    private final ArrayList<BindModel> numBindModels = new ArrayList<>();
    private final ArrayList<BindModel> betBindModels = new ArrayList<>(new ArrayList<BindModel>() {
        {
            add(new BetRacingBetTitleModel());
            add(new BetRacingBetModel());
            add(new BetRacingBetModel());
            add(new BetRacingBetModel());
            add(new BetRacingBetModel());
            add(new BetRacingBetModel());
        }
    });
    public final BaseDatabindingAdapter.onBindListener numOnBindListener = new BaseDatabindingAdapter.onBindListener() {
        @Override
        public void onBind(@NonNull BindingAdapter.BindingViewHolder bindingViewHolder, @NonNull View view, int itemViewType) {
        }

        @Override
        public void onItemClick(int modelPosition, int layoutPosition, int itemViewType) {
            BetRacingNumModel nData = (BetRacingNumModel) numBindModels.get(modelPosition);
            if (!nData.enable.get()) {
                return;
            }
            for (int i = 1; i < betBindModels.size(); i++) {
                BetRacingBetModel bData = (BetRacingBetModel) betBindModels.get(i);
                if (bData.number_v1.get() == null) {
                    bData.setV1(nData);
                    //设置某个号码不可选
                    changeNumsChooseEnable(nData.number);
                    break;
                }
                if (bData.number_v2.get() == null) {
                    bData.setV2(nData);
                    //恢复所有号码可选
                    changeNumsChooseEnable("?");
                    break;
                }
            }
            lotteryNumbs.set(formatCode());
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
                            lotteryNumbs.set(formatCode());
                            //恢复所有号码可选
                            changeNumsChooseEnable("?");
                        }
                    }
                });
            }

            if (itemViewType == R.layout.item_bet_racing_bets) {
                BetRacingBetModel bData = (BetRacingBetModel) betBindModels.get(bindingViewHolder.getModelPosition());
//                view.findViewById(R.id.item_racing_bet_bt1).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        bData.clearV1();
//                        lotteryNumbs.set(formatCode());
//                    }
//                });
//                view.findViewById(R.id.item_racing_bet_bt2).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        bData.clearV2();
//                        lotteryNumbs.set(formatCode());
//                    }
//                });
                view.findViewById(R.id.item_racing_bet_bt3).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bData.clearV1();
                        bData.clearV2();
                        lotteryNumbs.set(formatCode());
                        //平移后面代码
                        shiftNumsForward(bindingViewHolder.getModelPosition()-1);//去掉标题的index
                        //恢复所有号码可选
                        changeNumsChooseEnable("?");
                    }
                });
            }

        }

        @Override
        public void onItemClick(int modelPosition, int layoutPosition, int itemViewType) {

        }
    };
    private BasePopupView pop;
    private LotteryBetsModel betModel;

    /**
     * 往前平移选号
     *
     * @param index
     */
    private void shiftNumsForward(int index) {
        List<BetRacingBetModel> betChooseBindModels = betBindModels.stream()
                .filter(bindModel -> bindModel instanceof BetRacingBetModel)
                .map(bindModel -> (BetRacingBetModel) bindModel)
                .filter(bData -> bData.number_v1.get() != null)
                .collect(Collectors.toList());

        List<BetRacingBetModel> betNumBindModels = betBindModels.stream()
                .filter(bindModel -> bindModel instanceof BetRacingBetModel)
                .map(bindModel -> (BetRacingBetModel) bindModel)
                .collect(Collectors.toList());

        int size = betNumBindModels.size();
        int chooseSize = betChooseBindModels.size();
        if (index < 0 || index >= size) return;

        for (int i = index; i < size; i++) {
            BetRacingBetModel bData = betNumBindModels.get(i);
            if (i < chooseSize) {
                BetRacingBetModel bChooseData = betChooseBindModels.get(i);
                bData.setV1(bChooseData.number_v1.get());
                bData.setV2(bChooseData.number_v2.get());
            } else {//其余的清空
                bData.clearV1();
                bData.clearV2();
            }
        }
    }


    /**
     * 格式化投注号码
     */
    public String formatCode() {
        StringBuilder codesBuildr = new StringBuilder();

        for (int i = 0; i < betBindModels.size(); i++) {

            //排除title
            if (!(betBindModels.get(i) instanceof BetRacingBetModel)) {
                continue;
            }

            //获取投注号
            BetRacingBetModel bData = (BetRacingBetModel) betBindModels.get(i);

            StringBuilder itemBuildrV1 = new StringBuilder();
            StringBuilder itemBuildrV2 = new StringBuilder();
            if (bData.number_v1.get() != null) {
                itemBuildrV1.append(bData.number_v1.get().number);
            }
            if (bData.number_v2.get() != null) {
                itemBuildrV2.append(bData.number_v2.get().number);
            }

            if (!TextUtils.isEmpty(itemBuildrV1)) {
                codesBuildr.append(itemBuildrV1);
            } else {
                codesBuildr.append(" ");
            }
            codesBuildr.append(" ");
            if (!TextUtils.isEmpty(itemBuildrV2)) {
                codesBuildr.append(itemBuildrV2);
            } else {
                codesBuildr.append(" ");
            }
            codesBuildr.append(",");
        }
        if (codesBuildr.length() > 1) {
            return codesBuildr.deleteCharAt(codesBuildr.length() - 1).toString();
        } else {
            return codesBuildr.toString();
        }
    }

    /**
     * 一组投注号码的解析
     */
    public ArrayList<List<String>> reFormatCode(String codes) {
        ArrayList<List<String>> codeList = new ArrayList<>();
        String[] parts = codes.split("\\|");
        for (String s : parts) {
            ArrayList<String> cs = new ArrayList<>();
            if (TextUtils.isEmpty(s.replace("|", "").trim())) {
                codeList.add(cs);
            } else {
                String[] c = s.split("&");
                for (String s1 : c) {
                    cs.add(s1);
                }
                codeList.add(cs);
            }
        }
        return codeList;
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

    /**
     * 号码可选或不可选
     */
    private void changeNumsChooseEnable(String flagNumber) {
        //恢复所有号码可选
        for (BindModel bindModel : numDatas.get()) {
            BetRacingNumModel betRacingNumModel = (BetRacingNumModel) bindModel;
            if (Objects.equals(betRacingNumModel.number, flagNumber)) {
                betRacingNumModel.enable.set(false);
            } else {
                betRacingNumModel.enable.set(true);
            }

        }
    }
}
