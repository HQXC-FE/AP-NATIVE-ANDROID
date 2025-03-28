package com.xtree.lottery.ui.view.viewmodel;

import static com.xtree.lottery.ui.lotterybet.model.LotteryBetsModel.LAYOUT_NO_SPLIT;

import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.drake.brv.BindingAdapter;
import com.xtree.base.mvvm.recyclerview.BaseDatabindingAdapter;
import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.lottery.R;
import com.xtree.lottery.data.source.vo.MenuMethodsData;
import com.xtree.lottery.ui.lotterybet.model.LotteryBetsModel;
import com.xtree.lottery.ui.view.LotteryPickView;
import com.xtree.lottery.ui.view.model.BetDxdsModel;
import com.xtree.lottery.ui.view.model.LotteryPickModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by KAKA on 2024/5/6.
 * Describe:
 */
public class BetDxdsViewModel {

    public final ObservableField<ArrayList<BindModel>> datas = new ObservableField<>(new ArrayList<>());

    public final ObservableField<ArrayList<Integer>> itemType = new ObservableField<>(
            new ArrayList<Integer>() {
                {
                    add(R.layout.item_bet_dxds);
                }
            });
    private final ArrayList<BindModel> bindModels = new ArrayList<>();
    //选中号码
    public ObservableField<String> codesData = new ObservableField<>("");
    public final BaseDatabindingAdapter.onBindListener onBindListener = new BaseDatabindingAdapter.onBindListener() {
        @Override
        public void onBind(@NonNull BindingAdapter.BindingViewHolder bindingViewHolder, @NonNull View view, int itemViewType) {

            if (itemViewType == R.layout.item_bet_dxds) {

                LotteryPickView pickView = view.findViewById(R.id.item_dxds_lotterypickview);
                //全
                view.findViewById(R.id.item_dxds_a).setOnClickListener(v -> pickView.pickAll());
                //清
                view.findViewById(R.id.item_dxds_c).setOnClickListener(v -> pickView.pickClear());

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

    public void initData(LotteryBetsModel model) {

        bindModels.clear();

        for (MenuMethodsData.LabelsDTO.Labels1DTO.Labels2DTO.SelectareaDTO.LayoutDTO layoutDTO : model.getMenuMethodLabelData().getSelectarea().getLayout()) {
            String[] split = layoutDTO.getNo().split(LAYOUT_NO_SPLIT);
            ArrayList<LotteryPickModel> picks = new ArrayList<>();

            for (int i = 0; i < split.length; i++) {
                picks.add(new LotteryPickModel(i+1, split[i]));
            }
            bindModels.add(new BetDxdsModel(layoutDTO.getTitle(), picks));
        }

        datas.set(bindModels);
    }

    /**
     * 格式化投注号码
     */
    private String formatCode() {
        StringBuilder codesBuildr = new StringBuilder();

        //判断是多个投注条目还是一个条目的拆分
        int titleCount = 0;
        for (BindModel bindModel : bindModels) {
            BetDxdsModel dxdsModel = (BetDxdsModel) bindModel;
            if (!TextUtils.isEmpty(dxdsModel.getTag())) {
                titleCount++;
            }
        }

        for (int i = 0; i < bindModels.size(); i++) {
            BetDxdsModel dxdsModel = (BetDxdsModel) bindModels.get(i);

            StringBuilder itemBuildr = new StringBuilder();
            for (LotteryPickModel pickData : dxdsModel.getPickDatas()) {
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

    /**
     * 投注号码拼接的解析
     */
    public ArrayList<String> reFormatCode2(String codes) {
        ArrayList<String> codeList = new ArrayList<>();
        String[] parts = codes.split("&");
        codeList.addAll(Arrays.asList(parts));
        return codeList;
    }

    public void clearBet() {
        for (BindModel bindModel : bindModels) {
            BetDxdsModel dxdsModel = (BetDxdsModel) bindModel;
            for (LotteryPickModel pickData : dxdsModel.getPickDatas()) {
                pickData.checked.set(false);
            }
        }
        codesData.set("");
    }

}
