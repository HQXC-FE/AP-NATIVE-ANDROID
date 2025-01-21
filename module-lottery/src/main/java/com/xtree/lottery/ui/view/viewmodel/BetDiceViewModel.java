package com.xtree.lottery.ui.view.viewmodel;

import static com.xtree.lottery.ui.lotterybet.model.LotteryBetsModel.LAYOUT_NO_SPLIT;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.drake.brv.BindingAdapter;
import com.xtree.base.mvvm.recyclerview.BaseDatabindingAdapter;
import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.lottery.R;
import com.xtree.lottery.data.source.vo.MenuMethodsData;
import com.xtree.lottery.databinding.ItemBetDiceNumsBinding;
import com.xtree.lottery.ui.lotterybet.model.LotteryBetsModel;
import com.xtree.lottery.ui.view.model.BetDiceModel;
import com.xtree.lottery.ui.view.model.BetRacingNumModel;
import com.xtree.lottery.utils.DiceCutter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by KAKA on 2024/5/4.
 * Describe:
 */
public class BetDiceViewModel extends BindModel {

    public final ObservableField<List<List<String>>> lotteryNumbs = new ObservableField<>(Arrays.asList(Collections.emptyList(), Collections.emptyList()));
    public final ObservableField<ArrayList<Integer>> itemTypes = new ObservableField<>(new ArrayList<Integer>() {
        {
            add(R.layout.item_bet_dice_nums);
        }
    });
    public final ObservableField<List<BindModel>> num0Datas = new ObservableField<>(new ArrayList<>());
    public final ObservableField<List<BindModel>> num1Datas = new ObservableField<>(new ArrayList<>());
    public final ObservableField<String> title0 = new ObservableField<>("");
    public final ObservableField<String> title1 = new ObservableField<>("");
    public Dice dice;
    public final BaseDatabindingAdapter.onBindListener onBindListener = new BaseDatabindingAdapter.onBindListener() {
        @Override
        public void onBind(@NonNull BindingAdapter.BindingViewHolder bindingViewHolder, @NonNull View view, int itemViewType) {
            ItemBetDiceNumsBinding binding = (ItemBetDiceNumsBinding) bindingViewHolder.getViewBinding();
            Bitmap diceSource = BitmapFactory.decodeResource(binding.getRoot().getResources(), R.mipmap.dice);
            BetDiceModel model = bindingViewHolder.getModel();
            boolean isActived = dice.isActived(model.number, lotteryNumbs);
            String actionNumber;
            if ("1".equals(model.number) || "2".equals(model.number) || "3".equals(model.number) || "4".equals(model.number) || "5".equals(model.number) || "6".equals(model.number)) {
                binding.iv1.setImageBitmap(DiceCutter.cutDiceImage(diceSource, Integer.parseInt(model.number), isActived ? 1 : 0));
                binding.iv1.setVisibility(View.VISIBLE);
                binding.iv2.setVisibility(View.GONE);
                binding.iv3.setVisibility(View.GONE);
                binding.tvTitle.setVisibility(View.GONE);
                actionNumber = model.number;
            } else if ("11*".equals(model.number) || "22*".equals(model.number) || "33*".equals(model.number) || "44*".equals(model.number) || "55*".equals(model.number) || "66*".equals(model.number)) {
                binding.iv1.setImageBitmap(DiceCutter.cutDiceImage(diceSource, 7, isActived ? 1 : 0));
                binding.iv2.setImageBitmap(DiceCutter.cutDiceImage(diceSource, Integer.parseInt(String.valueOf(model.number.charAt(0))), isActived ? 1 : 0));
                binding.iv3.setImageBitmap(DiceCutter.cutDiceImage(diceSource, Integer.parseInt(String.valueOf(model.number.charAt(0))), isActived ? 1 : 0));
                binding.tvTitle.setText(model.number);
                binding.iv1.setVisibility(View.VISIBLE);
                binding.iv2.setVisibility(View.VISIBLE);
                binding.iv3.setVisibility(View.VISIBLE);
                binding.tvTitle.setVisibility(View.VISIBLE);
                actionNumber = String.valueOf(model.number.charAt(0));
            } else if ("111".equals(model.number) || "222".equals(model.number) || "333".equals(model.number) || "444".equals(model.number) || "555".equals(model.number) || "666".equals(model.number)) {
                binding.iv1.setImageBitmap(DiceCutter.cutDiceImage(diceSource, Integer.parseInt(String.valueOf(model.number.charAt(0))), isActived ? 1 : 0));
                binding.iv2.setImageBitmap(DiceCutter.cutDiceImage(diceSource, Integer.parseInt(String.valueOf(model.number.charAt(0))), isActived ? 1 : 0));
                binding.iv3.setImageBitmap(DiceCutter.cutDiceImage(diceSource, Integer.parseInt(String.valueOf(model.number.charAt(0))), isActived ? 1 : 0));
                binding.tvTitle.setText(model.number);
                binding.iv1.setVisibility(View.VISIBLE);
                binding.iv2.setVisibility(View.VISIBLE);
                binding.iv3.setVisibility(View.VISIBLE);
                binding.tvTitle.setVisibility(View.VISIBLE);
                actionNumber = String.valueOf(model.number.charAt(0));
            } else if ("123".equals(model.number) || "234".equals(model.number) || "345".equals(model.number) || "456".equals(model.number)) {
                binding.iv1.setImageBitmap(DiceCutter.cutDiceImage(diceSource, Integer.parseInt(String.valueOf(model.number.charAt(0))), isActived ? 1 : 0));
                binding.iv2.setImageBitmap(DiceCutter.cutDiceImage(diceSource, Integer.parseInt(String.valueOf(model.number.charAt(1))), isActived ? 1 : 0));
                binding.iv3.setImageBitmap(DiceCutter.cutDiceImage(diceSource, Integer.parseInt(String.valueOf(model.number.charAt(2))), isActived ? 1 : 0));
                binding.tvTitle.setText(model.number);
                binding.iv1.setVisibility(View.VISIBLE);
                binding.iv2.setVisibility(View.VISIBLE);
                binding.iv3.setVisibility(View.VISIBLE);
                binding.tvTitle.setVisibility(View.VISIBLE);
                actionNumber = "6";
            } else if ("11".equals(model.number) || "22".equals(model.number) || "33".equals(model.number) || "44".equals(model.number) || "55".equals(model.number) || "66".equals(model.number)) {
                binding.iv2.setImageBitmap(DiceCutter.cutDiceImage(diceSource, Integer.parseInt(String.valueOf(model.number.charAt(0))), isActived ? 1 : 0));
                binding.iv3.setImageBitmap(DiceCutter.cutDiceImage(diceSource, Integer.parseInt(String.valueOf(model.number.charAt(0))), isActived ? 1 : 0));
                binding.iv1.setVisibility(View.GONE);
                binding.iv2.setVisibility(View.VISIBLE);
                binding.iv3.setVisibility(View.VISIBLE);
                binding.tvTitle.setVisibility(View.GONE);
                actionNumber = String.valueOf(model.number.charAt(0));
            } else {
                actionNumber = "";
            }
            binding.clRoot.setOnClickListener((v) -> {
                if (dice == Dice.Relation) {
                    dice.handleClick(actionNumber, model.index, lotteryNumbs);
                } else {
                    dice.handleClick(actionNumber, lotteryNumbs);
                }
                notifyChange();
            });
        }

        @Override
        public void onItemClick(int modelPosition, int layoutPosition, int itemViewType) {
        }
    };
    private LotteryBetsModel betModel;

