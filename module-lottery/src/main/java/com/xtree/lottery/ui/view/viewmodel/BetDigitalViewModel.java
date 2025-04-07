package com.xtree.lottery.ui.view.viewmodel;

import static com.xtree.lottery.ui.lotterybet.model.LotteryBetsModel.LAYOUT_NO_SPLIT;

import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;

import com.drake.brv.BindingAdapter;
import com.xtree.base.mvvm.recyclerview.BaseDatabindingAdapter;
import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.lottery.R;
import com.xtree.lottery.data.source.vo.MenuMethodsData;
import com.xtree.lottery.ui.lotterybet.model.LotteryBetsModel;
import com.xtree.lottery.ui.view.LotteryPickView;
import com.xtree.lottery.ui.view.model.BetDigitalModel;
import com.xtree.lottery.ui.view.model.LotteryPickModel;
import com.xtree.lottery.utils.LotteryAnalyzer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import me.xtree.mvvmhabit.base.BaseApplication;

/**
 * Created by KAKA on 2024/5/4.
 * Describe:
 */
public class BetDigitalViewModel {

    public final ObservableField<ArrayList<BindModel>> datas = new ObservableField<>(new ArrayList<>());
    public final ObservableField<ArrayList<Integer>> itemType = new ObservableField<>(
            new ArrayList<Integer>() {
                {
                    add(R.layout.item_bet_digital);
                }
            });
    private final ArrayList<BindModel> bindModels = new ArrayList<>();
    //热值标签颜色
    private final int hotMaxTagColor = BaseApplication.getInstance().getResources().getColor(R.color.lt_color_text9);
    //冷值标签颜色
    private final int hotMinTagColor = BaseApplication.getInstance().getResources().getColor(R.color.lt_color_text12);
    //最大遗漏值标签颜色
    private final int missMaxTagColor = BaseApplication.getInstance().getResources().getColor(R.color.lt_color_text9);
    //普通标签颜色
    private final int normalTagColor = BaseApplication.getInstance().getResources().getColor(R.color.lt_color_text3);
    //遗漏/冷热开关
    public ObservableField<Boolean> buttonStatus = new ObservableField<>(false);
    //0:不显示选项 1:显示遗漏选项 2:显示冷热选项
    public ObservableField<Integer> screenStatus = new ObservableField<>(0);
    //是否显示位数按钮
    public ObservableField<Boolean> showSeatView = new ObservableField<>(false);
    public MutableLiveData<List<String>> lotteryHistoryLiveData = new MutableLiveData<>();
    //选中号码
    public ObservableField<String> codesData = new ObservableField<>("");
    public final BaseDatabindingAdapter.onBindListener onBindListener = new BaseDatabindingAdapter.onBindListener() {
        @Override
        public void onBind(@NonNull BindingAdapter.BindingViewHolder bindingViewHolder, @NonNull View view, int itemViewType) {

            if (itemViewType == R.layout.item_bet_digital) {

                LotteryPickView pickView = view.findViewById(R.id.item_digital_lotterypickview);
                //全
                view.findViewById(R.id.item_digital_a).setOnClickListener(v -> pickView.pickAll());
                //大
                view.findViewById(R.id.item_digital_l).setOnClickListener(v -> pickView.pickLarge());
                //小
                view.findViewById(R.id.item_digital_s).setOnClickListener(v -> pickView.pickSmall());
                //奇
                view.findViewById(R.id.item_digital_o).setOnClickListener(v -> pickView.pickOdd());
                //偶
                view.findViewById(R.id.item_digital_e).setOnClickListener(v -> pickView.pickEven());
                //清
                view.findViewById(R.id.item_digital_c).setOnClickListener(v -> pickView.pickClear());

                pickView.setPickListener(new LotteryPickView.onPickListener() {
                    @Override
                    public void onPick(List<String> pickNums) {
                        codesData.set(formatCode());
                    }
                });
            }
        }

        @Override
        public void onItemClick(int modelPosition, int layoutPosition, int itemViewType) {

        }
    };
    //
    private String showStr;

