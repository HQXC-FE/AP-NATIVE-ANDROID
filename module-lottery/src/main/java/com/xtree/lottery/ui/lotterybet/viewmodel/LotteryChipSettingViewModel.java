package com.xtree.lottery.ui.lotterybet.viewmodel;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.xtree.lottery.data.LotteryRepository;

import java.util.Objects;

import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.utils.ToastUtils;

/**
 * Created by KAKA on 2024/5/18.
 * Describe: 筹码设置弹窗ViewModel
 */
public class LotteryChipSettingViewModel extends BaseViewModel<LotteryRepository> {

    public LotteryChipSettingViewModel(@NonNull Application application) {
        super(application);
    }

    public LotteryChipSettingViewModel(@NonNull Application application, LotteryRepository model) {
        super(application, model);
    }
    public LotteryHandicapViewModel handicapViewModel;
    public MutableLiveData<String> chip1 = new MutableLiveData<>();
    public MutableLiveData<String> chip2 = new MutableLiveData<>();
    public MutableLiveData<String> chip3 = new MutableLiveData<>();
    public MutableLiveData<String> chip4 = new MutableLiveData<>();
    public MutableLiveData<String> chip5 = new MutableLiveData<>();

    public void initData() {
        int[] chipsValue = handicapViewModel.chips.getValue();
        if (chipsValue != null) {
            chip1.setValue(String.valueOf(chipsValue[0]));
            chip2.setValue(String.valueOf(chipsValue[1]));
            chip3.setValue(String.valueOf(chipsValue[2]));
            chip4.setValue(String.valueOf(chipsValue[3]));
            chip5.setValue(String.valueOf(chipsValue[4]));
        }
    }

    public void clearAmount(int index) {
        switch (index) {
            case 0:
                chip1.setValue("");
                break;
            case 1:
                chip2.setValue("");
                break;
            case 2:
                chip3.setValue("");
                break;
            case 3:
                chip4.setValue("");
                break;
            case 4:
                chip5.setValue("");
                break;
        }
    }

    public void setDefult() {
        handicapViewModel.chips.setValue(new int[]{10, 50, 100, 5000, 10000});
        finish();
    }

    public void save() {
        int[] c = new int[5];
        if (TextUtils.isEmpty(chip1.getValue())
                || TextUtils.isEmpty(chip2.getValue())
                || TextUtils.isEmpty(chip3.getValue())
                || TextUtils.isEmpty(chip4.getValue())
                || TextUtils.isEmpty(chip5.getValue())) {
            ToastUtils.show("金额不得为空", ToastUtils.ShowType.Default);
            return;
        }
        c[0] = Integer.parseInt(Objects.requireNonNull(chip1.getValue()));
        c[1] = Integer.parseInt(Objects.requireNonNull(chip2.getValue()));
        c[2] = Integer.parseInt(Objects.requireNonNull(chip3.getValue()));
        c[3] = Integer.parseInt(Objects.requireNonNull(chip4.getValue()));
        c[4] = Integer.parseInt(Objects.requireNonNull(chip5.getValue()));
        handicapViewModel.chips.setValue(c);
        finish();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        handicapViewModel = null;
    }
}