    public BetDiceViewModel(Dice dice) {
        this.dice = dice;
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

        if (dice == Dice.All) {
            if (betModel.getMenuMethodLabelData().getDescription().contains("三同号")) {
                num0Datas.set(Arrays.asList(
                        new BetDiceModel("111"),
                        new BetDiceModel("222"),
                        new BetDiceModel("333"),
                        new BetDiceModel("444"),
                        new BetDiceModel("555"),
                        new BetDiceModel("666")));
            } else if (betModel.getMenuMethodLabelData().getDescription().contains("三连号")) {
                num0Datas.set(Arrays.asList(
                        new BetDiceModel("123"),
                        new BetDiceModel("234"),
                        new BetDiceModel("333"),
                        new BetDiceModel("345"),
                        new BetDiceModel("456")));
            }
            title0.set(betModel.getMenuMethodLabelData().getSelectarea().getLayout().get(0).getTitle());
        } else if (dice == Dice.Relation) {
            List<MenuMethodsData.LabelsDTO.Labels1DTO.Labels2DTO.SelectareaDTO.LayoutDTO> layoutDTOS = betModel.getMenuMethodLabelData().getSelectarea().getLayout();
            if (layoutDTOS == null || layoutDTOS.size() <= 1) {
                return;
            }
            List<BindModel> num0List = new ArrayList<>();
            List<BindModel> num1List = new ArrayList<>();
            for (String no : layoutDTOS.get(0).getNo().split(LAYOUT_NO_SPLIT)) {
                num0List.add(new BetDiceModel(no, 0));
            }
            for (String no : layoutDTOS.get(1).getNo().split(LAYOUT_NO_SPLIT)) {
                num1List.add(new BetDiceModel(no, 1));
            }
            num0Datas.set(num0List);
            num1Datas.set(num1List);
            title0.set(layoutDTOS.get(0).getTitle());
            title1.set(layoutDTOS.get(1).getTitle());
        } else {
            List<MenuMethodsData.LabelsDTO.Labels1DTO.Labels2DTO.SelectareaDTO.LayoutDTO> layoutDTOS = betModel.getMenuMethodLabelData().getSelectarea().getLayout();
            if (layoutDTOS == null || layoutDTOS.isEmpty()) {
                return;
            }
            List<BindModel> numList = new ArrayList<>();
            for (String no : layoutDTOS.get(0).getNo().split(LAYOUT_NO_SPLIT)) {
                numList.add(new BetDiceModel(no));
            }
            num0Datas.set(numList);
            title0.set(layoutDTOS.get(0).getTitle());
        }

    }


