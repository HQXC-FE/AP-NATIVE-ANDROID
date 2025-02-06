package com.xtree.lottery.ui.view.viewmodel;

import androidx.databinding.Observable;
import androidx.databinding.ObservableField;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.lottery.R;
import com.xtree.lottery.data.source.vo.MenuMethodsData;
import com.xtree.lottery.ui.lotterybet.model.LotteryBetsModel;
import com.xtree.lottery.ui.view.model.BetDxdsTagModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
    private final ArrayList<BindModel> bindModels = new ArrayList<>();
    public ObservableField<GridLayoutManager> layoutManager = new ObservableField<>();
    //选中号码
    public ObservableField<String> codesData = new ObservableField<>("");

    public void initData(LotteryBetsModel model) {

        bindModels.clear();

        for (MenuMethodsData.LabelsDTO.Labels1DTO.Labels2DTO.SelectareaDTO.LayoutDTO layoutDTO : model.getMenuMethodLabelData().getSelectarea().getLayout()) {
            //            [{code=5单0双, display=5单0双}, {code=4单1双, display=4单1双}, {code=3单2双, display=3单2双}, {code=2单3双, display=2单3双}, {code=1单4双, display=1单4双}, {code=0单5双, display=0单5双}]
            Gson gson = new Gson();
            List<Map<String, String>> split = gson.fromJson(layoutDTO.getNo(), new TypeToken<List<Map<String, String>>>() {
            }.getType());
            for (Map<String, String> item : split) {
                BetDxdsTagModel dxdsTagModel = new BetDxdsTagModel(item.get("display"), item.get("code"));
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
                codesBuildr.append(dxdsTagModel.getCode());
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
     * 投注号码拼接的解析
     */
    public ArrayList<String> reFormatCode(String codes) {
        ArrayList<String> codeList = new ArrayList<>();
        String[] parts = codes.split("&");
        codeList.addAll(Arrays.asList(parts));
        return codeList;
    }

    public void clearBet() {
        for (BindModel bindModel : bindModels) {
            BetDxdsTagModel betDxdsTagModel = (BetDxdsTagModel) bindModel;
            betDxdsTagModel.clicked.set(false);
        }
        codesData.set("");
    }
}
