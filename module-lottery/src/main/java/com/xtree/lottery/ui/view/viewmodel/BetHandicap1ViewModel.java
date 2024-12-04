package com.xtree.lottery.ui.view.viewmodel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.recyclerview.widget.RecyclerView;

import com.drake.brv.BindingAdapter;
import com.xtree.base.mvvm.recyclerview.BaseDatabindingAdapter;
import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.lottery.R;
import com.xtree.lottery.data.source.request.LotteryBetRequest;
import com.xtree.lottery.data.source.response.HandicapResponse;
import com.xtree.lottery.ui.lotterybet.model.LotteryBetsModel;
import com.xtree.lottery.ui.view.model.BetHandicapModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KAKA on 2024/5/20.
 * Describe:
 */
public class BetHandicap1ViewModel {

    public final ObservableField<ArrayList<BindModel>> datas = new ObservableField<>(new ArrayList<>());

    public final ObservableField<ArrayList<Integer>> itemType = new ObservableField<>(
            new ArrayList<Integer>() {
                {
                    add(R.layout.item_bet_handicap1);
                    add(R.layout.item_bet_handicap2);
                }
            });

    public ObservableField<RecyclerView.LayoutManager> layoutManager = new ObservableField<>();

    private final ArrayList<BindModel> bindModels = new ArrayList<>();

    public final BaseDatabindingAdapter.onBindListener onBindListener = new BaseDatabindingAdapter.onBindListener() {
        @Override
        public void onBind(@NonNull BindingAdapter.BindingViewHolder bindingViewHolder, @NonNull View view, int itemViewType) {

            if (itemViewType == R.layout.item_bet_handicap1) {

                GridLayout gridLayout = view.findViewById(R.id.item_handicap1_gridlayout);
                TextView itemLabel = view.findViewById(R.id.item_handicap1_label);

                gridLayout.removeAllViews();

                BetHandicapModel m = (BetHandicapModel) bindModels.get(bindingViewHolder.getModelPosition());
                itemLabel.setText(m.getData().getName());


                for (int i = 0; i < m.getData().getCodes().size(); i++) {

                    HandicapResponse.DataDTO.GroupsDTO.CodesDTO code = m.getData().getCodes().get(i);

                    View labelView = LayoutInflater.from(view.getContext()).inflate(R.layout.label_bet_handicap1, null);
                    View labelLayout = labelView.findViewById(R.id.label_layout);
                    TextView name = labelView.findViewById(R.id.label_name);
                    TextView odd = labelView.findViewById(R.id.label_odd);

                    name.setText(code.getName());
                    odd.setText(code.getNonRebatePrize());

                    if (code.isChecked()) {
                        labelLayout.setBackgroundResource(R.drawable.lt_shape_solid_clrmain13_c8);
                        odd.setTextColor(view.getResources().getColor(R.color.clr_white));
                        if (code.getType().equals("digital")) {
                            name.setTextColor(view.getResources().getColor(R.color.lt_color_text6));
                            name.setBackgroundResource(R.mipmap.lt_icon_handicap_digital_checked);
                        } else {
                            name.setTextColor(view.getResources().getColor(R.color.clr_white));
                        }
                    } else {
                        labelLayout.setBackgroundResource(R.drawable.lt_shape_solid_white_stroke_clrmain23_05_c8);
                        name.setTextColor(view.getResources().getColor(R.color.lt_color_text6));
                        odd.setTextColor(view.getResources().getColor(R.color.lt_color_text6));
                        if (code.getType().equals("digital")) {
                            name.setBackgroundResource(R.mipmap.lt_icon_handicap_digital_unchecked);
                        } else {
                            name.setTextColor(view.getResources().getColor(R.color.lt_color_text6));
                        }
                    }

                    labelView.setOnClickListener(v -> {
                        code.setChecked(!code.isChecked());
                        codesData.set(formatCode());

                        if (code.isChecked()) {
                            labelLayout.setBackgroundResource(R.drawable.lt_shape_solid_clrmain13_c8);
                            odd.setTextColor(view.getResources().getColor(R.color.clr_white));
                            if (code.getType().equals("digital")) {
                                name.setTextColor(view.getResources().getColor(R.color.lt_color_text6));
                                name.setBackgroundResource(R.mipmap.lt_icon_handicap_digital_checked);
                            } else {
                                name.setTextColor(view.getResources().getColor(R.color.clr_white));
                            }
                        } else {
                            labelLayout.setBackgroundResource(R.drawable.lt_shape_solid_white_stroke_clrmain23_05_c8);
                            name.setTextColor(view.getResources().getColor(R.color.lt_color_text6));
                            odd.setTextColor(view.getResources().getColor(R.color.lt_color_text6));
                            if (code.getType().equals("digital")) {
                                name.setBackgroundResource(R.mipmap.lt_icon_handicap_digital_unchecked);
                            } else {
                                name.setTextColor(view.getResources().getColor(R.color.lt_color_text6));
                            }
                        }
                    });
                    GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
                    layoutParams.width = 0;
                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    layoutParams.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
                    if ((i + 1) % 4 == 0) {
                        layoutParams.setMargins(0, 16, 0, 0);
                    } else {
                        layoutParams.setMargins(0, 16, 10, 0);
                    }
                    labelView.setLayoutParams(layoutParams);
                    gridLayout.addView(labelView);
                }
            }
        }

        @Override
        public void onItemClick(int modelPosition, int layoutPosition, int itemViewType) {

            if (itemViewType == R.layout.item_bet_handicap2) {
                BetHandicapModel m = (BetHandicapModel) bindModels.get(modelPosition);
                m.getData().getCodes().get(0).setChecked(Boolean.FALSE.equals(m.clicked.get()));
                m.clicked.set(Boolean.FALSE.equals(m.clicked.get()));
                codesData.set(formatCode());
            }
        }
    };