    /**
     * 清空彩票输入框
     */
    public void clear() {
        lotteryNumbs.set(Arrays.asList(Collections.emptyList(), Collections.emptyList()));
    }

    public enum Dice {
        All {
            @Override
            public boolean isActived(String no, ObservableField<List<List<String>>> lotteryNumb) {
                return !lotteryNumb.get().get(0).isEmpty();
            }

            @Override
            public void handleClick(String no, ObservableField<List<List<String>>> lotteryNumbs) {
                List<List<String>> currentLists = lotteryNumbs.get();
                List<String> betCode = lotteryNumbs.get().get(0);
                if (betCode.isEmpty()) {
                    // 如果betCode为空，设置为包含 "通选"
                    betCode = Collections.singletonList("通选");
                } else {
                    betCode = Collections.emptyList();
                }
                // 创建新的 List 并设置到 ObservableField 中
                List<List<String>> updatedLists = new ArrayList<>(currentLists);
                updatedLists.set(0, betCode);
                lotteryNumbs.set(updatedLists); // 使用 set() 更新值以通知观察者
            }
        }, Relation {
            @Override
            public boolean isActived(String no, ObservableField<List<List<String>>> lotteryNumbs) {
                switch (no) {
                    case "1": {
                        return lotteryNumbs.get().get(1).contains("1");
                    }
                    case "11": {
                        return lotteryNumbs.get().get(0).contains("1");
                    }
                    case "2": {
                        return lotteryNumbs.get().get(1).contains("2");
                    }
                    case "22": {
                        return lotteryNumbs.get().get(0).contains("2");
                    }
                    case "3": {
                        return lotteryNumbs.get().get(1).contains("3");
                    }
                    case "33": {
                        return lotteryNumbs.get().get(0).contains("3");
                    }
                    case "4": {
                        return lotteryNumbs.get().get(1).contains("4");
                    }
                    case "44": {
                        return lotteryNumbs.get().get(0).contains("4");
                    }
                    case "5": {
                        return lotteryNumbs.get().get(1).contains("5");
                    }
                    case "55": {
                        return lotteryNumbs.get().get(0).contains("5");
                    }
                    case "6": {
                        return lotteryNumbs.get().get(1).contains("6");
                    }
                    case "66": {
                        return lotteryNumbs.get().get(0).contains("6");
                    }
                    default:
                        return false;
                }
            }

            @Override
            public void handleClick(String no, int index, ObservableField<List<List<String>>> lotteryNumbs) {
                List<List<String>> currentLists = lotteryNumbs.get();
                //另一个组索引
                int otherIndex = Math.abs(index - 1);
                // 当前组别
                List<String> betCode = lotteryNumbs.get().get(index);
                //另一个组别
                List<String> betCodeOther = lotteryNumbs.get().get(otherIndex);
                // 检查当前组别是否包含目标数字
                if (betCode.contains(no)) {
                    // 如果包含该数字，从当前组别移除该数字
                    List<String> filteredCodes = new ArrayList<>();
                    for (String item : betCode) {
                        if (!item.equals(no)) {
                            filteredCodes.add(item);
                        }
                    }
                    betCode = filteredCodes; // 更新 betCode
                } else {
                    // 如果当前组别不包含目标数字，将其添加并排序
                    List<String> updatedCodes = new ArrayList<>(betCode);
                    updatedCodes.add(no);
                    Collections.sort(updatedCodes);
                    betCode = updatedCodes; // 更新 betCode

                    // 检查另一个组别是否包含目标数字
                    if (currentLists.size() > otherIndex) {
                        List<String> otherCodes = currentLists.get(otherIndex);
                        if (otherCodes.contains(no)) {
                            // 如果另一个组别包含目标数字，从另一个组别移除该数字
                            List<String> filteredOtherCodes = new ArrayList<>();
                            for (String item : otherCodes) {
                                if (!item.equals(no)) {
                                    filteredOtherCodes.add(item);
                                }
                            }
                            betCodeOther = filteredOtherCodes;// 更新另一个组别
                        }
                    }
                }
                // 创建新的 List 并设置到 ObservableField 中
                List<List<String>> updatedLists = new ArrayList<>(currentLists);
                updatedLists.set(index, betCode);
                updatedLists.set(otherIndex, betCodeOther);
                lotteryNumbs.set(updatedLists); // 使用 set() 更新值以通知观察者
            }
        }, Normal {
            @Override
            public boolean isActived(String no, ObservableField<List<List<String>>> lotteryNumbs) {
                switch (no) {
                    case "1":
                    case "111":
                    case "11*": {
                        return lotteryNumbs.get().get(0).contains("1");
                    }
                    case "2":
                    case "222":
                    case "22*": {
                        return lotteryNumbs.get().get(0).contains("2");
                    }
                    case "3":
                    case "333":
                    case "33*": {
                        return lotteryNumbs.get().get(0).contains("3");
                    }
                    case "4":
                    case "444":
                    case "44*": {
                        return lotteryNumbs.get().get(0).contains("4");
                    }
                    case "5":
                    case "555":
                    case "55*": {
                        return lotteryNumbs.get().get(0).contains("5");
                    }
                    case "6":
                    case "666":
                    case "66*": {
                        return lotteryNumbs.get().get(0).contains("6");
                    }
                    default:
                        return false;
                }
            }

            @Override
            public void handleClick(String no, ObservableField<List<List<String>>> lotteryNumbs) {
                List<List<String>> currentLists = lotteryNumbs.get();
                List<String> betCode = lotteryNumbs.get().get(0);
                // 如果包含该数字，移除该数字
                if (betCode.contains(no)) {
                    List<String> filteredCodes = new ArrayList<>();
                    for (String item : betCode) {
                        if (!item.equals(no)) {
                            filteredCodes.add(item);
                        }
                    }
                    betCode = filteredCodes; // 更新 betCode
                } else {
                    // 如果不包含该数字，添加后排序
                    List<String> updatedCodes = new ArrayList<>(betCode);
                    updatedCodes.add(no);
                    Collections.sort(updatedCodes);
                    betCode = updatedCodes; // 更新 betCode
                }
                // 创建新的 List 并设置到 ObservableField 中
                List<List<String>> updatedLists = new ArrayList<>(currentLists);
                updatedLists.set(0, betCode);
                lotteryNumbs.set(updatedLists); // 使用 set() 更新值以通知观察者
            }
        };

        // 抽象方法
        public boolean isActived(String no, ObservableField<List<List<String>>> lotteryNumb) {
            return false;
        }

        public void handleClick(String no, ObservableField<List<List<String>>> lotteryNumbs) {

        }

        public void handleClick(String no, int index, ObservableField<List<List<String>>> lotteryNumbs) {

        }
    }
}
