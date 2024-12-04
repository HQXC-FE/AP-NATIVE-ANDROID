package com.xtree.lottery.ui.view.viewmodel;

import static com.xtree.lottery.ui.lotterybet.model.LotteryBetsModel.LAYOUT_NO_SPLIT;

import androidx.databinding.Observable;
import androidx.databinding.ObservableField;
import androidx.recyclerview.widget.GridLayoutManager;

import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.lottery.R;
import com.xtree.lottery.data.source.response.MenuMethodsResponse;
import com.xtree.lottery.ui.lotterybet.model.LotteryBetsModel;
import com.xtree.lottery.ui.view.model.BetDxdsTagModel;

import java.util.ArrayList;

/**
 * Created by KAKA on 2024/5/6.
 * Describe:
 */
public class BetDxdsTagViewModel {

    public final ObservableField<ArrayList<BindModel>> datas = new ObservableField<>(new ArrayList<>());

    public final ObservableField<ArrayList<Integer>> itemType = new ObservableField<>(
            new ArrayList<Integer>() {
                {
                    add(R.layout.item_bet_dxdstag);
                }
            });

    public ObservableField<GridLayoutManager> layoutManager = new ObservableField<>();

    private final ArrayList<BindModel> bindModels = new ArrayList<>();

    //选中号码
    public ObservableField<String> codesData = new ObservableField<>("");

    public void initData(LotteryBetsModel model) {

        bindModels.clear();

        for (MenuMethodsResponse.DataDTO.LabelsDTO.Labels1DTO.Labels2DTO.SelectareaDTO.LayoutDTO layoutDTO : model.getMenuMethodLabelData().getSelectarea().getLayout()) {
            String[] split = layoutDTO.getNo().split(LAYOUT_NO_SPLIT);
            for (int i = 0; i < split.length; i++) {
                BetDxdsTagModel dxdsTagModel = new BetDxdsTagModel(split[i]);
                dxdsTagModel.clicked.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
                    @Override
                    public void onPropertyChanged(Observable sender, int propertyId) {
                        codesData.set(formatCode());
                    }
                });
                bindModels.add(dxdsTagModel);
            }
        }

        datas.set(bindModels);
    }

    /**
     * 格式化投注号码
     */
    private String formatCode() {
        StringBuilder codesBuildr = new StringBuilder();
        for (int i = 0; i < bindModels.size(); i++) {
            BetDxdsTagModel dxdsTagModel = (BetDxdsTagModel) bindModels.get(i);
            if (Boolean.TRUE.equals(dxdsTagModel.clicked.get())) {
                codesBuildr.append(dxdsTagModel.getTag());
                codesBuildr.append("&");
            }
        }

        if (codesBuildr.length() > 1) {
            return codesBuildr.deleteCharAt(codesBuildr.length() - 1).toString();
        } else {
            return codesBuildr.toString();
        }
    }

    public void clearBet() {
        for (BindModel bindModel : bindModels) {
            BetDxdsTagModel betDxdsTagModel = (BetDxdsTagModel) bindModel;
            betDxdsTagModel.clicked.set(false);
        }
        codesData.set("");
    }
}