    @androidx.databinding.BindingAdapter("data")
    public static void initLotteryPickView(LotteryPickView view, List<LotteryPickModel> picks) {
        if (picks != null) {
            view.setData(picks);
        }
    }

    public void initData(LotteryBetsModel model) {

        bindModels.clear();
        showStr = model.getMenuMethodLabelData().getShowStr();
        boolean isButton = model.getMenuMethodLabelData().getSelectarea().isIsButton();
        for (MenuMethodsData.LabelsDTO.Labels1DTO.Labels2DTO.SelectareaDTO.LayoutDTO layoutDTO : model.getMenuMethodLabelData().getSelectarea().getLayout()) {
            String[] split = layoutDTO.getNo().split(LAYOUT_NO_SPLIT);
            ArrayList<LotteryPickModel> picks = new ArrayList<>();

            for (int i = 0; i < split.length; i++) {
                // 若占位是$的话隐藏
//                if (method.show_str.split(',')[index] === '$') {
//                    return (<></>)
//                }
                if (!TextUtils.isEmpty(showStr) && showStr.split(",").length > i && "$".equals(showStr.split(",")[i])) {
                    continue;
                }
                picks.add(new LotteryPickModel(i + 1, split[i]));
            }
            bindModels.add(new BetDigitalModel(layoutDTO.getTitle(), picks, isButton));
        }

        datas.set(bindModels);
    }