    //选中号码
    public ObservableField<List<LotteryBetRequest.BetOrderData>> codesData = new ObservableField<>(new ArrayList<>());

    public void initData(LotteryBetsModel model) {

        bindModels.clear();

        String category = model.getHandicapMethodData().getCategory();
        for (HandicapResponse.DataDTO.GroupsDTO group : model.getHandicapMethodData().getGroups()) {

            for (HandicapResponse.DataDTO.GroupsDTO.CodesDTO code : group.getCodes()) {
                code.setChecked(false);
            }

            switch (category) {
                case "整合":
                case "炸金花":
                case "龙虎斗":
                    initHandicapModel1(group);
                    break;
                case "牛牛":
                case "梭哈":
                    initHandicapModel2(group);
                    break;
                case "百家乐":
                    initHandicapModel3(group);
                    break;
                default:
                    break;
            }

        }

        datas.set(bindModels);
    }

    private void initHandicapModel1(HandicapResponse.DataDTO.GroupsDTO group) {
        BetHandicapModel handicapModel = new BetHandicapModel(group);
        handicapModel.setItemType(0);
        bindModels.add(handicapModel);
    }

    private void initHandicapModel2(HandicapResponse.DataDTO.GroupsDTO group) {
        for (HandicapResponse.DataDTO.GroupsDTO.CodesDTO code : group.getCodes()) {
            HandicapResponse.DataDTO.GroupsDTO groupsDTO = new HandicapResponse.DataDTO.GroupsDTO();
            groupsDTO.setName(code.getName());
            ArrayList<HandicapResponse.DataDTO.GroupsDTO.CodesDTO> codesDTOS = new ArrayList<>();
            codesDTOS.add(code);
            groupsDTO.setCodes(codesDTOS);

            BetHandicapModel handicapModel = new BetHandicapModel(groupsDTO);
            handicapModel.setItemType(1);
            bindModels.add(handicapModel);
        }
    }

    private void initHandicapModel3(HandicapResponse.DataDTO.GroupsDTO group) {
        for (HandicapResponse.DataDTO.GroupsDTO.CodesDTO code : group.getCodes()) {
            HandicapResponse.DataDTO.GroupsDTO groupsDTO = new HandicapResponse.DataDTO.GroupsDTO();
            if (group.getCodes().size() > 1) {
                groupsDTO.setName(group.getName() + code.getName());
            } else {
                groupsDTO.setName(code.getName());
            }
            ArrayList<HandicapResponse.DataDTO.GroupsDTO.CodesDTO> codesDTOS = new ArrayList<>();
            codesDTOS.add(code);
            groupsDTO.setCodes(codesDTOS);
            BetHandicapModel handicapModel = new BetHandicapModel(groupsDTO);
            handicapModel.setItemType(1);
            bindModels.add(handicapModel);
        }
    }

    /**
     * 格式化投注号码
     */
    private List<LotteryBetRequest.BetOrderData> formatCode() {

        ArrayList<LotteryBetRequest.BetOrderData> betList = new ArrayList<>();

        for (BindModel bindModel : bindModels) {
            BetHandicapModel handicapModel = (BetHandicapModel) bindModel;

            for (HandicapResponse.DataDTO.GroupsDTO.CodesDTO code : handicapModel.getData().getCodes()) {

                if (code.isChecked()) {
                    LotteryBetRequest.BetOrderData betOrderData = new LotteryBetRequest.BetOrderData();
                    StringBuilder codeStr = new StringBuilder();
                    if (code.getType().equals("digital")) {
                        switch (handicapModel.getData().getName()) {
                            case "万位":
                                codeStr.append(code.getName())
                                        .append("|")
                                        .append("|")
                                        .append("|")
                                        .append("|");
                                break;
                            case "千位":
                                codeStr
                                        .append("|")
                                        .append(code.getName())
                                        .append("|")
                                        .append("|")
                                        .append("|");
                                break;
                            case "百位":
                                codeStr
                                        .append("|")
                                        .append("|")
                                        .append(code.getName())
                                        .append("|")
                                        .append("|");
                                break;
                            case "十位":
                                codeStr
                                        .append("|")
                                        .append("|")
                                        .append("|")
                                        .append(code.getName())
                                        .append("|");
                                break;
                            case "ge位":
                                codeStr
                                        .append("|")
                                        .append("|")
                                        .append("|")
                                        .append("|")
                                        .append(code.getName());
                                break;
                        }
                    } else {
                        codeStr.append(code.getName());
                    }
                    betOrderData.setCodes(codeStr.toString());
                    betOrderData.setDesc(handicapModel.getData().getName() + "@" + code.getName());
                    betOrderData.setMenuid(code.getMenuid());
                    betOrderData.setMethodid(code.getMethodid());
                    betOrderData.setType(code.getType());
                    betList.add(betOrderData);
                }
            }

        }

        return betList;
    }

    public void clearBet() {
        for (BindModel bindModel : bindModels) {
            BetHandicapModel handicapModel = (BetHandicapModel) bindModel;
            handicapModel.clicked.set(false);
            for (HandicapResponse.DataDTO.GroupsDTO.CodesDTO code : handicapModel.getData().getCodes()) {
                code.setChecked(false);
            }
        }
        codesData.set(null);
        datas.set(bindModels);
    }

}
