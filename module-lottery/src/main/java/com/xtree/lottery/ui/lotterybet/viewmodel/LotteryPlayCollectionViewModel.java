package com.xtree.lottery.ui.lotterybet.viewmodel;

import android.app.Application;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.drake.brv.BindingAdapter;
import com.google.android.flexbox.FlexboxLayout;
import com.xtree.base.mvvm.recyclerview.BaseDatabindingAdapter;
import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.lottery.R;
import com.xtree.lottery.data.LotteryRepository;
import com.xtree.lottery.data.source.vo.MenuMethodsData;
import com.xtree.lottery.ui.lotterybet.model.LotteryPlayCollectionModel;

import java.util.ArrayList;

import me.xtree.mvvmhabit.base.BaseViewModel;

/**
 * Created by KAKA on 2024/4/26.
 * Describe:彩种投注-彩票收藏 viewmodel
 */
public class LotteryPlayCollectionViewModel extends BaseViewModel<LotteryRepository> {

    public final ObservableField<ArrayList<BindModel>> datas = new ObservableField<>(new ArrayList<>());

    public final ObservableField<ArrayList<Integer>> itemType = new ObservableField<>(
            new ArrayList<Integer>() {
                {
                    add(R.layout.item_lottery_play_collection);
                }
            });

    public BaseDatabindingAdapter.onBindListener onBindListener = new BaseDatabindingAdapter.onBindListener() {
        @Override
        public void onBind(@NonNull BindingAdapter.BindingViewHolder bindingViewHolder, @NonNull View view, int itemViewType) {

            if (itemViewType == R.layout.item_lottery_play_collection) {
                FlexboxLayout flexboxLayout = view.findViewById(R.id.flexLayout);
                flexboxLayout.removeAllViews();

                ArrayList<BindModel> bindModels = datas.get();

                LotteryPlayCollectionModel lotteryPlayCollectionModel = (LotteryPlayCollectionModel) bindModels.get(bindingViewHolder.getModelPosition());

                for (MenuMethodsData.LabelsDTO.Labels1DTO.Labels2DTO label : lotteryPlayCollectionModel.getLabel().getLabels()) {
                    View inflate = LayoutInflater.from(view.getContext()).inflate(R.layout.label_lottery_play_collection, null);
                    CheckBox labelBox = inflate.findViewById(R.id.label_box);
                    labelBox.setClickable(false);
                    labelBox.setText(label.getName());
                    labelBox.setChecked(label.isUserPlay());
                    labelBox.setOnClickListener(v -> {

//                        if (checkPlayCount(bindModels) <= 1) {
//                            ToastUtils.show("当前玩法不可取消收藏", ToastUtils.ShowType.Default);
//                            return;
//                        }

                        label.setUserPlay(!label.isUserPlay());
                        if (betsViewModel != null) {
                            betsViewModel.initTabs();
                        }
                    });
                    flexboxLayout.addView(inflate);
                }
            }
        }

        @Override
        public void onItemClick(int modelPosition, int layoutPosition, int itemViewType) {
        }
    };

    public LotteryPlayCollectionViewModel(@NonNull Application application) {
        super(application);
    }

    public LotteryPlayCollectionViewModel(@NonNull Application application, LotteryRepository model) {
        super(application, model);
    }

    public LotteryBetsViewModel betsViewModel;


    public void initData() {
        datas.set(betsViewModel.playModels);
    }

    private int checkPlayCount(ArrayList<BindModel> bindModels) {
        int count = 0;
        for (BindModel bindModel : bindModels) {
            LotteryPlayCollectionModel m = (LotteryPlayCollectionModel) bindModel;
            for (MenuMethodsData.LabelsDTO.Labels1DTO.Labels2DTO label : m.getLabel().getLabels()) {
                if (label.isUserPlay()) {
                    count++;
                }
            }
        }
        return count;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        betsViewModel = null;
    }
}
