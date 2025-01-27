package com.xtree.lottery.ui.view.viewmodel;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.drake.brv.BindingAdapter;
import com.xtree.base.mvvm.recyclerview.BaseDatabindingAdapter;
import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.base.vo.UserMethodsResponse;
import com.xtree.lottery.R;
import com.xtree.lottery.data.source.vo.MenuMethodsData;
import com.xtree.lottery.databinding.ItemBetLhcBinding;
import com.xtree.lottery.ui.lotterybet.model.LotteryBetsModel;
import com.xtree.lottery.ui.view.model.BetLhcModel;
import com.xtree.lottery.ui.view.model.BetRacingNumModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by KAKA on 2024/5/4.
 * Describe:
 */
public class BetLhcViewModel extends BindModel {

    public final ObservableField<List<Map<String, String>>> lotteryNumbs = new ObservableField<>(Collections.emptyList());
    public final ObservableField<ArrayList<Integer>> itemTypes = new ObservableField<>(new ArrayList<Integer>() {
        {
            add(R.layout.item_bet_lhc);
        }
    });
    public final ObservableField<List<BindModel>> datas = new ObservableField<>(new ArrayList<>());
    public final BaseDatabindingAdapter.onBindListener onBindListener = new BaseDatabindingAdapter.onBindListener() {
        @Override
        public void onBind(@NonNull BindingAdapter.BindingViewHolder bindingViewHolder, @NonNull View view, int itemViewType) {

            ItemBetLhcBinding binding = (ItemBetLhcBinding) bindingViewHolder.getViewBinding();
            BetLhcModel model = bindingViewHolder.getModel();
            binding.tvNum.setText(model.number);
            binding.tvNum.setBackgroundResource(model.ball.getResourceId());
            binding.tvOdds.setText(model.odds);
            binding.etMoney.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    handleInput(binding.etMoney.getText().toString(), model.methodid);
                }
            });
        }

        @Override
        public void onItemClick(int modelPosition, int layoutPosition, int itemViewType) {
        }
    };
    private LotteryBetsModel betModel;

    public BetLhcViewModel() {
    }

    public void initData(LotteryBetsModel model, UserMethodsResponse.DataDTO.PrizeGroupDTO prizeGroup) {
        this.betModel = model;
        String menuid = betModel.getMenuMethodLabelData().getMenuid();
        String methodid = betModel.getMenuMethodLabelData().getMethodid();
        boolean isCarMenu = BetRacingNumModel.TYPE_CAR_MENUIDS.contains(menuid);
        boolean isJssmMenu = BetRacingNumModel.TYPE_JSSM_MENUIDS.contains(menuid);
        boolean isXyftMenu = BetRacingNumModel.TYPE_XYFT_MENUIDS.contains(menuid);
        boolean isCarMETHODIDS = BetRacingNumModel.TYPE_CAR_METHODIDS.contains(methodid);
        boolean isJssmMETHODIDS = BetRacingNumModel.TYPE_JSSM_METHODIDS.contains(methodid);
        boolean isXyftMETHODIDS = BetRacingNumModel.TYPE_XYFT_METHODIDS.contains(methodid);
        List<MenuMethodsData.LabelsDTO.Labels1DTO> labels1DTOS = betModel.getMenuMethodLabel().getLabels();
        String regex = "[\\u4e00-\\u9fa5]+\\s\\S+-\\S+";
        String label = prizeGroup.getLabel();
        String odds = "0.00";
        if (label != null && label.matches(regex)) {
            odds = label.split(" ")[1].split("-")[0];
        }
        if (labels1DTOS == null || labels1DTOS.isEmpty() || labels1DTOS.get(0).getLabels() == null || labels1DTOS.get(0).getLabels().isEmpty()) {
            return;
        }
        List<BindModel> dataList = new ArrayList<>();

        for (MenuMethodsData.LabelsDTO.Labels1DTO.Labels2DTO itemLabel : labels1DTOS.get(0).getLabels()) {
            dataList.add(new BetLhcModel(itemLabel.getNum(), BetLhcModel.Ball.getBallByColor(itemLabel.getColor()), odds, itemLabel.getMethodid(), itemLabel.getMenuid()));
        }
        datas.set(dataList);
    }


    /**
     * 清空彩票输入框
     */
    public void clear() {
        lotteryNumbs.set(Collections.emptyList());
        notifyChange();
    }

    public void handleInput(String money, String methodid) {
        if (TextUtils.isEmpty(money) || !(Integer.parseInt(money) > 0)) {
            // Remove items where methodid does not match
            List<Map<String, String>> newCodes = new ArrayList<>();
            for (Map<String, String> item : lotteryNumbs.get()) {
                if (!methodidEquals(item, methodid)) {
                    newCodes.add(item);
                }
            }
            lotteryNumbs.set(newCodes);
            return;
        }

        // Find the existing code with matching methodid
        Map<String, String> existingCode = null;
        for (Map<String, String> item : lotteryNumbs.get()) {
            if (methodidEquals(item, methodid)) {
                existingCode = item;
                break;
            }
        }

        if (existingCode != null) {
            // Update value if found
            existingCode.put("value", money);
            lotteryNumbs.set(new ArrayList<>(lotteryNumbs.get()));
        } else {
            // Find current method from groups and add it to codes
            BetLhcModel currentMethod = findCurrentMethod(methodid);
            if (currentMethod != null) {
                Map<String, String> newMethod = new HashMap<>();
                newMethod.put("value", money);
                newMethod.put("menuid", currentMethod.menuid);
                newMethod.put("methodid", currentMethod.methodid);
                newMethod.put("num", currentMethod.number);
                ArrayList updatedLists = new ArrayList<>(lotteryNumbs.get());
                updatedLists.add(newMethod);
                lotteryNumbs.set(updatedLists);
            }
        }

    }

    private boolean methodidEquals(Map<String, String> item, String methodid) {
        return item.containsKey("methodid") && item.get("methodid").equals(methodid);
    }

    private BetLhcModel findCurrentMethod(String methodid) {
        for (BindModel bindModel : datas.get()) {
            if (bindModel instanceof BetLhcModel) {
                if (((BetLhcModel) bindModel).methodid.equals(methodid)) {
                    return (BetLhcModel) bindModel;
                }
            }

        }
        return null;
    }


}