    /**
     * 格式化投注号码
     */
    public String formatCode() {
        StringBuilder codesBuildr = new StringBuilder();

        //判断是多个投注条目还是一个条目的拆分
        int titleCount = 0;
        for (BindModel bindModel : bindModels) {
            BetDigitalModel digitalModel = (BetDigitalModel) bindModel;
            if (!TextUtils.isEmpty(digitalModel.getTag())) {
                titleCount++;
            }
        }

        for (int i = 0; i < bindModels.size(); i++) {
            BetDigitalModel digitalModel = (BetDigitalModel) bindModels.get(i);

            StringBuilder itemBuildr = new StringBuilder();
            for (LotteryPickModel pickData : digitalModel.getPickDatas()) {
                if (Boolean.TRUE.equals(pickData.checked.get())) {
                    itemBuildr.append(pickData.table).append("&");
                }
            }
            if (itemBuildr.length() > 0) {
                itemBuildr.deleteCharAt(itemBuildr.length() - 1);
            }

            if (TextUtils.isEmpty(itemBuildr)) {
                codesBuildr.append(" ");
            } else {
                codesBuildr.append(itemBuildr);
            }

            if (titleCount > 1) {
                codesBuildr.append("|");
            } else {
                codesBuildr.append("&");
            }
        }
        if (codesBuildr.length() > 1) {
            return codesBuildr.deleteCharAt(codesBuildr.length() - 1).toString().trim();
        } else {
            return codesBuildr.toString().trim();
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

    /**
     * 投注号码拼接的解析
     */
    public ArrayList<String> reFormatCode2(String codes) {
        ArrayList<String> codeList = new ArrayList<>();
        String[] parts = codes.split("&");
        codeList.addAll(Arrays.asList(parts));
        return codeList;
    }

    /**
     * 显示遗漏数据
     *
     * @param maxMiss 最大遗漏
     */
    public synchronized void showMiss(boolean maxMiss) {
        List<String> historys = lotteryHistoryLiveData.getValue();
        if (historys != null && !bindModels.isEmpty()) {

            for (int i = 0; i < bindModels.size(); i++) {
                BetDigitalModel digitalModel = (BetDigitalModel) bindModels.get(i);
                ArrayList<String> validNumbers = new ArrayList<>();
                //获取有效号码
                for (LotteryPickModel pickData : digitalModel.getPickDatas()) {
                    validNumbers.add(String.valueOf(pickData.table));
                }
                ArrayList<String> splitNumbs = new ArrayList<>();
                for (String history : historys) {
                    splitNumbs.add(history.split(LotteryAnalyzer.SPLIT)[getIndexFromCodes(i)]);
                }
                Map<String, Integer> missingValue;
                //获取遗漏值
                if (maxMiss) {
                    missingValue = LotteryAnalyzer.calculateMaxMissingValue(splitNumbs, validNumbers);
                } else {
                    missingValue = LotteryAnalyzer.calculateMissedCounts(splitNumbs, validNumbers);
                }

                // 找到最大值
                int maxValue = LotteryAnalyzer.getMaxValue(missingValue);

                // 找到最小值
                int minValue = LotteryAnalyzer.getMinValue(missingValue);

                //设置遗漏数据
                for (LotteryPickModel pickData : digitalModel.getPickDatas()) {
                    Integer missValue = missingValue.get(String.valueOf(pickData.table));
                    if (missValue != null) {
                        pickData.tag.set(String.valueOf(missValue));
                        if (missValue.equals(maxValue)) {
                            pickData.tagColor.set(missMaxTagColor);
                        } else {
                            pickData.tagColor.set(normalTagColor);
                        }
                    }
                }
            }
        }
    }

    /**
     * 显示冷热数据
     *
     * @param checkCount 检查期数
     */
    public synchronized void showHot(int checkCount) {
        if (lotteryHistoryLiveData.getValue() != null && !bindModels.isEmpty()) {
            List<String> historys = lotteryHistoryLiveData.getValue();
            if (historys != null && !bindModels.isEmpty()) {
                if (historys.size() > checkCount) {
                    historys = historys.subList(0, checkCount);
                }
                for (int i = 0; i < bindModels.size(); i++) {
                    BetDigitalModel digitalModel = (BetDigitalModel) bindModels.get(i);
                    ArrayList<String> validNumbers = new ArrayList<>();
                    //获取有效号码
                    for (LotteryPickModel pickData : digitalModel.getPickDatas()) {
                        validNumbers.add(String.valueOf(pickData.table));
                    }
                    //分割位数
                    String tag = digitalModel.getTag();
                    ArrayList<String> splitNumbs = new ArrayList<>();
                    for (String history : historys) {
                        splitNumbs.add(history.split(LotteryAnalyzer.SPLIT)[getIndexFromCodes(i)]);
                    }
                    //获取冷热值
                    Map<String, Integer> hotValues = LotteryAnalyzer
                            .calculateHotValues(splitNumbs, validNumbers);

                    // 找到最大值
                    int maxValue = LotteryAnalyzer.getMaxValue(hotValues);

                    // 找到最小值
                    int minValue = LotteryAnalyzer.getMinValue(hotValues);

                    //设置冷热数据
                    for (LotteryPickModel pickData : digitalModel.getPickDatas()) {
                        Integer hotValue = hotValues.get(String.valueOf(pickData.table));
                        if (hotValue != null) {
                            pickData.tag.set(String.valueOf(hotValue));
                            if (hotValue.equals(maxValue)) {
                                pickData.tagColor.set(hotMaxTagColor);
                            } else if (hotValue.equals(minValue)) {
                                pickData.tagColor.set(hotMinTagColor);
                            } else {
                                pickData.tagColor.set(normalTagColor);
                            }
                        }
                    }
                }
            }
        }
    }

    public Integer getIndexFromCodes(int index) {
        String[] parts = showStr.split(",");
        List<Integer> xIndexes = new ArrayList<>();
        for (int i = 0; i < parts.length; i++) {
            if ("X".equals(parts[i])) {
                xIndexes.add(i);
            }
        }
        if (index < xIndexes.size()) {
            return xIndexes.get(index);
        }
        return null;
    }

    /**
     * 关闭遗漏和冷热标签
     */
    public synchronized void closeMissHot() {
        for (BindModel bindModel : bindModels) {
            BetDigitalModel digitalModel = (BetDigitalModel) bindModel;
            //清空tag
            for (LotteryPickModel pickData : digitalModel.getPickDatas()) {
                pickData.tag.set("");
                pickData.tagColor.set(normalTagColor);
            }
        }
    }

    public void clearBet() {
        for (BindModel bindModel : bindModels) {
            BetDigitalModel digitalModel = (BetDigitalModel) bindModel;
            for (LotteryPickModel pickData : digitalModel.getPickDatas()) {
                pickData.checked.set(false);
            }
        }
        codesData.set("");
    }
}
